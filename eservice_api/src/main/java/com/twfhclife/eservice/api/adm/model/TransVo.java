package com.twfhclife.eservice.api.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class TransVo extends Pagination {
	
	private String transNum;
	private Date transDate;
	private String transType;
	private String policyNo;
	private String userId;
	private String status;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	/**
	 * 商品名稱
	 */
	private String productName;
	/**
	 * 要保人ID
	 */
	private String policyHolderId;
	
	private String startDate;
	private String endDate;
	
	//畫面顯示申請項目的中文
	private String showTransType;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPolicyHolderId() {
		return policyHolderId;
	}

	public void setPolicyHolderId(String policyHolderId) {
		this.policyHolderId = policyHolderId;
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

	public String getShowTransType() {
		return showTransType;
	}

	public void setShowTransType(String showTransType) {
		this.showTransType = showTransType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
