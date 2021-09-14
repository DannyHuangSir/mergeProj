package com.twfhclife.adm.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class RoleMgntVo extends Pagination {
	
	private String systemId;
	
	private String roleName;
	
	private String roleId;

	private List<String> sysIds;

	private List<String> funIds;

	private List<String> divIds;
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<String> getSysIds() {
		return sysIds;
	}

	public void setSysIds(List<String> sysIds) {
		this.sysIds = sysIds;
	}

	public List<String> getFunIds() {
		return funIds;
	}

	public void setFunIds(List<String> funIds) {
		this.funIds = funIds;
	}

	public List<String> getDivIds() {
		return divIds;
	}

	public void setDivIds(List<String> divIds) {
		this.divIds = divIds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}