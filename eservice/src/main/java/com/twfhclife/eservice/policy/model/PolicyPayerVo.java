package com.twfhclife.eservice.policy.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PolicyPayerVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private String id;
	/**  */
	private String policyNo;
	/**  */
	private String payerName;
	/**  */
	private String sex;
	/**  */
	private String rocId;
	/**  */
	private String bankName;
	/**  */
	private String acctNumber;
	/**  */
	private String expireDate;
	
	private String payerNameBase64;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getPayerName() {
		return this.payerName;
	}
	
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getRocId() {
		return this.rocId;
	}
	
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}
	
	public String getBankName() {
		return this.bankName;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getAcctNumber() {
		return this.acctNumber;
	}
	
	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}
	
	public String getExpireDate() {
		return this.expireDate;
	}
	
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getPayerNameBase64() {
		return payerNameBase64;
	}

	public void setPayerNameBase64(String payerNameBase64) {
		this.payerNameBase64 = payerNameBase64;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}