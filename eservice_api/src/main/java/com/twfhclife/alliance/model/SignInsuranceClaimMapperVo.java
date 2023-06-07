package com.twfhclife.alliance.model;

import java.sql.Timestamp;
import java.util.List;

public class SignInsuranceClaimMapperVo {
	
	/**
	 * 申請中
	 */
	public static final String STATUS_INIT = "INIT";//申請中
	
	/**
	 * 處理中
	 */
	public static final String STATUS_PROCESSING = "PROCESSING";//處理中
	
	/**
	 * 待補件
	 */
	public static final String STATUS_ADDITIONAL = "ADDITIONAL";//待補件
	
	/**
	 * 已審核
	 */
	public static final String STATUS_AUDITED = "AUDITED";//已審核
	
	/**
	 * 已退件
	 */
	public static final String STATUS_RETURN = "RETURN";//已退件
	
	/**
	 * 已撤消
	 */
	public static final String STATUS_INVALID  = "INVALID";//已撤消
	
	/**
	 * 已上傳完成
	 */
	public static final String STATUS_UPLOADED = "UPLOADED";//已上傳完成
	
	/**
	 * 已收到所有紙本
	 */
	public static final String FILE_RECEIVED_YES = "1";
	
	/**
	 * 沒收到紙本
	 */
	public static final String FILE_RECEIVED_NO = "2";
	
	/**
	 * 紙本註記已通知聯盟
	 */
	public static final String FILE_RECEIVED_OK = "OK";
	
	private Float claimSeqId;
	
	/**
	 * 聯盟通知件的Db Table sequence id
	 */
	private Float notifySeqId;
	
	/**
	 * 當資料來自於聯盟鏈時才有值
	 */
	private String caseId;
	
	/**
	 * 當資料來自於聯盟鏈時才有值
	 */
	private String code;
	
	/**
	 * 當資料來自於聯盟鏈時才有值
	 */
	private String msg;
	
	/**
	 * eservice.Trans.transNum
	 */
	private String transNum;
	
	/**
	 * 是否已收到所有紙本
	 * "1":收到，"2":沒收到
	 */
	private String fileReceived;

	/**
	 * 被保險人姓名
	 * 最多 200個中文字
	 */
	private String name;
	
	/**
	 * 被保險人身分證號碼
	 * 依身分證號碼 20位數填列。例如：  A102510420
	 */
	private String idNo;
	
	/**
	 *  被保險人出生日期
	 *  採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String birdate;
	
	/**
	 * 聯絡(行動)電話
	 * 長度：15
	 */
	private String phone;
	
	/**
	 * 郵遞區號
	 * 長度：6
	 */
	private String zipCode;
	
	/**
	 * 聯絡地址
	 * 最多 100個中文字
	 */
	private String address;
	
	/**
	 * 電郵
	 * 長度：100
	 */
	private String mail;
	
	/**
	 * 付款方式
	 * “1”:匯款。長度：1
	 */
	private String paymentMethod;
	
	/**
	 * 銀行名稱
	 * 長度：3
	 */
	private String bankCode;
	
	/**
	 * 分行名稱
	 * 長度：4
	 */
	private String branchCode;
	
	/**
	 * 匯款帳號
	 * 長度：14
	 */
	private String bankAccount;
	
	/**
	 * 申請日期
	 * 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String applicationDate;
	
	/**
	 * 申請時間
	 * 採 24小時制格式，hhmm 例如：1520。長度：4
	 */
	private String applicationTime;
	
	/**
	 * 申請項目
	 * “1”:醫療保險金。長度：1
	 */
	private String applicationItem;
	
	/**
	 * 事故時被保險人職業
	 * 最多 20個中文字
	 */
	private String job;
	
	/**
	 * 工作內容
	 * 最多 200個中文字
	 */
	private String jobDescr;
	
	/**
	 * 事故日期
	 * 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String accidentDate;
	
	/**
	 * 事故時間
	 * 採 24小時制格式，hhmm 例如：1520。長度：4
	 */
	private String accidentTime;
	
	/**
	 * 事故原因
	 * "1":疾病，"2":意外。長度：1
	 */
	private String accidentCause;
	
	/**
	 * 事故地點
	 * 最多 20個中文字
	 */
	private String accidentLocation;
	
