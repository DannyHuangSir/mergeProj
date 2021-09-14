package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransBeneficiaryDtlVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transBeneficiaryId;
	/**  */
	private BigDecimal beneficiaryType;
	/**  */
	private String beneficiaryName;
	/**  */
	private String beneficiaryRocid;
	/**  */
	private String beneficiaryRelation;
	/**  */
	private String reason;
	/**  */
	private BigDecimal allocateType;
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
}

