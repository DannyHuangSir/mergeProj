package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.eservice.web.model.PageInfoVo;

public class PremiumTransactionVo extends PageInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/** 保單號碼 */
	private String policyNo;
	/** 交易日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date premiumDate;
	/** INV101, INV102, INV103=首期保費費用; (INV104)=單筆保費費用;  (INV201)=管理費 */
	private String premiumCode;
	/** 序號  */
	private BigDecimal premiumSeq;
	/**  */
	private BigDecimal applyAmount;
	/** 保險成本 */
	private BigDecimal policyPremium;
	/** 利息 */
	private BigDecimal intAmount;
	/** 手續費 */
	private BigDecimal feeAmount;
	/** 第二次扣收COI */
	private BigDecimal charge2nd;

	/** 繳納金額 */
	private BigDecimal investAmt;
	/** 首期保費費用 */
	private BigDecimal targetLoading; 
	/** 單筆保費費用 */
	private BigDecimal increLoading;
	/** 保單管理費 */
	private BigDecimal maintenanceFee;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public Date getPremiumDate() {
		return this.premiumDate;
	}
	
	public void setPremiumDate(Date premiumDate) {
		this.premiumDate = premiumDate;
	}
	
	public String getPremiumCode() {
		return this.premiumCode;
	}
	
	public void setPremiumCode(String premiumCode) {
		this.premiumCode = premiumCode;
	}
	
	public BigDecimal getPremiumSeq() {
		return this.premiumSeq;
	}
	
	public void setPremiumSeq(BigDecimal premiumSeq) {
		this.premiumSeq = premiumSeq;
	}
	
	public BigDecimal getApplyAmount() {
		return this.applyAmount;
	}
	
	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}
	
	public BigDecimal getPolicyPremium() {
		return this.policyPremium;
	}
	
	public void setPolicyPremium(BigDecimal policyPremium) {
		this.policyPremium = policyPremium;
	}
	
	public BigDecimal getIntAmount() {
		return this.intAmount;
	}
	
	public void setIntAmount(BigDecimal intAmount) {
		this.intAmount = intAmount;
	}
	
	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}
	
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	public BigDecimal getCharge2nd() {
		return this.charge2nd;
	}
	
	public void setCharge2nd(BigDecimal charge2nd) {
		this.charge2nd = charge2nd;
	}
	
	public BigDecimal getInvestAmt() {
		return investAmt;
	}

	public void setInvestAmt(BigDecimal investAmt) {
		this.investAmt = investAmt;
	}

	public BigDecimal getTargetLoading() {
		return targetLoading;
	}

	public void setTargetLoading(BigDecimal targetLoading) {
		this.targetLoading = targetLoading;
	}

	public BigDecimal getIncreLoading() {
		return increLoading;
	}

	public void setIncreLoading(BigDecimal increLoading) {
		this.increLoading = increLoading;
	}

	public BigDecimal getMaintenanceFee() {
		return maintenanceFee;
	}

	public void setMaintenanceFee(BigDecimal maintenanceFee) {
		this.maintenanceFee = maintenanceFee;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}