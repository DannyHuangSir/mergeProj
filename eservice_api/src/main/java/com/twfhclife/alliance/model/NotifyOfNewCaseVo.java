package com.twfhclife.alliance.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class NotifyOfNewCaseVo {
	
	/**
	 * 未取得查詢理賠資料
	 */
	public static final String NC_STATUS_ZERO = "0";//未取得查詢理賠資料
	
	/**
	 * 已取得查詢理賠資料
	 */
	public static final String NC_STATUS_ONE  = "1";//已取得查詢理賠資料
	
	/**
	 * 非台銀保戶
	 */
	public static final String MSG  = "非保戶";//非台銀保戶
	
	private Float seqId;
	
	private String caseId;
	
	private String type;
	
	private Timestamp createDate;
	
	private String ncStatus;
	
	private Timestamp statusDate;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getNcStatus() {
		return ncStatus;
	}

	public void setNcStatus(String ncStatus) {
		this.ncStatus = ncStatus;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
