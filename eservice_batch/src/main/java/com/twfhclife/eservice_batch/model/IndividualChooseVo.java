package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class IndividualChooseVo {
	/**
	 * 身分證號碼
	 */
	private String rocId;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 生日
	 */
	private String birthDate;
	/**
	 * 電話
	 */
	private String mobile;
	/**
	 * 信箱
	 */
	private String email;
	/**
	 * 教育程度
	 */
	private String educationLevel;
	/**
	 * 領有全民健康保險重大傷病證明或身心障礙證明
	 */
	private String disability;
	/**
	 * 收入
	 */
	private String income;
	/**
	 * 風險問題、答案
	 */
	private String choose;
	/**
	 * 風險等級
	 */
	private String riskAttr;
	/**
	 * 來源 1:官網 3:CTC
	 */
	private String source;
	/**
	 * 填寫日期
	 */
	private String editDate;
	/**
	 * 內部人員邊奧
	 */
	private String employeeId;
	/**
	 * 
	 */
	private String ratingDate;
	public String getRocId() {
		return rocId;
	}
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getMobile() {
		return mobile;
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
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getDisability() {
		return disability;
	}
	public void setDisability(String disability) {
		this.disability = disability;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getChoose() {
		return choose;
	}
	public void setChoose(String choose) {
		this.choose = choose;
	}
	public String getRiskAttr() {
		return riskAttr;
	}
	public void setRiskAttr(String riskAttr) {
		this.riskAttr = riskAttr;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getEditDate() {
		return editDate;
	}
	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getRatingDate() {
		return ratingDate;
	}
	public void setRatingDate(String ratingDate) {
		this.ratingDate = ratingDate;
	}
	
}
