package com.twfhclife.eservice.onlineChange.model;

public class IndividualVo {
	
	private String individualId;
	private String name;
	private String rocId;
	private String sex;
	private String birthDate;
	private String riskAttr;

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getRiskAttr() {
		return riskAttr;
	}

	public void setRiskAttr(String riskAttr) {
		this.riskAttr = riskAttr;
	}
}
