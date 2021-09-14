package com.twfhclife.generic.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.generic.utils.MyStringUtil;

public class BaseController extends BaseMvcController {

	@Value("${eservice_api.secret.code}")
	protected String SECRET;
	
	@Value("${eservice_api.secret.required}")
	protected boolean SECRET_REQUIRE;
	
	protected List<String> errorMessages;
	
	/**
	 * 添加變量至request
	 * 
	 * @param key
	 * @param value
	 */
	protected void addAttribute(String key, Object value) {
		getRequest().setAttribute(key, value);
	}

	/**
	 * 添加系統錯誤至request.
	 * 
	 * @param errorMessage 錯誤訊息
	 */
	protected void addSystemError(String errorMessage) {
		addAttribute("errorMessage", errorMessage);
	}

	/**
	 * 添加變量至Session.
	 * 
	 * @return UsersVo
	 */
	protected void addSession(String key, Object value) {
		getRequest().getSession().setAttribute(key, value);
	}

	/**
	 * 從Session取出物件.
	 * 
	 * @return Object
	 */
	protected Object getSession(String key) {
		return getRequest().getSession().getAttribute(key);
	}
	
	/**
	 * 取得請求者IP.
	 * 
	 * @return 回傳請求者IP
	 */
	protected String getClientIp() {
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
	
	protected boolean validateSecret(String secret) {
		if(!MyStringUtil.nullToString(secret).equals(SECRET)) {
			return false;
		}
		return true;
	}
	
	protected void setErrorMessages(String error) {
		if(errorMessages == null) {
			errorMessages = new ArrayList<>();
		}
		errorMessages.add(error);
	}

}
