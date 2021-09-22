package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransPaymodeVo extends AbstractOnlineChangeModelBean {
	
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
}