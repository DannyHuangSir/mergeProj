package com.twfhclife.eservice.web.model;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PolicyVo {

	private static final long serialVersionUID = 1L;

	private List<String> policyNos;

	/** 使用者證號 */
	private String rocId;

	public List<String> getPolicyNos() {
		return policyNos;
	}

	public void setPolicyNos(List<String> policyNos) {
		this.policyNos = policyNos;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	/**  */
	private String policyNo;
	/**  */
	private String groupNo;
	/**  */
	private String seqNo;
	/**  */
	private String status;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date effectiveDate;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date expireDate;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date paidToDate;
	/**  */
	private String paymentMethod;
	/**  */
	private String paymentMode;
	/**  */
	private String policyType;
	/**  */
	private BigDecimal mainAmount;
	/**  */
	private BigDecimal premYear;
	/**  */
	private String unpaidStatus;
	/**  */
	private BigDecimal unpaidAmount;
	/**  */
	private String currency;
	/**  */
	private String agentCode;
	/**  */
	private String agentNaCode;
	/**  */
	private String agentCodeBranch;
	/**  */
	private Integer lipmTredTms;
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getGroupNo() {
		return this.groupNo;
	}
	
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	
	public String getSeqNo() {
		return this.seqNo;
	}
	
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}
	
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public Date getExpireDate() {
		return this.expireDate;
	}
	
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public Date getPaidToDate() {
		return this.paidToDate;
	}
	
	public void setPaidToDate(Date paidToDate) {
		this.paidToDate = paidToDate;
	}
	
	public String getPaymentMethod() {
		return this.paymentMethod;
	}
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public String getPaymentMode() {
		return this.paymentMode;
	}
	
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	public String getPolicyType() {
		return this.policyType;
	}
	
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	
	public BigDecimal getMainAmount() {
		return this.mainAmount;
	}
	
	public void setMainAmount(BigDecimal mainAmount) {
		this.mainAmount = mainAmount;
	}
	
	public BigDecimal getPremYear() {
		return this.premYear;
	}
	
	public void setPremYear(BigDecimal premYear) {
		this.premYear = premYear;
	}
	
	public String getUnpaidStatus() {
		return this.unpaidStatus;
	}
	
	public void setUnpaidStatus(String unpaidStatus) {
		this.unpaidStatus = unpaidStatus;
	}
	
	public BigDecimal getUnpaidAmount() {
		return this.unpaidAmount;
	}
	
	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getAgentCode() {
		return this.agentCode;
	}
	
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	
	public String getAgentNaCode() {
		return this.agentNaCode;
	}
	
	public void setAgentNaCode(String agentNaCode) {
		this.agentNaCode = agentNaCode;
	}
	
	public String getAgentCodeBranch() {
		return this.agentCodeBranch;
	}
	
	public void setAgentCodeBranch(String agentCodeBranch) {
		this.agentCodeBranch = agentCodeBranch;
	}

	public Integer getLipmTredTms() {
		return lipmTredTms;
	}

	public void setLipmTredTms(Integer lipmTredTms) {
		this.lipmTredTms = lipmTredTms;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

