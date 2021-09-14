package com.twfhclife.alliance.model;

import java.util.List;

public class DnsReturnVo {

	/**
	 * 資料數量
	 */
	private String dataNumber;
	
	/**
	 * *更新筆數
	 */
	private String updateNumber;
	
	private List<DnsContentVo> content;

	public String getDataNumber() {
		return dataNumber;
	}

	public void setDataNumber(String dataNumber) {
		this.dataNumber = dataNumber;
	}

	public String getUpdateNumber() {
		return updateNumber;
	}

	public void setUpdateNumber(String updateNumber) {
		this.updateNumber = updateNumber;
	}

	public List<DnsContentVo> getContent() {
		return content;
	}

	public void setContent(List<DnsContentVo> content) {
		this.content = content;
	}

}
