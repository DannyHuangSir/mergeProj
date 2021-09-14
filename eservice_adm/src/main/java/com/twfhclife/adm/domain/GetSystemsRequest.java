package com.twfhclife.adm.domain;

/**
 * Java bean for getting system list.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class GetSystemsRequest {

	private String sysName;

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

}
