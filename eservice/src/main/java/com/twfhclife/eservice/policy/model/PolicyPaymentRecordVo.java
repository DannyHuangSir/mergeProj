package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PolicyPaymentRecordVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/**  */
	private String policyNo;
	/**  */
	private BigDecimal paidAmount;
	/**  */
	private BigDecimal planAmount;
	/**  */
	private String payMethod;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date payDate;
	
	private String payDesc;
	
	private PolicyVo policyVo;
	
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
	
	public BigDecimal getPaidAmount() {
		return this.paidAmount;
	}
	
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	public BigDecimal getPlanAmount() {
		return this.planAmount;
	}
	
	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	
	public String getPayMethod() {
		return this.payMethod;
	}
	
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	public Date getPayDate() {
		return this.payDate;
	}
	
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getPayDesc() {
		return payDesc;
	}

	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}

	public PolicyVo getPolicyVo() {
		return policyVo;
	}

	public void setPolicyVo(PolicyVo policyVo) {
		this.policyVo = policyVo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}