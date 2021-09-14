package com.twfhclife.eservice.api.elife.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class TransHistoryListRequest implements Serializable {

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
	 * 交易種類
	 */
	private String transType;

	/**
	 * 交易狀態
	 */
	private String transStatus;

	/**
	 * 保單號碼
	 */
	private String policyNo;

	/**
	 * 開始日期
	 */
	private String startDate;

	/**
	 * 結束日期
	 */
	private String endDate;

	/**
	 * 當前頁數
	 */
	private Integer pageNum;

	/**
	 * 每頁幾筆
	 */
	private Integer pageSize;

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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}