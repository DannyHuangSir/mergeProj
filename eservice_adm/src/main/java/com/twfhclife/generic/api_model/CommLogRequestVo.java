package com.twfhclife.generic.api_model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class CommLogRequestVo extends Pagination{
	
	private String id;
	
	private String systemId;
	
	private String userName;
	
	private String commType;
	
	private String commTarget;
	
	private String commDate;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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


	public String getCommDate() {
		return commDate;
	}

	public void setCommDate(String commDate) {
		this.commDate = commDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
