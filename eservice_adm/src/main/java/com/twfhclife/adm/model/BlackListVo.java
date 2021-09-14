package com.twfhclife.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BlackListVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Float blId;
	
	private String transNum;

	private String transStatus;
	
	private String idNo;
	
	private Date createDate;


	public Float getBlId() {
		return blId;
	}


	public void setBlId(Float blId) {
		this.blId = blId;
	}


	public String getTransNum() {
		return transNum;
	}


	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}


	public String getTransStatus() {
		return transStatus;
	}


	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}


	public String getIdNo() {
		return idNo;
	}


	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
