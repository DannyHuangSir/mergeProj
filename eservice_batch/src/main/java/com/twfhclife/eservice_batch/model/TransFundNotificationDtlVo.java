package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransFundNotificationDtlVo {

	/** 
	 * 序號 
	 */
	private Integer id;
	/**
	 * 對應 TRANS_FUND_NOTIFICATION 使用的序號
	 */
	private Integer transFundNotificationId;
	/**
	 * 基金代碼
	 */
	private String fundCode;
	/**
	 * 停利百分點
	 */
	private BigDecimal percentageUp;
	/**
	 * 停損百分點
	 */
	private BigDecimal percentageDown;

	private String type;

	private BigDecimal upValue;

	private BigDecimal downValue;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTransFundNotificationId() {
		return transFundNotificationId;
	}
	public void setTransFundNotificationId(Integer transFundNotificationId) {
		this.transFundNotificationId = transFundNotificationId;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public BigDecimal getPercentageUp() {
		return percentageUp;
	}
	public void setPercentageUp(BigDecimal percentageUp) {
		this.percentageUp = percentageUp;
	}
	public BigDecimal getPercentageDown() {
		return percentageDown;
	}
	public void setPercentageDown(BigDecimal percentageDown) {
		this.percentageDown = percentageDown;
	}

	public BigDecimal getUpValue() {
		return upValue;
	}

	public void setUpValue(BigDecimal upValue) {
		this.upValue = upValue;
	}

	public BigDecimal getDownValue() {
		return downValue;
	}

	public void setDownValue(BigDecimal downValue) {
		this.downValue = downValue;
	}

}
