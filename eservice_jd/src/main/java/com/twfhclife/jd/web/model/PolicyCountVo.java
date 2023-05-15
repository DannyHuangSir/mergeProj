package com.twfhclife.jd.web.model;

public class PolicyCountVo {

    private int effectiveCount = 0;
    private int stopCount = 0;
    private int expireCount = 0;
    private int total = 0;

    public int getEffectiveCount() {
        return effectiveCount;
    }

    public void setEffectiveCount(int effectiveCount) {
        this.effectiveCount = effectiveCount;
    }

    public int getStopCount() {
        return stopCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    public int getExpireCount() {
        return expireCount;
    }

    public void setExpireCount(int expireCount) {
        this.expireCount = expireCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
