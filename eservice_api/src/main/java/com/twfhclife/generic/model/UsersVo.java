package com.twfhclife.generic.model;

import java.util.Date;

public class UsersVo {

	private String userId;
	private String rocId;
	private String mobile;
	private String email;
	private Integer loginFailCount;
	private Date lastChangPasswordDate;
	private String smsFlag;
	private String mailFlag;
	private String fbId;
	private String moicaId;
	private String userType;
	private Date createDate;
	private String createUser;
	private String password;
	private String newPassword;
	private String userName;
	private String isMember;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(Integer loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public Date getLastChangPasswordDate() {
		return lastChangPasswordDate;
	}

	public void setLastChangPasswordDate(Date lastChangPasswordDate) {
		this.lastChangPasswordDate = lastChangPasswordDate;
	}

	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getMailFlag() {
		return mailFlag;
	}

	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getMoicaId() {
		return moicaId;
	}

	public void setMoicaId(String moicaId) {
		this.moicaId = moicaId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

}
