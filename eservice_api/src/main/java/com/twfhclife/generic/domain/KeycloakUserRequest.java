package com.twfhclife.generic.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class KeycloakUserRequest {

	@NotEmpty(message = "systemId cannot empty!")
	private String username;// 帳號
	private String password;
	private String name;// 姓名
	private String email;
	private String mobile;
	private String rocId;
	private String fbId;
	private String cardSn;// moicaId

	private String clientId;// eservice, eservice_adm, eform
	private String realm;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getCardSn() {
		return cardSn;
	}

	public void setCardSn(String cardSn) {
		this.cardSn = cardSn;
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
