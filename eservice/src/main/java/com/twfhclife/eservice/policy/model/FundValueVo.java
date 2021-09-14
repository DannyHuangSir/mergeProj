package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundValueVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String fundCode;
	/**  */
	private Date effectiveDate;
	/**  */
	private BigDecimal buyPrice;
	/**  */
	private BigDecimal sellPrice;
	
	private String queryType;
	
	private String queryMonth;
	
	public String getFundCode() {
		return this.fundCode;
	}
	
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}
	
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
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