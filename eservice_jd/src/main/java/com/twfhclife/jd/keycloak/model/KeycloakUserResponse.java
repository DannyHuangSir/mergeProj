package com.twfhclife.jd.keycloak.model;

import java.io.Serializable;

public class KeycloakUserResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String userId;
	private String username;
	private String email;
	private String mobile;
	private String rocId;
	private String clientId;// eservice, eservice_adm, eform
	private String realm;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRocId() {
		return rocId;
	}
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}

}
