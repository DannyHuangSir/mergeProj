package com.twfhclife.jd.web.model;

import java.io.Serializable;

public class AuthenticationVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String authenticationType;
	private String authenticationNum;

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getAuthenticationNum() {
		return authenticationNum;
	}

	public void setAuthenticationNum(String authenticationNum) {
		this.authenticationNum = authenticationNum;
	}

}
