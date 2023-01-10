package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class UsersVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
	private String userNameBase64;
	private String isMember;
	private String onlineFlag;
	private String status;
	private Map<String, Boolean> identity;
	
	private String validateCode;

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

	public String getUserNameBase64() {
		return userNameBase64;
	}

	public void setUserNameBase64(String userNameBase64) {
		this.userNameBase64 = userNameBase64;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public String getOnlineFlag() {
		return onlineFlag;
	}

	public void setOnlineFlag(String onlineFlag) {
		this.onlineFlag = onlineFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Map<String, Boolean> getIdentity() {
		return identity;
	}

	public void setIdentity(Map<String, Boolean> identity) {
		this.identity = identity;
	}
	

}
