package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExchangeRateVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Date effectiveDate;
	/**  */
	private String exchangeCode;
	/**  */
	private String currencyCode;
	/**  */
	private BigDecimal buyPrice;
	/**  */
	private BigDecimal sellPrice;
	
	private String queryType;
	
	private String queryMonth;
	
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}
	
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public String getExchangeCode() {
		return this.exchangeCode;
	}
	
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public BigDecimal getBuyPrice() {
		return this.buyPrice;
	}
	
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	public BigDecimal getSellPrice() {
		return this.sellPrice;
	}
	
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryMonth() {
		return queryMonth;
	}

	public void setQueryMonth(String queryMonth) {
		this.queryMonth = queryMonth;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}