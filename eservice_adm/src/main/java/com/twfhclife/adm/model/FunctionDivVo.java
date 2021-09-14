package com.twfhclife.adm.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FunctionDivVo {
	
	/**  */
	private BigDecimal divId;
	/**  */
	private String divName;
	/**  */
	private BigDecimal functionId;
	/**  */
	private String createUser;
	/**  */
	private Date createTimestamp;
	/**  */
	private String updateUser;
	/**  */
	private Date updateTimestamp;
	
	public BigDecimal getDivId() {
		return this.divId;
	}
	
	public void setDivId(BigDecimal divId) {
		this.divId = divId;
	}
	
	public String getDivName() {
		return this.divName;
	}
	
	public void setDivName(String divName) {
		this.divName = divName;
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
	
	public Date getCreateTimestamp() {
		return this.createTimestamp;
	}
	
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
	public String getUpdateUser() {
		return this.updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public Date getUpdateTimestamp() {
		return this.updateTimestamp;
	}
	
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}