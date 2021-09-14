package com.twfhclife.eservice.api.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PartnerUserEntityVo extends UserEntityVo {
	
	private String roleCode;
	
	private String chalCode;
	
	private String agenCode;
	
	private String branchCode;
	
	private String mobile;
	
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getChalCode() {
		return chalCode;
	}

	public void setChalCode(String chalCode) {
		this.chalCode = chalCode;
	}

	public String getAgenCode() {
		return agenCode;
	}

	public void setAgenCode(String agenCode) {
		this.agenCode = agenCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}