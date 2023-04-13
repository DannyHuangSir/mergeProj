package com.twfhclife.eservice.onlineChange.model;

public class TransContractRevocationVo {
	private String id;
	/** 保單號碼 */
	private String lipmInsuNo;
	/** 保單名稱 */
	private String settChName;
	/** 要保人姓名 */
	private String lipmName;
	/** 被保人姓名 */
	private String lipiName;
	/** 退費幣別 */
	private String prodCurrency;
	/** 保單生效日 */
	private String lipmInsuBegDate;
	/** 退費金額 */
	private String prpaActAmt;
	/** SwiftCode */
	private String swiftCode;
	/** 英文戶名 */
	private String englishName;
	/** 帳戶名稱 */
	private String neacName;
	/** 銀行代碼 */
	private String bankCode;
	/** 銀行名稱 */
	private String bankName;
	/** 分行代碼 */
	private String branchCode;
	/** 分行名稱 */
	private String branchName;
	/** 帳號 */
	private String account;
	/** 是否為信用卡 */
	private String rcpTypeCodeFlag;
	/** 保單規劃不符需求 */
	private String needsFlag;
	/** 經濟因素 */
	private String economyFlag;
	/** 家人反對 */
	private String familyFlag;
	/** 對商品認知有誤 */
	private String cognitionFlag;
	/** 其他 */
	private String otherFlag;
	/** 申請序號 */
	private String transNum;
	/** */
	private String authenticationNum;		
	/** 身分證明1 */
	private String image1;
	/** 身分證明2 */
	private String image2;	
	/** 經辦人員 */
	private String aginInveArea;
	/** 舊退款資料 */
	private String oldDetails;
	/** 契撤其他原因 */
	private String otherReason;	
	/** USER mail*/
	private String email;
	/** 要保人身分證號碼*/
	private String lipmId;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getRcpTypeCodeFlag() {
		return rcpTypeCodeFlag;
	}
	public void setRcpTypeCodeFlag(String rcpTypeCodeFlag) {
		this.rcpTypeCodeFlag = rcpTypeCodeFlag;
	}
	public String getAuthenticationNum() {
		return authenticationNum;
	}
	public void setAuthenticationNum(String authenticationNum) {
		this.authenticationNum = authenticationNum;
	}
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
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
	public String getProdCurrency() {
		return prodCurrency;
	}
	public void setProdCurrency(String prodCurrency) {
		this.prodCurrency = prodCurrency;
	}
	public String getLipmInsuBegDate() {
		return lipmInsuBegDate;
	}
	public void setLipmInsuBegDate(String lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
	}
	public String getPrpaActAmt() {
		return prpaActAmt;
	}
	public void setPrpaActAmt(String prpaActAmt) {
		this.prpaActAmt = prpaActAmt;
	}
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
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
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
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
	public String getAginInveArea() {
		return aginInveArea;
	}
	public void setAginInveArea(String aginInveArea) {
		this.aginInveArea = aginInveArea;
	}
	public String getOldDetails() {
		return oldDetails;
	}
	public void setOldDetails(String oldDetails) {
		this.oldDetails = oldDetails;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLipmId() {
		return lipmId;
	}
	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}
	
}
