package com.twfhclife.eservice.api.shouxian.model;

import java.math.BigDecimal;

public class IncomeDistributionVo {


    private String invtName;
    private String invtNo;
    private String compuDate;
    private String currency;
    private String expeUnits;
    private String inprRate;
    private String trDate;
    private BigDecimal expeNtd;
    private BigDecimal exchRate;

    private BigDecimal units;
    private String trCode;
    private String payDate;

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

    public String getCompuDate() {
        return compuDate;
    }

    public void setCompuDate(String compuDate) {
        this.compuDate = compuDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpeUnits() {
        return expeUnits;
    }

    public void setExpeUnits(String expeUnits) {
        this.expeUnits = expeUnits;
    }

    public String getInprRate() {
        return inprRate;
    }

    public void setInprRate(String inprRate) {
        this.inprRate = inprRate;
    }

    public String getTrDate() {
        return trDate;
    }

    public void setTrDate(String trDate) {
        this.trDate = trDate;
    }

    public BigDecimal getExpeNtd() {
        return expeNtd;
    }

    public void setExpeNtd(BigDecimal expeNtd) {
        this.expeNtd = expeNtd;
    }

    public BigDecimal getExchRate() {
        return exchRate;
    }

    public void setExchRate(BigDecimal exchRate) {
        this.exchRate = exchRate;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}
