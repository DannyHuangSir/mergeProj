package com.twfhclife.eservice_batch.model;

import java.util.Date;

public class TransVo {
	
	/**  */
	private String transNum;
	/**  */
	private Date transDate;
	/**  */
	private String transType;
	/**  */
	private String userId;
	/**  */
	private String status;
	/**  */
	private String createUser;
	/**  */
	private Date createDate;
	/**  */
	private String updateUser;
	/**  */
	private Date updateDate;
	
	public String getTransNum() {
		return this.transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	public Date getTransDate() {
		return this.transDate;
	}
	
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	
	public String getTransType() {
		return this.transType;
	}
	
	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
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
	
	public String getUpdateUser() {
		return this.updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public Date getUpdateDate() {
		return this.updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}

