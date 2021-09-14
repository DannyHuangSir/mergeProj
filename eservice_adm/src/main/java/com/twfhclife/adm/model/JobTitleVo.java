package com.twfhclife.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class JobTitleVo extends Pagination {
	
	private String titleId;
	private String titleName;
	private String description;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	
	public String getTitleId() {
		return this.titleId;
	}
	
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	
	public String getTitleName() {
		return this.titleName;
	}
	
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getModifyUser() {
		return this.modifyUser;
	}
	
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	public Date getModifyDate() {
		return this.modifyDate;
	}
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

