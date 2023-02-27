package com.twfhclife.eservice.api.shouxian.model;

public class PaymentRecordVo {

    private String rcpDate;
    private String desc;
    private String paabAmt;
    private String actAmt;
    private String rcpCode;

    public String getRcpDate() {
        return rcpDate;
    }

    public void setRcpDate(String rcpDate) {
        this.rcpDate = rcpDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPaabAmt() {
        return paabAmt;
    }

    public void setPaabAmt(String paabAmt) {
        this.paabAmt = paabAmt;
    }

    public String getActAmt() {
        return actAmt;
    }

    public void setActAmt(String actAmt) {
        this.actAmt = actAmt;
    }

    public String getRcpCode() {
        return rcpCode;
    }

    public void setRcpCode(String rcpCode) {
        this.rcpCode = rcpCode;
    }
}
