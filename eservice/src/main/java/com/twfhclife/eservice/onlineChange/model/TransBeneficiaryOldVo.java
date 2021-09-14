package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransBeneficiaryOldVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transBeneficiaryId;
	/**  */
	private BigDecimal beneficiaryType;
	/**  */
	private String beneficiaryName;
	/**  */
	private String beneficiaryRelation;
	/**  */
	private BigDecimal allocateType;
	/**  */
	private BigDecimal allocateSeq;
	
	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public BigDecimal getTransBeneficiaryId() {
		return this.transBeneficiaryId;
	}
	
	public void setTransBeneficiaryId(BigDecimal transBeneficiaryId) {
		this.transBeneficiaryId = transBeneficiaryId;
	}
	
	public BigDecimal getBeneficiaryType() {
		return this.beneficiaryType;
	}
	
	public void setBeneficiaryType(BigDecimal beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}
	
	public String getBeneficiaryName() {
		return this.beneficiaryName;
	}
	
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	
	public String getBeneficiaryRelation() {
		return this.beneficiaryRelation;
	}
	
	public void setBeneficiaryRelation(String beneficiaryRelation) {
		this.beneficiaryRelation = beneficiaryRelation;
	}
	
	public BigDecimal getAllocateType() {
		return this.allocateType;
	}
	
	public void setAllocateType(BigDecimal allocateType) {
		this.allocateType = allocateType;
	}
	
	public BigDecimal getAllocateSeq() {
		return this.allocateSeq;
	}
	
	public void setAllocateSeq(BigDecimal allocateSeq) {
		this.allocateSeq = allocateSeq;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}