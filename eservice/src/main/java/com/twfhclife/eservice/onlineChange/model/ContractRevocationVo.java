package com.twfhclife.eservice.onlineChange.model;

public class ContractRevocationVo {

	/** 保單號碼*/
	private String lipmInsuNo;
	/** 簽收日 */
	private String assnArriDate;
	/** 商品名稱 */
	private String settChName;
	/** 要保人 */
	private String lipmName;
	/** 主被保人姓名 */
	private String lipiName;
	/** 保單生效日 */
	private String lipmInsuBegDate;
	/** 保額 */
	private String lipiMainAmt;
	/** 每期保費 */
	private String lipiTablPrem;
	/** 幣別 */
	private String prodCurrency;
	/** 繳別 */
	private String lipmRcpCode;
	/** 保單鎖定註記 Flag */
	private String applyLockedFlag = "N"; 
	/** 保單鎖定訊息 */
	private String applyLockedMsg;
	/** 是否為信用卡*/
	private String rcpTypeCodeFlag;
	/**  保單規劃不符需求 */	
	private String needsFlag;
	/** 經濟因素 */	
	private String economyFlag;
	/** 家人反對 */	
	private String familyFlag;
	/** 對商品認知有誤 */	
	private String cognitionFlag;
	/** 其他 */	
	private String otherFlag;
		/** 證明文件1 */	
	private String image1;	
	/** 證明文件1*/	
	private String image2;
	
	private String image1Tobase64;
	
	private String image2Tobase64;
	
	private String prpaRcpBatch ;
	/** 退費金額 **/
	private String prpaActAmt;
	
	private String prpkRcpTypeCode;
		
	private String statusFlag;
	
	private String authenticationNum;
	
	private String accUnit;
	/** 是否可契撤的*/
	private String settTerminate;
	
	private String transNum;
	
	private String changeAccFlag;	
	
	/** 
	 * 原始帳戶資ㄌ廖 
	 */
	/** 國際號 */
	private String oldSwiftCode;
	/**　英文名子　*/
	private String oldEnglishName;
	/** 戶名 */
	private String oldNeacName;
	/** 銀行代碼 */
	private String oldBankCode;
	/** 銀行名稱 */
	private String oldBankName;
	/** 分行代碼 */
	private String oldBranchCode;
	/** 分行名稱 */
	private String oldBranchName;
	/** 帳號 */
	private String oldAccount;
	/** 
	 * 需異動的帳戶資料 
	 */
	/** 國際號 */
	private String swiftCode;
	/** 英文名 */
	private String englishName;
	/** 戶名 */
	private String neacName;
	/** 銀行代碼 */ 
	private String bankCode;
	/** 銀行名稱 */
	private String bankName;
	/** 分行代碼 */
	private String branchCode;
	/** 分行名稱 */
	private String branchName;
	/** 銀行帳號 */
	private String account;
	
