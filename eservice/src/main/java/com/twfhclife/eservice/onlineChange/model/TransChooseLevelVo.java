package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

public class TransChooseLevelVo {
	/**  */
	private BigDecimal id;
	/** 申請單號 */
	private String transNum;
	/** 上一次的風險等級 */
	private String chooseLevelOld;
	/** 風險等級 */
	private String chooseLevelNew;
	/** 風險評估分數 */
	private int chooseScore;
	/** */
	private String chooseLevelDesc;
	/** 身分證號碼 */
	private String rocId;
	/**風險問題與答案 */
	private String choose;
	/**
	 * 年齡、學歷、重大醫療事故 規則
	 * 1.符合有勾選 2.符合規則但未勾選 3.非符合規則內
	 */
	private String ruleStatus;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getChooseLevelOld() {
		return chooseLevelOld;
	}

	public void setChooseLevelOld(String chooseLevelOld) {
		this.chooseLevelOld = chooseLevelOld;
	}

	public String getChooseLevelNew() {
		return chooseLevelNew;
	}

	public void setChooseLevelNew(String chooseLevelNew) {
		this.chooseLevelNew = chooseLevelNew;
	}

	public int getChooseScore() {
		return chooseScore;
	}

	public void setChooseScore(int chooseScore) {
		this.chooseScore = chooseScore;
	}

	public String getChooseLevelDesc() {
		return chooseLevelDesc;
	}

	public void setChooseLevelDesc(String chooseLevelDesc) {
		this.chooseLevelDesc = chooseLevelDesc;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}
		
}
