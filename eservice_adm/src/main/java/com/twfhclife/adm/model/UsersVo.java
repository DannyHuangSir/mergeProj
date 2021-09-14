package com.twfhclife.adm.model;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UsersVo {
	
	/**  */
	private String userId;
	/** 會員類型:保戶會員=member, 一般會員=normal */
	private String userType;
	/**  */
	private String rocId;
	/**  */
	private String mobile;
	/**  */
	private String email;
	/**  */
	private BigDecimal loginFailCount;
	/**  */
	private Date lastChangPasswordDate;
	/**  */
	private String smsFlag;
	/**  */
	private String mailFlag;
	/**  */
	private String fbId;
	/**  */
	private String moicaId;
	/**  */
	private Date createDate;
	/**  */
	private String createUser;
	/** 用來判斷線上申請可以使用 */
	private String onlineFlag;
	
	private String status;
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserType() {
		return this.userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getRocId() {
		return this.rocId;
	}
	
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}
	
	public String getMobile() {
		return this.mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public BigDecimal getLoginFailCount() {
		return this.loginFailCount;
	}
	
	public void setLoginFailCount(BigDecimal loginFailCount) {
		this.loginFailCount = loginFailCount;
	}
	
	public Date getLastChangPasswordDate() {
		return this.lastChangPasswordDate;
	}
	
	public void setLastChangPasswordDate(Date lastChangPasswordDate) {
		this.lastChangPasswordDate = lastChangPasswordDate;
	}
	
	public String getSmsFlag() {
		return this.smsFlag;
	}
	
	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}
	
	public String getMailFlag() {
		return this.mailFlag;
	}
	
	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}
	
	public String getFbId() {
		return this.fbId;
	}
	
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	
	public String getMoicaId() {
		return this.moicaId;
	}
	
	public void setMoicaId(String moicaId) {
		this.moicaId = moicaId;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

