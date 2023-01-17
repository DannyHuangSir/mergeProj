package com.twfhclife.generic.interceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.twfhclife.generic.aspect.LoginStatusAspect;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.KeycloakUtil;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.keycloak.model.KeycloakUser;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LogManager.getLogger(AuthenticationInterceptor.class);
	
	@Autowired
	KeycloakUtil keycloakUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		List<String> nonAuthPages = Arrays.asList(
				"/login",
				"/logout",
				"/error",
				"/doLogin",
				"/doLogout",
				"/css/", 
				"/js/",
				"/images/",
				"/fonts/",
				"/font-awesome/",
				"/communicationTempReview",
				"/login-timeout"
				,"/test/logs"
				,"/getVerify"
				//,"/testpage","/test/getResult"
				);

		// 忽略需要驗證的URI
		String requestURI = request.getRequestURI();
		for (String nonAuthURI : nonAuthPages) {
			if (requestURI.indexOf(nonAuthURI) != -1) {
				if (nonAuthURI.endsWith("/")) {
					// 靜態資源下所有的目錄
					return true;
				} else {
					// 非靜態資源的請求
					if (requestURI.endsWith(nonAuthURI) || requestURI.indexOf(nonAuthURI + "?") != -1) {
						return true;
					}
				}
			}
		}

		// 驗證使否已經登入 or 是否有這個使用者
		boolean uriAuth = false;
		
		Object userSessionObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
		logger.debug("#uriAuth:", userSessionObj);
		if (userSessionObj == null) {
			uriAuth = false;
		} else {
			KeycloakUser keycloakUser = (KeycloakUser) userSessionObj;
			// TODO 取得功能清單
			Object menuObj = request.getSession().getAttribute(ApConstants.MENU_LIST);
			if (menuObj == null) {
				// TODO 判斷URI是否為在功能清單內
			}

			// TODO URI若存在，判斷是否該使用者角色權限能否使用(不做)

			// 判斷 keycloak 的 access_token時效性
//			String accessToken = keycloakUser.getAccessToken();
//			String userId = keycloakUtil.getUserIdByValidToken(accessToken);
//			if(MyStringUtil.isNullOrEmpty(userId)) {
//				// AccessToken不合法，須將user登出
//				logger.info("User ID("+keycloakUser.getId()+"): Access token expired. Logout user!");
//				//TODO
//				uriAuth = false;
//			} else {
//				logger.debug("User ID("+userId+") has valid token, access accepted.");
//				uriAuth = true;
//			}

			uriAuth = true;
		}

		if(!uriAuth && (requestURI.endsWith("/eservice_adm/") || requestURI.endsWith("/eservice_adm/index"))) {
			response.sendRedirect("/eservice_adm/login");
		}
		else if (!uriAuth) {
			logger.debug("***** login-timeout *****");
			response.sendRedirect("/eservice_adm/login-timeout");
		}

		return uriAuth;
	}
}
