package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;

public class BeneficiaryVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer transBeneficiaryId;

	/**
	 * 保單號碼
	 */
	private String policyNo;

	/**
	 * 受益人類型 同 beniCode
	 */
	private Integer beneficiaryType;

	/**
	 * 受益人類型-parameter字串名稱
	 */
	private String beneficiaryTypeStr;

	/**
	 * 受益人姓名 同 beniName
	 */
	private String beneficiaryName;

	/**
	 * 受益人姓名 同 beniName
	 */
	private String beneficiaryNameBase64;

	/**
	 * 受益人身分證
	 */
	private String beneficiaryRocid;

	/**
	 * 與被保人關係 同 beniRelation
	 */
	private String beneficiaryRelation;

	/**
	 * 與被保人關係-parameter字串名稱
	 */
	private String beneficiaryRelationStr;

	/**
	 * 非直屬受益人原因
	 */
	private String reason;

	/**
	 * 分配方式
	 */
	private Integer allocateType;

	/**
	 * 分配方式
	 */
	private String allocateTypeStr;

	/**
	 * 分配比例或順位 同 beniSeq
	 */
	private Integer allocateSeq;

	/**
	 * 受益人電話
	 */
	private String beneficiaryTel;

	/**
	 * 受益人居住城市
	 */
	private String beneficiaryCity;

	/**
	 * 受益人居住區域
	 */
	private String beneficiaryRegion;

	/**
	 * 受益人居住地址
	 */
	private String beneficiaryAddress;

	/**
	 * 完整地址
	 */
	private String beneficiaryAddressFull;

	private String name;

	/** 國籍 */
	private String country;

	private Date beniBirthday;
	
	private String legalInherit;

	// 申請類別 sql回傳需要
	private String transTypeStr;

	public Integer getTransBeneficiaryId() {
		return transBeneficiaryId;
	}

	public void setTransBeneficiaryId(Integer transBeneficiaryId) {
		this.transBeneficiaryId = transBeneficiaryId;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Integer getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(Integer beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	public String getBeneficiaryTypeStr() {
		return beneficiaryTypeStr;
	}

	public void setBeneficiaryTypeStr(String beneficiaryTypeStr) {
		this.beneficiaryTypeStr = beneficiaryTypeStr;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryNameBase64() {
		return beneficiaryNameBase64;
	}

	public void setBeneficiaryNameBase64(String beneficiaryNameBase64) {
		this.beneficiaryNameBase64 = beneficiaryNameBase64;
	}

	public String getBeneficiaryRocid() {
		return beneficiaryRocid;
	}

	public void setBeneficiaryRocid(String beneficiaryRocid) {
		this.beneficiaryRocid = beneficiaryRocid;
	}

	public String getBeneficiaryRelation() {
		return beneficiaryRelation;
	}

	public void setBeneficiaryRelation(String beneficiaryRelation) {
		this.beneficiaryRelation = beneficiaryRelation;
	}

	public String getBeneficiaryRelationStr() {
		return beneficiaryRelationStr;
	}

	public void setBeneficiaryRelationStr(String beneficiaryRelationStr) {
		this.beneficiaryRelationStr = beneficiaryRelationStr;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getAllocateType() {
		return allocateType;
	}

	public void setAllocateType(Integer allocateType) {
		this.allocateType = allocateType;
	}

	public String getAllocateTypeStr() {
		return allocateTypeStr;
	}

	public void setAllocateTypeStr(String allocateTypeStr) {
		this.allocateTypeStr = allocateTypeStr;
	}

	public Integer getAllocateSeq() {
		return allocateSeq;
	}

	public void setAllocateSeq(Integer allocateSeq) {
		this.allocateSeq = allocateSeq;
	}

	public String getBeneficiaryTel() {
		return beneficiaryTel;
	}

	public void setBeneficiaryTel(String beneficiaryTel) {
		this.beneficiaryTel = beneficiaryTel;
	}

	public String getBeneficiaryCity() {
		return beneficiaryCity;
	}

	public void setBeneficiaryCity(String beneficiaryCity) {
		this.beneficiaryCity = beneficiaryCity;
	}

	public String getBeneficiaryRegion() {
		return beneficiaryRegion;
	}

	public void setBeneficiaryRegion(String beneficiaryRegion) {
		this.beneficiaryRegion = beneficiaryRegion;
	}

	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	public String getBeneficiaryAddressFull() {
		return beneficiaryAddressFull;
	}

	public void setBeneficiaryAddressFull(String beneficiaryAddressFull) {
		this.beneficiaryAddressFull = beneficiaryAddressFull;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransTypeStr() {
		return transTypeStr;
	}

	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getBeniBirthday() {
		return beniBirthday;
	}

	public void setBeniBirthday(Date beniBirthday) {
		this.beniBirthday = beniBirthday;
	}

	public String getLegalInherit() {
		return legalInherit;
	}

	public void setLegalInherit(String legalInherit) {
		this.legalInherit = legalInherit;
	}

}
