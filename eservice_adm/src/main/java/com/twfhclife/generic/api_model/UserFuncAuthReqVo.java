package com.twfhclife.generic.api_model;

/**
 * Add new function item in DB.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class UserFuncAuthReqVo {

	/**
	 * 系統代碼.
	 */
	private String sysId;

	/**
	 * 使用者代碼
	 */
	private String userId;

	private String isAdmin;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

}
