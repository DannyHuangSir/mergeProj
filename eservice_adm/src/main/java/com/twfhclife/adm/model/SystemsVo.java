package com.twfhclife.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SystemsVo {
	
	/**  */
	private String sysId;
	/**  */
	private String sysName;
	/**  */
	private String createUser;
	/**  */
	private Date createTimestamp;
	/**  */
	private String updateUser;
	/**  */
	private Date updateTimestamp;
	
	public String getSysId() {
		return this.sysId;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public String getSysName() {
		return this.sysName;
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
