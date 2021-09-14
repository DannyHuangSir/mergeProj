package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransBankInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}