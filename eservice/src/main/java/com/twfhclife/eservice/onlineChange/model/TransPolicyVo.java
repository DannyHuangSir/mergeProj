package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.eservice.web.model.ProposerVo;

/**
 * 保單資料物件.
 * 
 * @author all
 */
public class TransPolicyVo implements Serializable {

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
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

	/** 要保人資料 */
	private ProposerVo proposerVo;

	/** 生效日 */
	private String effectiveDate;

	/** 失效日 */
	private String expireDate;

	/** 回傳用-是否顯示或disabled */
	private String isShow;

	/** 回傳用-disabled時訊息 */
	private String showMsg;
	
	/** 自動墊繳值 */
	private String autoRcpMk;
	
	/** 年金給付方式 */
	private String methAnnuPay;
	
	private String mainInsuredNameBase64;
	
	/**
	 * 要被保人是否同一人:1同一人,0:不同人
	 */
	private int sameIdCount;
	
	private int mainAmount;

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

	public ProposerVo getProposerVo() {
		return proposerVo;
	}

	public void setProposerVo(ProposerVo proposerVo) {
		this.proposerVo = proposerVo;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}

	public String getAutoRcpMk() {
		return autoRcpMk;
	}

	public void setAutoRcpMk(String autoRcpMk) {
		this.autoRcpMk = autoRcpMk;
	}

	public String getMethAnnuPay() {
		return methAnnuPay;
	}

	public void setMethAnnuPay(String methAnnuPay) {
		this.methAnnuPay = methAnnuPay;
	}

	public String getMainInsuredNameBase64() {
		return mainInsuredNameBase64;
	}

	public void setMainInsuredNameBase64(String mainInsuredNameBase64) {
		this.mainInsuredNameBase64 = mainInsuredNameBase64;
	}

	public int getSameIdCount() {
		return sameIdCount;
	}

	public void setSameIdCount(int sameIdCount) {
		this.sameIdCount = sameIdCount;
	}

	public int getMainAmount() {
		return mainAmount;
	}

	public void setMainAmount(int mainAmount) {
		this.mainAmount = mainAmount;
	}
}