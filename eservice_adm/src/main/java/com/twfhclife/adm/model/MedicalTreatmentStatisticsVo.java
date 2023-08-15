package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class MedicalTreatmentStatisticsVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**申請日期區間*/
	private String startDate;
	
	private String endDate;
	
	/**申請來源*/
	private String fromCompanyId;
	
	/** 申請項目 */
	private List<String> applyItemList;
	
	private String applyItem;
	
	private String applyItemName;
	
	/**申請狀態*/
	private List<String> status;
	
	/** 保單號碼 */
	private String policyNo;
	
	/**被保人身分證字號*/
	private String lilipiIdNo;
	
	/**要保人身分證字號*/
	private String lilipmIdNo;
	
	/**是否已紙本回收。 "1":收到，"2":沒收到*/
	private List<String> fileReceivedList;
	
	private String fileReceived;
	
	/**是否已開啓歡送公會聯盟鏈*/
	private List<String> sendAllianceList;
	
	private String sendAlliance;

	/**異常件註記原因*/
	private String rejectReason;
	/**醫療資料介接案件狀態*/
	private String allianceStatus;
	private List<String> allianceStatusList;
	/**是否傳送其他醫療單位(Y/N)</br>  */
	private String fromHospitalId;

	private List<String>  column;
	
	private List<String>  columnName;

	private String applyDate;
	private String signAgree;

	public String getSignAgree() {
		return signAgree;
	}

	public void setSignAgree(String signAgree) {
		this.signAgree = signAgree;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public List<String> getAllianceStatusList() {
		return allianceStatusList;
	}

	public void setAllianceStatusList(List<String> allianceStatusList) {
		this.allianceStatusList = allianceStatusList;
	}

	public String getAllianceStatus() {
		return allianceStatus;
	}

	public void setAllianceStatus(String allianceStatus) {
		this.allianceStatus = allianceStatus;
	}

	public String getFromHospitalId() {
		return fromHospitalId;
	}

	public void setFromHospitalId(String fromHospitalId) {
		this.fromHospitalId = fromHospitalId;
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


	public String getFromCompanyId() {
		return fromCompanyId;
	}


	public void setFromCompanyId(String fromCompanyId) {
		this.fromCompanyId = fromCompanyId;
	}


	public String getApplyItemName() {
		return applyItemName;
	}


	public void setApplyItemName(String applyItemName) {
		this.applyItemName = applyItemName;
	}


	public List<String> getApplyItemList() {
		return applyItemList;
	}


	public void setApplyItemList(List<String> applyItemList) {
		this.applyItemList = applyItemList;
	}


	public List<String> getStatus() {
		return status;
	}


	public void setStatus(List<String> status) {
		this.status = status;
	}


	public String getPolicyNo() {
		return policyNo;
	}


	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}


	public String getLilipiIdNo() {
		return lilipiIdNo;
	}


	public void setLilipiIdNo(String lilipiIdNo) {
		this.lilipiIdNo = lilipiIdNo;
	}


	public String getLilipmIdNo() {
		return lilipmIdNo;
	}


	public void setLilipmIdNo(String lilipmIdNo) {
		this.lilipmIdNo = lilipmIdNo;
	}


	public String getFileReceived() {
		return fileReceived;
	}


	public void setFileReceived(String fileReceived) {
		this.fileReceived = fileReceived;
	}


	public String getSendAlliance() {
		return sendAlliance;
	}


	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}


	public String getRejectReason() {
		return rejectReason;
	}


	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}


	public String getApplyItem() {
		return applyItem;
	}


	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}


	public List<String> getFileReceivedList() {
		return fileReceivedList;
	}


	public void setFileReceivedList(List<String> fileReceivedList) {
		this.fileReceivedList = fileReceivedList;
	}


	public List<String> getSendAllianceList() {
		return sendAllianceList;
	}


	public void setSendAllianceList(List<String> sendAllianceList) {
		this.sendAllianceList = sendAllianceList;
	}


	public List<String> getColumn() {
		return column;
	}


	public void setColumn(List<String> column) {
		this.column = column;
	}


	public List<String> getColumnName() {
		return columnName;
	}


	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
