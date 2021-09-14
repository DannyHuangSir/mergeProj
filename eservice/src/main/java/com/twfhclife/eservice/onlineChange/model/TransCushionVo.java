package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransCushionVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String cushionMode;
	/**  */
	private String cushionModeOld;
	
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
	
	public String getCushionMode() {
		return this.cushionMode;
	}
	
	public void setCushionMode(String cushionMode) {
		this.cushionMode = cushionMode;
	}
	
	public String getCushionModeOld() {
		return this.cushionModeOld;
	}
	
	public void setCushionModeOld(String cushionModeOld) {
		this.cushionModeOld = cushionModeOld;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}