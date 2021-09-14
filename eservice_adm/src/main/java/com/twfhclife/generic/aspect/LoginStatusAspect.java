package com.twfhclife.generic.aspect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.adm.service.IFunctionDailyUsageService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.KeycloakUtil;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

@Aspect
@Component
public class LoginStatusAspect {

	private static final Logger logger = LogManager.getLogger(LoginStatusAspect.class);
	
	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	KeycloakUtil kutil;
	
	@Autowired
	private IFunctionDailyUsageService functionDailyUsageService;
	
	@Value("${data-source.mode}")
	public String DATASOURCE_MODE;
	
	@Pointcut("@annotation(com.twfhclife.generic.annotation.LoginCheck)")
	public void token() {
	}

	@Before("token()")
	public void doBeforeController(JoinPoint joinPoint) throws IOException {
		KeycloakUser keycloakUser = null;
		logger.debug("Check login session alive.");
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		Object userObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
		if(userObj == null) {
			// session不存在，須將user登出
			logger.info("Session not exists, User not logged in!");
			response.sendRedirect("/eservice_adm/login-timeout");
			return;
		} else {
			try {
				keycloakUser = (KeycloakUser) userObj;
				if("api".equals(DATASOURCE_MODE)) {
					KeycloakLoginResponse apiResponse = null;
					
						apiResponse = keycloakService.validateToken(keycloakUser.getAccessToken(), keycloakUser.getRefreshToken());
						if(apiResponse != null) {
							String status = MyStringUtil.objToStr(apiResponse.getStatus());
							if(status.equals("REFRESH")) {
								// 已換發Token，重新放入session
								keycloakUser.setAccessToken(apiResponse.getAccessToken());
								keycloakUser.setRefreshToken(apiResponse.getRefreshToken());
								request.getSession().setAttribute(ApConstants.KEYCLOAK_USER, keycloakUser);
							} else if(!status.equals("SUCCESS")) {
								// 驗證不通過，登出
								logger.info("Session timeout, force logout!");
								request.getSession().removeAttribute(ApConstants.KEYCLOAK_USER);
								Enumeration<String> em = request.getSession().getAttributeNames();
								while (em.hasMoreElements()) {
									request.getSession().removeAttribute(em.nextElement().toString());
								}
								response.sendRedirect("/eservice_adm/login-timeout");
								return;
							}
						} else {
							// 驗證失敗，登出
							logger.info("Session timeout, force logout!");
							request.getSession().removeAttribute(ApConstants.KEYCLOAK_USER);
							Enumeration<String> em = request.getSession().getAttributeNames();
							while (em.hasMoreElements()) {
								request.getSession().removeAttribute(em.nextElement().toString());
							}
							response.sendRedirect("/eservice_adm/login-timeout");
							return;
						}
					
				} else {
					String userId = kutil.getUserIdByValidToken(keycloakUser.getAccessToken());
					if(MyStringUtil.isNullOrEmpty(userId)) {
						// AccessToken不合法，須將user登出
						logger.info("User ID("+keycloakUser.getId()+"): Access token expired. Logout user!");
						logger.info("Session timeout, force logout!");
						request.getSession().removeAttribute(ApConstants.KEYCLOAK_USER);
						Enumeration<String> em = request.getSession().getAttributeNames();
						while (em.hasMoreElements()) {
							request.getSession().removeAttribute(em.nextElement().toString());
						}
						response.sendRedirect("/eservice_adm/login-timeout");
						return;
					} else {
						logger.debug("User ID("+userId+") has valid token, access accepted.");
					}
				}
				
				// TODO add function usage count 
			} catch (Exception e) {
				logger.error("Unable to validateToken: {}", ExceptionUtils.getStackTrace(e));
			}
		}
		
		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Method method = methodSignature.getMethod();
			LoginCheck loginCheck = method.getAnnotation(LoginCheck.class);
			FuncUsageParam param = loginCheck.value();
			this.functionUsageCount(param.funcId(), param.systemId());
		} catch(Exception e) {
			logger.error("Unable to get FuncUsageParam: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	@AfterReturning(pointcut = "token()", returning = "retValue")
	public void doAfterController(JoinPoint joinPoint, Object retValue) {
		// do noting
	}
	
	/**
	 * 計算功能點擊次數
	 * @param functionId
	 * @param systemId
	 */
	private void functionUsageCount(String functionId, String systemId) {
		try {
			if("".equals(functionId) || "".equals(systemId)) {
				/* 沒有 funciton id 或者 system id 不做事 */
				return ;
			} else {
				logger.debug("Function usage add start.");
				logger.debug("Function=" + functionId + ", System=" + systemId);
				functionDailyUsageService.addFunctionUsageCount(functionId, systemId);
			}
		} catch(Exception e) {
			logger.error("Unable to functionUsageCount: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			logger.debug("Function usage add end.");
		}
	}
}
