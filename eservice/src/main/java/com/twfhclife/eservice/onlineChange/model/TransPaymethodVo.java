package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransPaymethodVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String paymethod;
	/**  */
	private String paymethodOld;
	/**  */
	private String cardType;
	/**  */
	private String cardNo;
	/**  */
	private String validMm;
	/**  */
	private String validYy;
	/**  */
	private String cardTypeOld;
	/**  */
	private String cardNoOld;
	/**  */
	private String validMmOld;
	/**  */
	private String validYyOld;
	/**  */
	private String policyNo;
	/** 匯款帳戶 */
	private String accountName;
	/** 銀行名稱 */
	private String bankId;
	private String bankName;
	/** 分行名稱 */
	private String branchId;
	private String branchName;
	/** 帳號 */
	private String accountNo;
	
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
	
	public String getPaymethod() {
		return this.paymethod;
	}
	
	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}
	
	public String getPaymethodOld() {
		return this.paymethodOld;
	}
	
	public void setPaymethodOld(String paymethodOld) {
		this.paymethodOld = paymethodOld;
	}
	
	public String getCardType() {
		return this.cardType;
	}
	
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getCardNo() {
		return this.cardNo;
	}
	
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
	
	public String getCardTypeOld() {
		return this.cardTypeOld;
	}
	
	public void setCardTypeOld(String cardTypeOld) {
		this.cardTypeOld = cardTypeOld;
	}
	
	public String getCardNoOld() {
		return this.cardNoOld;
	}
	
	public void setCardNoOld(String cardNoOld) {
		this.cardNoOld = cardNoOld;
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
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}