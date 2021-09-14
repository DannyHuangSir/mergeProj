package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransAnnuityMethodVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String annuityMethod;
	/**  */
	private String annuityMethodOld;
	
	/**
	 * 保證期間
	 */
	private String guaranteePeriod;
	
	private String guaranteePeriodOld;
	
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
	
	public String getAnnuityMethod() {
		return this.annuityMethod;
	}
	
	public void setAnnuityMethod(String annuityMethod) {
		this.annuityMethod = annuityMethod;
	}
	
	public String getAnnuityMethodOld() {
		return this.annuityMethodOld;
	}
	
	public void setAnnuityMethodOld(String annuityMethodOld) {
		this.annuityMethodOld = annuityMethodOld;
	}

	public String getGuaranteePeriod() {
		return guaranteePeriod;
	}

	public void setGuaranteePeriod(String guaranteePeriod) {
		this.guaranteePeriod = guaranteePeriod;
	}

	public String getGuaranteePeriodOld() {
		return guaranteePeriodOld;
	}

	public void setGuaranteePeriodOld(String guaranteePeriodOld) {
		this.guaranteePeriodOld = guaranteePeriodOld;
	}
	
}

