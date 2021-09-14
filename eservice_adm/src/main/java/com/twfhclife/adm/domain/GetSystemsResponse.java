package com.twfhclife.adm.domain;

import java.util.Date;

/**
 * Response java bean for getting system list.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class GetSystemsResponse {

	/**
	 * 系統名稱.
	 */
	private String system;
	
	/**
	 * 系統ID
	 */
	private String systemId;

	/**
	 * 使用者角色Id.
	 */
	private String userRoleId;

	/**
	 * 使用者角色.
	 */
	private String userRole;

	/**
	 * 使用者名稱.
	 */
	private String userName;

	/**
	 * 使用者帳號.
	 */
	private String userAcct;

	/**
	 * 狀態.
	 */
	private String status;

	/**
	 * 啟用時間.
	 */
	private Date createTime;

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAcct() {
		return userAcct;
	}

	public void setUserAcct(String userAcct) {
		this.userAcct = userAcct;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
