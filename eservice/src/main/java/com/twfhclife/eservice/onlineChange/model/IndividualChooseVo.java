package com.twfhclife.eservice.onlineChange.model;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

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
	 * 舊風險等級
	 */
	private String oldRiskAttr;
	/**
	 *風險分數
	 */
	private String score;
	/**
	 * 來源 1:官網 3:CTC
	 */
	private String source;
	/**
	 * 填寫日期
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date editDate;
	/**
	 * 內部人員邊奧
	 */
	private String employeeId;
	/**
	 * 內幕人員密碼
	 */
	private String employeePassword;
	/**
	 * 
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date ratingDate;
	/**
	 * 手機驗證碼
	 */
	private String authenticationNum;
	/**
	 * 判斷是否為既有客戶
	 * true : 既有客戶 。 false : 非本公司客戶
	 */
	private boolean chooseType = false;
	/**
	 * 判斷是不適網服會員
	 * true : 網服會員 。 false : 非網服會員
	 */
	private boolean usersType = false;
	
	private String desc;
	/**
	 * 判斷風險評估問題8、9 選項是否不允許評估
	 */
	private boolean optionsType = true;
	/**
	 * 
	 */
	private String optionsMsg ;
	/**
	 * 
	 */
	private boolean riskType = true;
	/**
	 * 填寫日期是否滿一年
	 */
	private boolean riskDateType = true;
	/**
	 * 是否顯示 問題畫面pup
	 */
	private boolean showPup = false;
	
    private String questions;
    private List<String> answers = Lists.newArrayList();
    
	/**
	 * 年齡、學歷、重大醫療事故 規則
	 * 1.符合規則但未勾選 2.符合有勾選 3.非符合規則內
	 */
	private String ruleStatus;
	
	private boolean readPdfType = false;
	/**
	 * 徒刑驗證碼
	 */
	private String validateCode ;
	/**
	 * 
	 */
	private String validateSessionCode;
	
	
	
	public String getValidateSessionCode() {
		return validateSessionCode;
	}
	public void setValidateSessionCode(String validateSessionCode) {
		this.validateSessionCode = validateSessionCode;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public boolean isReadPdfType() {
		return readPdfType;
	}
	public void setReadPdfType(boolean readPdfType) {
		this.readPdfType = readPdfType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getQuestions() {
		return questions;
	}
	public void setQuestions(String questions) {
		this.questions = questions;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
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
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Date getRatingDate() {
		return ratingDate;
	}
	public void setRatingDate(Date ratingDate) {
		this.ratingDate = ratingDate;
	}
	public String getAuthenticationNum() {
		return authenticationNum;
	}
	public void setAuthenticationNum(String authenticationNum) {
		this.authenticationNum = authenticationNum;
	}
	public boolean isChooseType() {
		return chooseType;
	}
	public void setChooseType(boolean chooseType) {
		this.chooseType = chooseType;
	}
	public boolean isUsersType() {
		return usersType;
	}
	public void setUsersType(boolean usersType) {
		this.usersType = usersType;
	}
	public String getOldRiskAttr() {
		return oldRiskAttr;
	}
	public void setOldRiskAttr(String oldRiskAttr) {
		this.oldRiskAttr = oldRiskAttr;
	}
	public boolean isOptionsType() {
		return optionsType;
	}
	public void setOptionsType(boolean optionsType) {
		this.optionsType = optionsType;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getOptionsMsg() {
		return optionsMsg;
	}
	public void setOptionsMsg(String optionsMsg) {
		this.optionsMsg = optionsMsg;
	}
	public boolean isRiskType() {
		return riskType;
	}
	public void setRiskType(boolean riskType) {
		this.riskType = riskType;
	}
	public String getEmployeePassword() {
		return employeePassword;
	}
	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}
	public boolean isRiskDateType() {
		return riskDateType;
	}
	public void setRiskDateType(boolean riskDateType) {
		this.riskDateType = riskDateType;
	}
	public boolean isShowPup() {
		return showPup;
	}
	public void setShowPup(boolean showPup) {
		this.showPup = showPup;
	}
	public String getRuleStatus() {
		return ruleStatus;
	}
	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}	
}
