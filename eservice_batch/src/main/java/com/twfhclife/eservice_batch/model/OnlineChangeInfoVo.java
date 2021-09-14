package com.twfhclife.eservice_batch.model;

import java.util.Date;

public class OnlineChangeInfoVo {

	private String policyNo;
	private String status;
	private Date effectiveDate;
	private Date expireDate;
	private String paymentMode;
	private String premYear;
	private String currency;
	private String lipmId;
	private String policyHolderName;
	private String lipiId;
	private String insuredName;
	private String mainAmount;
	private String productName;
	private String transType;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPremYear() {
		return premYear;
	}

	public void setPremYear(String premYear) {
		this.premYear = premYear;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPolicyHolderName() {
		return policyHolderName;
	}

	public void setPolicyHolderName(String policyHolderName) {
		this.policyHolderName = policyHolderName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getMainAmount() {
		return mainAmount;
	}

	public void setMainAmount(String mainAmount) {
		this.mainAmount = mainAmount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLipmId() {
		return lipmId;
	}

	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}

	public String getLipiId() {
		return lipiId;
	}

	public void setLipiId(String lipiId) {
		this.lipiId = lipiId;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

}
