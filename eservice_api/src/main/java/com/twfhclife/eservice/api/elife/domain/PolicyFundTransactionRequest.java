package com.twfhclife.eservice.api.elife.domain;

public class PolicyFundTransactionRequest extends AbstractBasePolicyNoDomain {

	private static final long serialVersionUID = 1L;

	/**
	 * 交易種類
	 */
	private String transType;

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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
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