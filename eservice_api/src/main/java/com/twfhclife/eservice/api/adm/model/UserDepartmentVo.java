package com.twfhclife.eservice.api.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class UserDepartmentVo extends Pagination {

	/**  */
	private String userId;
	/**  */
	private String depId;
	private String depName;
	private String depDescription;
	private String parentDep;

	/** 職位 */
	private String titleId;
	private String titleName;
	private String titleDescription;

	public String getUserId() {
		return userId;
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

	public String getDepDescription() {
		return depDescription;
	}

	public void setDepDescription(String depDescription) {
		this.depDescription = depDescription;
	}

	public String getParentDep() {
		return parentDep;
	}

	public void setParentDep(String parentDep) {
		this.parentDep = parentDep;
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

	public String getTitleDescription() {
		return titleDescription;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}