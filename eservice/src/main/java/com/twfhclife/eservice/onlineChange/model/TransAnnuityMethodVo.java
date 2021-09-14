package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransAnnuityMethodVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String annuityMethod;
	/**  */
	private String annuityMethodOld;
	
	private String guaranteePeriod;//保證期間
	
	private String guaranteePeriodOld;//舊保證期間
	
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}