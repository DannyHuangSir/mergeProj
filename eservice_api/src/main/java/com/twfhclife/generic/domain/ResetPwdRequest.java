package com.twfhclife.generic.domain;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

public class ResetPwdRequest {

	private String userId;// UUID
	@NotEmpty(message = "cannot empty!")
	private String username;// 帳號
	private String oldPassword;// 舊密碼
	@NotEmpty(message = "cannot empty!")
	private String newPassword;// 新密碼
	private String accessToken;
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

}
