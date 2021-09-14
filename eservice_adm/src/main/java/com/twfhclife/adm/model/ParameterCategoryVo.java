package com.twfhclife.adm.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class ParameterCategoryVo extends Pagination {
	
	/**  */
	private Integer parameterCategoryId;
	/**  */
	private String systemId;
	/**  */
	private String categoryCode;
	/**  */
	private String categoryName;
	/**  */
	private String remark;
	/**  */
	private Integer status;
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
	
	public Integer getParameterCategoryId() {
		return this.parameterCategoryId;
	}
	
	public void setParameterCategoryId(Integer parameterCategoryId) {
		this.parameterCategoryId = parameterCategoryId;
	}
	
	public String getSystemId() {
		return this.systemId;
	}
	
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	public String getCategoryCode() {
		return this.categoryCode;
	}
	
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	public String getCategoryName() {
		return this.categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getUpdateDate() {
		return this.updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getUpdateUser() {
		return this.updateUser;
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

