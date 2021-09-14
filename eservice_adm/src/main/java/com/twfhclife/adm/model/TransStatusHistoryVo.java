package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransStatusHistoryVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Float tsId;
	
	private String transNum;
	
	private String customerName;

	private String status;
	
	private String rejectReason;

	private String content;
	
	private String identity;
	
	private String requestDate;
	
	private String startDate;
	
	private String endDate;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Float getTsId() {
		return tsId;
	}

	public void setTsId(Float tsId) {
		this.tsId = tsId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
