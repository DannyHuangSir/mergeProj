package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransRewardVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}