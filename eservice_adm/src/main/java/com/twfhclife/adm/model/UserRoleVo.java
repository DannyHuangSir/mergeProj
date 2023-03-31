package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class UserRoleVo extends Pagination {
	
	/**  */
	private String userId;
	/**  */
	private String roleId;
	
	private String roleName;
	
	private String description;

	private String divRoleId;

	public void setDivRoleId(String divRoleId) {
		this.divRoleId = divRoleId;
	}

	public String getDivRoleId() {
		return divRoleId;
	}

	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRoleId() {
		return this.roleId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}