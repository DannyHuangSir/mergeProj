package com.twfhclife.eservice.policy.model;

public class CompareInvestmentVo {

    private String invtNo;
    private String invtName;
    private Integer preRatio = 0;
    private Integer afterRatio;

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

    public Integer getPreRatio() {
        return preRatio;
    }

    public void setPreRatio(Integer preRatio) {
        this.preRatio = preRatio;
    }

    public Integer getAfterRatio() {
        return afterRatio;
    }

    public void setAfterRatio(Integer afterRatio) {
        this.afterRatio = afterRatio;
    }
}
