package com.twfhclife.eservice.api.adm.domain;

import java.util.List;

/**
 * Add new functions in DB.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class FuncListReqObj {

	/**
	 * 使用者代碼.
	 */
	private String userId;

	/**
	 * 功能.
	 */
	private List<Function> functionList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Function> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<Function> functionList) {
		this.functionList = functionList;
	}

}
