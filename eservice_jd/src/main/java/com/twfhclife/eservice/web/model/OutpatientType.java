package com.twfhclife.eservice.web.model;

import java.util.List;

/**
 * 就診醫院類型
 *
 */
public class OutpatientType {
	/**
	 * 就診類型代碼
	 */
	private String otype;
	
	/**
	 * 就診類型名稱
	 */
	private String oname;

	/**
	 * 醫院科別清單
	 */
	private Division[] divisions;
	
	/**
	 * 醫院資料群組
	 */
	private List<MedicalDataFileGroup> fileGroup;

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getOname() {
		return oname;
	}

	public void setOname(String oname) {
		this.oname = oname;
	}

	public Division[] getDivisions() {
		return divisions;
	}

	public void setDivisions(Division[] divisions) {
		this.divisions = divisions;
	}

	public List<MedicalDataFileGroup> getFileGroup() {
		return fileGroup;
	}

	public void setFileGroup(List<MedicalDataFileGroup> fileGroup) {
		this.fileGroup = fileGroup;
	}
}
