package com.twfhclife.eservice.web.model;

public class SubNotifyScheduleVo {

    private String policyNo;
    private String invtNo;
    private Integer upRate;
    private Integer downRate;

    public String getInvtNo() {
        return invtNo;
    }

    public void setInvtNo(String invtNo) {
        this.invtNo = invtNo;
    }

    public Integer getUpRate() {
        return upRate;
    }

    public void setUpRate(Integer upRate) {
        this.upRate = upRate;
    }

    public Integer getDownRate() {
        return downRate;
    }

    public void setDownRate(int downRate) {
        this.downRate = downRate;
    }
    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }
}
