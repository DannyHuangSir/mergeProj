package com.twfhclife.generic.model;

/**
 * Contain USER_ENTITY, FUNCTION_ITEM, and FUNCTION_DIV tables.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class UserAuthVoEntity {

	/**
	 * 使用者代碼.
	 */
	private String userId;

	/**
	 * 使用者帳號.
	 */
	private String userName;

	/**
	 * 名.
	 */
	private String firstName;

	/**
	 * 姓.
	 */
	private String lastName;

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
	 * 授權.
	 */
	private Boolean autho;

	/**
	 * 系統名稱.
	 */
	private String sysName;

	/**
	 * DIV代碼.
	 */
	private Integer divId;

	/**
	 * DIV名稱.
	 */
	private String divName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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

	public Boolean getAutho() {
		return autho;
	}

	public void setAutho(Boolean autho) {
		this.autho = autho;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

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

}
