package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransCustInfoVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String mobile;
	/**  */
	private String email;
	/**  */
	private String smsFlag;
	/**  */
	private String mailFlag;
	/**  */
	private String mobileOld;
	/**  */
	private String emailOld;
	/**  */
	private String smsFlagOld;
	/**  */
	private String mailFlagOld;
	
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
	
	public String getMobile() {
		return this.mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSmsFlag() {
		return this.smsFlag;
	}
	
	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}
	
	public String getMailFlag() {
		return this.mailFlag;
	}
	
	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}
	
	public String getMobileOld() {
		return this.mobileOld;
	}
	
	public void setMobileOld(String mobileOld) {
		this.mobileOld = mobileOld;
	}
	
	public String getEmailOld() {
		return this.emailOld;
	}
	
	public void setEmailOld(String emailOld) {
		this.emailOld = emailOld;
	}
	
	public String getSmsFlagOld() {
		return this.smsFlagOld;
	}
	
	public void setSmsFlagOld(String smsFlagOld) {
		this.smsFlagOld = smsFlagOld;
	}
	
	public String getMailFlagOld() {
		return this.mailFlagOld;
	}
	
	public void setMailFlagOld(String mailFlagOld) {
		this.mailFlagOld = mailFlagOld;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}