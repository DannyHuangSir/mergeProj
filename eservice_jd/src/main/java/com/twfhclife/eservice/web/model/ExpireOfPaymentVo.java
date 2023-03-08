package com.twfhclife.eservice.web.model;

public class ExpireOfPaymentVo {


    private String payReas;
    private String payAmount;
    private String bookDate;
    private String payMethod;
    private String payName;
    private String bank;

    private String remitAt;
    private String bankCode;

    public String getPayReas() {
        return payReas;
    }

    public void setPayReas(String payReas) {
        this.payReas = payReas;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getRemitAt() {
        return remitAt;
    }

    public void setRemitAt(String remitAt) {
        this.remitAt = remitAt;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
