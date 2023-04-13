package com.twfhclife.eservice.onlineChange.model;

public class OnlineTrialVo {
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
	/** 繳清生效日 */
	private String lipiStDate;
	
	/** */
	private String 	applyLockedFlag = "N"; 
	/** */
	private String applyLockedMsg;
	
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
	public String getLipiStDate() {
		return lipiStDate;
	}
	public void setLipiStDate(String lipiStDate) {
		this.lipiStDate = lipiStDate;
	}
	
}
