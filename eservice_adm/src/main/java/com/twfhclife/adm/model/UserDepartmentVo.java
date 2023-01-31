package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class UserDepartmentVo extends Pagination {
	
	/**  */
	private String userId;
	/**  */
	private String depId;
	private String depName;
	/** 職位 */
	private String titleId;
	private String titleName;
	/**
	 * 通路别
	 */
	private String channel;
	private String branch;
	private String employeeNum;

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}

	public void setRegisterNum(String registerNum) {
		this.registerNum = registerNum;
	}

	public String getChannel() {
		return channel;
	}

	public String getBranch() {
		return branch;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public String getRegisterNum() {
		return registerNum;
	}

	/**
	 * 登录字号
	 */
	private String registerNum;


	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}