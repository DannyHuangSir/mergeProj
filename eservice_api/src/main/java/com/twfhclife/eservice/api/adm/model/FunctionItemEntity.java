package com.twfhclife.eservice.api.adm.model;

import java.sql.Date;

/**
 * FunctionItem entity.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class FunctionItemEntity {

	/**
	 * 功能代碼.
	 */
	private Integer functionId;

	/**
	 * 功能名稱.
	 */
	private String functionName;

	/**
	 * 功能項目類型.
	 */
	private String functionType;

	/**
	 * 功能執行URL.
	 */
	private String functionUrl;

	/**
	 * 排序.
	 */
	private Integer sort;

	/**
	 * 上層項目代碼.
	 */
	private String parentFuncId;

	/**
	 * 系統代碼.
	 */
	private String sysId;

	/**
	 * 是否啟用.
	 */
	private String active;

	/**
	 * 建立人員.
	 */
	private String createUser;

	/**
	 * 建立時間.
	 */
	private Date createData;

	/**
	 * 建立時間.
	 */
	private String updateUser;

	/**
	 * 更新時間.
	 */
	private Date updateDate;

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getParentFuncId() {
		return parentFuncId;
	}

	public void setParentFuncId(String parentFuncId) {
		this.parentFuncId = parentFuncId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateData() {
		return createData;
	}

	public void setCreateData(Date createData) {
		this.createData = createData;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
