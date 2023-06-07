package com.twfhclife.alliance.model;

/**
 * 此案件的上傳檔案狀態清單
 *
 */
public class SignInsuranceClaimFileDataVo {

	private String type;
	private String fileName;
	private int fileRequired;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileRequired() {
		return fileRequired;
	}

	public void setFileRequired(int fileRequired) {
		this.fileRequired = fileRequired;
	}
}
