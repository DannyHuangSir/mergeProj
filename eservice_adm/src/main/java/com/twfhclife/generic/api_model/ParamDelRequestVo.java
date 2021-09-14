package com.twfhclife.generic.api_model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import com.twfhclife.generic.model.Pagination;

public class ParamDelRequestVo extends Pagination {

	@NotEmpty(message = "userId cannot empty!")
	private String userId;

	@NotEmpty(message = "userId cannot empty!")
	private String parameterId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParameterId() {
		return parameterId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
