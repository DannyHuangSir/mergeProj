package com.twfhclife.generic.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestLogAspect {

	private static final Logger logger = LogManager.getLogger(RequestLogAspect.class);

	/**
	 * 忽略敏感資訊的欄位的參數.
	 */
	private static final List<String> IGNORE_REQ_PARAMS = Arrays.asList("password");

	@Pointcut("@annotation(com.twfhclife.generic.annotation.RequestLog)")
	public void log() {
	}

	@Before("log()")
	public void doBeforeController(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String packageName = method.getDeclaringClass().getName();
		String methodName = method.getName();
		if (logger.isDebugEnabled()) {
			logger.debug("entering {}.{}()", packageName, methodName);
		}

		String[] parameterNames = codeSignature.getParameterNames();
		for (int i = 0; i < parameterNames.length; i++) {
			if (!IGNORE_REQ_PARAMS.contains(parameterNames[i])) {
				logger.debug("{} parameter[{}]: {}", methodName, parameterNames[i], args[i]);	
			}
		}
	}

	@AfterReturning(pointcut = "log()", returning = "retValue")
	public void doAfterController(JoinPoint joinPoint, Object retValue) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting {}.{}()", method.getDeclaringClass().getName(), method.getName());
		}
	}
}
