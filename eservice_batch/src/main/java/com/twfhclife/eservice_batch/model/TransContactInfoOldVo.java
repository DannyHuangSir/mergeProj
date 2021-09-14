package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransContactInfoOldVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transContactId;
	/**  */
	private String telHome;
	/**  */
	private String telOffice;
	/**  */
	private String mobile;
	
	private String email;
	/**  */
	private String city;
	/**  */
	private String region;
	/**  */
	private String address;
	/**  */
	private String addressFull;
	/**  */
	private String cityCharge;
	/**  */
	private String regionCharge;
	/**  */
	private String addressCharge;
	/**  */
	private String addressFullCharge;
	/**  */
	private String policyNo;
	private String transType;

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public BigDecimal getTransContactId() {
		return this.transContactId;
	}
	
	public void setTransContactId(BigDecimal transContactId) {
		this.transContactId = transContactId;
	}
	
	public String getTelHome() {
		return this.telHome;
	}
	
	public void setTelHome(String telHome) {
		this.telHome = telHome;
	}
	
	public String getTelOffice() {
		return this.telOffice;
	}
	
	public void setTelOffice(String telOffice) {
		this.telOffice = telOffice;
	}
	
	public String getMobile() {
		return this.mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddressFull() {
		return this.addressFull;
	}
	
	public void setAddressFull(String addressFull) {
		this.addressFull = addressFull;
	}
	
	public String getCityCharge() {
		return this.cityCharge;
	}
	
	public void setCityCharge(String cityCharge) {
		this.cityCharge = cityCharge;
	}
	
	public String getRegionCharge() {
		return this.regionCharge;
	}
	
	public void setRegionCharge(String regionCharge) {
		this.regionCharge = regionCharge;
	}
	
	public String getAddressCharge() {
		return this.addressCharge;
	}
	
	public void setAddressCharge(String addressCharge) {
		this.addressCharge = addressCharge;
	}
	
	public String getAddressFullCharge() {
		return this.addressFullCharge;
	}
	
	public void setAddressFullCharge(String addressFullCharge) {
		this.addressFullCharge = addressFullCharge;
	}
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
}