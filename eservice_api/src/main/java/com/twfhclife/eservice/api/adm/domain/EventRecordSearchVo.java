package com.twfhclife.eservice.api.adm.domain;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class EventRecordSearchVo {

	private Integer businessEventId;
	private String userId;
	private Date eventDate;
	private String sourceIp;
	private String targetIp;
	@NotEmpty(message = "cannot empty!")
	private String targetSystemId;
	private String eventName;
	private String eventCode;
	private String eventStatus;
	private String eventMsg;
	private Date createDate;
	private String createUser;

	// 開始日期
	private String startDate;
	// 結束日期
	private String endDate;

	public Integer getBusinessEventId() {
		return businessEventId;
	}

	public void setBusinessEventId(Integer businessEventId) {
		this.businessEventId = businessEventId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public String getTargetSystemId() {
		return targetSystemId;
	}

	public void setTargetSystemId(String targetSystemId) {
		this.targetSystemId = targetSystemId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventMsg() {
		return eventMsg;
	}

	public void setEventMsg(String eventMsg) {
		this.eventMsg = eventMsg;
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

}
