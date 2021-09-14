package com.twfhclife.generic.api_model;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import com.twfhclife.generic.model.Pagination;

public class ParamCategoryDelRequestVo extends Pagination {

	@NotNull(message = "can't empty!")
	private Integer parameterCategoryId;

	@NotEmpty(message = "cannot empty!")
	private String userId;

	public Integer getParameterCategoryId() {
		return parameterCategoryId;
	}

	public void setParameterCategoryId(Integer parameterCategoryId) {
		this.parameterCategoryId = parameterCategoryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
