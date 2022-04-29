package com.twfhclife.alliance.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * 向聯盟查詢案件資訊才會有的物件
 *
 */
public class MedicalTreatmentClaimApplyDataVo {

	private Float miId;

	private Float claimId;
	
	/**
	 * 申請資料序號Stirng(2)
	 */
	private String seNo;
	
	/**
	 * 授權開始時間
	 * String(8),採西元年YYYYMMDD,例如：19801115
	 */
	private String hsTime;
	
	/**
	 * 授權結束時間
	 * String(8),採西元年YYYYMMDD,例如：19801115
	 */
	private String heTime;
	
	/**
	 * 就診類型代碼,門/急/住,String(50)
	 */
	private String otype;
	
	/**
	 * 科別代碼,String(50)
	 */
	private String depid;

	private String path;

	private String fileName;

	private String fileId;

	private String fileBase64;

	private String fileStatus;

	private String transNum;

	private String caseId;

	/**
	 * 文件類型,Stirng(200),請參考(醫療資料檔案類型代碼表)
	 * 可使用MedicalTreatmentClaimFileTypeEnum
	 */
	private List<String> dtypes = Lists.newArrayList();

	private List<Map<String, String>> dtypeList = Lists.newArrayList();
	
	private List<MedicalTreatmentClaimApplyDataFileDataVo> fileData;

	public Float getMiId() {
		return miId;
	}

	public void setMiId(Float miId) {
		this.miId = miId;
	}

	public Float getClaimId() { return claimId; }

	public void setClaimId(Float claimId) { this.claimId = claimId; }

	public String getSeNo() {
		return seNo;
	}

	public void setSeNo(String seNo) {
		this.seNo = seNo;
	}

	public String getHsTime() {
		return hsTime;
	}

	public void setHsTime(String hsTime) {
		this.hsTime = hsTime;
	}

	public String getHeTime() {
		return heTime;
	}

	public void setHeTime(String heTime) {
		this.heTime = heTime;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() { return fileName; }

	public void setFileName(String fileName) { this.fileName = fileName; }

	public String getFileId() { return fileId; }

	public void setFileId(String fileId) { this.fileId = fileId; }

	public String getFileBase64() { return fileBase64; }

	public void setFileBase64(String fileBase64) { this.fileBase64 = fileBase64; }

	public String getFileStatus() { return fileStatus; }

	public void setFileStatus(String fileStatus) { this.fileStatus = fileStatus; }

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public List<String> getDtypes() {
		return dtypes;
	}

	public void setDtypes(List<String> dtypes) {
		this.dtypes = dtypes;
	}

	public List<Map<String, String>> getDtypeList() {
		return dtypeList;
	}

	public void setDtypeList(List<Map<String, String>> dtypeList) {
		this.dtypeList = dtypeList;
	}

	public List<MedicalTreatmentClaimApplyDataFileDataVo> getFileData() {
		return fileData;
	}

	public void setFileData(List<MedicalTreatmentClaimApplyDataFileDataVo> fileData) {
		this.fileData = fileData;
	}

}
