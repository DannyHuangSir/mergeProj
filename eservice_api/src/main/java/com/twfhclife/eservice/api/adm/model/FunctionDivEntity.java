package com.twfhclife.eservice.api.adm.model;

import java.sql.Date;

/**
 * FunctionDiv entity.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class FunctionDivEntity {

	/**
	 * DIV代碼.
	 */
	private Integer divId;

	/**
	 * DIV名稱.
	 */
	private String divName;

	/**
	 * 功能代碼.
	 */
	private Integer functionId;

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

	public Integer getDivId() {
		return divId;
	}

	public void setDivId(Integer divId) {
		this.divId = divId;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
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
