package com.twfhclife.eservice.api.adm.domain;

import java.util.List;

/**
 * Function.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class Function {

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
	 * 創建者
	 */
	private String createUser;

	/**
	 * 區塊清單.
	 */
	private List<FunctionDiv> divList;

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public List<FunctionDiv> getDivList() {
		return divList;
	}

	public void setDivList(List<FunctionDiv> divList) {
		this.divList = divList;
	}

}
