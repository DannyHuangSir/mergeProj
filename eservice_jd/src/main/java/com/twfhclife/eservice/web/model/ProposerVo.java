package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 要保人資料物件.
 * 
 * @author alan
 */
public class ProposerVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 要保人 */
	private String proposer;

	/** 性別 */
	private String gender;

	/** 生日 */
	private Date birthday;

	/** 身分證字號 */
	private String identityId;

	/** 通訊地址 */
	private String address;

	/** 聯絡電話(公) */
	private String officeTel;

	/** 聯絡電話(宅) */
	private String homeTel;

	/** 行動電話 */
	private String cellphoneNum;

	/** 電子信箱 */
	private String email;

	/** 國籍 */
	private String country;

	private String sameAddr;

	private String relationToInsured;

	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSameAddr() {
		return sameAddr;
	}

	public void setSameAddr(String sameAddr) {
		this.sameAddr = sameAddr;
	}

	public String getRelationToInsured() {
		return relationToInsured;
	}

	public void setRelationToInsured(String relationToInsured) {
		this.relationToInsured = relationToInsured;
	}

}
