package com.twfhclife.eservice.policy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 帳戶價值通知單物件.
 * 
 * @author alan
 */
public class AcctValuePdfVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 保單號碼 */
	private String policyNo;

	/** 商品名稱 */
	private String productName;
	
	/** 投保始期 */
	private Date effectivedate;
	
	/** 要 保 人 */
	private String customerName;
	
	/** 被 保 人 */
	private String insuredName;
	
	/** 保險型態 */
	private String policyType;
	
	/** 保險金額 */
	private BigDecimal mainAmount;
	
	/** 商品名稱 */
	private BigDecimal loanAmount;
	
	/** 商品名稱 */
	private BigDecimal intAmount;
	
	/** 商品名稱 */
	private BigDecimal deathInsuranceAmount;

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

	public Date getEffectivedate() {
		return effectivedate;
	}

	public void setEffectivedate(Date effectivedate) {
		this.effectivedate = effectivedate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public BigDecimal getMainAmount() {
		return mainAmount;
	}

	public void setMainAmount(BigDecimal mainAmount) {
		this.mainAmount = mainAmount;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getIntAmount() {
		return intAmount;
	}

	public void setIntAmount(BigDecimal intAmount) {
		this.intAmount = intAmount;
	}

	public BigDecimal getDeathInsuranceAmount() {
		return deathInsuranceAmount;
	}

	public void setDeathInsuranceAmount(BigDecimal deathInsuranceAmount) {
		this.deathInsuranceAmount = deathInsuranceAmount;
	}
}