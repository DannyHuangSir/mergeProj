package com.twfhclife.adm.model;

import java.util.Date;

import com.twfhclife.generic.model.Pagination;

public class BusinessEventJobVo extends Pagination {
	
	public static final String EVENT_JOB_CODE = "EVJOB";
	
	private Integer eventJobId;

	private String systemId;
	
	private String eventCode;
	
	private String eventName;
	
	private String cronExp;
	
	private String executeCmd;
	
	private Integer messagingTemplageId;
	
	private Date createDate;
	
	private String createUser;
	
	private Date modifyDate;
	
	private String modifyUser;
	
	public Integer getEventJobId() {
		return eventJobId;
	}

	public void setEventJobId(Integer eventJobId) {
		this.eventJobId = eventJobId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public String getExecuteCmd() {
		return executeCmd;
	}

	public void setExecuteCmd(String executeCmd) {
		this.executeCmd = executeCmd;
	}

	public Integer getMessagingTemplageId() {
		return messagingTemplageId;
	}

	public void setMessagingTemplageId(Integer messagingTemplageId) {
		this.messagingTemplageId = messagingTemplageId;
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

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}
