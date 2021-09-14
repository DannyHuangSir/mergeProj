package com.twfhclife.adm.domain;

/**
 * Request bean for getting function items by user account.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class GetFuncItemsByUserAcctRequest {

	private String userAcct;
	private String roleId;
	private String roleName;

	public String getUserAcct() {
		return userAcct;
	}

	public void setUserAcct(String userAcct) {
		this.userAcct = userAcct;
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
