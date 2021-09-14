package com.twfhclife.generic.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;

public class CheckPolicyNoInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LogManager.getLogger(CheckPolicyNoInterceptor.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		String policyNo = request.getParameter("policyNo");
		try {
			if (!StringUtils.isEmpty(policyNo)) {
				Object userObj = request.getSession().getAttribute(UserDataInfo.USER_DETAIL);
				if (userObj != null) {
					UsersVo usersVo = (UsersVo) userObj;
					String loginUserType = usersVo.getUserType();
					
					if ("normal".equals(loginUserType) || "member".equals(loginUserType)) {
						Object userPolicyNoListObj = request.getSession().getAttribute(UserDataInfo.USER_POLICY_NO_LIST);
						List<String> userPolicyNoList = (List<String>) userPolicyNoListObj;
						
						// 若保單號碼不在使用者的保單清單，轉到錯誤頁面
						if (userPolicyNoList != null && !userPolicyNoList.contains(policyNo)) {
							logger.error("PolicyNo not in UserPolicyNoList!");
							response.sendRedirect("/eservice/errorPage");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}

}