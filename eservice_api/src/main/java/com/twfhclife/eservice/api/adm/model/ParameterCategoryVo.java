package com.twfhclife.eservice.api.adm.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class ParameterCategoryVo extends Pagination {

	/**  */
	private BigDecimal parameterCategoryId;
	/**  */
	private String systemId;
	/**  */
	private String parameterCategoryCode;
	/**  */
	private String parameterCategoryName;
	/**  */
	private String remark;
	/**  */
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

	public BigDecimal getParameterCategoryId() {
		return parameterCategoryId;
	}

	public void setParameterCategoryId(BigDecimal parameterCategoryId) {
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
