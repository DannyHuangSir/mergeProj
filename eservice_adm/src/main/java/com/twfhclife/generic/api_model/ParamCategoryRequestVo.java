package com.twfhclife.generic.api_model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.twfhclife.generic.model.Pagination;

public class ParamCategoryRequestVo extends Pagination {

	@NotEmpty(message = "systemId can't empty!")
	private String systemId;

	@NotEmpty(message = "userId cannot empty!")
	private String userId;

	private Integer parameterCategoryId;

	private String parameterCategoryCode;

	private String parameterCategoryName;

	/**  */
	private String remark;

	@Range(min = 0, max = 1)
	private BigDecimal status;
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

	public Integer getParameterCategoryId() {
		return parameterCategoryId;
	}

	public void setParameterCategoryId(Integer parameterCategoryId) {
		this.parameterCategoryId = parameterCategoryId;
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

	public BigDecimal getStatus() {
		return status;
	}

	public void setStatus(BigDecimal status) {
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
