package com.twfhclife.adm.model;

import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RoleFuncAuthVo {
	
	/**  */
	private String roleId;
	/**  */
	private BigDecimal functionId;
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
	
	public BigDecimal getFunctionId() {
		return this.functionId;
	}
	
	public void setFunctionId(BigDecimal functionId) {
		this.functionId = functionId;
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