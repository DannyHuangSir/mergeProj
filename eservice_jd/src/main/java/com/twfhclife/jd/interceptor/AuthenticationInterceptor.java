package com.twfhclife.jd.interceptor;

import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.web.model.UserDataInfo;
import com.twfhclife.jd.web.model.UsersVo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            UsersVo userDetail = (UsersVo) eserviceUser;
            if (userDetail.getLastChangPasswordDate() == null) {
                if (request.getServletPath().indexOf("doLogout") == -1 && request.getServletPath().indexOf("Password") == -1) {
                    response.sendRedirect("firstPassword1");
                    return uriAuth;
                }
            }
            uriAuth = true;
        }
        if (!uriAuth) {
            response.sendRedirect("login");
        }

        return uriAuth;
    }

}