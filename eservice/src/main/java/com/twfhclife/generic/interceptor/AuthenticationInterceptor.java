package com.twfhclife.generic.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.util.ApConstants;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		// 驗證使否已經登入 or 是否有這個使用者
		boolean uriAuth = false;
		Object keycloakUser = request.getSession().getAttribute(ApConstants.LOGIN_USER_ID);
		Object eserviceUser = request.getSession().getAttribute(UserDataInfo.USER_DETAIL);
		
		if (keycloakUser == null || eserviceUser == null) {
			uriAuth = false;
		} else {
			//force change password if LastChangePasswordDate is null
			UsersVo userDetail = (UsersVo) eserviceUser;
//			System.out.println(request.getRequestURI());
//			System.out.println(request.getContextPath());
//			System.out.println(request.getServletPath());
			if (userDetail.getLastChangPasswordDate() == null) {
				if (request.getServletPath().indexOf("doLogout") == -1 && request.getServletPath().indexOf("Password") == -1) {
					response.sendRedirect("/eservice/changePassword1");
					return uriAuth;
				}
			}
			uriAuth = true;
		}

		if (!uriAuth) {
			response.sendRedirect("/eservice/login");
		}

		return uriAuth;
	}

}