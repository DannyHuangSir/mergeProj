package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransFundNotificationDtlVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal transFundNotificationId;
	/** 投資標的代碼 */
	private String fundCode;
	/**  */
	private BigDecimal percentageUp;
	/**  */
	private BigDecimal percentageDown;
	
	/** 投資標的名稱 */
	private String invtName;
	/** 幣別名稱 */
	private String currency;
	/** 風險收益等級 */
	private String riskBeneLevel;
	/** 參考報酬率(%) */
	private String roiRate;
	/** 帳戶價值 */
	private String acctValue;

	private String type;

	private BigDecimal upValue;

	private Date effectiveDate;

	private BigDecimal sellPrice;

	private BigDecimal downValue;

	private String inCurr;

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public BigDecimal getTransFundNotificationId() {
		return this.transFundNotificationId;
	}
	
	public void setTransFundNotificationId(BigDecimal transFundNotificationId) {
		this.transFundNotificationId = transFundNotificationId;
	}
	
	public String getFundCode() {
		return this.fundCode;
	}
	
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	
	public BigDecimal getPercentageUp() {
		return this.percentageUp;
	}
	
	public void setPercentageUp(BigDecimal percentageUp) {
		this.percentageUp = percentageUp;
	}
	
	public BigDecimal getPercentageDown() {
		return this.percentageDown;
	}
	
	public void setPercentageDown(BigDecimal percentageDown) {
		this.percentageDown = percentageDown;
	}

	public String getInvtName() {
		return invtName;
	}

	public void setInvtName(String invtName) {
		this.invtName = invtName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRiskBeneLevel() {
		return riskBeneLevel;
	}

	public void setRiskBeneLevel(String riskBeneLevel) {
		this.riskBeneLevel = riskBeneLevel;
	}

	public String getRoiRate() {
		return roiRate;
	}

	public void setRoiRate(String roiRate) {
		this.roiRate = roiRate;
	}

	public String getAcctValue() {
		return acctValue;
	}

	public void setAcctValue(String acctValue) {
		this.acctValue = acctValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getInCurr() {
		return inCurr;
	}

	public void setInCurr(String inCurr) {
		this.inCurr = inCurr;
	}
}