package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BankInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String bankId;
	/**  */
	private String branchId;
	/**  */
	private String bankName;
	/**  */
	private String branchName;
	/**  */
	private String address;
	
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
	
	public String getBankName() {
		return this.bankName;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getBranchName() {
		return this.branchName;
	}
	
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}