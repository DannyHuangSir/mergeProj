package com.twfhclife.adm.domain;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.PermissionsDetailVo;

/**
 * Response bean for getting function items by user account.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class GetFuncItemsByUserAcctResponse {

	private List<Map<String, Object>> functionItems;

	List<PermissionsDetailVo> roles;

	public List<Map<String, Object>> getFunctionItems() {
		return functionItems;
	}

	public void setFunctionItems(List<Map<String, Object>> functionItems) {
		this.functionItems = functionItems;
	}

	public List<PermissionsDetailVo> getRoles() {
		return roles;
	}

	public void setRoles(List<PermissionsDetailVo> roles) {
		this.roles = roles;
	}

}
