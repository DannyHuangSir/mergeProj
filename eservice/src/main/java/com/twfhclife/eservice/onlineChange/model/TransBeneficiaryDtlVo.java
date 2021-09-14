package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransBeneficiaryDtlVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transBeneficiaryId;
	/**  */
	private BigDecimal beneficiaryType;
	private String beneficiaryTypeStr;
	/**  */
	private String beneficiaryName;
	/**  */
	private String beneficiaryRocid;
	/**  */
	private String beneficiaryRelation;
	private String beneficiaryRelationStr;
	/**  */
	private String reason;
	/**  */
	private BigDecimal allocateType;
	private String allocateTypeStr;
	/**  */
	private BigDecimal allocateSeq;
	/**  */
	private String beneficiaryTel;
	/**  */
	private String beneficiaryCity;
	/**  */
	private String beneficiaryRegion;
	/**  */
	private String beneficiaryAddress;
	/**  */
	private String beneficiaryAddressFull;
	
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
	
	public String getBeneficiaryRocid() {
		return this.beneficiaryRocid;
	}
	
	public void setBeneficiaryRocid(String beneficiaryRocid) {
		this.beneficiaryRocid = beneficiaryRocid;
	}
	
	public String getBeneficiaryRelation() {
		return this.beneficiaryRelation;
	}
	
	public void setBeneficiaryRelation(String beneficiaryRelation) {
		this.beneficiaryRelation = beneficiaryRelation;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
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
	
	public String getBeneficiaryTel() {
		return this.beneficiaryTel;
	}
	
	public void setBeneficiaryTel(String beneficiaryTel) {
		this.beneficiaryTel = beneficiaryTel;
	}
	
	public String getBeneficiaryCity() {
		return this.beneficiaryCity;
	}
	
	public void setBeneficiaryCity(String beneficiaryCity) {
		this.beneficiaryCity = beneficiaryCity;
	}
	
	public String getBeneficiaryRegion() {
		return this.beneficiaryRegion;
	}
	
	public void setBeneficiaryRegion(String beneficiaryRegion) {
		this.beneficiaryRegion = beneficiaryRegion;
	}
	
	public String getBeneficiaryAddress() {
		return this.beneficiaryAddress;
	}
	
	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}
	
	public String getBeneficiaryAddressFull() {
		return this.beneficiaryAddressFull;
	}
	
	public void setBeneficiaryAddressFull(String beneficiaryAddressFull) {
		this.beneficiaryAddressFull = beneficiaryAddressFull;
	}

	public String getBeneficiaryTypeStr() {
		return beneficiaryTypeStr;
	}

	public void setBeneficiaryTypeStr(String beneficiaryTypeStr) {
		this.beneficiaryTypeStr = beneficiaryTypeStr;
	}

	public String getBeneficiaryRelationStr() {
		return beneficiaryRelationStr;
	}

	public void setBeneficiaryRelationStr(String beneficiaryRelationStr) {
		this.beneficiaryRelationStr = beneficiaryRelationStr;
	}

	public String getAllocateTypeStr() {
		return allocateTypeStr;
	}

	public void setAllocateTypeStr(String allocateTypeStr) {
		this.allocateTypeStr = allocateTypeStr;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}