package com.twfhclife.adm.model;

import java.io.Serializable;

public class MedicalTreatmentClaimFileDataVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Float fdId;
	private Float claimSeqId;
	//申請序號
	private String transNum;
	// 檔案類型
	private String type;

	// 檔案路經
	private String filePath;

	// 檔案名稱
	private String fileName;
	
	private String fileBase64;
	//文件大小
	private Float fileSize;
	//重新上傳原因
	private String reUpload;
	//聯盟案件狀態
	private String allianceStatus;

	public String getAllianceStatus() {
		return allianceStatus;
	}

	public void setAllianceStatus(String allianceStatus) {
		this.allianceStatus = allianceStatus;
	}

	public String getReUpload() {
		return reUpload;
	}

	public void setReUpload(String reUpload) {
		this.reUpload = reUpload;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}


	public Float getFdId() {
		return fdId;
	}

	public void setFdId(Float fdId) {
		this.fdId = fdId;
	}

	public Float getFileSize() {
		return fileSize;
	}

	public void setFileSize(Float fileSize) {
		this.fileSize = fileSize;
	}

	public Float getClaimSeqId() {
		return claimSeqId;
	}

	public void setClaimSeqId(Float claimSeqId) {
		this.claimSeqId = claimSeqId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileBase64() {
		return fileBase64;
	}

	public void setFileBase64(String fileBase64) {
		this.fileBase64 = fileBase64;
	}
	
}
