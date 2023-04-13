package com.twfhclife.generic.api_model;

public class TransCtcSelectDataVo {

	/** 保號 */
	private String lipmInsuNo;
	/** 要保人 */
	private String lipmName1;
	/** 保單生效日 */
	private String lipmInsuBegDate;
	/** 契約狀態 */
	private String lipmSt;
	/** 繳清生效日 */
	private String lipiStDate;
	/** 主被保人姓名 */
	private String lipiName;
	/** 保額 */
	private String lipiMainAmt;
	/** 每期保費 */
	private String lipiTablPrem;
	/** 保護是否有開通電子表單通知 */
	private String pmdaEInfoN;
	/** 被保人電話 */
	private String pmdaApplicantCellphone;
	/** 被保人信箱 */
	private String pmdaApplicantEmail;
	/** 保戶是否有申請電子保單 */
	private String pmdaEpoMk;
	/** 簽收日 */
	private String assnArriDate;
	/** 商品名稱 */
	private String settChName;
	/** 是否可契撤的欄位=N 就不能契撤 */
	private String settTerminate;
	/** 幣別 */
	private String prodCurrency;
	/** 繳別 */
	private String lipmRcpCode;
	
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}
	public String getLipmName1() {
		return lipmName1;
	}
	public void setLipmName1(String lipmName1) {
		this.lipmName1 = lipmName1;
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
	public String getLipmSt() {
		return lipmSt;
	}
	public void setLipmSt(String lipmSt) {
		this.lipmSt = lipmSt;
	}
	public String getLipiName() {
		return lipiName;
	}
	public void setLipiName(String lipiName) {
		this.lipiName = lipiName;
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
	public String getPmdaEInfoN() {
		return pmdaEInfoN;
	}
	public void setPmdaEInfoN(String pmdaEInfoN) {
		this.pmdaEInfoN = pmdaEInfoN;
	}
	public String getPmdaApplicantCellphone() {
		return pmdaApplicantCellphone;
	}
	public void setPmdaApplicantCellphone(String pmdaApplicantCellphone) {
		this.pmdaApplicantCellphone = pmdaApplicantCellphone;
	}
	public String getPmdaApplicantEmail() {
		return pmdaApplicantEmail;
	}
	public void setPmdaApplicantEmail(String pmdaApplicantEmail) {
		this.pmdaApplicantEmail = pmdaApplicantEmail;
	}
	public String getPmdaEpoMk() {
		return pmdaEpoMk;
	}
	public void setPmdaEpoMk(String pmdaEpoMk) {
		this.pmdaEpoMk = pmdaEpoMk;
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
	public String getSettTerminate() {
		return settTerminate;
	}
	public void setSettTerminate(String settTerminate) {
		this.settTerminate = settTerminate;
	}
	public String getProdCurrency() {
		return prodCurrency;
	}
	public void setProdCurrency(String prodCurrency) {
		this.prodCurrency = prodCurrency;
	}
	public String getLipiStDate() {
		return lipiStDate;
	}
	public void setLipiStDate(String lipiStDate) {
		this.lipiStDate = lipiStDate;
	}
	
}
