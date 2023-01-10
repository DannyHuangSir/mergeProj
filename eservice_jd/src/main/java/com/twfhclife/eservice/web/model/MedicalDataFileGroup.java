package com.twfhclife.eservice.web.model;

/**
 * 醫療資料檔案群
 *
 */
public class MedicalDataFileGroup {
	/**
	 * 此群組的必選項目有幾個
	 */
	private String require;

	private MedicalDataFileType[] fileType;

	public String getRequire() {
		return require;
	}

	public void setRequire(String require) {
		this.require = require;
	}

	public MedicalDataFileType[] getFileType() {
		return fileType;
	}

	public void setFileType(MedicalDataFileType[] fileType) {
		this.fileType = fileType;
	}
	
}
