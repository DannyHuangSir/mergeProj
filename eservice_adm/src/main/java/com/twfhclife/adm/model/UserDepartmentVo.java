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