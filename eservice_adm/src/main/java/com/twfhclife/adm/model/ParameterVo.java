package com.twfhclife.adm.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class ParameterVo extends Pagination {

	/**  */
	private Integer parameterId;
	/**  */
	private String systemId;
	/**  */
	private String parameterCode;
	/**  */
	private String parameterName;
	/**  */
	private String parameterValue;
	/**  */
	private Integer parameterCategoryId;
	/**  */
	private Integer sortNo;
	/**  */
	private String remark;
	/**  */
	private Integer status;
	/**  */
	private String encryptType;
	/**  */
	private Integer parentParameterId;
	/**  */
	private Date createDate;
	/**  */
	private String createUser;
	/**  */
	private Date updateDate;
	/**  */
	private String updateUser;

	private String sysName;
	private String statusName;
	private String categoryCode;
	private String categoryName;

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
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

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	public Integer getParentParameterId() {
		return parentParameterId;
	}

	public void setParentParameterId(Integer parentParameterId) {
		this.parentParameterId = parentParameterId;
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

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
