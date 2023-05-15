package com.twfhclife.jd.api_model;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public abstract class AbstractBasePolicyNoDomain implements Serializable {

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
	 * 保單號碼
	 */
	@NotEmpty(message = "policyNo cannot empty!")
	private String policyNo;

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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
}