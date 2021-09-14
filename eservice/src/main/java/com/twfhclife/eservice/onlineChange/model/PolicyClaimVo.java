package com.twfhclife.eservice.onlineChange.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PolicyClaimVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	
	/** 要保人名稱 */
	private String customerName;
	
	/** 保單號碼*/
	private String policyNo;
	
	/** 申請項目*/
	private List<String> applyItemList;
	
	/** 申請項目-其他 */
	private String itemOther;
	
	/** 被保險人姓名 */
	private String insuredName;
	
	/** 與主被保險人(員工)之關係 */
	private String relation;
	
	/** 身分證號碼 */
	private String rocId;
	
	/** 聯絡電話 */
	private String tel1;
	
	/** 出生日期 */
	private String birthDate;
	
	/** 手機號碼 */
	private String mobile;
	
	/** email */
	private String email;
	
	/** 聯絡地址 */
	private String addr;
	
	/** 診斷病名 */
	private String diseaseName;
	
	/** 該疾病初診日 */
	private String diagnosisDate;
	
	/** 曾就診之醫院 */
	private String hospital;
	
	private String job;
	
	/** 發生時間-年 */
	private String accidentYear;
	
	/** 發生時間-月 */
	private String accidentMonth;
	
	/** 發生時間-日 */
	private String accidentDay;
	
	/** 發生時間-時 */
	private String accidentHour;
	
	/** 事故地點 */
	private String accidentLocation;
	
	/** 事故處理單位 */
	private String accidentHandleUnit;
	
	/** 承辦員警 */
	private String policeName;
	
	/** 員警電話 */
	private String policeTel;
	
	/** 事故原因及送醫經過詳情 */
	private String accidentReason;
	
	private String callPolice;
	
	private String callPoliceYear;
	
	private String callPoliceMonth;
	
	private String callPoliceDay;
	
	private String callPoliceHour;
	
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
	
	private List<String> allianceInsu; 
	
	/** 給付方式 */
	private String paymentMethod;
	
	/** 匯款帳戶 */
	private String accountName;
	
	/** 銀行名稱 */
	private String bankName;
	
	/** 分行名稱 */
	private String branchName;
	
	/** 帳號 */
	private String accountNumber;
	
	/** 郵遞區號 */
	private String postalCode;
	
	/** 郵寄地址 */
	private String postalAddr;
	
	private String acceptance;
	
	private String diagnosis;
	
	private String receipt;
	
	private String accident;
	
	private String beneficiary;
	
	private String accountCopy;

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

	public List<String> getApplyItemList() {
		return applyItemList;
	}

	public void setApplyItemList(List<String> applyItemList) {
		this.applyItemList = applyItemList;
	}

	public String getItemOther() {
		return itemOther;
	}

	public void setItemOther(String itemOther) {
		this.itemOther = itemOther;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getAccidentYear() {
		return accidentYear;
	}

	public void setAccidentYear(String accidentYear) {
		this.accidentYear = accidentYear;
	}

	public String getAccidentMonth() {
		return accidentMonth;
	}

	public void setAccidentMonth(String accidentMonth) {
		this.accidentMonth = accidentMonth;
	}

	public String getAccidentDay() {
		return accidentDay;
	}

	public void setAccidentDay(String accidentDay) {
		this.accidentDay = accidentDay;
	}

	public String getAccidentHour() {
		return accidentHour;
	}

	public void setAccidentHour(String accidentHour) {
		this.accidentHour = accidentHour;
	}

	public String getAccidentLocation() {
		return accidentLocation;
	}

	public void setAccidentLocation(String accidentLocation) {
		this.accidentLocation = accidentLocation;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	public String getPoliceTel() {
		return policeTel;
	}

	public void setPoliceTel(String policeTel) {
		this.policeTel = policeTel;
	}

	public String getAccidentReason() {
		return accidentReason;
	}

	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}

	public String getCallPolice() {
		return callPolice;
	}

	public void setCallPolice(String callPolice) {
		this.callPolice = callPolice;
	}

	public String getCallPoliceYear() {
		return callPoliceYear;
	}

	public void setCallPoliceYear(String callPoliceYear) {
		this.callPoliceYear = callPoliceYear;
	}

	public String getCallPoliceMonth() {
		return callPoliceMonth;
	}

	public void setCallPoliceMonth(String callPoliceMonth) {
		this.callPoliceMonth = callPoliceMonth;
	}

	public String getCallPoliceDay() {
		return callPoliceDay;
	}

	public void setCallPoliceDay(String callPoliceDay) {
		this.callPoliceDay = callPoliceDay;
	}

	public String getCallPoliceHour() {
		return callPoliceHour;
	}

	public void setCallPoliceHour(String callPoliceHour) {
		this.callPoliceHour = callPoliceHour;
	}

	public String getAccidentHandleUnit() {
		return accidentHandleUnit;
	}

	public void setAccidentHandleUnit(String accidentHandleUnit) {
		this.accidentHandleUnit = accidentHandleUnit;
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

	public List<String> getAllianceInsu() {
		return allianceInsu;
	}

	public void setAllianceInsu(List<String> allianceInsu) {
		this.allianceInsu = allianceInsu;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPostalAddr() {
		return postalAddr;
	}

	public void setPostalAddr(String postalAddr) {
		this.postalAddr = postalAddr;
	}

	public String getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getAccident() {
		return accident;
	}

	public void setAccident(String accident) {
		this.accident = accident;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public String getAccountCopy() {
		return accountCopy;
	}

	public void setAccountCopy(String accountCopy) {
		this.accountCopy = accountCopy;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}