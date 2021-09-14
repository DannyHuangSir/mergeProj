package com.twfhclife.adm.model;

public class PermissionsDetailVo {

	private String userAcct;

	private String role;

	private String functionId;

	private String functionName;

	private String parentFuncId;

	private String sysId;

	private String sort;

	private String functionType;

	private String canUse;

	public String getUserAcct() {
		return userAcct;
	}

	public void setUserAcct(String userAcct) {
		this.userAcct = userAcct;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getCanUse() {
		return canUse;
	}

	public void setCanUse(String canUse) {
		this.canUse = canUse;
	}

}
