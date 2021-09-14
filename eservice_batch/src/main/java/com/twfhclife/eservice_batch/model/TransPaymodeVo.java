package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransPaymodeVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String paymode;
	/**  */
	private String paymodeOld;
	/** 生效日*/
	private String activeDate;
	
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

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}
}

