package com.twfhclife.alliance.model;

import java.sql.Timestamp;

public class NotifyOfNewCaseDnsVo {

	/**
	 * 未取得查詢理賠資料
	 */
	public static final String NC_STATUS_ZERO = "0";//未取得查詢理賠資料
	
	/**
	 * 已取得查詢理賠資料
	 */
	public static final String NC_STATUS_ONE  = "1";//已取得查詢理賠資料
	
	/*
	 * 採用身分證字號之案件編號回報
	 */
	public static final String TYPE_ID_NO = "idNo";
	
	/**
	 * 採用具保單號碼之案件編號回報
	 */
	public static final String TYPE_INSURANCEID = "InsuranceId";
	
	/**
	 * 非台銀保戶
	 */
	public static final String MSG  = "非保戶";//非台銀保戶
	
	/**
	 * sequence
	 */
	private Float seqId;
	
	/**
	 * 用於分辨被聯盟同一次呼叫的ID</b>
	 * 同一批jobId,其CALL_ID會相同
	 */
	private String callId;
	
	/**
	 * IdNo=採用身分證字號之案件編號回報,
	 * InsuranceId=採用具保單號碼之案件編號回報
	 */
	private String type;

	/**
	 * 每一個工作編號 String(50)
	 */
	private String jobId;

	private Timestamp createDate;
	
	/**
	 * NC_STATUS_ZERO/NC_STATUS_ONE
	 */
	private String ncStatus;
	
	private Timestamp statusDate;
	
	private String msg;

	public Float getSeqId() {
		return seqId;
	}

	public void setSeqId(Float seqId) {
		this.seqId = seqId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}
	
}
