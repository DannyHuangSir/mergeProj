package com.twfhclife.eservice.api.adm.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import com.twfhclife.generic.model.Pagination;

public class SystemUserRequestVo extends Pagination {

	@NotEmpty(message = "cannot empty!")
	private String sysId;

	@NotEmpty(message = "cannot empty!")
	private String userId;

	private Boolean isAdmin;

	private String userNameQuery;

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

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getUserNameQuery() {
		return userNameQuery;
	}

	public void setUserNameQuery(String userNameQuery) {
		this.userNameQuery = userNameQuery;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
