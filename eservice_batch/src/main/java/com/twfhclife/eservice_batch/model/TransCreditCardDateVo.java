package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransCreditCardDateVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String validMm;
	/**  */
	private String validYy;
	/**  */
	private String validMmOld;
	/**  */
	private String validYyOld;
	
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
	
	public String getValidMm() {
		return this.validMm;
	}
	
	public void setValidMm(String validMm) {
		this.validMm = validMm;
	}
	
	public String getValidYy() {
		return this.validYy;
	}
	
	public void setValidYy(String validYy) {
		this.validYy = validYy;
	}
	
	public String getValidMmOld() {
		return this.validMmOld;
	}
	
	public void setValidMmOld(String validMmOld) {
		this.validMmOld = validMmOld;
	}
	
	public String getValidYyOld() {
		return this.validYyOld;
	}
	
	public void setValidYyOld(String validYyOld) {
		this.validYyOld = validYyOld;
	}
}

