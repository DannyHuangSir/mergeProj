package com.twfhclife.eservice.api.shouxian.model;

public class PaymentRecordVo {

    private String rcpDate;
    private String desc;
    private String paabAmt;
    private String actAmt;
    private String rcpCode;

    private Integer premYear;
    private String prpaPrem;
    private String prpaPrem2;

    public String getPrpaPrem2() {
        return prpaPrem2;
    }

    public void setPrpaPrem2(String prpaPrem2) {
        this.prpaPrem2 = prpaPrem2;
    }

    public String getPrpaPrem() {
        return prpaPrem;
    }

    public void setPrpaPrem(String prpaPrem) {
        this.prpaPrem = prpaPrem;
    }

    public Integer getPremYear() {
        return premYear;
    }

    public void setPremYear(Integer premYear) {
        this.premYear = premYear;
    }

    private String paymentCode;

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

    public String getPaymentCode() { return paymentCode; }

    public void setPaymentCode(String paymentCode) { this.paymentCode = paymentCode; }
}
