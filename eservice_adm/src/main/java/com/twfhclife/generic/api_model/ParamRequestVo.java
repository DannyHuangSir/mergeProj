package com.twfhclife.generic.api_model;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.twfhclife.generic.model.Pagination;

public class ParamRequestVo extends Pagination {

	private String systemId;

	private String userId;

	private String parameterCategoryCode;

	private String parameterCategoryName;

	private String parameterId;

	private String parameterCode;

	private String parameterName;

	private String parameterValue;

	private Integer parameterCategoryId;

	private Integer parentParameterId;

	private Integer sortNo;

	@Range(min = 0, max = 1)
	private Integer status;

	private String remark;

	@Pattern(regexp = "[YN]", message = "只能輸入Y/N")
	private String encryptType;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getParameterId() {
		return parameterId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Integer getParameterCategoryId() {
		return parameterCategoryId;
	}

	public void setParameterCategoryId(Integer parameterCategoryId) {
		this.parameterCategoryId = parameterCategoryId;
	}

	public Integer getParentParameterId() {
		return parentParameterId;
	}

	public void setParentParameterId(Integer parentParameterId) {
		this.parentParameterId = parentParameterId;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
