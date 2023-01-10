package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;

public class AttitudeMailVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String attitudeName;
	private String attitudeId;
	private Integer attitudeOld;
	private String attitudeInsuNo;
	private String attitudeTelDay;
	private String attitudeTelNight;
	private String attitudeCity;
	private String attitudeArea;
	private String attitudeZipCode;
	private String attitudeAddr;
	private String attitudeEmail;
	private String attitudeContent;
	private Integer attitudeContaceType;
	private Date attitudeSubmitDate;
	public String getAttitudeName() {
		return attitudeName;
	}
	public void setAttitudeName(String attitudeName) {
		this.attitudeName = attitudeName;
	}
	public String getAttitudeId() {
		return attitudeId;
	}
	public void setAttitudeId(String attitudeId) {
		this.attitudeId = attitudeId;
	}
	public Integer getAttitudeOld() {
		return attitudeOld;
	}
	public void setAttitudeOld(Integer attitudeOld) {
		this.attitudeOld = attitudeOld;
	}
	public String getAttitudeInsuNo() {
		return attitudeInsuNo;
	}
	public void setAttitudeInsuNo(String attitudeInsuNo) {
		this.attitudeInsuNo = attitudeInsuNo;
	}
	public String getAttitudeTelDay() {
		return attitudeTelDay;
	}
	public void setAttitudeTelDay(String attitudeTelDay) {
		this.attitudeTelDay = attitudeTelDay;
	}
	public String getAttitudeTelNight() {
		return attitudeTelNight;
	}
	public void setAttitudeTelNight(String attitudeTelNight) {
		this.attitudeTelNight = attitudeTelNight;
	}
	public String getAttitudeCity() {
		return attitudeCity;
	}
	public void setAttitudeCity(String attitudeCity) {
		this.attitudeCity = attitudeCity;
	}
	public String getAttitudeArea() {
		return attitudeArea;
	}
	public void setAttitudeArea(String attitudeArea) {
		this.attitudeArea = attitudeArea;
	}
	public String getAttitudeZipCode() {
		return attitudeZipCode;
	}
	public void setAttitudeZipCode(String attitudeZipCode) {
		this.attitudeZipCode = attitudeZipCode;
	}
	public String getAttitudeAddr() {
		return attitudeAddr;
	}
	public void setAttitudeAddr(String attitudeAddr) {
		this.attitudeAddr = attitudeAddr;
	}
	public String getAttitudeEmail() {
		return attitudeEmail;
	}
	public void setAttitudeEmail(String attitudeEmail) {
		this.attitudeEmail = attitudeEmail;
	}
	public String getAttitudeContent() {
		return attitudeContent;
	}
	public void setAttitudeContent(String attitudeContent) {
		this.attitudeContent = attitudeContent;
	}
	public Integer getAttitudeContaceType() {
		return attitudeContaceType;
	}
	public void setAttitudeContaceType(Integer attitudeContaceType) {
		this.attitudeContaceType = attitudeContaceType;
	}
	public Date getAttitudeSubmitDate() {
		return attitudeSubmitDate;
	}
	public void setAttitudeSubmitDate(Date attitudeSubmitDate) {
		this.attitudeSubmitDate = attitudeSubmitDate;
	}
}
