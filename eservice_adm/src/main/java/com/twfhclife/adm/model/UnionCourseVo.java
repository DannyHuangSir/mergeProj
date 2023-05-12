package com.twfhclife.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UnionCourseVo {
	
	/**
	 * 執行狀態-失敗
	 */
	public static final String NC_STATUS_F = "0";
	
	/**
	 * 執行狀態-成功
	 */
	public static final String NC_STATUS_S  = "1";
	
	/**
	 * 轉送聯盟鏈
	 */
	public static final String NC_TYPE = "轉送聯盟鏈";
	
	/**
	 * 動作名稱
	 */
	public static final String NAME_API_105 = "API-105";
	
	/** 序列 */
	private Float seqId;
	
	/** 序號 */
	private String caseId;
	
	/** 申請序號 */
	private String transNum;
	
	/** 動作類型 */
	private String type;
	
	/** 動作名稱 */
	private String name;
	
	/** 執行時間起 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date createDate;
	
	/** 執行時間止 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date completeDate;
	
	/** 執行狀態 */
	private String ncStatus;
	
	/** 錯誤訊息 */
	private String msg;
	
	


	public Float getSeqId() {
		return seqId;
	}




	public void setSeqId(Float seqId) {
		this.seqId = seqId;
	}




	public String getCaseId() {
		return caseId;
	}




	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}




	public String getTransNum() {
		return transNum;
	}




	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public Date getCreateDate() {
		return createDate;
	}




	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}




	public Date getCompleteDate() {
		return completeDate;
	}




	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}




	public String getNcStatus() {
		return ncStatus;
	}




	public void setNcStatus(String ncStatus) {
		this.ncStatus = ncStatus;
	}




	public String getMsg() {
		return msg;
	}




	public void setMsg(String msg) {
		this.msg = msg;
	}




	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
