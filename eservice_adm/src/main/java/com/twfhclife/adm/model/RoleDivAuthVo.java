package com.twfhclife.adm.model;

import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RoleDivAuthVo {

	private String roleId;
	private BigDecimal divId;
	private String createUser;
	private Date createDate;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public BigDecimal getDivId() {
		return divId;
	}

	public void setDivId(BigDecimal divId) {
		this.divId = divId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}