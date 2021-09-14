package com.twfhclife.eservice.api.adm.domain;

import org.hibernate.validator.constraints.NotEmpty;

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
	@NotEmpty(message = "cannot empty!")
	private String sysId;

	/**
	 * 使用者代碼
	 */
	//@NotEmpty(message = "cannot empty!")
	private String userId;
	
	/**
	 * 使用者帳號
	 */
	private String userName;
	
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

}
