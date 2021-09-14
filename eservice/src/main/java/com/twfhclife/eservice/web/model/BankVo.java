package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 銀行代碼 */
	private String bankId;

	/** 銀行名稱 */
	private String bankName;

	/** 分行代碼 */
	private String branchesId;

	/** 分行名稱 */
	private String branches;

	/** 銀行帳號 */
	private String accountNo;

	/** 銀行帳戶名稱 */
	private String accountName;

	/** sql傳值用-申請項目 */
	private String transTypeStr;

	/** sql傳值用-已貸額度 */
	private BigDecimal loanAmount;

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

	public String getBranchesId() {
		return branchesId;
	}

	public void setBranchesId(String branchesId) {
		this.branchesId = branchesId;
	}

	public String getBranches() {
		return branches;
	}

	public void setBranches(String branches) {
		this.branches = branches;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTransTypeStr() {
		return transTypeStr;
	}

	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

}
