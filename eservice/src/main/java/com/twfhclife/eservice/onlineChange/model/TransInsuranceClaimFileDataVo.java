package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransInsuranceClaimFileDataVo implements Serializable {

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
