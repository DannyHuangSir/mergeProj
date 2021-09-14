package com.twfhclife.eservice.api.elife.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class TransHistoryDetailRequest implements Serializable {

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
	 * 交易序號
	 */
	@NotEmpty(message = "transNums cannot empty!")
	private List<String> transNums;

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

	public List<String> getTransNums() {
		return transNums;
	}

	public void setTransNums(List<String> transNums) {
		this.transNums = transNums;
	}
}