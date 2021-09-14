package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransBounsVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}