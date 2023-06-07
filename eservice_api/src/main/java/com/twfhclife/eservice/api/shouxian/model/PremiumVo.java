package com.twfhclife.eservice.api.shouxian.model;

public class PremiumVo {

    private String beginDate;
    private String premium;
    private String currentYearDividend;
    private String balance;
    private String code;
    private String bonDate;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getCurrentYearDividend() { return currentYearDividend; }

    public void setCurrentYearDividend(String currentYearDividend) { this.currentYearDividend = currentYearDividend; }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBonDate() {
        return bonDate;
    }

    public void setBonDate(String bonDate) {
        this.bonDate = bonDate;
    }
}
