package com.twfhclife.jd.web.model;

public class NotifyConfigVo extends PortfolioVo {

    private String userId;
    private int upRate;
    private int downRate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUpRate() {
        return upRate;
    }

    public void setUpRate(int upRate) {
        this.upRate = upRate;
    }

    public int getDownRate() {
        return downRate;
    }

    public void setDownRate(int downRate) {
        this.downRate = downRate;
    }
}
