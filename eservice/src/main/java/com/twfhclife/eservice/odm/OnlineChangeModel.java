package com.twfhclife.eservice.odm;

public class OnlineChangeModel {

	/**
	 * 線上申請功能類別代碼,ex:ANNUITY_METHOD,INSURANCE_CLAIM
	 */
	public String transType;
	
	/**
	 * 要保人年齡
	 */
	private int applicantAge;
	
	/**
	 * 被保人年齡
	 */
	private int insuredAge;
	
	/**
	 * 檢核結果,default is false.
	 */
	private boolean resultPass = false;
	
	/**
	 * 檢核結果回傳訊息
	 */
	private String resultMsg;

	public int getApplicantAge() {
		return applicantAge;
	}

	public void setApplicantAge(int applicantAge) {
		this.applicantAge = applicantAge;
	}

	public int getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(int insuredAge) {
		this.insuredAge = insuredAge;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public boolean isResultPass() {
		return resultPass;
	}

	public void setResultPass(boolean resultPass) {
		this.resultPass = resultPass;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
