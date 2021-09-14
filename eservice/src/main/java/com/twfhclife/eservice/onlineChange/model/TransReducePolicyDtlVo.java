package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransReducePolicyDtlVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transReducePolicyId;
	/**  */
	private String policyNo;
	/**  */
	private String contractType;
	/**  */
	private String productName;
	/**  */
	private BigDecimal contractAmount;
	/**  */
	private BigDecimal contractAmountOld;
	
	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public BigDecimal getTransReducePolicyId() {
		return this.transReducePolicyId;
	}
	
	public void setTransReducePolicyId(BigDecimal transReducePolicyId) {
		this.transReducePolicyId = transReducePolicyId;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getContractType() {
		return this.contractType;
	}
	
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public BigDecimal getContractAmount() {
		return this.contractAmount;
	}
	
	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}

	public BigDecimal getContractAmountOld() {
		return contractAmountOld;
	}

	public void setContractAmountOld(BigDecimal contractAmountOld) {
		this.contractAmountOld = contractAmountOld;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}