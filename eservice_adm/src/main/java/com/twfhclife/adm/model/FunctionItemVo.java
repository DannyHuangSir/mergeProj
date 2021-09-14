package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FunctionItemVo {

	private String functionId;
	private String functionName;
	private String functionType;
	private String functionUrl;
	private String sort;
	private String parentFuncId;
	private String sysId;
	private String active;
	private String createUser;
	private String createDate;
	private String updateUser;
	private String updateDate;
	// 檢查 角色是否有此功能
	private String checkMapper;
	// 檢查 角色是否有此功能(FG類)
	private String checkFG;
	// 檢查 角色是否有此系統
	private String sysMapper;

	private String divArr;

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getDivArr() {
		return divArr;
	}

	public void setDivArr(String divArr) {
		this.divArr = divArr;
	}

	public String getCheckMapper() {
		return checkMapper;
	}

	public void setCheckMapper(String checkMapper) {
		this.checkMapper = checkMapper;
	}

	public String getCheckFG() {
		return checkFG;
	}

	public void setCheckFG(String checkFG) {
		this.checkFG = checkFG;
	}

	public String getSysMapper() {
		return sysMapper;
	}

	public void setSysMapper(String sysMapper) {
		this.sysMapper = sysMapper;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
