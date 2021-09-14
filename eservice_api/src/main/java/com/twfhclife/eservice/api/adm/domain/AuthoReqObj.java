package com.twfhclife.eservice.api.adm.domain;

/**
 * Authorization API request object.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class AuthoReqObj {

	/**
	 * 使用這帳號.
	 */
	private String userId;

	/**
	 * 系統代碼.
	 */
	private String sysId;

	/**
	 * 角色名稱查詢字串.
	 */
	private String roleNameQuery;

	/**
	 * 角色代碼.
	 */
	private String roleId;

	/**
	 * 角色名稱.
	 */
	private String roleName;

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

	public String getRoleNameQuery() {
		return roleNameQuery;
	}

	public void setRoleNameQuery(String roleNameQuery) {
		this.roleNameQuery = roleNameQuery;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
