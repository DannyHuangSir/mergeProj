package com.twfhclife.adm.model;

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

	private String dataScope;
	private String fileReceived;
	private String to;
	private String sendToAlliance;
	private String code;

	private String dnsCode;
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
	//申請項目保全首家/轉收的項目
	private String fromCompanyId;

	//畫面顯示申請項目的中文
	private String showTransType;
	//异常件注記原因
	private String abnormalReason;
	//是否授權取得醫療資料
	private String fromHospitalId;
	//是否已開啟歡送醫療資料介接
	private String sendToHospitalIdItem;
	//醫療資料介接案件狀態
	private String medicalCaseStatus;
	private String signAgree;

	public String getAbnormalReason() {
		return abnormalReason;
	}

	public void setAbnormalReason(String abnormalReason) {
		this.abnormalReason = abnormalReason;
	}

	public String getFromHospitalId() {
		return fromHospitalId;
	}

	public void setFromHospitalId(String fromHospitalId) {
		this.fromHospitalId = fromHospitalId;
	}

	public String getSendToHospitalIdItem() {
		return sendToHospitalIdItem;
	}

	public void setSendToHospitalIdItem(String sendToHospitalIdItem) {
		this.sendToHospitalIdItem = sendToHospitalIdItem;
	}

	public String getMedicalCaseStatus() {
		return medicalCaseStatus;
	}

	public void setMedicalCaseStatus(String medicalCaseStatus) {
		this.medicalCaseStatus = medicalCaseStatus;
	}

	public String getFromCompanyId() {
		return fromCompanyId;
	}

	public void setFromCompanyId(String fromCompanyId) {
		this.fromCompanyId = fromCompanyId;
	}

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

	
	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getFileReceived() {
		return fileReceived;
	}

	public void setFileReceived(String fileReceived) {
		this.fileReceived = fileReceived;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSendToAlliance() {
		return sendToAlliance;
	}

	public void setSendToAlliance(String sendToAlliance) {
		this.sendToAlliance = sendToAlliance;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDnsCode() {
		return dnsCode;
	}

	public void setDnsCode(String dnsCode) {
		this.dnsCode = dnsCode;
	}

	public String getSignAgree() {
		return signAgree;
	}

	public void setSignAgree(String signAgree) {
		this.signAgree = signAgree;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
