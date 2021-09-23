package com.twfhclife.adm.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;



public class TransRFEVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Float rfeId;

	private String transNum;
	
	private Float fdID;

	private String status;
	
	private String statusName;

	private String requestContent;
	
	private String responseContent;
	
	private String requestDate;
	
	private String responseDate;
	
	private String startDate;
	
	private String endDate;
	
	/**
	 * 此案件的上傳檔案狀態清單
	 */
	private List<TransInsuranceClaimFileDataVo> fileDatas;
	/**
	 * 保單醫療
	 * 此案件的上傳檔案狀態清單
	 */
	private List<MedicalTreatmentClaimFileDataVo> medicalTreatmentFileDatas;

	public List<MedicalTreatmentClaimFileDataVo> getMedicalTreatmentFileDatas() {
		return medicalTreatmentFileDatas;
	}

	public void setMedicalTreatmentFileDatas(List<MedicalTreatmentClaimFileDataVo> medicalTreatmentFileDatas) {
		this.medicalTreatmentFileDatas = medicalTreatmentFileDatas;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public Float getRfeId() {
		return rfeId;
	}

	public void setRfeId(Float rfeId) {
		this.rfeId = rfeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
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

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public Float getFdID() {
		return fdID;
	}

	public void setFdID(Float fdID) {
		this.fdID = fdID;
	}

	public List<TransInsuranceClaimFileDataVo> getFileDatas() {
		return fileDatas;
	}

	public void setFileDatas(List<TransInsuranceClaimFileDataVo> fileDatas) {
		this.fileDatas = fileDatas;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
