package com.twfhclife.eservice.web.model;

/**
 * 醫院科別清單
 *
 */
public class Division {
	/**
	 * 主科別/部門代碼
	 */
	private String mainDepid;
	
	/**
	 * 主科別/部門名稱
	 */
	private String mainDepname;
	
	/**
	 * 子科別區段
	 */
	private SubDivision[] subDivisions;

	public String getMainDepid() {
		return mainDepid;
	}

	public void setMainDepid(String mainDepid) {
		this.mainDepid = mainDepid;
	}

	public String getMainDepname() {
		return mainDepname;
	}

	public void setMainDepname(String mainDepname) {
		this.mainDepname = mainDepname;
	}

	public SubDivision[] getSubDivisions() {
		return subDivisions;
	}

	public void setSubDivisions(SubDivision[] subDivisions) {
		this.subDivisions = subDivisions;
	}
	
}
