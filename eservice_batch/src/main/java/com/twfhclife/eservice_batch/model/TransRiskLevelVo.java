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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}