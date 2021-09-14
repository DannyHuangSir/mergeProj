package com.twfhclife.generic.domain;

import org.hibernate.validator.constraints.NotEmpty;

import com.twfhclife.generic.annotation.ValidateString;

public class KeycloakUserAddRequest {

	@NotEmpty(message = "cannot empty!")
	private String username;// 帳號
	@NotEmpty(message = "cannot empty!")
	private String password;
	private String name;// 姓名
	private String email;
	private String mobile;
	@NotEmpty(message = "cannot empty!")
	private String rocId;
	private String fbId;
	private String cardSn;// moicaId
	@NotEmpty(message = "cannot empty!")
	@ValidateString(acceptedValues={"member", "normal"}, message="invalid value!")
	private String userType;
	private String clientId;// eservice, eservice_adm, eform
	private String realm;
	@NotEmpty(message = "cannot empty!")
	private String createUser;

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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