	/**
	 * 簡述事故經過
	 * 最多 200個中文字
	 */
	private String accidentDescr;
	
	/**
	 * 報案機關
	 * 最多 200個中文字
	 */
	private String policeStation;
	
	/**
	 * 承辦員警
	 * 最多 200個中文字
	 */
	private String policeName;
	
	/**
	 * 警方電話
	 * 長度：20
	 */
	private String policePhone;
	
	/**
	 * 報案日期
	 * 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String policeDate;
	
	/**
	 * 報案時間
	 * 採 24小時制格式，hhmm 例如：1520。長度：4
	 */
	private String policeTime;
	
	/**
	 * 首家公司代號
	 * "L"+保險公司代號，或"N"+產險公司代號。例如：L02為台灣人壽、N05為富邦產險
	 */
	private String from;
	
	/**
	 * 轉收公司代號清單</br>
	 * 包含案件的每家轉收公司代號
	 */
	private String to;
	
	/**
	 * 案件處理狀態</br>
	 * 申請中</br>
	 * 處理中</br>
	 * 待補件</br>
	 * 已審核</br>
	 * 已退件</br>
	 * 已撤消-INVALID</br>
	 * ...etc.
	 */
	private String status;
	
	/**
	 * 是否可傳送給聯盟註記(Y/N)
	 */
	private String sendAlliance;
	
	private Timestamp createDate;

	/**
	 * 此案件的上傳檔案狀態清單
	 */
	private List<InsuranceClaimFileDataVo> fileDatas;

	private String actionId;
	private String toaFileId;

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getToaFileId() {
		return toaFileId;
	}

	public void setToaFileId(String toaFileId) {
		this.toaFileId = toaFileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getBirdate() {
		return birdate;
	}

	public void setBirdate(String birdate) {
		this.birdate = birdate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(String applicationTime) {
		this.applicationTime = applicationTime;
	}

	public String getApplicationItem() {
		return applicationItem;
	}

	public void setApplicationItem(String applicationItem) {
		this.applicationItem = applicationItem;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJobDescr() {
		return jobDescr;
	}

	public void setJobDescr(String jobDescr) {
		this.jobDescr = jobDescr;
	}

	public String getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(String accidentDate) {
		this.accidentDate = accidentDate;
	}

	public String getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}

	public String getAccidentCause() {
		return accidentCause;
	}

	public void setAccidentCause(String accidentCause) {
		this.accidentCause = accidentCause;
	}

	public String getAccidentLocation() {
		return accidentLocation;
	}

	public void setAccidentLocation(String accidentLocation) {
		this.accidentLocation = accidentLocation;
	}

	public String getAccidentDescr() {
		return accidentDescr;
	}

	public void setAccidentDescr(String accidentDescr) {
		this.accidentDescr = accidentDescr;
	}

	public String getPoliceStation() {
		return policeStation;
	}

	public void setPoliceStation(String policeStation) {
		this.policeStation = policeStation;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	public String getPolicePhone() {
		return policePhone;
	}

	public void setPolicePhone(String policePhone) {
		this.policePhone = policePhone;
	}

	public String getPoliceDate() {
		return policeDate;
	}

	public void setPoliceDate(String policeDate) {
		this.policeDate = policeDate;
	}

	public String getPoliceTime() {
		return policeTime;
	}

	public void setPoliceTime(String policeTime) {
		this.policeTime = policeTime;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
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

	public Float getClaimSeqId() {
		return claimSeqId;
	}

	public void setClaimSeqId(Float claimSeqId) {
		this.claimSeqId = claimSeqId;
	}


	public String getFileReceived() {
		return fileReceived;
	}

	public void setFileReceived(String fileReceived) {
		this.fileReceived = fileReceived;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSendAlliance() {
		return sendAlliance;
	}

	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}

	public Float getNotifySeqId() {
		return notifySeqId;
	}

	public void setNotifySeqId(Float notifySeqId) {
		this.notifySeqId = notifySeqId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<InsuranceClaimFileDataVo> getFileDatas() {
		return fileDatas;
	}

	public void setFileDatas(List<InsuranceClaimFileDataVo> fileDatas) {
		this.fileDatas = fileDatas;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
}
