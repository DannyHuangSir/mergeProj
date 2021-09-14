package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransLoanVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/** 欲借金額 */
	private BigDecimal loanAmount;
	/** 已借金額 */
	private BigDecimal loanedAmount;
	/** 保單號碼*/
	private String policyNo;
	/** 要保人名稱 */
	private String customerName;
	/** 被保險人姓名 */
	private String insuredName;
	/** 保單商品名稱 */
	private String productName;
	/**  */
	private BigDecimal remainLoanValue;
	/** 匯款帳戶 */
	private String accountName;
	/** 銀行名稱 */
	private String bankId;
	private String bankName;
	/** 分行名稱 */
	private String branchId;
	private String branchName;
	/** 帳號 */
	private String accountNumber;
	
	private String noticeMethod;
	private String noticeTarget;
	
	public String getNoticeMethod() {
		return noticeMethod;
	}

	public void setNoticeMethod(String noticeMethod) {
		this.noticeMethod = noticeMethod;
	}

	public String getNoticeTarget() {
		return noticeTarget;
	}

	public void setNoticeTarget(String noticeTarget) {
		this.noticeTarget = noticeTarget;
	}

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
	
	public BigDecimal getLoanAmount() {
		return this.loanAmount;
	}
	
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getRemainLoanValue() {
		return remainLoanValue;
	}

	public void setRemainLoanValue(BigDecimal remainLoanValue) {
		this.remainLoanValue = remainLoanValue;
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
	
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getLoanedAmount() {
		return loanedAmount;
	}

	public void setLoanedAmount(BigDecimal loanedAmount) {
		this.loanedAmount = loanedAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}