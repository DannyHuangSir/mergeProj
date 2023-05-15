package com.twfhclife.jd.web.model;

import java.io.Serializable;

public class UserRoleVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String userId;

	private String roleId;

	private String userType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
