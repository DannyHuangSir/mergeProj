package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MaturityVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	
	/** 保單號碼*/
	private String policyNo;
	
	/** 要保人名稱 */
	private String customerName;
	
	/** 被保險人姓名 */
	private String insuredName;
	
	/** 滿期金受益人 */
	private String beneficiaryName;
	
	/** 投保始期 */
	private String effectiveDate;
	
	/** 保險金額 */
	private BigDecimal mainAmount;
	
	/** 滿期日期 */
	private String expireDate;
	
	/** 給付方式 */
	private String paymentMethod;
	
	/** 匯款帳戶 */
	private String accountName;
	
	/** 銀行名稱 */
	private String bankName;
	
	/** 分行名稱 */
	private String branchName;
	
	/** 帳號 */
	private String accountNumber;
	
	/** 郵遞區號 */
	private String postalCode;
	
	/** 郵寄地址 */
	private String postalAddr;
	/**  */
	private String transNum;
	
	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPostalAddr() {
		return postalAddr;
	}

	public void setPostalAddr(String postalAddr) {
		this.postalAddr = postalAddr;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getMainAmount() {
		return mainAmount;
	}

	public void setMainAmount(BigDecimal mainAmount) {
		this.mainAmount = mainAmount;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}