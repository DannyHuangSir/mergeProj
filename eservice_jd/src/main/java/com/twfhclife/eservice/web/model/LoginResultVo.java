package com.twfhclife.eservice.web.model;

import com.twfhclife.eservice.keycloak.model.KeycloakUser;

import java.io.Serializable;

public class LoginResultVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private KeycloakUser keycloakUser;
	
	private String loginType;

	private String returnCode;
	
	private String returnMsg;
	
	private boolean verifyValidateCode;

	public KeycloakUser getKeycloakUser() {
		return keycloakUser;
	}

	public void setKeycloakUser(KeycloakUser keycloakUser) {
		this.keycloakUser = keycloakUser;
	}
	
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public boolean isVerifyValidateCode() {
		return verifyValidateCode;
	}

	public void setVerifyValidateCode(boolean verifyValidateCode) {
		this.verifyValidateCode = verifyValidateCode;
	}
}