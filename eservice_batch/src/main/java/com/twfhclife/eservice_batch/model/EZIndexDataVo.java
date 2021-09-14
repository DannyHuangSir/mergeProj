package com.twfhclife.eservice_batch.model;

import com.google.gson.annotations.SerializedName;

public class EZIndexDataVo {

	@SerializedName("ScanTypeId")
	private String scanTypeId;
	@SerializedName("Branch")
	private String branch;
	@SerializedName("BusinessType")
	private String businessType;
	@SerializedName("FormId")
	private String formId;
	@SerializedName("PolicyNumber")
	private String policyNumber;
	@SerializedName("ApplicantId")
	private String applicantId;
	@SerializedName("InsurantId")
	private String insurantId;

	public String getScanTypeId() {
		return scanTypeId;
	}

	public void setScanTypeId(String scanTypeId) {
		this.scanTypeId = scanTypeId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getInsurantId() {
		return insurantId;
	}

	public void setInsurantId(String insurantId) {
		this.insurantId = insurantId;
	}

}
