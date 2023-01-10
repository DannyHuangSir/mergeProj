package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 被保人資料物件.
 * 
 * @author alan
 */
public class InsuredVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 個人主體ID */
	private String individualId;

	/** 被保人姓名 */
	private String insuredName;

	/** 保單人email */
	private String email;

	/** 性別 */
	private String gender;

	/** 生日 */
	private Date birthday;

	/** 身分證字號 */
	private String identityId;

	/** 與要保人關係 */
	private String relation;

	/** 與要保人關係 - Parameter 名 */
	private String relationStr;

	/** 職業 */
	private String job;

	/** 地址 */
	private String address;

	/** 聯絡電話(公) */
	private String officeTel;

	/** 聯絡電話(宅) */
	private String homeTel;

	/** 行動電話 */
	private String cellphoneNum;
	
	/** 商品代碼 */
	private String prodCode;
	
	/** 目前是否受有監護宣告 */
	private String wnpiAnnounce;
	
	private String country;

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRelationStr() {
		return relationStr;
	}

	public void setRelationStr(String relationStr) {
		this.relationStr = relationStr;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getCellphoneNum() {
		return cellphoneNum;
	}

	public void setCellphoneNum(String cellphoneNum) {
		this.cellphoneNum = cellphoneNum;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getWnpiAnnounce() {
		return wnpiAnnounce;
	}

	public void setWnpiAnnounce(String wnpiAnnounce) {
		this.wnpiAnnounce = wnpiAnnounce;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
