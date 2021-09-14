package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransReducePolicyVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	
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
}

