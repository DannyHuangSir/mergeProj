package com.twfhclife.eservice.onlineChange.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TransMedicalTreatmentClaimVo extends AbstractOnlineChangeModelBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Float claimSeqId;

	private String transNum;

	/**
	 * 聯盟通知件：一開始就有</br>
	 * 台銀件：理賠申請書上傳完聯盟，才會有值
	 */
	private String caseId;

	/**
	 * 是否已收到所有紙本 "1":收到，"2":沒收到
	 */
	private String fileReceived;

	/** 申請項目 */
	private List<String> applyItemList;

	/** 申請項目-其他 */
	private String itemOther;
	
	/** 申請項目 */
	private String applyItem;

	/** 要保人名稱 */
	private String customerName;

	/** 保單號碼 */
	private String policyNo;

	/**
	 * 被保險人姓名 最多 200個中文字
	 */
	private String name;

	/** 與主被保險人(員工)之關係 */
	private String relation;
	
	private String relation_name;

	/**
	 * 被保險人身分證號碼 依身分證號碼 20位數填列。例如： A102510420
	 */
	private String idNo;

	/**
	 * 被保險人出生日期 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String birdate;

	/** 聯絡電話 */
	private String tel;

	/**
	 * 聯絡(行動)電話 長度：15
	 */
	private String phone;

	/**
	 * 郵遞區號 長度：6
	 */
	private String zipCode;

	/**
	 * 聯絡地址 最多 100個中文字
	 */
	private String address;

	/**
	 * 電郵 長度：100
	 */
	private String mail;

	/** 診斷病名 */
	private String diseaseName;

	/** 該疾病初診日 */
	private String diagnosisDate;

	/** 曾就診之醫院 */
	private String hospital;

	/** 被保險人是否投保別家保險公司之保險 */
	private String otherCompanyInsured;

	/** 公司名稱 */
	private String otherInsuCompany;

	/** 保險種類 */
	private String otherInsuType;

	/** 保險金額 */
	private String otherInsuAmout;

	/** 同時申請理賠 */
	private String otherInsuClaim;

	/**
	 * 付款方式 “1”:匯款。長度：1
	 */
	private String paymentMethod;

	/**
	 * 銀行名稱 長度：3
	 */
	private String bankCode;
	
	/**
	 * 銀行名稱 
	 */
	private String bankName;

	/**
	 * 分行名稱 長度：4
	 */
	private String branchCode;
	
	/**
	 * 分行名稱
	 */
	private String branchName;

	/**
	 * 匯款帳號 長度：14
	 */
	private String bankAccount;

	/** 匯款帳戶 */
	private String accountName;

	/** 郵寄地址 */
	private String postalAddr;

	/**
	 * 申請日期 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String applicationDate;

	/**
	 * 申請時間 採 24小時制格式，hhmm 例如：1520。長度：4
	 */
	private String applicationTime;

	/**
	 * 申請項目 “1”:醫療保險金。長度：1
	 */
	private String applicationItem;

	/**
	 * 事故時被保險人職業 最多 20個中文字
	 */
	private String job;

	/**
	 * 工作內容 最多 200個中文字
	 */
	private String jobDescr;

	/**
	 * 事故日期 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String accidentDate;

	/**
	 * 事故時間 採 24小時制格式，hhmm 例如：1520。長度：4
	 */
	private String accidentTime;

	/**
	 * 事故原因 "1":疾病，"2":意外。長度：1
	 */
	private String accidentCause;

	/**
	 * 事故地點 最多 20個中文字
	 */
	private String accidentLocation;

	/**
	 * 簡述事故經過 最多 200個中文字
	 */
	private String accidentDescr;

	/**
	 * 報案機關 最多 200個中文字
	 */
	private String policeStation;

	/**
	 * 承辦員警 最多 200個中文字
	 */
	private String policeName;

	/**
	 * 警方電話 長度：20
	 */
	private String policePhone;

	/**
	 * 報案日期 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 */
	private String policeDate;

	/**
	 * 報案時間 採 24小時制格式，hhmm 例如：1520。長度：4
	 */
	private String policeTime;
	
	/** 是否報警*/
	private String callPolice;

	/**
	 * 首家公司代號 "L"+保險公司代號，或"N"+產險公司代號。例如：L02為台灣人壽、N05為富邦產險
	 */
	private String from;

	/**
	 * 轉收公司代號清單</br>
	 * 包含案件的每家轉收公司代號</br>
	 * *value format is like "L2,L3,L4"
	 */
	private String to;

	/**
	 * 是否可傳送給聯盟註記(Y/N)</br>
	 * *聯盟通知件:always set "N".
	 */
	private String sendAlliance;

	private String signAgree;

	/**
	 * 此案件的上傳檔案狀態清單
	 */
	private List<TransMedicalTreatmentClaimFileDataVo> fileDatas;
	
	private String fileDataList;
	
	private List<String> toItem;
	//醫院公司名稱
	private List<String> toHospitalIdItem;
	//授權文件類型：
	private List<String> fileType;

	//private Timestamp createDate;
	/**
	 * 事故原因 "1":診斷證明書，"2":收據。長度：1
	 * */
	private String authorizationFileType;
	/**
	 * 授權開始日期 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 * */
	private String authorizationStartDate;
	/**
	 * 授權結束日期 採西元年格式，YYYYMMDD 例如：19801115。長度：8
	 * */
	private String authorizationEndDate;
	/**
	 * 是否可傳送給聯盟註記(Y/N)</br>*聯盟通知件:always set "N".
	 *
	 *
	 * */
	private String fromHospitalId;
	/**
	 * 轉收公司代號清單</br> 包含案件的每家轉收公司代號</br>*value  "L2,L3,L4"
	 * */
	private String toHospitalId;
	private String toSubHospitalId;

	//創建的時間
	private Timestamp createDate;
	//聯盟狀態
	private String allianceStatus;
	//與醫院對接的序號
	private String hpUid;
	//保⼾同意條款(純文字)
	private String cpoa;
	
	private String stauts;
	//便於重新發送驗證
	private String mobile;
	
	private String email;

	private String sendConfirmTime;

	public String getSendConfirmTime() {
		return sendConfirmTime;
	}

	public void setSendConfirmTime(String sendConfirmTime) {
		this.sendConfirmTime = sendConfirmTime;
	}

	/**
	 * Spec v1.5.2
	 * 申請資料
	 */
	private List<TransMedicalTreatmentClaimMedicalInfoVo> medicalInfo = Lists.newArrayList();

	private String medicalInfoList;

	private Date applyDate;

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}


	public String getToSubHospitalId() {
		return toSubHospitalId;
	}

	public void setToSubHospitalId(String toSubHospitalId) {
		this.toSubHospitalId = toSubHospitalId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStauts() {
		return stauts;
	}

	public void setStauts(String stauts) {
		this.stauts = stauts;
	}

	public String getAllianceStatus() {
		return allianceStatus;
	}

	public void setAllianceStatus(String allianceStatus) {
		this.allianceStatus = allianceStatus;
	}

	public String getHpUid() {
		return hpUid;
	}

	public void setHpUid(String hpUid) {
		this.hpUid = hpUid;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}


	public List<String> getToHospitalIdItem() {
		return toHospitalIdItem;
	}

	public void setToHospitalIdItem(List<String> toHospitalIdItem) {
		this.toHospitalIdItem = toHospitalIdItem;
	}

	public List<String> getFileType() {
		return fileType;
	}

	public void setFileType(List<String> fileType) {
		this.fileType = fileType;
	}

	public String getAuthorizationFileType() {
		return authorizationFileType;
	}

	public void setAuthorizationFileType(String authorizationFileType) {
		this.authorizationFileType = authorizationFileType;
	}

	public String getAuthorizationStartDate() {
		return authorizationStartDate;
	}

	public void setAuthorizationStartDate(String authorizationStartDate) {
		this.authorizationStartDate = authorizationStartDate;
	}

	public String getAuthorizationEndDate() {
		return authorizationEndDate;
	}

	public void setAuthorizationEndDate(String authorizationEndDate) {
		this.authorizationEndDate = authorizationEndDate;
	}

	public String getFromHospitalId() {
		return fromHospitalId;
	}

	public void setFromHospitalId(String fromHospitalId) {
		this.fromHospitalId = fromHospitalId;
	}

	public String getToHospitalId() {
		return toHospitalId;
	}

	public void setToHospitalId(String toHospitalId) {
		this.toHospitalId = toHospitalId;
	}

	public Float getClaimSeqId() {
		return claimSeqId;
	}

	public void setClaimSeqId(Float claimSeqId) {
		this.claimSeqId = claimSeqId;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getFileReceived() {
		return fileReceived;
	}

	public void setFileReceived(String fileReceived) {
		this.fileReceived = fileReceived;
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
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
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

	public String getSendAlliance() {
		return sendAlliance;
	}

	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}

	public List<TransMedicalTreatmentClaimFileDataVo> getFileDatas() {
		return fileDatas;
	}

	public void setFileDatas(List<TransMedicalTreatmentClaimFileDataVo> fileDatas) {
		this.fileDatas = fileDatas;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRelation_name() {
		return relation_name;
	}

	public void setRelation_name(String relation_name) {
		this.relation_name = relation_name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getOtherCompanyInsured() {
		return otherCompanyInsured;
	}

	public void setOtherCompanyInsured(String otherCompanyInsured) {
		this.otherCompanyInsured = otherCompanyInsured;
	}

	public String getOtherInsuCompany() {
		return otherInsuCompany;
	}

	public void setOtherInsuCompany(String otherInsuCompany) {
		this.otherInsuCompany = otherInsuCompany;
	}

	public String getOtherInsuType() {
		return otherInsuType;
	}

	public void setOtherInsuType(String otherInsuType) {
		this.otherInsuType = otherInsuType;
	}

	public String getOtherInsuAmout() {
		return otherInsuAmout;
	}

	public void setOtherInsuAmout(String otherInsuAmout) {
		this.otherInsuAmout = otherInsuAmout;
	}

	public String getOtherInsuClaim() {
		return otherInsuClaim;
	}

	public void setOtherInsuClaim(String otherInsuClaim) {
		this.otherInsuClaim = otherInsuClaim;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPostalAddr() {
		return postalAddr;
	}

	public void setPostalAddr(String postalAddr) {
		this.postalAddr = postalAddr;
	}

	public String getItemOther() {
		return itemOther;
	}

	public void setItemOther(String itemOther) {
		this.itemOther = itemOther;
	}

	public List<String> getApplyItemList() {
		return applyItemList;
	}

	public void setApplyItemList(List<String> applyItemList) {
		this.applyItemList = applyItemList;
	}

	public String getCallPolice() {
		return callPolice;
	}

	public void setCallPolice(String callPolice) {
		this.callPolice = callPolice;
	}

	public String getApplyItem() {
		return applyItem;
	}

	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}

	public List<String> getToItem() {
		return toItem;
	}

	public void setToItem(List<String> toItem) {
		this.toItem = toItem;
	}

	public String getFileDataList() {
		return fileDataList;
	}

	public void setFileDataList(String fileDataList) {
		this.fileDataList = fileDataList;
	}

//	public Timestamp getCreateDate() {
//		return createDate;
//	}
//
//	public void setCreateDate(Timestamp createDate) {
//		this.createDate = createDate;
//	}

	public String getCpoa() {
		return cpoa;
	}

	public void setCpoa(String cpoa) {
		this.cpoa = cpoa;
	}

	public List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfo() {
		return medicalInfo;
	}

	public void setMedicalInfo(List<TransMedicalTreatmentClaimMedicalInfoVo> medicalInfo) {
		this.medicalInfo = medicalInfo;
	}

	public String getMedicalInfoList() {
		return medicalInfoList;
	}

	public void setMedicalInfoList(String medicalInfoList) {
		this.medicalInfoList = medicalInfoList;
	}

	@Override
	public String toString() {
		return "TransMedicalTreatmentClaimVo{" +
				"claimSeqId=" + claimSeqId +
				", transNum='" + transNum + '\'' +
				", caseId='" + caseId + '\'' +
				", fileReceived='" + fileReceived + '\'' +
				", applyItemList=" + applyItemList +
				", itemOther='" + itemOther + '\'' +
				", applyItem='" + applyItem + '\'' +
				", customerName='" + customerName + '\'' +
				", policyNo='" + policyNo + '\'' +
				", name='" + name + '\'' +
				", relation='" + relation + '\'' +
				", relation_name='" + relation_name + '\'' +
				", idNo='" + idNo + '\'' +
				", birdate='" + birdate + '\'' +
				", tel='" + tel + '\'' +
				", phone='" + phone + '\'' +
				", zipCode='" + zipCode + '\'' +
				", address='" + address + '\'' +
				", mail='" + mail + '\'' +
				", diseaseName='" + diseaseName + '\'' +
				", diagnosisDate='" + diagnosisDate + '\'' +
				", hospital='" + hospital + '\'' +
				", otherCompanyInsured='" + otherCompanyInsured + '\'' +
				", otherInsuCompany='" + otherInsuCompany + '\'' +
				", otherInsuType='" + otherInsuType + '\'' +
				", otherInsuAmout='" + otherInsuAmout + '\'' +
				", otherInsuClaim='" + otherInsuClaim + '\'' +
				", paymentMethod='" + paymentMethod + '\'' +
				", bankCode='" + bankCode + '\'' +
				", bankName='" + bankName + '\'' +
				", branchCode='" + branchCode + '\'' +
				", branchName='" + branchName + '\'' +
				", bankAccount='" + bankAccount + '\'' +
				", accountName='" + accountName + '\'' +
				", postalAddr='" + postalAddr + '\'' +
				", applicationDate='" + applicationDate + '\'' +
				", applicationTime='" + applicationTime + '\'' +
				", applicationItem='" + applicationItem + '\'' +
				", job='" + job + '\'' +
				", jobDescr='" + jobDescr + '\'' +
				", accidentDate='" + accidentDate + '\'' +
				", accidentTime='" + accidentTime + '\'' +
				", accidentCause='" + accidentCause + '\'' +
				", accidentLocation='" + accidentLocation + '\'' +
				", accidentDescr='" + accidentDescr + '\'' +
				", policeStation='" + policeStation + '\'' +
				", policeName='" + policeName + '\'' +
				", policePhone='" + policePhone + '\'' +
				", policeDate='" + policeDate + '\'' +
				", policeTime='" + policeTime + '\'' +
				", callPolice='" + callPolice + '\'' +
				", from='" + from + '\'' +
				", to='" + to + '\'' +
				", sendAlliance='" + sendAlliance + '\'' +
				", fileDatas=" + fileDatas +
				", fileDataList='" + fileDataList + '\'' +
				", toItem=" + toItem +
				", fileType=" + fileType +
				", authorizationFileType='" + authorizationFileType + '\'' +
				", authorizationStartDate='" + authorizationStartDate + '\'' +
				", authorizationEndDate='" + authorizationEndDate + '\'' +
				", fromHospitalId='" + fromHospitalId + '\'' +
				", toHospitalId='" + toHospitalId + '\'' +
				'}';
	}

	public String getSignAgree() {
		return signAgree;
	}

	public void setSignAgree(String signAgree) {
		this.signAgree = signAgree;
	}

//	@Override
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
//	}
}
