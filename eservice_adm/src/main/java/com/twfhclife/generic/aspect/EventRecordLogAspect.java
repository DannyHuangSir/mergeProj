package com.twfhclife.generic.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.adm.dao.ParameterDao;
import com.twfhclife.adm.domain.EventRecordRequestVo;
import com.twfhclife.adm.model.BusinessEventVo;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.model.SystemEventVo;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MybatisSqlUtil;
import com.twfhclife.keycloak.model.KeycloakUser;

/**
 * 業務事件記錄AOP.
 */
@Aspect
@Component
public class EventRecordLogAspect {

	private static final Logger logger = LogManager.getLogger(EventRecordLogAspect.class);

	@Autowired
	public ParameterDao parameterDao;

	@Autowired
	public MybatisSqlUtil mybatisSqlUtil;

	@Value("${event.record.url}")
	public String eventRecordApiUrl;

	@Value("${event.record}")
	public boolean needEventRecord;
	
	public Map<String, String> eventNameCacheMap = new HashMap<>();

	@Pointcut("@annotation(com.twfhclife.generic.annotation.EventRecordLog)")
	public void log() {
	}

	@Before("log()")
	public void doBeforeController(JoinPoint joinPoint) {
	}

	@AfterReturning(pointcut = "log()", returning = "retValue")
	public void doAfterController(JoinPoint joinPoint, Object retValue) {
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String methodName = method.getName();
		Map<String, Object> runSqlParams = new HashMap<>();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		try {
			// 從註解取出相關設定資料
			EventRecordLog eventRecordLog = method.getAnnotation(EventRecordLog.class);
			EventRecordParam eventRecordParam = eventRecordLog.value();
			String eventCode = eventRecordParam.eventCode();
			String sqlId = eventRecordParam.sqlId();
			String daoVoParamKey = eventRecordParam.daoVoParamKey();
			logger.info("eventCode: {}, sqlId: {}", eventCode, sqlId);

			// 檢查是否有daoVoParamKey註解，表示sql優先使用vo當參數傳入
			if (!StringUtils.isEmpty(daoVoParamKey)) {
				runSqlParams.put(daoVoParamKey, getRequestBody(args, parameterAnnotations));
			} else {
				// 若不是用vo當參數傳入，從註解取出使用者設定的
				SqlParam[] sqlParams = eventRecordParam.sqlParams();
				for (SqlParam param: sqlParams) {
					String requestParamkey = param.requestParamkey();
					String sqlParamkey = param.sqlParamkey();
					
					// 若sqlParamkey有設定，代表sql的參數不是前端參數的key值，以sqlParamkey為優先
					String runSqlParamkey = (!StringUtils.isEmpty(sqlParamkey) ? sqlParamkey : requestParamkey);
					
					runSqlParams.put(runSqlParamkey, getRequestValue(args, parameterAnnotations, requestParamkey));
				}
			}

			EventRecordRequestVo erReq = getBaseEventRecord(methodName, eventCode, sqlId, runSqlParams);
			logger.info("eventCode: {}, running sql: {}", eventCode, erReq.getSystemEvent().getExecSql());
			postEvent(erReq);
		} catch (Exception e) {
			logger.error("Unable to record event log: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	private Object getRequestValue(Object[] args, Annotation[][] parameterAnnotations, String paramKey) {
		Object respValue = null;
		for (int argIndex = 0; argIndex < args.length; argIndex++) {
			for (Annotation annotation : parameterAnnotations[argIndex]) {
				if (annotation instanceof RequestParam) {
					// 根據註解的key取值
					RequestParam requestParam = (RequestParam) annotation;
					if (requestParam.value().equals(paramKey)) {
						respValue = args[argIndex];
					}
				} else if (annotation instanceof RequestBody) {
					try {
						respValue = FieldUtils.readField(args[argIndex], paramKey, true);
					} catch (IllegalAccessException e) {
						logger.warn("Unable to get field value: {}", ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}

		return respValue;
	}

	private Object getRequestBody(Object[] args, Annotation[][] parameterAnnotations) {
		Object respValue = null;
		for (int argIndex = 0; argIndex < args.length; argIndex++) {
			for (Annotation annotation : parameterAnnotations[argIndex]) {
				if (annotation instanceof RequestBody) {
					respValue = args[argIndex];
				}
			}
		}

		return respValue;
	}

	private EventRecordRequestVo getBaseEventRecord(String methodName, String eventCode, String sqlId, Object sqlParam) {
		Date nowDate = new Date();
		String userId = getUserId();
		
		BusinessEventVo be = new BusinessEventVo();
		be.setUserId(userId);
		be.setEventDate(nowDate);
		be.setEventCode(eventCode);
		be.setEventName(getEventName(eventCode));
		be.setEventMsg(getEventName(eventCode));
		be.setSourceIp(getClientIp());
		be.setTargetIp(getRequest().getRemoteAddr());
		be.setTargetSystemId(ApConstants.SYSTEM_ID);
		be.setCreateDate(nowDate);
		be.setCreateUser(userId);
		be.setEventStatus("1");

		SystemEventVo se = new SystemEventVo();
		se.setExecDate(nowDate);
		se.setExecUser(userId);
		se.setExecMethod(methodName);
		se.setCreateDate(nowDate);
		se.setCreateUser(userId);
		se.setExecStatus("1");

		if (!StringUtils.isEmpty(sqlId)) {
			try {
				se.setExecSql(mybatisSqlUtil.getNativeSql(sqlId, sqlParam));
			} catch (Exception e) {
				logger.warn("Unable to getNativeSql: {}", ExceptionUtils.getStackTrace(e));
			}
		}
		
		EventRecordRequestVo reqs = new EventRecordRequestVo();
		reqs.setBusinessEvent(be);
		reqs.setSystemEvent(se);
		return reqs;
	}

	/**
	 * 取得業務代碼名稱
	 * 
	 * @param eventCode
	 * @return
	 */
	private String getEventName(String eventCode) {
		String eventName = "";
		
		if(eventNameCacheMap==null) {
			eventNameCacheMap = new HashMap<>();
		}
		
		//如果cache有就直接取用
		boolean containEventCode = eventNameCacheMap.containsKey(eventCode);
		if(containEventCode) {
			eventName = eventNameCacheMap.get(eventCode);
		}

		if(!containEventCode || eventNameCacheMap.isEmpty()) {
			List<ParameterVo> paramterList = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID,
					Arrays.asList(ApConstants.EVENT_TYPE_PARAMETER_CATEGORY_CODE));
			if (paramterList != null) {
				for (ParameterVo vo : paramterList) {
					if (eventCode.equals(vo.getParameterValue())) {
						eventName = vo.getParameterName();
						eventNameCacheMap.put(eventCode, eventName);
					}
				}
			}
		}
		
		if (eventNameCacheMap != null) {
			eventName = eventNameCacheMap.get(eventCode);
		}
		
		return eventName;
	}

	/**
	 * 傳送業務記錄資料至API服務.
	 * 
	 * @param erReq EventRecordRequestVo
	 */
	private void postEvent(EventRecordRequestVo erReq) {
		if (needEventRecord) {
			try {
				SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
					     SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), 
					        NoopHostnameVerifier.INSTANCE);
				CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				
				HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
				requestFactory.setHttpClient(httpClient);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<Object> entity = new HttpEntity<Object>(erReq, headers);
				new RestTemplate(requestFactory).postForEntity(eventRecordApiUrl, entity, String.class);
			} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
				// TODO Auto-generated catch block
				logger.debug("Create httpClient fail: {}", ExceptionUtils.getStackTrace(e));
			}

		}
	}

	/**
	 * 取得登入者資訊.
	 * 
	 * @return KeycloakUser UsersVo
	 */
	private KeycloakUser getLoginUser() {
		Object userObj = getRequest().getSession().getAttribute(ApConstants.KEYCLOAK_USER);
		return (userObj != null ? (KeycloakUser) userObj : null);
	}

	/**
	 * 取得登入者資訊.
	 * 
	 * @return UsersVo
	 */
	private String getUserId() {
		KeycloakUser userVo = getLoginUser();
		return (userVo != null ? userVo.getUsername() : null);
	}
	
	/**
	 * 取得請求者IP.
	 * 
	 * @return 回傳請求者IP
	 */
	private String getClientIp() {
		HttpServletRequest req = getRequest();
		String remoteAddr = req.getHeader("X-FORWARDED-FOR");
		if (StringUtils.isEmpty(remoteAddr)) {
			remoteAddr = req.getRemoteAddr();
		}
		return remoteAddr;
	}

	/**
	 * 取得 HttpServletRequest
	 * 
	 * @return request
	 */
	private HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
}
