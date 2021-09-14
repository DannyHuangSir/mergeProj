package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransValuePrintVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String lang;
	/**  */
	private String deliverType;
	/**  */
	private String deliverAddr;
	/**  */
	private String beliverMail;
	
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
	
	public String getLang() {
		return this.lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getDeliverType() {
		return this.deliverType;
	}
	
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	
	public String getDeliverAddr() {
		return this.deliverAddr;
	}
	
	public void setDeliverAddr(String deliverAddr) {
		this.deliverAddr = deliverAddr;
	}
	
	public String getBeliverMail() {
		return this.beliverMail;
	}
	
	public void setBeliverMail(String beliverMail) {
		this.beliverMail = beliverMail;
	}
}