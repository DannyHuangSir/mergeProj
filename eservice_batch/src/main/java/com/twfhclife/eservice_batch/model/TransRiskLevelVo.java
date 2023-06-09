package com.twfhclife.eservice_batch.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class TransRiskLevelVo {

	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String riskLevelOld;
	/**  */
	private String riskLevelNew;
	
	private int riskScore;
	
	private String riskLevelDesc;

	private String rocId;
	
	private String choose;
	
	/**
	 * 年齡、學歷、重大醫療事故 規則
	 * 1.符合規則但未勾選 2.符合有勾選 3.非符合規則內
	 */
	private String ruleStatus;

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTransNum() {
		return this.transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getRiskLevelOld() {
		return riskLevelOld;
	}

	public void setRiskLevelOld(String riskLevelOld) {
		this.riskLevelOld = riskLevelOld;
	}

	public String getRiskLevelNew() {
		return riskLevelNew;
	}

	public void setRiskLevelNew(String riskLevelNew) {
		this.riskLevelNew = riskLevelNew;
	}

	public int getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(int riskScore) {
		this.riskScore = riskScore;
	}

	public String getRiskLevelDesc() {
		return riskLevelDesc;
	}

	public void setRiskLevelDesc(String riskLevelDesc) {
		this.riskLevelDesc = riskLevelDesc;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}