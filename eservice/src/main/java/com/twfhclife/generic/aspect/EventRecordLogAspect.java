package com.twfhclife.generic.aspect;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.eservice.web.domain.EventRecordRequestVo;
import com.twfhclife.eservice.web.model.BusinessEventVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
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

	@Pointcut("@annotation(com.twfhclife.generic.annotation.EventRecordLog)")
	public void log() {
	}

	@Before("log()")
	public void doBeforeController(JoinPoint joinPoint) {
	}

	@AfterReturning(pointcut = "log()", returning = "retValue")
	public void doAfterController(JoinPoint joinPoint, Object retValue) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		try {
			EventRecordLog eventRecordLog = method.getAnnotation(EventRecordLog.class);
			EventRecordParam eventRecordParam = eventRecordLog.value();
			postEvent(eventRecordParam.eventCode());
		} catch (Exception e) {
			logger.error("Unable to record event log: {}", ExceptionUtil.printStackTrace(e));
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

	private EventRecordRequestVo getBaseEventRecord(String eventCode) {
		Date nowDate = new Date();
		String userId = getUserId();
		
		BusinessEventVo be = new BusinessEventVo();
		be.setUserId(userId);
		be.setEventDate(nowDate);
		be.setEventCode(eventCode);
		be.setEventName(getEventName(eventCode));
		be.setEventMsg(getEventName(eventCode));
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
	 */
	private void postEvent(String eventCode) {
		if (needEventRecord) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			EventRecordRequestVo erReq = getBaseEventRecord(eventCode);
			logger.info("Post event[{}] to api: {}", eventCode, erReq.getBusinessEvent());
			
			HttpEntity<Object> entity = new HttpEntity<Object>(erReq, headers);
			new RestTemplate().postForEntity(eventRecordApiUrl, entity, String.class);
		}
	}
	
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
}