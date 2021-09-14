package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransCertPrintVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String lang;
	/**  */
	private String passportName;
	/**  */
	private String passportNo;
	/**  */
	private String printCurr;
	/**  */
	private String deliverType;
	/**  */
	private String deliverAddr;
	/**  */
	private String beliverMail;
	/** 新增要保人護照英文姓名 */
	private String lipmPassportName;
	
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
	
	public String getPassportName() {
		return this.passportName;
	}
	
	public void setPassportName(String passportName) {
		this.passportName = passportName;
	}
	
	public String getPassportNo() {
		return this.passportNo;
	}
	
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	
	public String getPrintCurr() {
		return this.printCurr;
	}
	
	public void setPrintCurr(String printCurr) {
		this.printCurr = printCurr;
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

	public String getLipmPassportName() {
		return lipmPassportName;
	}

	public void setLipmPassportName(String lipmPassportName) {
		this.lipmPassportName = lipmPassportName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}