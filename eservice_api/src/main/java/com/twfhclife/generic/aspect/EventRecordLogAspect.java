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

import com.google.common.collect.Lists;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.eservice.api.adm.domain.EventRecordRequestVo;
import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;
import com.twfhclife.eservice.api.adm.service.IBusinessEventService;
import com.twfhclife.eservice.api.adm.service.ISystemEventService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.KeycloakLoginRequest;
import com.twfhclife.generic.domain.KeycloakUserAddRequest;
import com.twfhclife.generic.domain.KeycloakUserSessionRequest;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.MybatisSqlUtil;

import io.netty.util.internal.StringUtil;

/**
 * 業務事件記錄AOP.
 */
@Aspect
@Component
public class EventRecordLogAspect {

	private static final Logger logger = LogManager.getLogger(EventRecordLogAspect.class);
	
	@Autowired
	public IBusinessEventService businessEventService;
	
	@Autowired
	public ISystemEventService systemEventService;

	@Autowired
	public ParameterDao parameterDao;

	@Autowired
	public MybatisSqlUtil mybatisSqlUtil;
	
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
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		try {
			// 從註解取出相關設定資料
			EventRecordLog eventRecordLog = method.getAnnotation(EventRecordLog.class);
			EventRecordParam eventRecordParam = eventRecordLog.value();
			
			String eventCode = eventRecordParam.eventCode();
			
			String eventStatus = "0";
			String ssoExecMsg = "";
			String userName = "";
			if (retValue != null) {
				ResponseEntity<?> responseEntity = (ResponseEntity<?>) retValue;
				ApiResponseObj<?> apiResponseObj = (ApiResponseObj<?>) responseEntity.getBody();
				if (apiResponseObj != null) {
					ReturnHeader returnHeader = apiResponseObj.getReturnHeader();
					if (returnHeader != null) {
						if (ReturnHeader.SUCCESS_CODE.equals(returnHeader.getReturnCode())) {
							eventStatus = "1";
						}
						if (!StringUtils.isEmpty(returnHeader.getReturnMesg())) {
							ssoExecMsg = returnHeader.getReturnMesg();
						}
					}

					if (method.getName().equals("logout") && apiResponseObj.getResult() != null) {
						if (apiResponseObj.getResult() instanceof String) {
							userName = (String) apiResponseObj.getResult();
						}
					}
				}
			}
			
			String sysIdKey = "sysId";
			String userIdKey = "userId";
			if (args[0] instanceof KeycloakLoginRequest || args[0] instanceof KeycloakUserAddRequest) {
				sysIdKey = "clientId";
				userIdKey = "username";
			} else if (method.getName().equals("logout")){
				sysIdKey = "realm";
			} else if (args[0] instanceof KeycloakUserSessionRequest) {
				sysIdKey = "realm";
				userIdKey = "userId";
			}
			
			String sysId = "";
			Object sysIdObj = getRequestValue(args, parameterAnnotations, sysIdKey);
			if (sysIdObj != null) {
				if (sysIdObj.equals("elife")) {
					sysId = "eservice";
				} else if (sysIdObj.equals("twfhclife")) {
					sysId = "eservice_adm";
				} else if (sysIdObj.equals("elife_jdzq")) {
					sysId = "eservice_jd";
				} else {
					sysId = (String) sysIdObj;
				}
			}
			
			String userId = "";
			if (method.getName().equals("logout")) {
				userId = userName;
			} else {
				Object userIdObj = getRequestValue(args, parameterAnnotations, userIdKey);
				userId = (String) userIdObj;
			}
			
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
			if(StringUtil.isNullOrEmpty(userId)) {
				String fbId = (String) getRequestValue(args, parameterAnnotations, "fbId");
				String moicaId = (String) getRequestValue(args, parameterAnnotations, "moicaId");
				logger.debug("No username when recording event. Get user by: fbId=" + fbId + ",moicaId=" + moicaId);
				if(!StringUtil.isNullOrEmpty(fbId)) {
					userId = "FBID=" + fbId;// 加上前輟的ID，讓後端判斷取出fbId
				} else if(!StringUtil.isNullOrEmpty(moicaId)) {
					userId = "ICID=" + moicaId;// 加上前輟的ID，讓後端判斷取出moicaId
				}
			}
			be.setUserId(userId);
			be.setCreateUser(userId);			
			if (sysId.equals("eservice_adm")) {
				be.setEventName(getEventName("eservice", eventCode));
				be.setEventMsg(getEventName("eservice", eventCode));
			} else {
				be.setEventName(getEventName(sysId, eventCode));
				be.setEventMsg(getEventName(sysId, eventCode));
			}
			
			logger.debug("BusinessEvent record: " + MyJacksonUtil.object2Json(be));
			
			// 系統事件
			SystemEventParam[] systemEventParams = eventRecordParam.systemEventParams();
			if(systemEventParams!=null) {
				for (SystemEventParam systemEventParam : systemEventParams) {
					Map<String, Object> runSqlParams = new HashMap<>();
					
					String sqlId = systemEventParam.sqlId();
					String execMethod = systemEventParam.execMethod();
					String execFile = !"".equals(sqlId) ? "SQL DATA" : systemEventParam.execFile();
					String sqlVoType = systemEventParam.sqlVoType();
					String sqlVoKey = systemEventParam.sqlVoKey();

					// 表示sql優先使用vo當參數傳入
					if (!StringUtils.isEmpty(sqlVoType) && !StringUtils.isEmpty(sqlVoKey)) {
						Class sqlClass = Class.forName(sqlVoType);
						Object sqlVoObj = sqlClass.newInstance();

						// 若不是用vo當參數傳入，從註解取出使用者設定的
						SqlParam[] sqlParams = systemEventParam.sqlParams();
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
						SqlParam[] sqlParams = systemEventParam.sqlParams();
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

					List<String> jdEvents = Lists.newArrayList("JD-001", "JD-002", "JD-003", "JD-004", "JD-005", "JD-020");
					List<String> shouxianEvents = Lists.newArrayList("JD-006", "JD-007", "JD-008", "JD-009", "JD-010", "JD-011", "JD-012", "JD-013", "JD-014", "JD-015", "JD-016", "JD-017", "JD-018", "JD-019");

					if (!StringUtils.isEmpty(sqlId)) {
						try {
							if (jdEvents.contains(eventCode)) {
								se.setExecSql(mybatisSqlUtil.getJdNativeSql(sqlId, runSqlParams));
							} else if (shouxianEvents.contains(eventCode)) {
								se.setExecSql(mybatisSqlUtil.getShouxianNativeSql(sqlId, runSqlParams));
							} else {
								se.setExecSql(mybatisSqlUtil.getNativeSql(sqlId, runSqlParams));
							}
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
			}
			
			erReq.setBusinessEvent(be);
			erReq.setSystemEventList(systemEventList);
			
			saveEvent(erReq);
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

	private Object getRequestBody(String sqlVoType, Object[] args, Annotation[][] parameterAnnotations) {
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

	/**
	 * 取得業務代碼名稱.
	 * 
	 * @param sysId
	 * @param eventCode
	 * @return
	 */
	private String getEventName(String sysId, String eventCode) {
		String eventName = "";
		
		if(eventNameCacheMap==null) {
			eventNameCacheMap = new HashMap<>();
		}
		
		//如果cache有就直接取用
		boolean containEventCode = eventNameCacheMap.containsKey(sysId + "_"+ eventCode);
		if(containEventCode) {
			eventName = eventNameCacheMap.get(sysId + "_" + eventCode);
		}
		
		if(!containEventCode || eventNameCacheMap.isEmpty()) {
			List<ParameterVo> paramterList = parameterDao.getParameterByCategoryCode(sysId, "EVENT_TYPE");
			if (paramterList != null) {
				for (ParameterVo vo : paramterList) {
					if (eventCode.equals(vo.getParameterValue())) {
						eventName = vo.getParameterName();
						eventNameCacheMap.put(sysId + "_" + eventCode, eventName);
					}
				}
			}
		}
		
		if (eventNameCacheMap != null) {
			eventName = eventNameCacheMap.get(sysId + "_" + eventCode);
		}
		
		return eventName;
	}

	/**
	 * 儲存業務記錄資料.
	 * 
	 * @param erReq EventRecordRequestVo
	 */
	private void saveEvent(EventRecordRequestVo eventReq) {
		try {
			int nextBusinessEventId = businessEventService.getNextId();
			
			BusinessEventVo businessEventVo = eventReq.getBusinessEvent();
			businessEventVo.setBusinessEventId(nextBusinessEventId);
			businessEventService.add(businessEventVo);
			
			List<SystemEventVo> systemEventList = eventReq.getSystemEventList();
			for (SystemEventVo systemEventVo : systemEventList) {
				systemEventVo.setBusinessEventId(nextBusinessEventId);
				systemEventService.add(systemEventVo);
			}
		} catch (Exception e) {
			logger.warn("Unable to insert data from EventRecordRequestVo: {}", ExceptionUtils.getStackTrace(e));
		}
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
