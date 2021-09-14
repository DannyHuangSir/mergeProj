package com.twfhclife.eservice_batch.model;

import java.util.Date;

public class TransStatusHistoryVo {
	private String transNum;
	private String userIdentity;
	private String customerName;
	private String rejectReason;
	private String status;
	private String content;
	private Date requestDate;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
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

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
}

