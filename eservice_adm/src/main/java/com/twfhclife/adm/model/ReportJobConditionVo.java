package com.twfhclife.adm.model;

import java.util.Date;

public class ReportJobConditionVo {
	
	private Integer reportJobId;

	private String condition;
	
	private String conditionCht;
	
	private String conditionValue;
	
	private String conditionValueCht;
	
	private String createUser;
	
	private Date createDate;
	
	private String modifyUser;
	
	private Date modifyDate;

	public Integer getReportJobId() {
		return reportJobId;
	}

	public void setReportJobId(Integer reportJobId) {
		this.reportJobId = reportJobId;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getConditionCht() {
		return conditionCht;
	}

	public void setConditionCht(String conditionCht) {
		this.conditionCht = conditionCht;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getConditionValueCht() {
		return conditionValueCht;
	}

	public void setConditionValueCht(String conditionValueCht) {
		this.conditionValueCht = conditionValueCht;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
