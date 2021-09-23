package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class TransChangePremiumVo {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String paymode;
	/**  */
	private String paymodeOld;

	private BigDecimal amount;

	private BigDecimal oldAmount;

	private String paidToDate;

	private String nextPayDate;

	private String premYear;

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
	
	public String getPaymode() {
		return this.paymode;
	}
	
	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}
	
	public String getPaymodeOld() {
		return this.paymodeOld;
	}
	
	public void setPaymodeOld(String paymodeOld) {
		this.paymodeOld = paymodeOld;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(BigDecimal oldAmount) {
		this.oldAmount = oldAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getPremYear() {
		return premYear;
	}

	public void setPremYear(String premYear) {
		this.premYear = premYear;
	}

	public String getPaidToDate() {
		return paidToDate;
	}

	public void setPaidToDate(String paidToDate) {
		this.paidToDate = paidToDate;
	}

	public String getNextPayDate() {
		return nextPayDate;
	}

	public void setNextPayDate(String nextPayDate) {
		this.nextPayDate = nextPayDate;
	}
}