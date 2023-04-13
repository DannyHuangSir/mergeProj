package com.twfhclife.generic.api_model;

import java.io.Serializable;

/**
 * 查詢 ILIPM_VIEW1, LILIPI_VIEW1, LIPMDA_VIEW1 共用
 * 
 * @author iisi04
 *
 */
public class TransCtcSelectUtilVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lipmInsuNo;
	private String lipmInsuType;
	private String lipmInsuGrpNo;
	private String lipmInsuSeqNo;
	private String lipmName1;
	private String lipmName2;
	private String lipmAddr;
	private String lipmZipCode;
	private String lipmTelH;
	private String lipmTelO;
	private String lipmExt;
	private String lipmCharAddr;
	private String lipmCharZipCode;
	private String lipmCollCode;
	private String lipmAgenCode;
	private String lipmAgenNaCode;
	private String lipmAgenCodeBranch;
	private String lipmInsuBegDate;
	private String lipmRcpCode;
	private String lipmTredTms;
	private String lipmTredRcpDate;
	private String lipmWaitTms;
	private String lipmTredPaabDate;
	private String lipmBonCode;
	private String lipmLoMk;
	private String lipmLiMk;
	private String lipmLiDate;
	private String lipmAutoRcpMk;
	private String lipmRcpMethCode;
	private String lipmCharUnit;
	private String lipmCharAt1;
	private String lipmCharAt2;
	private String lipmSubBegDate;
	private String lipmSubEndDate;
	private String lipmSt;
	private String lipmStDate;
	private String lipmAccDate;
	private String lipmApprDate;
	private String lipmAccUnit;
	private String lipmSupTms;
	private String lipmOpCode;
	private String lipmUpDate;
	private String lipmHiContSeq;
	private String lipmHiBeniSeq;
	private String lipmDdChanKind;
	private String lipmDiscRate;
	private String lipm5Gp;
	private String lipmCharId;
	private String lipmAssmNo;
	private String lipmAssmAge;
	private String lipmAssmTypeNo;
	private String lipmBankCode;
	private String lipmBankBranchCode;
	private String lipmBankTranDd;
	private String lipmBon3Code;
	private String lipm5GUnit;
	private String lipm5GSeq;
	private String lipmProjectCode;
	private String lipmId;
	private String lipmDmMk;
	private String lipmEarthquakeMk;
	private String lipmEarthqkDueDate;
	private String lipmOriRcpCode;
	private String lipmOriTredTms;
	private String lipmOriBonCode;
	private String lipmMortBank;
	private String lipmTrustMk;
	private String lipmTargetPrem;
	private String lipmFlexRcpMk;
	private String lipmLoanMethMk;
	private String lipmRiderMethMk;
	private String lipiInsuType;
	private String lipiInsuGrpNo;
	private String lipiInsuSeqNo;
	private String lipiGpNo;
	private String lipiSeq;
	private String lipiName;
	private String lipiBirth;
	private String lipiBirthErr;
	private String lipiAge;
	private String lipiSex;
	private String lipiId;
	private String lipiJobCode;
	private String lipiJob;
	private String lipiRela;
	private String lipiAddr;
	private String lipiHealCode;
	private String lipiInve;
	private String lipiInsuBegDate;
	private String lipiInsuEndDate;
	private String lipiAgeExpi;
	private String lipiStoreYear;
	private String lipiPremYear;
	private String lipiLiRate;
	private String lipiMainAmt;
	private String lipiTablPrem;
	private String lipiUnHeal;
	private String lipiUnHealYear;
	private String lipiUnHealPrem;
	private String lipiDanger;
	private String lipiDangPrem;
	private String lipiAddMk;
	private String lipiLoMk;
	private String lipiLoTrMk;
	private String lipiReinMk;
	private String lipiTredRcpDate;
	private String lipiTredRcpTms;
	private String lipiSt;
	private String lipiStDate;
	private String lipiLiPaidDate;
	private String lipiLiPaidTms;
	private String lipiSupTms;
	private String lipiLostTms;
	private String lipiFirPay;
	private String lipiHiContSeq;
	private String lipiHiChanSeq;
	private String lipiHiAddSeq;
	private String lipiHiBeniSeq;
	private String lipiDdChanKind;
	private String lipiDfAppKind;
	private String lipiOldMainAmt;
	private String lipiTrInsuNo;
	private String lipiLiMatuDate;
	private String lipiMatuDate;
	private String lipiLastInsuDate;
	private String lipiOpCode;
	private String lipiUpDate;
	private String lipiCancerType;
	private String lipiCancerQuan;
	private String lipiAssmLevel;
	private String lipiSocialCode;
	private String lipiCancerSt;
	private String lipiCommMk;
	private String lipiSpecialMk;
	private String lipiTrAInsuNo;
	private String lipiTrDate;
	private String lipiNoComm;
	private String lipiLiTrMk;
	private String lipiExtendLiAmt;
	private String lipiMailMk;
	private String lipiPaLiRatio;
	private String lipiAgeAnnuPay;
	private String lipiYearAnnuPay;
	private String lipiGuarTerm;
	private String lipiLiving;
	private String lipiMaTrMk;
	private String lipiMethAnnuPay;
	private String lipiDeathBeneType;
	private String pmdaInsuType;
	private String pmdaInsuGrpNo;
	private String pmdaInsuSeqNo;
	private String pmda3Name;
	private String pmda3Addr;
	private String pmda3ZipCode;
	private String pmdaOpCode;
	private String pmdaUpDate;
	private String pmdaAtmNo;
	private String pmdaApplicantEmail;
	private String pmdaApplicantCellphone;
	private String pmdaInsuredEmail;
	private String pmdaInsuredCellphone;
	private String pmdaSourcePolicy;
	private String pmdaAsdaCode;
	private String pmdaAsdaUnit;
	private String pmdaAsdaSeq;
	private String pmdaMinorityCode;
	private String pmdaLinkCode;
	private String pmdaCreDate;
	private String pmdaRecreMk;
	private String pmdaArriDate;
	private String pmdaUpDateA;
	private String pmdaOpCodeA;
	private String pmdaFirstRcpAmt;
	private String pmdaInsuNewsMk;
	private String pmdaEInfoN;
	private String pmdaEInfoC;
	private String pmdaEInfoP;
	private String pmdaEStmt;
	private String pmdaPiTel;
	private String pmdaPiZipCode;
	private String pmdaPiName2;
	private String pmdaReviewDate;
	private String pmdaApplicantPeps;
	private String pmdaInsuredPeps;
	private String pmdaInveAttr;
	private String pmdaMail;
	private String pmdaFirstTrMethod;
	private String pmdaPmCtype;
	private String pmdaPmTrade;
	private String pmdaPmNation;
	private String pmdaPrintDate;
	private String pmdaPmStocks;
	private String pmdaPmAddrTw;
	private String pmdaPmExistPerd;
	private String pmdaPiNation;
	private String pmdaPmJobLevel;
	private String pmdaCreditBank;
	private String pmdaCreditNo;
	private String pmdaRiskTrail;
	private String pmdaWriter;
	private String pmdaBackMethod;
	private String pmdaEpoMk;
	private String pmdaOldPo;
	private String pmda2monthPapr;
	private String pmda1stCfDate;
	private String pmdaPremSource;
	private String pmdaSubMino;
	private String assnArriDate;
	private String settChName;
	private String prodCurrency;
	private String settTerminate;
	
	public String getSettTerminate() {
		return settTerminate;
	}
	public void setSettTerminate(String settTerminate) {
		this.settTerminate = settTerminate;
	}

	public String getLipmInsuNo() {
		return lipmInsuNo;
	}

	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}

	public String getLipmInsuType() {
		return lipmInsuType;
	}

	public void setLipmInsuType(String lipmInsuType) {
		this.lipmInsuType = lipmInsuType;
	}

	public String getLipmInsuGrpNo() {
		return lipmInsuGrpNo;
	}

	public void setLipmInsuGrpNo(String lipmInsuGrpNo) {
		this.lipmInsuGrpNo = lipmInsuGrpNo;
	}

	public String getLipmInsuSeqNo() {
		return lipmInsuSeqNo;
	}

	public void setLipmInsuSeqNo(String lipmInsuSeqNo) {
		this.lipmInsuSeqNo = lipmInsuSeqNo;
	}

	public String getLipmName1() {
		return lipmName1;
	}

	public void setLipmName1(String lipmName1) {
		this.lipmName1 = lipmName1;
	}

	public String getLipmName2() {
		return lipmName2;
	}

	public void setLipmName2(String lipmName2) {
		this.lipmName2 = lipmName2;
	}

	public String getLipmAddr() {
		return lipmAddr;
	}

	public void setLipmAddr(String lipmAddr) {
		this.lipmAddr = lipmAddr;
	}

	public String getLipmZipCode() {
		return lipmZipCode;
	}

	public void setLipmZipCode(String lipmZipCode) {
		this.lipmZipCode = lipmZipCode;
	}

	public String getLipmTelH() {
		return lipmTelH;
	}

	public void setLipmTelH(String lipmTelH) {
		this.lipmTelH = lipmTelH;
	}

	public String getLipmTelO() {
		return lipmTelO;
	}

	public void setLipmTelO(String lipmTelO) {
		this.lipmTelO = lipmTelO;
	}

	public String getLipmExt() {
		return lipmExt;
	}

	public void setLipmExt(String lipmExt) {
		this.lipmExt = lipmExt;
	}

	public String getLipmCharAddr() {
		return lipmCharAddr;
	}

	public void setLipmCharAddr(String lipmCharAddr) {
		this.lipmCharAddr = lipmCharAddr;
	}

	public String getLipmCharZipCode() {
		return lipmCharZipCode;
	}

	public void setLipmCharZipCode(String lipmCharZipCode) {
		this.lipmCharZipCode = lipmCharZipCode;
	}

	public String getLipmCollCode() {
		return lipmCollCode;
	}

	public void setLipmCollCode(String lipmCollCode) {
		this.lipmCollCode = lipmCollCode;
	}

	public String getLipmAgenCode() {
		return lipmAgenCode;
	}

	public void setLipmAgenCode(String lipmAgenCode) {
		this.lipmAgenCode = lipmAgenCode;
	}

	public String getLipmAgenNaCode() {
		return lipmAgenNaCode;
	}

	public void setLipmAgenNaCode(String lipmAgenNaCode) {
		this.lipmAgenNaCode = lipmAgenNaCode;
	}

	public String getLipmAgenCodeBranch() {
		return lipmAgenCodeBranch;
	}

	public void setLipmAgenCodeBranch(String lipmAgenCodeBranch) {
		this.lipmAgenCodeBranch = lipmAgenCodeBranch;
	}

	public String getLipmInsuBegDate() {
		return lipmInsuBegDate;
	}

	public void setLipmInsuBegDate(String lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
	}

	public String getLipmRcpCode() {
		return lipmRcpCode;
	}

	public void setLipmRcpCode(String lipmRcpCode) {
		this.lipmRcpCode = lipmRcpCode;
	}

	public String getLipmTredTms() {
		return lipmTredTms;
	}

	public void setLipmTredTms(String lipmTredTms) {
		this.lipmTredTms = lipmTredTms;
	}

	public String getLipmTredRcpDate() {
		return lipmTredRcpDate;
	}

	public void setLipmTredRcpDate(String lipmTredRcpDate) {
		this.lipmTredRcpDate = lipmTredRcpDate;
	}

	public String getLipmWaitTms() {
		return lipmWaitTms;
	}

	public void setLipmWaitTms(String lipmWaitTms) {
		this.lipmWaitTms = lipmWaitTms;
	}

	public String getLipmTredPaabDate() {
		return lipmTredPaabDate;
	}

	public void setLipmTredPaabDate(String lipmTredPaabDate) {
		this.lipmTredPaabDate = lipmTredPaabDate;
	}

	public String getLipmBonCode() {
		return lipmBonCode;
	}

	public void setLipmBonCode(String lipmBonCode) {
		this.lipmBonCode = lipmBonCode;
	}

	public String getLipmLoMk() {
		return lipmLoMk;
	}

	public void setLipmLoMk(String lipmLoMk) {
		this.lipmLoMk = lipmLoMk;
	}

	public String getLipmLiMk() {
		return lipmLiMk;
	}

	public void setLipmLiMk(String lipmLiMk) {
		this.lipmLiMk = lipmLiMk;
	}

	public String getLipmLiDate() {
		return lipmLiDate;
	}

	public void setLipmLiDate(String lipmLiDate) {
		this.lipmLiDate = lipmLiDate;
	}

	public String getLipmAutoRcpMk() {
		return lipmAutoRcpMk;
	}

	public void setLipmAutoRcpMk(String lipmAutoRcpMk) {
		this.lipmAutoRcpMk = lipmAutoRcpMk;
	}

	public String getLipmRcpMethCode() {
		return lipmRcpMethCode;
	}

	public void setLipmRcpMethCode(String lipmRcpMethCode) {
		this.lipmRcpMethCode = lipmRcpMethCode;
	}

	public String getLipmCharUnit() {
		return lipmCharUnit;
	}

	public void setLipmCharUnit(String lipmCharUnit) {
		this.lipmCharUnit = lipmCharUnit;
	}

	public String getLipmCharAt1() {
		return lipmCharAt1;
	}

	public void setLipmCharAt1(String lipmCharAt1) {
		this.lipmCharAt1 = lipmCharAt1;
	}

	public String getLipmCharAt2() {
		return lipmCharAt2;
	}

	public void setLipmCharAt2(String lipmCharAt2) {
		this.lipmCharAt2 = lipmCharAt2;
	}

	public String getLipmSubBegDate() {
		return lipmSubBegDate;
	}

	public void setLipmSubBegDate(String lipmSubBegDate) {
		this.lipmSubBegDate = lipmSubBegDate;
	}

	public String getLipmSubEndDate() {
		return lipmSubEndDate;
	}

	public void setLipmSubEndDate(String lipmSubEndDate) {
		this.lipmSubEndDate = lipmSubEndDate;
	}

	public String getLipmSt() {
		return lipmSt;
	}

	public void setLipmSt(String lipmSt) {
		this.lipmSt = lipmSt;
	}

	public String getLipmStDate() {
		return lipmStDate;
	}

	public void setLipmStDate(String lipmStDate) {
		this.lipmStDate = lipmStDate;
	}

	public String getLipmAccDate() {
		return lipmAccDate;
	}

	public void setLipmAccDate(String lipmAccDate) {
		this.lipmAccDate = lipmAccDate;
	}

	public String getLipmApprDate() {
		return lipmApprDate;
	}

	public void setLipmApprDate(String lipmApprDate) {
		this.lipmApprDate = lipmApprDate;
	}

	public String getLipmAccUnit() {
		return lipmAccUnit;
	}

	public void setLipmAccUnit(String lipmAccUnit) {
		this.lipmAccUnit = lipmAccUnit;
	}

	public String getLipmSupTms() {
		return lipmSupTms;
	}

	public void setLipmSupTms(String lipmSupTms) {
		this.lipmSupTms = lipmSupTms;
	}

	public String getLipmOpCode() {
		return lipmOpCode;
	}

	public void setLipmOpCode(String lipmOpCode) {
		this.lipmOpCode = lipmOpCode;
	}

	public String getLipmUpDate() {
		return lipmUpDate;
	}

	public void setLipmUpDate(String lipmUpDate) {
		this.lipmUpDate = lipmUpDate;
	}

	public String getLipmHiContSeq() {
		return lipmHiContSeq;
	}

	public void setLipmHiContSeq(String lipmHiContSeq) {
		this.lipmHiContSeq = lipmHiContSeq;
	}

	public String getLipmHiBeniSeq() {
		return lipmHiBeniSeq;
	}

	public void setLipmHiBeniSeq(String lipmHiBeniSeq) {
		this.lipmHiBeniSeq = lipmHiBeniSeq;
	}

	public String getLipmDdChanKind() {
		return lipmDdChanKind;
	}

	public void setLipmDdChanKind(String lipmDdChanKind) {
		this.lipmDdChanKind = lipmDdChanKind;
	}

	public String getLipmDiscRate() {
		return lipmDiscRate;
	}

	public void setLipmDiscRate(String lipmDiscRate) {
		this.lipmDiscRate = lipmDiscRate;
	}

	public String getLipm5Gp() {
		return lipm5Gp;
	}

	public void setLipm5Gp(String lipm5Gp) {
		this.lipm5Gp = lipm5Gp;
	}

	public String getLipmCharId() {
		return lipmCharId;
	}

	public void setLipmCharId(String lipmCharId) {
		this.lipmCharId = lipmCharId;
	}

	public String getLipmAssmNo() {
		return lipmAssmNo;
	}

	public void setLipmAssmNo(String lipmAssmNo) {
		this.lipmAssmNo = lipmAssmNo;
	}

	public String getLipmAssmAge() {
		return lipmAssmAge;
	}

	public void setLipmAssmAge(String lipmAssmAge) {
		this.lipmAssmAge = lipmAssmAge;
	}

	public String getLipmAssmTypeNo() {
		return lipmAssmTypeNo;
	}

	public void setLipmAssmTypeNo(String lipmAssmTypeNo) {
		this.lipmAssmTypeNo = lipmAssmTypeNo;
	}

	public String getLipmBankCode() {
		return lipmBankCode;
	}

	public void setLipmBankCode(String lipmBankCode) {
		this.lipmBankCode = lipmBankCode;
	}

	public String getLipmBankBranchCode() {
		return lipmBankBranchCode;
	}

	public void setLipmBankBranchCode(String lipmBankBranchCode) {
		this.lipmBankBranchCode = lipmBankBranchCode;
	}

	public String getLipmBankTranDd() {
		return lipmBankTranDd;
	}

	public void setLipmBankTranDd(String lipmBankTranDd) {
		this.lipmBankTranDd = lipmBankTranDd;
	}

	public String getLipmBon3Code() {
		return lipmBon3Code;
	}

	public void setLipmBon3Code(String lipmBon3Code) {
		this.lipmBon3Code = lipmBon3Code;
	}

	public String getLipm5GUnit() {
		return lipm5GUnit;
	}

	public void setLipm5GUnit(String lipm5gUnit) {
		lipm5GUnit = lipm5gUnit;
	}

	public String getLipm5GSeq() {
		return lipm5GSeq;
	}

	public void setLipm5GSeq(String lipm5gSeq) {
		lipm5GSeq = lipm5gSeq;
	}

	public String getLipmProjectCode() {
		return lipmProjectCode;
	}

	public void setLipmProjectCode(String lipmProjectCode) {
		this.lipmProjectCode = lipmProjectCode;
	}

	public String getLipmId() {
		return lipmId;
	}

	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}

	public String getLipmDmMk() {
		return lipmDmMk;
	}

	public void setLipmDmMk(String lipmDmMk) {
		this.lipmDmMk = lipmDmMk;
	}

	public String getLipmEarthquakeMk() {
		return lipmEarthquakeMk;
	}

	public void setLipmEarthquakeMk(String lipmEarthquakeMk) {
		this.lipmEarthquakeMk = lipmEarthquakeMk;
	}

	public String getLipmEarthqkDueDate() {
		return lipmEarthqkDueDate;
	}

	public void setLipmEarthqkDueDate(String lipmEarthqkDueDate) {
		this.lipmEarthqkDueDate = lipmEarthqkDueDate;
	}

	public String getLipmOriRcpCode() {
		return lipmOriRcpCode;
	}

	public void setLipmOriRcpCode(String lipmOriRcpCode) {
		this.lipmOriRcpCode = lipmOriRcpCode;
	}

	public String getLipmOriTredTms() {
		return lipmOriTredTms;
	}

	public void setLipmOriTredTms(String lipmOriTredTms) {
		this.lipmOriTredTms = lipmOriTredTms;
	}

	public String getLipmOriBonCode() {
		return lipmOriBonCode;
	}

	public void setLipmOriBonCode(String lipmOriBonCode) {
		this.lipmOriBonCode = lipmOriBonCode;
	}

	public String getLipmMortBank() {
		return lipmMortBank;
	}

	public void setLipmMortBank(String lipmMortBank) {
		this.lipmMortBank = lipmMortBank;
	}

	public String getLipmTrustMk() {
		return lipmTrustMk;
	}

	public void setLipmTrustMk(String lipmTrustMk) {
		this.lipmTrustMk = lipmTrustMk;
	}

	public String getLipmTargetPrem() {
		return lipmTargetPrem;
	}

	public void setLipmTargetPrem(String lipmTargetPrem) {
		this.lipmTargetPrem = lipmTargetPrem;
	}

	public String getLipmFlexRcpMk() {
		return lipmFlexRcpMk;
	}

	public void setLipmFlexRcpMk(String lipmFlexRcpMk) {
		this.lipmFlexRcpMk = lipmFlexRcpMk;
	}

	public String getLipmLoanMethMk() {
		return lipmLoanMethMk;
	}

	public void setLipmLoanMethMk(String lipmLoanMethMk) {
		this.lipmLoanMethMk = lipmLoanMethMk;
	}

	public String getLipmRiderMethMk() {
		return lipmRiderMethMk;
	}

	public void setLipmRiderMethMk(String lipmRiderMethMk) {
		this.lipmRiderMethMk = lipmRiderMethMk;
	}

	public String getLipiInsuType() {
		return lipiInsuType;
	}

	public void setLipiInsuType(String lipiInsuType) {
		this.lipiInsuType = lipiInsuType;
	}

	public String getLipiInsuGrpNo() {
		return lipiInsuGrpNo;
	}

	public void setLipiInsuGrpNo(String lipiInsuGrpNo) {
		this.lipiInsuGrpNo = lipiInsuGrpNo;
	}

	public String getLipiInsuSeqNo() {
		return lipiInsuSeqNo;
	}

	public void setLipiInsuSeqNo(String lipiInsuSeqNo) {
		this.lipiInsuSeqNo = lipiInsuSeqNo;
	}

	public String getLipiGpNo() {
		return lipiGpNo;
	}

	public void setLipiGpNo(String lipiGpNo) {
		this.lipiGpNo = lipiGpNo;
	}

	public String getLipiSeq() {
		return lipiSeq;
	}

	public void setLipiSeq(String lipiSeq) {
		this.lipiSeq = lipiSeq;
	}

	public String getLipiName() {
		return lipiName;
	}

	public void setLipiName(String lipiName) {
		this.lipiName = lipiName;
	}

	public String getLipiBirth() {
		return lipiBirth;
	}

	public void setLipiBirth(String lipiBirth) {
		this.lipiBirth = lipiBirth;
	}

	public String getLipiBirthErr() {
		return lipiBirthErr;
	}

	public void setLipiBirthErr(String lipiBirthErr) {
		this.lipiBirthErr = lipiBirthErr;
	}

	public String getLipiAge() {
		return lipiAge;
	}

	public void setLipiAge(String lipiAge) {
		this.lipiAge = lipiAge;
	}

	public String getLipiSex() {
		return lipiSex;
	}

	public void setLipiSex(String lipiSex) {
		this.lipiSex = lipiSex;
	}

	public String getLipiId() {
		return lipiId;
	}

	public void setLipiId(String lipiId) {
		this.lipiId = lipiId;
	}

	public String getLipiJobCode() {
		return lipiJobCode;
	}

	public void setLipiJobCode(String lipiJobCode) {
		this.lipiJobCode = lipiJobCode;
	}

	public String getLipiJob() {
		return lipiJob;
	}

	public void setLipiJob(String lipiJob) {
		this.lipiJob = lipiJob;
	}

	public String getLipiRela() {
		return lipiRela;
	}

	public void setLipiRela(String lipiRela) {
		this.lipiRela = lipiRela;
	}

	public String getLipiAddr() {
		return lipiAddr;
	}

	public void setLipiAddr(String lipiAddr) {
		this.lipiAddr = lipiAddr;
	}

	public String getLipiHealCode() {
		return lipiHealCode;
	}

	public void setLipiHealCode(String lipiHealCode) {
		this.lipiHealCode = lipiHealCode;
	}

	public String getLipiInve() {
		return lipiInve;
	}

	public void setLipiInve(String lipiInve) {
		this.lipiInve = lipiInve;
	}

	public String getLipiInsuBegDate() {
		return lipiInsuBegDate;
	}

	public void setLipiInsuBegDate(String lipiInsuBegDate) {
		this.lipiInsuBegDate = lipiInsuBegDate;
	}

	public String getLipiInsuEndDate() {
		return lipiInsuEndDate;
	}

	public void setLipiInsuEndDate(String lipiInsuEndDate) {
		this.lipiInsuEndDate = lipiInsuEndDate;
	}

	public String getLipiAgeExpi() {
		return lipiAgeExpi;
	}

	public void setLipiAgeExpi(String lipiAgeExpi) {
		this.lipiAgeExpi = lipiAgeExpi;
	}

	public String getLipiStoreYear() {
		return lipiStoreYear;
	}

	public void setLipiStoreYear(String lipiStoreYear) {
		this.lipiStoreYear = lipiStoreYear;
	}

	public String getLipiPremYear() {
		return lipiPremYear;
	}

	public void setLipiPremYear(String lipiPremYear) {
		this.lipiPremYear = lipiPremYear;
	}

	public String getLipiLiRate() {
		return lipiLiRate;
	}

	public void setLipiLiRate(String lipiLiRate) {
		this.lipiLiRate = lipiLiRate;
	}

	public String getLipiMainAmt() {
		return lipiMainAmt;
	}

	public void setLipiMainAmt(String lipiMainAmt) {
		this.lipiMainAmt = lipiMainAmt;
	}

	public String getLipiTablPrem() {
		return lipiTablPrem;
	}

	public void setLipiTablPrem(String lipiTablPrem) {
		this.lipiTablPrem = lipiTablPrem;
	}

	public String getLipiUnHeal() {
		return lipiUnHeal;
	}

	public void setLipiUnHeal(String lipiUnHeal) {
		this.lipiUnHeal = lipiUnHeal;
	}

	public String getLipiUnHealYear() {
		return lipiUnHealYear;
	}

	public void setLipiUnHealYear(String lipiUnHealYear) {
		this.lipiUnHealYear = lipiUnHealYear;
	}

	public String getLipiUnHealPrem() {
		return lipiUnHealPrem;
	}

	public void setLipiUnHealPrem(String lipiUnHealPrem) {
		this.lipiUnHealPrem = lipiUnHealPrem;
	}

	public String getLipiDanger() {
		return lipiDanger;
	}

	public void setLipiDanger(String lipiDanger) {
		this.lipiDanger = lipiDanger;
	}

	public String getLipiDangPrem() {
		return lipiDangPrem;
	}

	public void setLipiDangPrem(String lipiDangPrem) {
		this.lipiDangPrem = lipiDangPrem;
	}

	public String getLipiAddMk() {
		return lipiAddMk;
	}

	public void setLipiAddMk(String lipiAddMk) {
		this.lipiAddMk = lipiAddMk;
	}

	public String getLipiLoMk() {
		return lipiLoMk;
	}

	public void setLipiLoMk(String lipiLoMk) {
		this.lipiLoMk = lipiLoMk;
	}

	public String getLipiLoTrMk() {
		return lipiLoTrMk;
	}

	public void setLipiLoTrMk(String lipiLoTrMk) {
		this.lipiLoTrMk = lipiLoTrMk;
	}

	public String getLipiReinMk() {
		return lipiReinMk;
	}

	public void setLipiReinMk(String lipiReinMk) {
		this.lipiReinMk = lipiReinMk;
	}

	public String getLipiTredRcpDate() {
		return lipiTredRcpDate;
	}

	public void setLipiTredRcpDate(String lipiTredRcpDate) {
		this.lipiTredRcpDate = lipiTredRcpDate;
	}

	public String getLipiTredRcpTms() {
		return lipiTredRcpTms;
	}

	public void setLipiTredRcpTms(String lipiTredRcpTms) {
		this.lipiTredRcpTms = lipiTredRcpTms;
	}

	public String getLipiSt() {
		return lipiSt;
	}

	public void setLipiSt(String lipiSt) {
		this.lipiSt = lipiSt;
	}

	public String getLipiStDate() {
		return lipiStDate;
	}

	public void setLipiStDate(String lipiStDate) {
		this.lipiStDate = lipiStDate;
	}

	public String getLipiLiPaidDate() {
		return lipiLiPaidDate;
	}

	public void setLipiLiPaidDate(String lipiLiPaidDate) {
		this.lipiLiPaidDate = lipiLiPaidDate;
	}

	public String getLipiLiPaidTms() {
		return lipiLiPaidTms;
	}

	public void setLipiLiPaidTms(String lipiLiPaidTms) {
		this.lipiLiPaidTms = lipiLiPaidTms;
	}

	public String getLipiSupTms() {
		return lipiSupTms;
	}

	public void setLipiSupTms(String lipiSupTms) {
		this.lipiSupTms = lipiSupTms;
	}

	public String getLipiLostTms() {
		return lipiLostTms;
	}

	public void setLipiLostTms(String lipiLostTms) {
		this.lipiLostTms = lipiLostTms;
	}

	public String getLipiFirPay() {
		return lipiFirPay;
	}

	public void setLipiFirPay(String lipiFirPay) {
		this.lipiFirPay = lipiFirPay;
	}

	public String getLipiHiContSeq() {
		return lipiHiContSeq;
	}

	public void setLipiHiContSeq(String lipiHiContSeq) {
		this.lipiHiContSeq = lipiHiContSeq;
	}

	public String getLipiHiChanSeq() {
		return lipiHiChanSeq;
	}

	public void setLipiHiChanSeq(String lipiHiChanSeq) {
		this.lipiHiChanSeq = lipiHiChanSeq;
	}

	public String getLipiHiAddSeq() {
		return lipiHiAddSeq;
	}

	public void setLipiHiAddSeq(String lipiHiAddSeq) {
		this.lipiHiAddSeq = lipiHiAddSeq;
	}

	public String getLipiHiBeniSeq() {
		return lipiHiBeniSeq;
	}

	public void setLipiHiBeniSeq(String lipiHiBeniSeq) {
		this.lipiHiBeniSeq = lipiHiBeniSeq;
	}

	public String getLipiDdChanKind() {
		return lipiDdChanKind;
	}

	public void setLipiDdChanKind(String lipiDdChanKind) {
		this.lipiDdChanKind = lipiDdChanKind;
	}

	public String getLipiDfAppKind() {
		return lipiDfAppKind;
	}

	public void setLipiDfAppKind(String lipiDfAppKind) {
		this.lipiDfAppKind = lipiDfAppKind;
	}

	public String getLipiOldMainAmt() {
		return lipiOldMainAmt;
	}

	public void setLipiOldMainAmt(String lipiOldMainAmt) {
		this.lipiOldMainAmt = lipiOldMainAmt;
	}

	public String getLipiTrInsuNo() {
		return lipiTrInsuNo;
	}

	public void setLipiTrInsuNo(String lipiTrInsuNo) {
		this.lipiTrInsuNo = lipiTrInsuNo;
	}

	public String getLipiLiMatuDate() {
		return lipiLiMatuDate;
	}

	public void setLipiLiMatuDate(String lipiLiMatuDate) {
		this.lipiLiMatuDate = lipiLiMatuDate;
	}

	public String getLipiMatuDate() {
		return lipiMatuDate;
	}

	public void setLipiMatuDate(String lipiMatuDate) {
		this.lipiMatuDate = lipiMatuDate;
	}

	public String getLipiLastInsuDate() {
		return lipiLastInsuDate;
	}

	public void setLipiLastInsuDate(String lipiLastInsuDate) {
		this.lipiLastInsuDate = lipiLastInsuDate;
	}

	public String getLipiOpCode() {
		return lipiOpCode;
	}

	public void setLipiOpCode(String lipiOpCode) {
		this.lipiOpCode = lipiOpCode;
	}

	public String getLipiUpDate() {
		return lipiUpDate;
	}

	public void setLipiUpDate(String lipiUpDate) {
		this.lipiUpDate = lipiUpDate;
	}

	public String getLipiCancerType() {
		return lipiCancerType;
	}

	public void setLipiCancerType(String lipiCancerType) {
		this.lipiCancerType = lipiCancerType;
	}

	public String getLipiCancerQuan() {
		return lipiCancerQuan;
	}

	public void setLipiCancerQuan(String lipiCancerQuan) {
		this.lipiCancerQuan = lipiCancerQuan;
	}

	public String getLipiAssmLevel() {
		return lipiAssmLevel;
	}

	public void setLipiAssmLevel(String lipiAssmLevel) {
		this.lipiAssmLevel = lipiAssmLevel;
	}

	public String getLipiSocialCode() {
		return lipiSocialCode;
	}

	public void setLipiSocialCode(String lipiSocialCode) {
		this.lipiSocialCode = lipiSocialCode;
	}

	public String getLipiCancerSt() {
		return lipiCancerSt;
	}

	public void setLipiCancerSt(String lipiCancerSt) {
		this.lipiCancerSt = lipiCancerSt;
	}

	public String getLipiCommMk() {
		return lipiCommMk;
	}

	public void setLipiCommMk(String lipiCommMk) {
		this.lipiCommMk = lipiCommMk;
	}

	public String getLipiSpecialMk() {
		return lipiSpecialMk;
	}

	public void setLipiSpecialMk(String lipiSpecialMk) {
		this.lipiSpecialMk = lipiSpecialMk;
	}

	public String getLipiTrAInsuNo() {
		return lipiTrAInsuNo;
	}

	public void setLipiTrAInsuNo(String lipiTrAInsuNo) {
		this.lipiTrAInsuNo = lipiTrAInsuNo;
	}

	public String getLipiTrDate() {
		return lipiTrDate;
	}

	public void setLipiTrDate(String lipiTrDate) {
		this.lipiTrDate = lipiTrDate;
	}

	public String getLipiNoComm() {
		return lipiNoComm;
	}

	public void setLipiNoComm(String lipiNoComm) {
		this.lipiNoComm = lipiNoComm;
	}

	public String getLipiLiTrMk() {
		return lipiLiTrMk;
	}

	public void setLipiLiTrMk(String lipiLiTrMk) {
		this.lipiLiTrMk = lipiLiTrMk;
	}

	public String getLipiExtendLiAmt() {
		return lipiExtendLiAmt;
	}

	public void setLipiExtendLiAmt(String lipiExtendLiAmt) {
		this.lipiExtendLiAmt = lipiExtendLiAmt;
	}

	public String getLipiMailMk() {
		return lipiMailMk;
	}

	public void setLipiMailMk(String lipiMailMk) {
		this.lipiMailMk = lipiMailMk;
	}

	public String getLipiPaLiRatio() {
		return lipiPaLiRatio;
	}

	public void setLipiPaLiRatio(String lipiPaLiRatio) {
		this.lipiPaLiRatio = lipiPaLiRatio;
	}

	public String getLipiAgeAnnuPay() {
		return lipiAgeAnnuPay;
	}

	public void setLipiAgeAnnuPay(String lipiAgeAnnuPay) {
		this.lipiAgeAnnuPay = lipiAgeAnnuPay;
	}

	public String getLipiYearAnnuPay() {
		return lipiYearAnnuPay;
	}

	public void setLipiYearAnnuPay(String lipiYearAnnuPay) {
		this.lipiYearAnnuPay = lipiYearAnnuPay;
	}

	public String getLipiGuarTerm() {
		return lipiGuarTerm;
	}

	public void setLipiGuarTerm(String lipiGuarTerm) {
		this.lipiGuarTerm = lipiGuarTerm;
	}

	public String getLipiLiving() {
		return lipiLiving;
	}

	public void setLipiLiving(String lipiLiving) {
		this.lipiLiving = lipiLiving;
	}

	public String getLipiMaTrMk() {
		return lipiMaTrMk;
	}

	public void setLipiMaTrMk(String lipiMaTrMk) {
		this.lipiMaTrMk = lipiMaTrMk;
	}

	public String getLipiMethAnnuPay() {
		return lipiMethAnnuPay;
	}

	public void setLipiMethAnnuPay(String lipiMethAnnuPay) {
		this.lipiMethAnnuPay = lipiMethAnnuPay;
	}

	public String getLipiDeathBeneType() {
		return lipiDeathBeneType;
	}

	public void setLipiDeathBeneType(String lipiDeathBeneType) {
		this.lipiDeathBeneType = lipiDeathBeneType;
	}

	public String getPmdaInsuType() {
		return pmdaInsuType;
	}

	public void setPmdaInsuType(String pmdaInsuType) {
		this.pmdaInsuType = pmdaInsuType;
	}

	public String getPmdaInsuGrpNo() {
		return pmdaInsuGrpNo;
	}

	public void setPmdaInsuGrpNo(String pmdaInsuGrpNo) {
		this.pmdaInsuGrpNo = pmdaInsuGrpNo;
	}

	public String getPmdaInsuSeqNo() {
		return pmdaInsuSeqNo;
	}

	public void setPmdaInsuSeqNo(String pmdaInsuSeqNo) {
		this.pmdaInsuSeqNo = pmdaInsuSeqNo;
	}

	public String getPmda3Name() {
		return pmda3Name;
	}

	public void setPmda3Name(String pmda3Name) {
		this.pmda3Name = pmda3Name;
	}

	public String getPmda3Addr() {
		return pmda3Addr;
	}

	public void setPmda3Addr(String pmda3Addr) {
		this.pmda3Addr = pmda3Addr;
	}

	public String getPmda3ZipCode() {
		return pmda3ZipCode;
	}

	public void setPmda3ZipCode(String pmda3ZipCode) {
		this.pmda3ZipCode = pmda3ZipCode;
	}

	public String getPmdaOpCode() {
		return pmdaOpCode;
	}

	public void setPmdaOpCode(String pmdaOpCode) {
		this.pmdaOpCode = pmdaOpCode;
	}

	public String getPmdaUpDate() {
		return pmdaUpDate;
	}

	public void setPmdaUpDate(String pmdaUpDate) {
		this.pmdaUpDate = pmdaUpDate;
	}

	public String getPmdaAtmNo() {
		return pmdaAtmNo;
	}

	public void setPmdaAtmNo(String pmdaAtmNo) {
		this.pmdaAtmNo = pmdaAtmNo;
	}

	public String getPmdaApplicantEmail() {
		return pmdaApplicantEmail;
	}

	public void setPmdaApplicantEmail(String pmdaApplicantEmail) {
		this.pmdaApplicantEmail = pmdaApplicantEmail;
	}

	public String getPmdaApplicantCellphone() {
		return pmdaApplicantCellphone;
	}

	public void setPmdaApplicantCellphone(String pmdaApplicantCellphone) {
		this.pmdaApplicantCellphone = pmdaApplicantCellphone;
	}

	public String getPmdaInsuredEmail() {
		return pmdaInsuredEmail;
	}

	public void setPmdaInsuredEmail(String pmdaInsuredEmail) {
		this.pmdaInsuredEmail = pmdaInsuredEmail;
	}

	public String getPmdaInsuredCellphone() {
		return pmdaInsuredCellphone;
	}

	public void setPmdaInsuredCellphone(String pmdaInsuredCellphone) {
		this.pmdaInsuredCellphone = pmdaInsuredCellphone;
	}

	public String getPmdaSourcePolicy() {
		return pmdaSourcePolicy;
	}

	public void setPmdaSourcePolicy(String pmdaSourcePolicy) {
		this.pmdaSourcePolicy = pmdaSourcePolicy;
	}

	public String getPmdaAsdaCode() {
		return pmdaAsdaCode;
	}

	public void setPmdaAsdaCode(String pmdaAsdaCode) {
		this.pmdaAsdaCode = pmdaAsdaCode;
	}

	public String getPmdaAsdaUnit() {
		return pmdaAsdaUnit;
	}

	public void setPmdaAsdaUnit(String pmdaAsdaUnit) {
		this.pmdaAsdaUnit = pmdaAsdaUnit;
	}

	public String getPmdaAsdaSeq() {
		return pmdaAsdaSeq;
	}

	public void setPmdaAsdaSeq(String pmdaAsdaSeq) {
		this.pmdaAsdaSeq = pmdaAsdaSeq;
	}

	public String getPmdaMinorityCode() {
		return pmdaMinorityCode;
	}

	public void setPmdaMinorityCode(String pmdaMinorityCode) {
		this.pmdaMinorityCode = pmdaMinorityCode;
	}

	public String getPmdaLinkCode() {
		return pmdaLinkCode;
	}

	public void setPmdaLinkCode(String pmdaLinkCode) {
		this.pmdaLinkCode = pmdaLinkCode;
	}

	public String getPmdaCreDate() {
		return pmdaCreDate;
	}

	public void setPmdaCreDate(String pmdaCreDate) {
		this.pmdaCreDate = pmdaCreDate;
	}

	public String getPmdaRecreMk() {
		return pmdaRecreMk;
	}

	public void setPmdaRecreMk(String pmdaRecreMk) {
		this.pmdaRecreMk = pmdaRecreMk;
	}

	public String getPmdaArriDate() {
		return pmdaArriDate;
	}

	public void setPmdaArriDate(String pmdaArriDate) {
		this.pmdaArriDate = pmdaArriDate;
	}

	public String getPmdaUpDateA() {
		return pmdaUpDateA;
	}

	public void setPmdaUpDateA(String pmdaUpDateA) {
		this.pmdaUpDateA = pmdaUpDateA;
	}

	public String getPmdaOpCodeA() {
		return pmdaOpCodeA;
	}

	public void setPmdaOpCodeA(String pmdaOpCodeA) {
		this.pmdaOpCodeA = pmdaOpCodeA;
	}

	public String getPmdaFirstRcpAmt() {
		return pmdaFirstRcpAmt;
	}

	public void setPmdaFirstRcpAmt(String pmdaFirstRcpAmt) {
		this.pmdaFirstRcpAmt = pmdaFirstRcpAmt;
	}

	public String getPmdaInsuNewsMk() {
		return pmdaInsuNewsMk;
	}

	public void setPmdaInsuNewsMk(String pmdaInsuNewsMk) {
		this.pmdaInsuNewsMk = pmdaInsuNewsMk;
	}

	public String getPmdaEInfoN() {
		return pmdaEInfoN;
	}

	public void setPmdaEInfoN(String pmdaEInfoN) {
		this.pmdaEInfoN = pmdaEInfoN;
	}

	public String getPmdaEInfoC() {
		return pmdaEInfoC;
	}

	public void setPmdaEInfoC(String pmdaEInfoC) {
		this.pmdaEInfoC = pmdaEInfoC;
	}

	public String getPmdaEInfoP() {
		return pmdaEInfoP;
	}

	public void setPmdaEInfoP(String pmdaEInfoP) {
		this.pmdaEInfoP = pmdaEInfoP;
	}

	public String getPmdaEStmt() {
		return pmdaEStmt;
	}

	public void setPmdaEStmt(String pmdaEStmt) {
		this.pmdaEStmt = pmdaEStmt;
	}

	public String getPmdaPiTel() {
		return pmdaPiTel;
	}

	public void setPmdaPiTel(String pmdaPiTel) {
		this.pmdaPiTel = pmdaPiTel;
	}

	public String getPmdaPiZipCode() {
		return pmdaPiZipCode;
	}

	public void setPmdaPiZipCode(String pmdaPiZipCode) {
		this.pmdaPiZipCode = pmdaPiZipCode;
	}

	public String getPmdaPiName2() {
		return pmdaPiName2;
	}

	public void setPmdaPiName2(String pmdaPiName2) {
		this.pmdaPiName2 = pmdaPiName2;
	}

	public String getPmdaReviewDate() {
		return pmdaReviewDate;
	}

	public void setPmdaReviewDate(String pmdaReviewDate) {
		this.pmdaReviewDate = pmdaReviewDate;
	}

	public String getPmdaApplicantPeps() {
		return pmdaApplicantPeps;
	}

	public void setPmdaApplicantPeps(String pmdaApplicantPeps) {
		this.pmdaApplicantPeps = pmdaApplicantPeps;
	}

	public String getPmdaInsuredPeps() {
		return pmdaInsuredPeps;
	}

	public void setPmdaInsuredPeps(String pmdaInsuredPeps) {
		this.pmdaInsuredPeps = pmdaInsuredPeps;
	}

	public String getPmdaInveAttr() {
		return pmdaInveAttr;
	}

	public void setPmdaInveAttr(String pmdaInveAttr) {
		this.pmdaInveAttr = pmdaInveAttr;
	}

	public String getPmdaMail() {
		return pmdaMail;
	}

	public void setPmdaMail(String pmdaMail) {
		this.pmdaMail = pmdaMail;
	}

	public String getPmdaFirstTrMethod() {
		return pmdaFirstTrMethod;
	}

	public void setPmdaFirstTrMethod(String pmdaFirstTrMethod) {
		this.pmdaFirstTrMethod = pmdaFirstTrMethod;
	}

	public String getPmdaPmCtype() {
		return pmdaPmCtype;
	}

	public void setPmdaPmCtype(String pmdaPmCtype) {
		this.pmdaPmCtype = pmdaPmCtype;
	}

	public String getPmdaPmTrade() {
		return pmdaPmTrade;
	}

	public void setPmdaPmTrade(String pmdaPmTrade) {
		this.pmdaPmTrade = pmdaPmTrade;
	}

	public String getPmdaPmNation() {
		return pmdaPmNation;
	}

	public void setPmdaPmNation(String pmdaPmNation) {
		this.pmdaPmNation = pmdaPmNation;
	}

	public String getPmdaPrintDate() {
		return pmdaPrintDate;
	}

	public void setPmdaPrintDate(String pmdaPrintDate) {
		this.pmdaPrintDate = pmdaPrintDate;
	}

	public String getPmdaPmStocks() {
		return pmdaPmStocks;
	}

	public void setPmdaPmStocks(String pmdaPmStocks) {
		this.pmdaPmStocks = pmdaPmStocks;
	}

	public String getPmdaPmAddrTw() {
		return pmdaPmAddrTw;
	}

	public void setPmdaPmAddrTw(String pmdaPmAddrTw) {
		this.pmdaPmAddrTw = pmdaPmAddrTw;
	}

	public String getPmdaPmExistPerd() {
		return pmdaPmExistPerd;
	}

	public void setPmdaPmExistPerd(String pmdaPmExistPerd) {
		this.pmdaPmExistPerd = pmdaPmExistPerd;
	}

	public String getPmdaPiNation() {
		return pmdaPiNation;
	}

	public void setPmdaPiNation(String pmdaPiNation) {
		this.pmdaPiNation = pmdaPiNation;
	}

	public String getPmdaPmJobLevel() {
		return pmdaPmJobLevel;
	}

	public void setPmdaPmJobLevel(String pmdaPmJobLevel) {
		this.pmdaPmJobLevel = pmdaPmJobLevel;
	}

	public String getPmdaCreditBank() {
		return pmdaCreditBank;
	}

	public void setPmdaCreditBank(String pmdaCreditBank) {
		this.pmdaCreditBank = pmdaCreditBank;
	}

	public String getPmdaCreditNo() {
		return pmdaCreditNo;
	}

	public void setPmdaCreditNo(String pmdaCreditNo) {
		this.pmdaCreditNo = pmdaCreditNo;
	}

	public String getPmdaRiskTrail() {
		return pmdaRiskTrail;
	}

	public void setPmdaRiskTrail(String pmdaRiskTrail) {
		this.pmdaRiskTrail = pmdaRiskTrail;
	}

	public String getPmdaWriter() {
		return pmdaWriter;
	}

	public void setPmdaWriter(String pmdaWriter) {
		this.pmdaWriter = pmdaWriter;
	}

	public String getPmdaBackMethod() {
		return pmdaBackMethod;
	}

	public void setPmdaBackMethod(String pmdaBackMethod) {
		this.pmdaBackMethod = pmdaBackMethod;
	}

	public String getPmdaEpoMk() {
		return pmdaEpoMk;
	}

	public void setPmdaEpoMk(String pmdaEpoMk) {
		this.pmdaEpoMk = pmdaEpoMk;
	}

	public String getPmdaOldPo() {
		return pmdaOldPo;
	}

	public void setPmdaOldPo(String pmdaOldPo) {
		this.pmdaOldPo = pmdaOldPo;
	}

	public String getPmda2monthPapr() {
		return pmda2monthPapr;
	}

	public void setPmda2monthPapr(String pmda2monthPapr) {
		this.pmda2monthPapr = pmda2monthPapr;
	}

	public String getPmda1stCfDate() {
		return pmda1stCfDate;
	}

	public void setPmda1stCfDate(String pmda1stCfDate) {
		this.pmda1stCfDate = pmda1stCfDate;
	}

	public String getPmdaPremSource() {
		return pmdaPremSource;
	}

	public void setPmdaPremSource(String pmdaPremSource) {
		this.pmdaPremSource = pmdaPremSource;
	}

	public String getPmdaSubMino() {
		return pmdaSubMino;
	}

	public void setPmdaSubMino(String pmdaSubMino) {
		this.pmdaSubMino = pmdaSubMino;
	}

	public String getAssnArriDate() {
		return assnArriDate;
	}

	public void setAssnArriDate(String assnArriDate) {
		this.assnArriDate = assnArriDate;
	}

	public String getSettChName() {
		return settChName;
	}

	public void setSettChName(String settChName) {
		this.settChName = settChName;
	}

	public String getProdCurrency() {
		return prodCurrency;
	}

	public void setProdCurrency(String prodCurrency) {
		this.prodCurrency = prodCurrency;
	}
}
