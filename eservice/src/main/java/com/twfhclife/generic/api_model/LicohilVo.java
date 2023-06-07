package com.twfhclife.generic.api_model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LicohilVo {
	private String lipmInsuNo;
	private String lipmName1;
	private String lipmId;
	private String sex;
	private String pmdaApplicantCellphone;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date bnagBirth;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipmInsuBegDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date ratingDate;
	private String inveAttr;
	private String policyListType;
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}
	public String getLipmName1() {
		return lipmName1;
	}
	public void setLipmName1(String lipmName1) {
		this.lipmName1 = lipmName1;
	}
	public String getLipmId() {
		return lipmId;
	}
	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBnagBirth() {
		return bnagBirth;
	}
	public void setBnagBirth(Date bnagBirth) {
		this.bnagBirth = bnagBirth;
	}
	public Date getLipmInsuBegDate() {
		return lipmInsuBegDate;
	}
	public void setLipmInsuBegDate(Date lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
	}
	public Date getRatingDate() {
		return ratingDate;
	}
	public void setRatingDate(Date ratingDate) {
		this.ratingDate = ratingDate;
	}
	public String getInveAttr() {
		return inveAttr;
	}
	public void setInveAttr(String inveAttr) {
		this.inveAttr = inveAttr;
	}
	public String getPmdaApplicantCellphone() {
		return pmdaApplicantCellphone;
	}
	public void setPmdaApplicantCellphone(String pmdaApplicantCellphone) {
		this.pmdaApplicantCellphone = pmdaApplicantCellphone;
	}
	public String getPolicyListType() {
		return policyListType;
	}
	public void setPolicyListType(String policyListType) {
		this.policyListType = policyListType;
	}
	
	
}
