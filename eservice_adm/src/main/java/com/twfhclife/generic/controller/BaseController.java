package com.twfhclife.generic.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.generic.controller.BaseMvcController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.keycloak.model.KeycloakUser;

public class BaseController extends BaseMvcController {

	@Value("${data-source.mode}")
	public String DATASOURCE_MODE;
	
	public boolean isApiMode() {
		if("dao".equals(DATASOURCE_MODE)) {
			return false;
		} else {
			return true;
		}
	}
	
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
	 * 添加系統錯誤至request.
	 */
	protected void addDefaultSystemError() {
		addAttribute("errorMessage", ApConstants.SYSTEM_ERROR);
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
	 * 取得登入者資訊.
	 * 
	 * @return KeycloakUser UsersVo
	 */
	protected KeycloakUser getLoginUser() {
		Object userObj = getRequest().getSession().getAttribute(ApConstants.KEYCLOAK_USER);
		return (userObj != null ? (KeycloakUser) userObj : null);
	}

	/**
	 * 取得登入者帳號.
	 * 
	 * @return String userName
	 */
	protected String getUserId() {
		KeycloakUser userVo = getLoginUser();
		return (userVo != null ? userVo.getUsername() : null);
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

	protected Model setDivAuth(Model model, String funcUrl) {
		Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>> divAuthMap = (Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>>) getSession(ApConstants.DIV_AUTH_MAP);
		List<com.twfhclife.generic.api_model.FunctionDivVo> divAuth = divAuthMap.get(funcUrl);
		if(divAuth != null) {
			for(com.twfhclife.generic.api_model.FunctionDivVo vo : divAuth) {
				model.addAttribute(vo.getDivName(), vo.getDivName());//有資料的就是不能用的功能
			}
		}
		return model;
	}
}
