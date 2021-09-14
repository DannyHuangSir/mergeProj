package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class CommLogVo extends Pagination{

	/**
	 * GUID
	 */
	private String id;
	
	/**
	 * 系統別,ex:eservice,eservice_batch
	 */
	private String systemId;
	
	/**
	 * 使用者帳號ESERVICE.USER_ID
	 */
	private String userId;
	
	/**
	 * 發送類型:email,sms
	 */
	private String commType;
	
	/**
	 * 發送目標
	 */
	private String commTarget;
	
	/**
	 * 發送內容
	 */
	private String commCnt;
	
	/**
	 * 發送日期(yyyy/mm/dd hh:mm:ss)
	 */
	private java.util.Date commDate;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getCommType() {
		return commType;
	}

	public void setCommType(String commType) {
		this.commType = commType;
	}

	public String getCommTarget() {
		return commTarget;
	}

	public void setCommTarget(String commTarget) {
		this.commTarget = commTarget;
	}

	public String getCommCnt() {
		return commCnt;
	}

	public void setCommCnt(String commCnt) {
		this.commCnt = commCnt;
	}

	public java.util.Date getCommDate() {
		return commDate;
	}

	public void setCommDate(java.util.Date commDate) {
		this.commDate = commDate;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
