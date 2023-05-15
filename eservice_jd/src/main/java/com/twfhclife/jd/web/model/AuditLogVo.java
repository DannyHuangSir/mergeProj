package com.twfhclife.jd.web.model;

import com.twfhclife.jd.util.DateUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class AuditLogVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String userId;
	private Date loginDate;
	private Date logoutDate;
	private String loginStatus;
	private String clientIp;
	private String startDate;
	private String endDate;
	private String loginDateStr;

	/**
	 * SR_GDPR
	 */
	private String euNationality;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLoginDateStr() {
		return DateUtil.formatDateTime(this.loginDate, "yyyy/MM/dd HH:mm:ss");
	}

	public void setLoginDateStr(String loginDateStr) {
		this.loginDateStr = loginDateStr;
	}

	public String getEuNationality() {
		return euNationality;
	}

	public void setEuNationality(String euNationality) {
		this.euNationality = euNationality;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
