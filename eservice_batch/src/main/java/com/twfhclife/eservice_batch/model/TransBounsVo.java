package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransBounsVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String bounsMode;
	/**  */
	private String bounsModeOld;
	
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
	
	public String getBounsMode() {
		return this.bounsMode;
	}
	
	public void setBounsMode(String bounsMode) {
		this.bounsMode = bounsMode;
	}
	
	public String getBounsModeOld() {
		return this.bounsModeOld;
	}
	
	public void setBounsModeOld(String bounsModeOld) {
		this.bounsModeOld = bounsModeOld;
	}
}

