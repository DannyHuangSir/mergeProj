package com.twfhclife.jd.api_model;

import java.math.BigDecimal;
import java.util.Date;

public class ExchangeRateRequest extends AbstractBasePolicyNoDomain {

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
}
