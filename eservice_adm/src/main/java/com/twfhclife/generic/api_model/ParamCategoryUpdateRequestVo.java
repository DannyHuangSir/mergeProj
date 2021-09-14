package com.twfhclife.generic.api_model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.twfhclife.generic.model.Pagination;

public class ParamCategoryUpdateRequestVo extends Pagination {

	@NotNull(message = "can't empty!")
	private Integer parameterCategoryId;

	@NotEmpty(message = "can't empty!")
	private String systemId;

	@NotEmpty(message = "can't empty!")
	private String parameterCategoryCode;

	@NotEmpty(message = "can't empty!")
	private String parameterCategoryName;
	/**  */
	private String remark;

	@Range(min = 0, max = 1)
	private Integer status;

	private Date createDate;

	private String createUser;

	private Date updateDate;

	@NotEmpty(message = "cannot empty!")
	private String updateUser;

	public Integer getParameterCategoryId() {
		return parameterCategoryId;
	}

	public void setParameterCategoryId(Integer parameterCategoryId) {
		this.parameterCategoryId = parameterCategoryId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getParameterCategoryCode() {
		return parameterCategoryCode;
	}

	public void setParameterCategoryCode(String parameterCategoryCode) {
		this.parameterCategoryCode = parameterCategoryCode;
	}

	public String getParameterCategoryName() {
		return parameterCategoryName;
	}

	public void setParameterCategoryName(String parameterCategoryName) {
		this.parameterCategoryName = parameterCategoryName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
