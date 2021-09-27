package com.twfhclife.alliance.model;

import java.sql.Timestamp;

public class DnsContentVo {
	
	/**
	 * 被保險人因自然死身亡
	 */
	public static final String CON_30 = "30";
	
	/**
	 * 被保險人因意外身故
	 */
	public static final String CON_31 = "31";
	
	/**
	 * 被保險人因其他原因身故
	 */
	public static final String CON_32 = "32";
	
	/**
	 * status default value
	 */
	public static final String STATUS_DEFAULT = "0";
	/**
	 * 已轉存至eservice TRANS
	 */
	public static final String STATUS_SAVED_TO_ESERVICE = "1";
	
	/**
	 * 已回報聯盟
	 */
	public static final String SEND_ALLIANCE_REPORT_TO_ALLIANCE = "1";
	
	private Float seqId;
	
	private Float notifySeqId;
	
	private String transNum;
	
	/**
	 * 保單號碼：InsuranceId
	 * 身份證字號：IdNo
	 */
	private String type;
	
	private String jobId;

	/**
	 * 案件編號
	 */
	private String caseId;
	
	/**
	 * 被保險人身份證字號
	 */
	private String idno;
	
	/**
	 * 被保險人姓名
	 */
	private String name;
	
	/**
	 * 出生年月日(民國年YYYMMDD)
	 */
	private String birdate;
	
	/**
	 * 主保單號碼
	 */
	private String insnom;
	
	/**
	 * 姓別(1:男;2:女)
	 */
	private String sex;
	
	/**
	 * 身故原因
	 * 若無值，則為空字串
	 * 30:被保險人因自然死身故
	 * 31:被保險人因意外身故
	 * 32:被保險人因其他原因身故
	 */
	private String con;
	
	/**
	 * 身故日期(民國年YYYMMDD)
	 */
	private String condate;
	
	/**
	 * 通知日期與時間(民國年 YYYMMDDhhmm)
	 */
	private String adddate;
	
	/**
	 * 0:default
	 * 1:saved to eservice TRANS
	 */
	private String status;
	
	private Timestamp createDate;
	
	/**
	 * 是否已回報聯盟</br>
	 * 1:已回報
	 */
	private String sendAlliance;
	
	private String code;
	
	private String msg;
	//單號
	private String policyNo;
	private String detailMessage;
	private String token;
	private String fsz1Id;
	private String fsz1PiSt;


	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFsz1Id() {
		return fsz1Id;
	}

	public void setFsz1Id(String fsz1Id) {
		this.fsz1Id = fsz1Id;
	}

	public String getFsz1PiSt() {
		return fsz1PiSt;
	}

	public void setFsz1PiSt(String fsz1PiSt) {
		this.fsz1PiSt = fsz1PiSt;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirdate() {
		return birdate;
	}

	public void setBirdate(String birdate) {
		this.birdate = birdate;
	}

	public String getInsnom() {
		return insnom;
	}

	public void setInsnom(String insnom) {
		this.insnom = insnom;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getCondate() {
		return condate;
	}

	public void setCondate(String condate) {
		this.condate = condate;
	}

	public String getAdddate() {
		return adddate;
	}

	public void setAdddate(String adddate) {
		this.adddate = adddate;
	}

	public Float getSeqId() {
		return seqId;
	}

	public void setSeqId(Float seqId) {
		this.seqId = seqId;
	}

	public Float getNotifySeqId() {
		return notifySeqId;
	}

	public void setNotifySeqId(Float notifySeqId) {
		this.notifySeqId = notifySeqId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSendAlliance() {
		return sendAlliance;
	}

	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
}
