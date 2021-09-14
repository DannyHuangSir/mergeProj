package com.twfhclife.eservice.policy.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class NotificationFundVo {

    public String getInvtNo() {
        return invtNo;
    }

    public void setInvtNo(String invtNo) {
        this.invtNo = invtNo;
    }

    public String getInvtName() {
        return invtName;
    }

    public void setInvtName(String invtName) {
        this.invtName = invtName;
    }

    public String getInvtExchCurr() {
        return invtExchCurr;
    }

    public void setInvtExchCurr(String invtExchCurr) {
        this.invtExchCurr = invtExchCurr;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

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

    public BigDecimal getUpValue() {
        return upValue;
    }

    public void setUpValue(BigDecimal upValue) {
        this.upValue = upValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDownValue() {
        return downValue;
    }

    public void setDownValue(BigDecimal downValue) {
        this.downValue = downValue;
    }

    private String invtNo;
    private String invtName;
    private String invtExchCurr;
    private String company;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date effectiveDate;
    private BigDecimal sellPrice;
    private BigDecimal upValue;
    private BigDecimal downValue;
    private String currency;

}
