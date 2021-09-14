package com.twfhclife.adm.model;

import com.twfhclife.generic.model.Pagination;

public class UserDeputyVo extends Pagination {

	private String id;
	
	private String userId;
	
	private String deputyId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeputyId() {
		return deputyId;
	}

	public void setDeputyId(String deputyId) {
		this.deputyId = deputyId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
