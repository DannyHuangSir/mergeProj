package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransReducePolicyDtlVo {
	
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
}

