package com.twfhclife.eservice.api.adm.domain;

import java.util.Date;

/**
 * Systems.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class Systems {

	/**
	 * 功能代碼.
	 */
	private String sysId;

	/**
	 * 系統名稱.
	 */
	private String sysName;

	/**
	 * 建立人員.
	 */
	private String createUser;

	/**
	 * 建立時間.
	 */
	private Date createTimestamp;

	/**
	 * 更新人員.
	 */
	private String updateUser;

	/**
	 * 更新時間.
	 */
	private Date updateTimestamp;

	public String getSysId() {
		return sysId;
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
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

}
