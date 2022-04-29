package com.twfhclife.alliance.model;

public class MedicalTreatmentClaimApplyDataFileDataVo {

	/**
	 * 文件類型
	 * 可使用MedicalTreatmentClaimFileTypeEnum,get/set value.
	 */
	private String dtype;
	
	/**
	 * 文件唯一編碼，未來若資料需重新上傳時，將透過此ID
	 */
	private String fileId;
	
	/**
	 * 檔案狀態
	 */
	private String fileStatus;

	/**
	 * 可使用MedicalTreatmentClaimFileTypeEnum,get/set value.
	 * @return String
	 */
	public String getDtype() {
		return dtype;
	}

	/**
	 * 可使用MedicalTreatmentClaimFileTypeEnum,get/set value.
	 * @param dtype
	 */
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

}
