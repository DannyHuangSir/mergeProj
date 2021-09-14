package com.twfhclife.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RoleSysAuthVo {
	
	/**  */
	private String roleId;
	/**  */
	private String sysId;
	/**  */
	private String sysName;
	/**  */
	private String createUser;
	/**  */
	private Date createDate;
	
	public String getRoleId() {
		return this.roleId;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getSysId() {
		return this.sysId;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}