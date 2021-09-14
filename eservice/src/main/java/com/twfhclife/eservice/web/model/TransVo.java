package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 申請序號 */
	private String transNum;
	/** 申請日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date transDate;
	/** 申請項目 */
	private String transType;
	/**  */
	private String userId;
	/**  */
	private String status;
	/**  */
	private String createUser;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date createDate;
	/**  */
	private String updateUser;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date updateDate;

	/** 保單號碼 */
	private String policyNo;
	
	/** 用戶證號 */
	private String rocId;
	
	/** 申請狀態清單 */
	private List<String> transStatusList;
	
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
	
	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public List<String> getTransStatusList() {
		return transStatusList;
	}

	public void setTransStatusList(List<String> transStatusList) {
		this.transStatusList = transStatusList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}