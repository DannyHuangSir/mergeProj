package com.twfhclife.jd.web.model;

import java.io.Serializable;

public class LoginRequestVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String password; 
	private String validateCode;
	private String sessionValidateCode;
	private String fbId;
	private String email;
	private String fbAccessToken;
	private String cardSN;
	private String certb64;
	
	private String salt;
	private String four;
	private String ret;
	
	/**
	 * SR_GDPR
	 */
	private String euNationality = "";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFbAccessToken() {
		return fbAccessToken;
	}
	public void setFbAccessToken(String fbAccessToken) {
		this.fbAccessToken = fbAccessToken;
	}
	public String getCardSN() {
		return cardSN;
	}
	public void setCardSN(String cardSN) {
		this.cardSN = cardSN;
	}
	public String getCertb64() {
		return certb64;
	}
	public void setCertb64(String certb64) {
		this.certb64 = certb64;
	}
	public String getSessionValidateCode() {
		return sessionValidateCode;
	}
	public void setSessionValidateCode(String sessionValidateCode) {
		this.sessionValidateCode = sessionValidateCode;
	}
	
	public String getEuNationality() {
		return euNationality;
	}
	public void setEuNationality(String euNationality) {
		this.euNationality = euNationality;
	}
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getFour() {
		return four;
	}
	public void setFour(String four) {
		this.four = four;
	}
	
}