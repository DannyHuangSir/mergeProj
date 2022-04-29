package com.twfhclife.eservice.web.model;

/**
 * 醫院科別清單-子科別
 *
 */
public class SubDivision {

	/**
	 * 科別代碼
	 */
	private String depid;
	
	/**
	 * 科別名稱
	 */
	private String depname;

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public String getDepname() {
		return depname;
	}

	public void setDepname(String depname) {
		this.depname = depname;
	}
}
