package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransContactInfoFileDataVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Float seqId;

	// 檔案類型
	private String type;

	// 檔案路經
	private String filePath;

	// 檔案名稱
	private String fileName;
	
	private Float rfeId;
	
	private String fileId;
	
	private String fileBase64;

	public Float getSeqId() {
		return seqId;
	}

	public void setSeqId(Float seqId) {
		this.seqId = seqId;
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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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
