package com.twfhclife.eservice.api.adm.domain;

/**
 * Add new function item in DB.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class FuncItemReqObj {

	/**
	 * 系統代碼.
	 */
	private String sysId;

	/**
	 * 使用者代碼
	 */
	private String userId;

	/**
	 * 功能物件.
	 */
	private Function functionItem;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Function getFunctionItem() {
		return functionItem;
	}

	public void setFunctionItem(Function functionItem) {
		this.functionItem = functionItem;
	}

	public Function getFunction() {
		return functionItem;
	}

	public void setFunction(Function functionItem) {
		this.functionItem = functionItem;
	}
}
