package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransRewardVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String rewardMode;
	/**  */
	private String rewardModeOld;
	
	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public String getTransNum() {
		return this.transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	public String getRewardMode() {
		return this.rewardMode;
	}
	
	public void setRewardMode(String rewardMode) {
		this.rewardMode = rewardMode;
	}
	
	public String getRewardModeOld() {
		return this.rewardModeOld;
	}
	
	public void setRewardModeOld(String rewardModeOld) {
		this.rewardModeOld = rewardModeOld;
	}
}

