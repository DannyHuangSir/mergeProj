package com.twfhclife.adm.model;

import com.twfhclife.generic.model.Pagination;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class DepartmentVo extends Pagination {

	private String depId;
	private String depName;
	private String description;
	private String parentDep;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String branchId;
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchId() {
		return branchId;
	}


	
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getParentDep() {
		return parentDep;
	}
	public void setParentDep(String parentDep) {
		this.parentDep = parentDep;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
