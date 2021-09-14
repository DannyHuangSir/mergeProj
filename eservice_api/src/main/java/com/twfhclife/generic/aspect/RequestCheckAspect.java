//package com.twfhclife.generic.aspect;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.CodeSignature;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.twfhclife.generic.utils.MyStringUtil;
//
//@Aspect
//@Component
//public class RequestCheckAspect {
//
//	private static final Logger logger = LogManager.getLogger(RequestCheckAspect.class);
//
//	/**
//	 * 忽略敏感資訊的欄位的參數.
//	 */
//	private static final List<String> IGNORE_REQ_PARAMS = Arrays.asList("password");
//
//	@Pointcut("@annotation(com.twfhclife.generic.annotation.RequestCheck)")
//	public void access() {
//	}
//
//	@Around("access()")
//	public void doBeforeController(ProceedingJoinPoint  joinPoint) {
////		Object[] args = joinPoint.getArgs();
//		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
//		Method method = signature.getMethod();
//		String packageName = method.getDeclaringClass().getName();
//		String methodName = method.getName();
//		
//		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = attributes.getRequest();
//		
//		String secret = request.getHeader("secret");
//		if(MyStringUtil.isNullOrEmpty(secret)) {
//			// 未傳入secret不可
//		} else {
////			if(!secret.equals("HpeEservicEApi201808")) {
////				//secret不合法
////				HttpServletResponse.setStatusCode("401");
////	            Response.setResultString("Unauthorized User");
////	            return  Response;  
////				return new ResponseEntity<>("Application id unknown!", HttpStatus.BAD_REQUEST);
////			}
//			
//		}
////		return joinPoint.proceed(args);
//		
//	}
//
//	@AfterReturning(pointcut = "access()", returning = "retValue")
//	public void doAfterController(JoinPoint joinPoint, Object retValue) {
//		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//		Method method = signature.getMethod();
//		if (logger.isDebugEnabled()) {
//			logger.debug("exiting {}.{}()", method.getDeclaringClass().getName(), method.getName());
//		}
//	}
//}
