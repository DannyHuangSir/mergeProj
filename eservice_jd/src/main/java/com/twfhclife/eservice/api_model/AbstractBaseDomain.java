package com.twfhclife.eservice.api_model;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public abstract class AbstractBaseDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查詢的系統代號
	 */
	@NotEmpty(message = "sysId cannot empty!")
	private String sysId;

	/**
	 * 查詢的使用者代號
	 */
	@NotEmpty(message = "userId cannot empty!")
	private String userId;

	/**
	 * 保戶證號
	 */
	@NotEmpty(message = "policyHolderId cannot empty!")
	private String policyHolderId;
	
	/**
	 * 類型名稱
	 */
	private String typeName;


	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPolicyHolderId() {
		return policyHolderId;
	}

	public void setPolicyHolderId(String policyHolderId) {
		this.policyHolderId = policyHolderId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}