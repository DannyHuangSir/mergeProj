package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransReducePolicyOldVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transReducePolicyId;
	/**  */
	private String contractType;
	/**  */
	private String productName;
	/**  */
	private BigDecimal contractAmount;
	
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}