package com.twfhclife.eservice.api.elife.domain;

public class PolicyDetailVo {
	/** 要保人-保單號碼 */
	private String lipmInsuNo;
	/**	要保人-保單類型 */
	private String lipmInsuType;
	/**	要保人-保單號碼 */
	private String lipmInsuGrpNo;
	/**	要保人-保單號碼 */
	private String lipmInsuSeqNo;
	/**	要保人-代理人別 */
	private String lipmAgenCode;
	/**	要保人-代理人身份 */
	private String lipmAgenNaCode;
	/**	要保人-代理人分區 */
	private String lipmAgenCodeBranch;
	/**	要保人-繳別 */
	private String lipmRcpCode;
	/**	要保人-已轉繳費日 */
	private String lipmTredPaabDate;
	/**	要保人-繳費方法 */
	private String lipmRcpMethCode;
	/**	要保人-契約狀況 */
	private String lipmSt;
	/**	要保人-集體彙繳 */
	private String lipm5Gp;
	/**	要保人-投保日期 */
	private String lipmInsuBegDate;
	/**	要保人-身份證號 */
	private String lipmId;
	/**	要保人-名稱1 */
	private String lipmName1;
	/**	要保人-名稱2	*/
	private String lipmName2;
	/**	要保人-住址 */
	private String lipmAddr;
	/**	要保人-郵遞區號 */
	private String lipmZipCode;
	/**	要保人-電話(H) */
	private String lipmTelH;
	/**	要保人-電話(O) */
	private String lipmTelO;
	/**	要保人-身份證號(收費帳號) */
	private String lipmCharId;
	/**	要保人-收費地址 */
	private String lipmCharAddr;
	/**	要保人-郵遞區號 */
	private String lipmCharZipCode;
	/**	要保人-自動墊繳註記 */
	private String lipmAutoRcpMk;
	/**	要保人-彈性繳費註記 */
	private String lipmFlexRcpMk;
	/**	被保人-保單號碼 */
	private String lipiInsuNo;
	/** 被保人-保單類別 */
	private String lipiInsuType;
	/** 被保人-保單號碼 */
	private String lipiInsuGrpNo;
	/** 被保人-保單號碼 */
	private String lipiInsuSeqNo;
	/** 被保人-投保日期 */
	private String lipiInsuBegDate;
	/** 被保人-投保終期 */
	private String lipiInsuEndDate;
	/** 被保人-團體代號 */
	private String lipiGpNo;
	/** 被保人-身份證字號 */
	private String lipiId;
	/** 被保人-保險人姓名 */
	private String lipiName;
	/** 被保人-出生日期 */
	private String lipiBirth;
	/** 被保人-性別 */
	private String lipiSex;
	/** 被保人-投保年齡 */
	private String lipiAge;
	/** 被保人-繳費年期 */
	private String lipiPremYear;
	/** 被保人-年金給付方式 */
	private String lipiMethAnnuPay;
	/** 被保人-生存保險金百分比 */
	private String lipiLiRate;
	/** 被保人-主契約保額 */
	private String lipiMainAmt;
	/** 被保人-表計保費 */
	private String lipiTablPrem;
	/** 被保人-次健加費 */
	private String lipiUnHealPrem;
	/** 被保人-危職加費 */
	private String lipiDangPrem;
	/** 被保人-附加險註記 */
	private String lipiAddMk;
	/** 被保人-貸款註記 */
	private String lipiLoMk;
	/** 被保人-契約狀況 */
	private String lipiSt;
	/** 被保人-契約狀況日期 */
	private String lipiStDate;
	/** 被保人-防癌投保單位 */
	private String lipiCancerQuan;
	/** 被保人-體檢別 */
	private String lipiHealCode;
	/** 被保人-地址 */
	private String lipiAddr;
	/** 保單類型 */
	private String policyListType;
	/** */
	private String drawDay;
	/** 被保人-表計保費 */
	private String paidAmount;
	/** 被保人姓名 */
	private String mainInsuredName;
	/** 險種中文名稱 */
	private String settChName;
	/** 投保終期狀態*/
	private String expiredFlag;
	/** */
	private String sameIdCount;
	/** 要保人-名稱1 */
	private String customerName;
	/** 被保人-身份證字號*/
	private String lipiRocid;
	/** 應繳金額 */
	private String paprPaabAmt;
	/** 幣別 */
	private String prodCurrency;
	/** */
	private String prodIsFatca;
	/** 險種一 */
	private String prodType;
	/** 貸款金額 */
	private String lomsAmt;
	/** 線上可借金額檔-型態*/
	private String val1Type;
	/** 線上可借金額檔-可借金額*/
	private String val1PaprAmt;
	/** 線上可借金額檔-保單號碼*/
	private String val1InsuNo;
	/** 線上可借金額檔-*/
	private String val1ReseAmt;
	/** 線上可借金額檔-*/
	private String val1CancAmt;
	/** 生存金-異動日期*/
	private String modifydatetime;
	/** 利率 */
	private String rat1Rate;
	/** 墊繳主檔-累計墊繳總保費 */
	private String autrSubPrem;
	/** 銀行代碼*/
	private String inmsBankCode;
	/** 分行代碼*/
	private String inmsBankBranchCode;
	/** 帳號*/
	private String inmsAccountNo;
	/** 要保人-出生日期 */
	private String bnagBirth;
	
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
	public String getLipmRcpCode() {
		return lipmRcpCode;
	}
	public void setLipmRcpCode(String lipmRcpCode) {
		this.lipmRcpCode = lipmRcpCode;
	}
	public String getLipmTredPaabDate() {
		return lipmTredPaabDate;
	}
	public void setLipmTredPaabDate(String lipmTredPaabDate) {
		this.lipmTredPaabDate = lipmTredPaabDate;
	}
	public String getLipmRcpMethCode() {
		return lipmRcpMethCode;
	}
	public void setLipmRcpMethCode(String lipmRcpMethCode) {
		this.lipmRcpMethCode = lipmRcpMethCode;
	}
	public String getLipmSt() {
		return lipmSt;
	}
	public void setLipmSt(String lipmSt) {
		this.lipmSt = lipmSt;
	}
	public String getLipm5Gp() {
		return lipm5Gp;
	}
	public void setLipm5Gp(String lipm5Gp) {
		this.lipm5Gp = lipm5Gp;
	}
	public String getLipmInsuBegDate() {
		return lipmInsuBegDate;
	}
	public void setLipmInsuBegDate(String lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
	}
	public String getLipmId() {
		return lipmId;
	}
	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
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
	public String getLipmCharId() {
		return lipmCharId;
	}
	public void setLipmCharId(String lipmCharId) {
		this.lipmCharId = lipmCharId;
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
	public String getLipmAutoRcpMk() {
		return lipmAutoRcpMk;
	}
	public void setLipmAutoRcpMk(String lipmAutoRcpMk) {
		this.lipmAutoRcpMk = lipmAutoRcpMk;
	}
	public String getLipmFlexRcpMk() {
		return lipmFlexRcpMk;
	}
	public void setLipmFlexRcpMk(String lipmFlexRcpMk) {
		this.lipmFlexRcpMk = lipmFlexRcpMk;
	}
	public String getLipiInsuNo() {
		return lipiInsuNo;
	}
	public void setLipiInsuNo(String lipiInsuNo) {
		this.lipiInsuNo = lipiInsuNo;
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
	public String getLipiGpNo() {
		return lipiGpNo;
	}
	public void setLipiGpNo(String lipiGpNo) {
		this.lipiGpNo = lipiGpNo;
	}
	public String getLipiId() {
		return lipiId;
	}
	public void setLipiId(String lipiId) {
		this.lipiId = lipiId;
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
	public String getLipiSex() {
		return lipiSex;
	}
	public void setLipiSex(String lipiSex) {
		this.lipiSex = lipiSex;
	}
	public String getLipiAge() {
		return lipiAge;
	}
	public void setLipiAge(String lipiAge) {
		this.lipiAge = lipiAge;
	}
	public String getLipiPremYear() {
		return lipiPremYear;
	}
	public void setLipiPremYear(String lipiPremYear) {
		this.lipiPremYear = lipiPremYear;
	}
	public String getLipiMethAnnuPay() {
		return lipiMethAnnuPay;
	}
	public void setLipiMethAnnuPay(String lipiMethAnnuPay) {
		this.lipiMethAnnuPay = lipiMethAnnuPay;
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
	public String getLipiUnHealPrem() {
		return lipiUnHealPrem;
	}
	public void setLipiUnHealPrem(String lipiUnHealPrem) {
		this.lipiUnHealPrem = lipiUnHealPrem;
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
	public String getLipiCancerQuan() {
		return lipiCancerQuan;
	}
	public void setLipiCancerQuan(String lipiCancerQuan) {
		this.lipiCancerQuan = lipiCancerQuan;
	}
	public String getLipiHealCode() {
		return lipiHealCode;
	}
	public void setLipiHealCode(String lipiHealCode) {
		this.lipiHealCode = lipiHealCode;
	}
	public String getLipiAddr() {
		return lipiAddr;
	}
	public void setLipiAddr(String lipiAddr) {
		this.lipiAddr = lipiAddr;
	}
	public String getPolicyListType() {
		return policyListType;
	}
	public void setPolicyListType(String policyListType) {
		this.policyListType = policyListType;
	}
	public String getDrawDay() {
		return drawDay;
	}
	public void setDrawDay(String drawDay) {
		this.drawDay = drawDay;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getMainInsuredName() {
		return mainInsuredName;
	}
	public void setMainInsuredName(String mainInsuredName) {
		this.mainInsuredName = mainInsuredName;
	}
	public String getSettChName() {
		return settChName;
	}
	public void setSettChName(String settChName) {
		this.settChName = settChName;
	}
	public String getExpiredFlag() {
		return expiredFlag;
	}
	public void setExpiredFlag(String expiredFlag) {
		this.expiredFlag = expiredFlag;
	}
	public String getSameIdCount() {
		return sameIdCount;
	}
	public void setSameIdCount(String sameIdCount) {
		this.sameIdCount = sameIdCount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getLipiRocid() {
		return lipiRocid;
	}
	public void setLipiRocid(String lipiRocid) {
		this.lipiRocid = lipiRocid;
	}
	public String getPaprPaabAmt() {
		return paprPaabAmt;
	}
	public void setPaprPaabAmt(String paprPaabAmt) {
		this.paprPaabAmt = paprPaabAmt;
	}
	public String getProdCurrency() {
		return prodCurrency;
	}
	public void setProdCurrency(String prodCurrency) {
		this.prodCurrency = prodCurrency;
	}
	public String getProdIsFatca() {
		return prodIsFatca;
	}
	public void setProdIsFatca(String prodIsFatca) {
		this.prodIsFatca = prodIsFatca;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getLomsAmt() {
		return lomsAmt;
	}
	public void setLomsAmt(String lomsAmt) {
		this.lomsAmt = lomsAmt;
	}
	public String getVal1Type() {
		return val1Type;
	}
	public void setVal1Type(String val1Type) {
		this.val1Type = val1Type;
	}
	public String getVal1PaprAmt() {
		return val1PaprAmt;
	}
	public void setVal1PaprAmt(String val1PaprAmt) {
		this.val1PaprAmt = val1PaprAmt;
	}
	public String getVal1InsuNo() {
		return val1InsuNo;
	}
	public void setVal1InsuNo(String val1InsuNo) {
		this.val1InsuNo = val1InsuNo;
	}
	public String getVal1ReseAmt() {
		return val1ReseAmt;
	}
	public void setVal1ReseAmt(String val1ReseAmt) {
		this.val1ReseAmt = val1ReseAmt;
	}
	public String getVal1CancAmt() {
		return val1CancAmt;
	}
	public void setVal1CancAmt(String val1CancAmt) {
		this.val1CancAmt = val1CancAmt;
	}
	public String getModifydatetime() {
		return modifydatetime;
	}
	public void setModifydatetime(String modifydatetime) {
		this.modifydatetime = modifydatetime;
	}
	public String getRat1Rate() {
		return rat1Rate;
	}
	public void setRat1Rate(String rat1Rate) {
		this.rat1Rate = rat1Rate;
	}
	public String getAutrSubPrem() {
		return autrSubPrem;
	}
	public void setAutrSubPrem(String autrSubPrem) {
		this.autrSubPrem = autrSubPrem;
	}
	public String getInmsBankCode() {
		return inmsBankCode;
	}
	public void setInmsBankCode(String inmsBankCode) {
		this.inmsBankCode = inmsBankCode;
	}
	public String getInmsBankBranchCode() {
		return inmsBankBranchCode;
	}
	public void setInmsBankBranchCode(String inmsBankBranchCode) {
		this.inmsBankBranchCode = inmsBankBranchCode;
	}
	public String getInmsAccountNo() {
		return inmsAccountNo;
	}
	public void setInmsAccountNo(String inmsAccountNo) {
		this.inmsAccountNo = inmsAccountNo;
	}
	public String getBnagBirth() {
		return bnagBirth;
	}
	public void setBnagBirth(String bnagBirth) {
		this.bnagBirth = bnagBirth;
	}	
	
}
