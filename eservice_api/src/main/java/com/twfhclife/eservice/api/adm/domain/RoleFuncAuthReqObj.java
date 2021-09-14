package com.twfhclife.eservice.api.adm.domain;

import java.util.List;

/**
 * Delete or insert new ROLE_FUNC_AUTH request object.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class RoleFuncAuthReqObj {

	/**
	 * 角色Id.
	 */
	private String roleId;

	/**
	 * 功能.
	 */
	private List<RoleFuncAuthVo> roleFunc;

	public List<RoleFuncAuthVo> getRoleFunc() {
		return roleFunc;
	}

	public void setRoleFunc(List<RoleFuncAuthVo> roleFunc) {
		this.roleFunc = roleFunc;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
