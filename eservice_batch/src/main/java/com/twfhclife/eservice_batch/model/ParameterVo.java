package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;
import java.util.Date;

public class ParameterVo {
	
	/**  */
	private BigDecimal parameterId;
	/**  */
	private String systemId;
	/**  */
	private String parameterCode;
	/**  */
	private String parameterName;
	/**  */
	private String parameterValue;
	/**  */
	private BigDecimal parameterCategoryId;
	/**  */
	private BigDecimal sortNo;
	/**  */
	private String remark;
	/**  */
	private BigDecimal status;
	/**  */
	private String encryptType;
	/**  */
	private BigDecimal parentParameterId;
	/**  */
	private Date createDate;
	/**  */
	private String createUser;
	/**  */
	private Date updateDate;
	/**  */
	private String updateUser;
	
	public BigDecimal getParameterId() {
		return this.parameterId;
	}
	
	public void setParameterId(BigDecimal parameterId) {
		this.parameterId = parameterId;
	}
	
	public String getSystemId() {
		return this.systemId;
	}
	
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	public String getParameterCode() {
		return this.parameterCode;
	}
	
	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
	
	public String getParameterName() {
		return this.parameterName;
	}
	
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	public String getParameterValue() {
		return this.parameterValue;
	}
	
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	public BigDecimal getParameterCategoryId() {
		return this.parameterCategoryId;
	}
	
	public void setParameterCategoryId(BigDecimal parameterCategoryId) {
		this.parameterCategoryId = parameterCategoryId;
	}
	
	public BigDecimal getSortNo() {
		return this.sortNo;
	}
	
	public void setSortNo(BigDecimal sortNo) {
		this.sortNo = sortNo;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public BigDecimal getStatus() {
		return this.status;
	}
	
	public void setStatus(BigDecimal status) {
		this.status = status;
	}
	
	public String getEncryptType() {
		return this.encryptType;
	}
	
	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}
	
	public BigDecimal getParentParameterId() {
		return this.parentParameterId;
	}
	
	public void setParentParameterId(BigDecimal parentParameterId) {
		this.parentParameterId = parentParameterId;
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
}
