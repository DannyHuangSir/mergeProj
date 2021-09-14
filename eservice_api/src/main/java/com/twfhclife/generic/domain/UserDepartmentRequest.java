package com.twfhclife.generic.domain;

public class UserDepartmentRequest {

	private String depId;
	private String titleId;
	private String userId;//指定userId

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
