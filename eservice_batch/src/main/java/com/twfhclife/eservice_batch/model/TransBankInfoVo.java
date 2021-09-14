package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransBankInfoVo {

	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String bankId;
	/**  */
	private String branchId;
	/**  */
	private String accountNo;
	/**  */
	private String accountName;

	private String bankName;

	private String branchName;

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

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBranchId() {
		return this.branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return this.accountName;
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
}