package com.twfhclife.generic.aspect;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.generic.utils.MyJacksonUtil;

@Aspect
@Component
public class RequestLogAspect {

	private static final Logger logger = LogManager.getLogger(RequestLogAspect.class);

	/**
	 * 忽略敏感資訊的欄位的參數.
	 */
	private static final List<String> IGNORE_REQ_PARAMS = Arrays.asList("password");

	@Pointcut("@annotation(com.twfhclife.generic.annotation.ApiRequest)")
	public void accessLog() {
	}

	@Before("accessLog()") 
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到請求時，計下開始時間, 并記錄下請求內容。
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();

			logger.info("====================================================================ST==");
			request.setAttribute("startTime", System.currentTimeMillis());
			
			logger.info("URL              : " + request.getRequestURL().toString());
			logger.info("IP               : " + request.getRemoteAddr());
			String uri = request.getRequestURI().toString();
			if(uri.indexOf("/sso/login") >= 0 || uri.indexOf("/user/create") >= 0) {
				logger.info("ARGS             : {**********}");
			} else {
				Object[] obj = joinPoint.getArgs();
				logger.info("ARGS             : " + MyJacksonUtil.object2Json(obj));
//				logger.info("ARGS             : " + Arrays.toString(obj));
			}
			logger.info("HTTP_METHOD      : " + request.getMethod());
			logger.info("CLASS_METHOD     : " + joinPoint.getSignature().getDeclaringTypeName() + "."
					+ joinPoint.getSignature().getName());
		} catch (Exception e) {
			logger.error("Error occured in RequestLogAspect before:", e);
		}

	}

	@AfterReturning(returning = "ret", pointcut = "accessLog()")
	public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
		// 請求處理完畢后，打印處理結果；并記錄下總共花費時間。
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();

			long startTime = (long) request.getAttribute("startTime");
			long endTime = System.currentTimeMillis();

			// Ignore getPromotionList Response info
			if (!StringUtils.equals("getPromotionList", joinPoint.getSignature().getName())) {
				logger.debug("RESPONSE         : " +MyJacksonUtil.object2Json(ret));
			}
			logger.info("API ELAPSED TIME : " + (endTime - startTime) + " ms");
			logger.info("====================================================================ED==");
			Object arg0 = null;
//			if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
//				arg0 = joinPoint.getArgs()[0];
//			}

			request.removeAttribute("startTime");
		} catch (Exception e) {
			logger.error("Error occured in RequestLogAspect after:", e);
		}
	}
	
}
