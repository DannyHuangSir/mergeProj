package com.twfhclife.generic.api_model;

import java.io.Serializable;

public class TransCtcSelectDetailVo implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 保單號碼 */
	private String lipmInsuNo;
	/** 要保人*/
	private String lipmName1;
	/** 保單生效日*/
	private String lipmInsuBegDate;
	/** 繳別 */
	private String lipmRcpCode;
	/** 狀態 */
	private String lipmSt;
	/** 要保人住所(戶籍)地址*/
	private String lipmAddr;
	/** 收費(通訊)地址 */
	private String lipmCharAddr;
	/** 住家電話 */
	private String lipmTelH;
	/** 公司電話 */
	private String lipmTelO;
	/** 行動電話 */
	private String pmdaApplicantCellphone;
	/** 電子信箱 */
	private String pmdaApplicantEmail;
	/** 主被保人姓名 */
	private String lipiName;
	/** 保額 */
	private String lipiMainAmt;
	/** 每期保費 */
	private String lipiTablPrem;
	/** 保護是否有開通電子表單通知 */
	private String pmdaEInfoN;
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
	public String getLipmAddr() {
		return lipmAddr;
	}
	public void setLipmAddr(String lipmAddr) {
		this.lipmAddr = lipmAddr;
	}
	public String getLipmCharAddr() {
		return lipmCharAddr;
	}
	public void setLipmCharAddr(String lipmCharAddr) {
		this.lipmCharAddr = lipmCharAddr;
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
	
}