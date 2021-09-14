package com.twfhclife.eservice_batch.model;

public class ReportJobConditionVo {

	private Integer reportJobId;

	private String condition;
	
	private String conditionCht;
	
	private String conditionValue;
	
	private String conditionValueCht;

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
	
}
