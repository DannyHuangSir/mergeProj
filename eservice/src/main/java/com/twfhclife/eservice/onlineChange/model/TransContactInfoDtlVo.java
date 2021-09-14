package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransContactInfoDtlVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
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
	/**  */
	private String city;
	/**  */
	private String region;
	private String road;
	/**  */
	private String address;
	/**  */
	private String addressFull;
	/**  */
	private String cityCharge;
	/**  */
	private String regionCharge;
	private String roadCharge;
	/**  */
	private String addressCharge;
	/**  */
	private String addressFullCharge;
	
	private String email;
	
	private String sendAlliance;
	
	private String formCompany;
	
	private String toCompany;
	
	private String lipmName1;
	
	private String status;
	private String name;
	private String idno;

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getRoadCharge() {
		return roadCharge;
	}

	public void setRoadCharge(String roadCharge) {
		this.roadCharge = roadCharge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSendAlliance() {
		return sendAlliance;
	}

	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}

	public String getFormCompany() {
		return formCompany;
	}

	public void setFormCompany(String formCompany) {
		this.formCompany = formCompany;
	}

	public String getToCompany() {
		return toCompany;
	}

	public void setToCompany(String toCompany) {
		this.toCompany = toCompany;
	}

	public String getLipmName1() {
		return lipmName1;
	}

	public void setLipmName1(String lipmName1) {
		this.lipmName1 = lipmName1;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}