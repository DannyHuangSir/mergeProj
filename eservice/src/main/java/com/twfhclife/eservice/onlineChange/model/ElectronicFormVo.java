package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.api_model.TransCtcLipmdaVo;



public class ElectronicFormVo extends AbstractOnlineChangeModelBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 是否尚未有保單未申請電子表單 */
	private boolean isElectronicForm ;
	/** 是否有保單已申請過電子表單 */
	private boolean isclean;
	/** 保單號碼 */
	private String lipmInsuNo;
	/** 保單 */
	private String policyNo; 
	/** 要保人 */
	private String customerName;
	/** 主被保險人 */
	private String mainInsuredName;
	/** 保單生效日 */
	private String effectiveDate;
	/** 每期保費 */
	private BigDecimal paidAmount ;
	/** 繳別 */
	private String paymentMode;
	/** 開通狀態 */
	private String pmdaEInfoNStatus;
	/** 是否可以點選 */
	private String applyLockedFlag;
	/** 保單不可點選的訊息 */
	private String applyLockedMsg;
	/** Mail 是否存在*/
	private String mailFlag;
	/** Mail不存在的訊息 */
	private String  mailMsg;
	
	private String lipmSt;
	
	private String titleName ;
	
	private String productName;
	
	private String email;
	
	private String electronicFormStatus;
	
	private String authenticationNum;
	
	private String settChName;
	
	private List<String> policyNoList;
	
	private List<TransCtcLipmdaVo> clopmdaVo;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public List<TransCtcLipmdaVo> getClopmdaVo() {
		return clopmdaVo;
	}

	public void setClopmdaVo(List<TransCtcLipmdaVo> clopmdaVo) {
		this.clopmdaVo = clopmdaVo;
	}

	public String getAuthenticationNum() {
		return authenticationNum;
	}

	public void setAuthenticationNum(String authenticationNum) {
		this.authenticationNum = authenticationNum;
	}

	public String getElectronicFormStatus() {
		return electronicFormStatus;
	}

	public void setElectronicFormStatus(String electronicFormStatus) {
		this.electronicFormStatus = electronicFormStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public List<String> getPolicyNoList() {
		return policyNoList;
	}

	public void setPolicyNoList(List<String> policyNoList) {
		this.policyNoList = policyNoList;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isElectronicForm() {
		return isElectronicForm;
	}

	public void setElectronicForm(boolean isElectronicForm) {
		this.isElectronicForm = isElectronicForm;
	}

	public boolean isIsclean() {
		return isclean;
	}

	public void setIsclean(boolean isclean) {
		this.isclean = isclean;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMainInsuredName() {
		return mainInsuredName;
	}

	public void setMainInsuredName(String mainInsuredName) {
		this.mainInsuredName = mainInsuredName;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getLipmInsuNo() {
		return lipmInsuNo;
	}

	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPmdaEInfoNStatus() {
		return pmdaEInfoNStatus;
	}

	public void setPmdaEInfoNStatus(String pmdaEInfoNStatus) {
		this.pmdaEInfoNStatus = pmdaEInfoNStatus;
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

	public String getMailFlag() {
		return mailFlag;
	}

	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}

	public String getMailMsg() {
		return mailMsg;
	}

	public void setMailMsg(String mailMsg) {
		this.mailMsg = mailMsg;
	}

	public String getLipmSt() {
		return lipmSt;
	}

	public void setLipmSt(String lipmSt) {
		this.lipmSt = lipmSt;
	}

	public String getSettChName() {
		return settChName;
	}

	public void setSettChName(String settChName) {
		this.settChName = settChName;
	}	
	
}