	/**
	 * PDF製作所需欄位
	 */
	/** 業務員姓名 */
	private String bnagRecClerk;
	/** 受理單位信箱*/
	private String acceptUnit;
	/** 經蘭單位 */
	private String aginInveArea;
	/** 契撤其他原因*/
	private String otherReason;	
	/** 要保人身分證號碼*/
	private String lipmId;
	
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public String getOldBranchName() {
		return oldBranchName;
	}
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	public void setOldBranchName(String oldBranchName) {
		this.oldBranchName = oldBranchName;
	}
	public String getOldBankName() {
		return oldBankName;
	}
	public void setOldBankName(String oldBankName) {
		this.oldBankName = oldBankName;
	}
	public String getSettTerminate() {
		return settTerminate;
	}
	public void setSettTerminate(String settTerminate) {
		this.settTerminate = settTerminate;
	}
	public String getAccUnit() {
		return accUnit;
	}
	public void setAccUnit(String accUnit) {
		this.accUnit = accUnit;
	}
	public String getOldBranchCode() {
		return oldBranchCode;
	}
	public void setOldBranchCode(String oldBranchCode) {
		this.oldBranchCode = oldBranchCode;
	}
	public String getOldBankCode() {
		return oldBankCode;
	}
	public void setOldBankCode(String oldBankCode) {
		this.oldBankCode = oldBankCode;
	}
	public String getAuthenticationNum() {
		return authenticationNum;
	}
	public void setAuthenticationNum(String authenticationNum) {
		this.authenticationNum = authenticationNum;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getNeacName() {
		return neacName;
	}
	public void setNeacName(String neacName) {
		this.neacName = neacName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getNeedsFlag() {
		return needsFlag;
	}
	public void setNeedsFlag(String needsFlag) {
		this.needsFlag = needsFlag;
	}
	public String getEconomyFlag() {
		return economyFlag;
	}
	public void setEconomyFlag(String economyFlag) {
		this.economyFlag = economyFlag;
	}
	public String getFamilyFlag() {
		return familyFlag;
	}
	public void setFamilyFlag(String familyFlag) {
		this.familyFlag = familyFlag;
	}
	public String getCognitionFlag() {
		return cognitionFlag;
	}
	public void setCognitionFlag(String cognitionFlag) {
		this.cognitionFlag = cognitionFlag;
	}
	public String getOtherFlag() {
		return otherFlag;
	}
	public void setOtherFlag(String otherFlag) {
		this.otherFlag = otherFlag;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	
	public String getRcpTypeCodeFlag() {
		return rcpTypeCodeFlag;
	}
	public void setRcpTypeCodeFlag(String rcpTypeCodeFlag) {
		this.rcpTypeCodeFlag = rcpTypeCodeFlag;
	}
	public String getPrpaRcpBatch() {
		return prpaRcpBatch;
	}
	public void setPrpaRcpBatch(String prpaRcpBatch) {
		this.prpaRcpBatch = prpaRcpBatch;
	}
	public String getPrpaActAmt() {
		return prpaActAmt;
	}
	public void setPrpaActAmt(String prpaActAmt) {
		this.prpaActAmt = prpaActAmt;
	}
	public String getPrpkRcpTypeCode() {
		return prpkRcpTypeCode;
	}
	public void setPrpkRcpTypeCode(String prpkRcpTypeCode) {
		this.prpkRcpTypeCode = prpkRcpTypeCode;
	}
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
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
	public String getLipmName() {
		return lipmName;
	}
	public void setLipmName(String lipmName) {
		this.lipmName = lipmName;
	}
	public String getLipiName() {
		return lipiName;
	}
	public void setLipiName(String lipiName) {
		this.lipiName = lipiName;
	}
	public String getLipmInsuBegDate() {
		return lipmInsuBegDate;
	}
	public void setLipmInsuBegDate(String lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
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
	public String getProdCurrency() {
		return prodCurrency;
	}
	public void setProdCurrency(String prodCurrency) {
		this.prodCurrency = prodCurrency;
	}
	public String getLipmRcpCode() {
		return lipmRcpCode;
	}
	public void setLipmRcpCode(String lipmRcpCode) {
		this.lipmRcpCode = lipmRcpCode;
	}
	public String getApplyLockedFlag() {
		return applyLockedFlag;
	}
	public void setApplyLockedFlag(String applyLockedFlag) {
		this.applyLockedFlag = applyLockedFlag;
	}
	public String getApplyLockedMsg() {
		return applyLockedMsg;
	}
	public void setApplyLockedMsg(String applyLockedMsg) {
		this.applyLockedMsg = applyLockedMsg;
	}
	public String getOldSwiftCode() {
		return oldSwiftCode;
	}
	public void setOldSwiftCode(String oldSwiftCode) {
		this.oldSwiftCode = oldSwiftCode;
	}
	public String getOldEnglishName() {
		return oldEnglishName;
	}
	public void setOldEnglishName(String oldEnglishName) {
		this.oldEnglishName = oldEnglishName;
	}
	public String getOldNeacName() {
		return oldNeacName;
	}
	public void setOldNeacName(String oldNeacName) {
		this.oldNeacName = oldNeacName;
	}
	public String getOldAccount() {
		return oldAccount;
	}
	public void setOldAccount(String oldAccount) {
		this.oldAccount = oldAccount;
	}
	
	public String getChangeAccFlag() {
		return changeAccFlag;
	}
	public void setChangeAccFlag(String changeAccFlag) {
		this.changeAccFlag = changeAccFlag;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	public String getBnagRecClerk() {
		return bnagRecClerk;
	}
	public void setBnagRecClerk(String bnagRecClerk) {
		this.bnagRecClerk = bnagRecClerk;
	}
	public String getAcceptUnit() {
		return acceptUnit;
	}
	public void setAcceptUnit(String acceptUnit) {
		this.acceptUnit = acceptUnit;
	}
	public String getAginInveArea() {
		return aginInveArea;
	}
	public void setAginInveArea(String aginInveArea) {
		this.aginInveArea = aginInveArea;
	}
	public String getImage1Tobase64() {
		return image1Tobase64;
	}
	public void setImage1Tobase64(String image1Tobase64) {
		this.image1Tobase64 = image1Tobase64;
	}
	public String getImage2Tobase64() {
		return image2Tobase64;
	}
	public void setImage2Tobase64(String image2Tobase64) {
		this.image2Tobase64 = image2Tobase64;
	}
	public String getLipmId() {
		return lipmId;
	}
	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}
	
}
