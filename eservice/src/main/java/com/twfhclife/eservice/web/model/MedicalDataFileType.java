package com.twfhclife.eservice.web.model;

/**
 * 醫療資料檔案-文件類型
 *
 */
public class MedicalDataFileType {
	/**
	 * 文件類型代碼
	 */
	private String dtype;
	
	/**
	 * 文件名稱
	 */
	private String dname;

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}
}
