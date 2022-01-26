package com.twfhclife.generic.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.twfhclife.eservice.generic.annotation.*;
import com.twfhclife.eservice.web.domain.EventRecordRequestVo;
import com.twfhclife.eservice.web.model.BusinessEventVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.SystemEventVo;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;
import com.twfhclife.generic.util.MybatisSqlUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
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
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.eservice.web.domain.EventRecordRequestVo;
import com.twfhclife.eservice.web.model.BusinessEventVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.ExceptionUtil;
import com.twfhclife.keycloak.model.KeycloakUser;

@Aspect
@Component
public class EventRecordLogAspect {

	private static final Logger logger = LogManager.getLogger(EventRecordLogAspect.class);

	@Value("${event.record.url}")
	public String eventRecordApiUrl;

	@Value("${event.record}")
	public boolean needEventRecord;

	@Autowired
	public MybatisSqlUtil mybatisSqlUtil;

	@Pointcut("@annotation(com.twfhclife.eservice.generic.annotation.EventRecordLog)")
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
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		try {
			// 從註解取出相關設定資料
			EventRecordLog eventRecordLog = method.getAnnotation(EventRecordLog.class);
			EventRecordParam eventRecordParam = eventRecordLog.value();

			String eventCode = eventRecordParam.eventCode();

			String eventStatus = "1";
			String ssoExecMsg = "";
			if (retValue != null) {
				if (retValue instanceof Integer) {
					eventStatus = String.valueOf(retValue);
				}
			}

			String sysId = "eservice";
			String userId = getUserId();

			// 業務事件
			EventRecordRequestVo erReq = new EventRecordRequestVo();
			List<SystemEventVo> systemEventList = new ArrayList<>();
			Date nowDate = new Date();

			BusinessEventVo be = new BusinessEventVo();
			be.setEventDate(nowDate);
			be.setEventCode(eventCode);
			be.setEventStatus(eventStatus);
			be.setSourceIp(getClientIp());
			be.setTargetIp(getRequest().getRemoteAddr());
			be.setTargetSystemId(sysId);
			be.setCreateDate(nowDate);
			be.setUserId(userId);
			be.setCreateUser(userId);
			be.setEventName(getEventName(eventCode));
			be.setEventMsg(getEventName(eventCode));

			logger.debug("BusinessEvent record: " + MyJacksonUtil.object2Json(be));
			// 系統事件
			EserviceEventParam[] eserviceEventParams = eventRecordParam.systemEventParams();
			for (EserviceEventParam eserviceEventParam : eserviceEventParams) {
				Map<String, Object> runSqlParams = new HashMap<>();

				String sqlId = eserviceEventParam.sqlId();
				String execMethod = eserviceEventParam.execMethod();
				String execFile = !"".equals(sqlId) ? "SQL DATA" : eserviceEventParam.execFile();
				String sqlVoType = eserviceEventParam.sqlVoType();
				String sqlVoKey = eserviceEventParam.sqlVoKey();

				// 表示sql優先使用vo當參數傳入
				if (!StringUtils.isEmpty(sqlVoType) && !StringUtils.isEmpty(sqlVoKey)) {
					Class sqlClass = Class.forName(sqlVoType);
					Object sqlVoObj = sqlClass.newInstance();

					// 若不是用vo當參數傳入，從註解取出使用者設定的
					SqlParam[] sqlParams = eserviceEventParam.sqlParams();
					for (SqlParam param: sqlParams) {
						String requestParamkey = param.requestParamkey();
						String sqlParamkey = param.sqlParamkey();

						Object fieldValue = getRequestValue(args, parameterAnnotations, requestParamkey);
						Field field = ReflectionUtils.findField(sqlClass, sqlParamkey);
						ReflectionUtils.makeAccessible(field);
						ReflectionUtils.setField(field, sqlVoObj, fieldValue);
					}

					runSqlParams.put(sqlVoKey, sqlVoObj);
				} else {
					// 若不是用vo當參數傳入，從註解取出使用者設定的
					SqlParam[] sqlParams = eserviceEventParam.sqlParams();
					for (SqlParam param: sqlParams) {
						String requestParamkey = param.requestParamkey();
						String sqlParamkey = param.sqlParamkey();

						// 若sqlParamkey有設定，代表sql的參數不是前端參數的key值，以sqlParamkey為優先
						String runSqlParamkey = (!StringUtils.isEmpty(sqlParamkey) ? sqlParamkey : requestParamkey);

						runSqlParams.put(runSqlParamkey, getRequestValue(args, parameterAnnotations, requestParamkey));
					}
				}

				SystemEventVo se = new SystemEventVo();
				se.setExecDate(nowDate);
				se.setExecUser(userId);
				se.setExecMethod(execMethod);
				se.setExecFile(execFile);
				se.setExecStatus(eventStatus);
				se.setCreateDate(nowDate);
				se.setCreateUser(userId);
				if (eventCode.contains("AA")) {
					se.setExecMsg(ssoExecMsg);
				}

				if (!StringUtils.isEmpty(sqlId)) {
					try {
						se.setExecSql(mybatisSqlUtil.getNativeSql(sqlId, runSqlParams));
						logger.info("sysId: {}, userId: {}, eventCode: {}, sqlId: {}, running sql: {}",
								sysId, userId, eventCode, sqlId, se.getExecSql());
					} catch (Exception e) {
						se.setExecStatus("0");
						se.setExecMsg(ExceptionUtils.getStackTrace(e));
						logger.warn("Unable to getNativeSql: {}", ExceptionUtils.getStackTrace(e));
					}
				}
				systemEventList.add(se);
			}

			erReq.setBusinessEvent(be);
			erReq.setSystemEventList(systemEventList);

			if (needEventRecord) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				logger.info("Post event[{}] to api: {}", eventCode, erReq.getBusinessEvent());
				HttpEntity<Object> entity = new HttpEntity<Object>(erReq, headers);
				new RestTemplate().postForEntity(eventRecordApiUrl, entity, String.class);
			}
		} catch (Exception e) {
			logger.error("Unable to record event log: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 取得事件名稱.
	 * 
	 * @param eventCode 事件代碼
	 * @return 回傳事件名稱
	 */
	@SuppressWarnings("unchecked")
	private String getEventName(String eventCode) {
		String eventName = "";
		Object sysParamObj = getSession(ApConstants.SYSTEM_PARAMETER);
		if (sysParamObj != null) {
			Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) sysParamObj;
			Map<String, ParameterVo> eventCodeMap = sysParamMap.get(ApConstants.EVENT_TYPE_PARAMETER_CATEGORY_CODE);
			if (eventCodeMap != null && eventCodeMap.get(eventCode) != null) {
				eventName = eventCodeMap.get(eventCode).getParameterName();
			}
		}
		return eventName;
	}

	private EventRecordRequestVo getBaseEventRecord(String eventCode, String eventStatus) {
		Date nowDate = new Date();
		String userId = getUserId();
		
		BusinessEventVo be = new BusinessEventVo();
		be.setUserId(userId);
		be.setEventDate(nowDate);
		be.setEventCode(eventCode);
		be.setEventName(getEventName(eventCode));
		be.setEventMsg(getEventName(eventCode));
		be.setEventStatus(eventStatus);
		be.setSourceIp(getClientIp());
		be.setTargetSystemId(ApConstants.SYSTEM_ID);
		be.setCreateDate(nowDate);
		be.setCreateUser(userId);
		
		EventRecordRequestVo reqs = new EventRecordRequestVo();
		reqs.setBusinessEvent(be);
		return reqs;
	}

	/**
	 * 傳送業務事件至 API.

	private void postEvent(String eventCode) {
		if (needEventRecord) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			EventRecordRequestVo erReq = getBaseEventRecord(eventCode);
			logger.info("Post event[{}] to api: {}", eventCode, erReq.getBusinessEvent());
			
			HttpEntity<Object> entity = new HttpEntity<Object>(erReq, headers);
			new RestTemplate().postForEntity(eventRecordApiUrl, entity, String.class);
		}
	}*/
	
	/**
	 * 取得 HttpServletRequest
	 * 
	 * @return request
	 */
	private HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 從Session取出物件.
	 * 
	 * @return Object
	 */
	private Object getSession(String key) {
		return getRequest().getSession().getAttribute(key);
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
				} else if (annotation instanceof PathVariable) {
					// 根據註解的key取值
					PathVariable pathVariable = (PathVariable) annotation;
					if (pathVariable.value().equals(paramKey)) {
						respValue = args[argIndex];
					}
				}
			}
		}
		return respValue;
	}
}