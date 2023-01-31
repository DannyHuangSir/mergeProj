package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 保單資料物件.
 * 
 * @author alan
 */
public class PolicyVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 保單號碼 */
	private String policyNo;

	/** 保單名稱 */
	private String productName;

	/** 保單類型 */
	private String policyType;

	/** 客戶名稱 */
	private String customerName;

	/** 主被保險人 */
	private String mainInsuredName;

	/** 保單狀態 */
	private String status;

	/** 保單狀態名稱 */
	private String policyStatus;

	/** 投保始期 */
	private Date policyStartDate;

	/** 保額 */
	private double insuredValue;

	/** 繳費年期 */
	private double paymentPeriod;

	/** 付款方式(支付工具) */
	private String paymentMethod;

	/** 繳別 */
	private String paymentMode;

	/** 幣別 */
	private String currency;

	/** 約定扣款日 */
	private String drawDay;

	/** 每期保費 */
	private String paidAmount;
	private String mainAmount;

	/** 生效日 */
	private String effectiveDate;

	/** 失效日 */
	private String expireDate;

	private String paidToDate;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public String getPaidToDate() {
		return paidToDate;
	}

	public void setPaidToDate(String paidToDate) {
		this.paidToDate = paidToDate;
	}

	public String getMainAmount() {
		return mainAmount;
	}

	public void setMainAmount(String mainAmount) {
		this.mainAmount = mainAmount;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public double getInsuredValue() {
		return insuredValue;
	}

	public void setInsuredValue(double insuredValue) {
		this.insuredValue = insuredValue;
	}

	public double getPaymentPeriod() {
		return paymentPeriod;
	}

	public void setPaymentPeriod(double paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getMainInsuredName() {
		return mainInsuredName;
	}

	public void setMainInsuredName(String mainInsuredName) {
		this.mainInsuredName = mainInsuredName;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
