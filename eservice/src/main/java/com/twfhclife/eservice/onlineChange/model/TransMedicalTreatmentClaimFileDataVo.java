package com.twfhclife.eservice.onlineChange.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class TransMedicalTreatmentClaimFileDataVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Float claimSeqId;
	private Float fdId;

	// 檔案類型
	private String type;

	/**
	 * 檔案編號</br>
	 * 長度：50
	 */
	private String fileId;

	// 檔案路經
	private String filePath;
	//文件显示的路径
	private String fileOrPng;

	// 檔案名稱
	private String fileName;
	
	private Float rfeId;
	
	private String fileBase64;

	private String fileStatus;

	private String originFileBase64;

	public String getOriginFileBase64() {
		return originFileBase64;
	}

	public void setOriginFileBase64(String originFileBase64) {
		this.originFileBase64 = originFileBase64;
	}

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Float getFdId() {
		return fdId;
	}

	public void setFdId(Float fdId) {
		this.fdId = fdId;
	}

	public String getFileOrPng() {
		return fileOrPng;
	}

	public void setFileOrPng(String fileOrPng) {
		this.fileOrPng = fileOrPng;
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

	public Float getRfeId() {
		return rfeId;
	}

	public void setRfeId(Float rfeId) {
		this.rfeId = rfeId;
	}

	public String getFileBase64() {
		return fileBase64;
	}

	public void setFileBase64(String fileBase64) {
		this.fileBase64 = fileBase64;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
