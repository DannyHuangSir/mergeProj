package com.twfhclife.adm.model;

import java.io.Serializable;

public class TransInsuranceClaimFileDataVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Float claimSeqId;

	// 檔案類型
	private String type;

	// 檔案路經
	private String filePath;

	// 檔案名稱
	private String fileName;
	
	private String fileBase64;

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
