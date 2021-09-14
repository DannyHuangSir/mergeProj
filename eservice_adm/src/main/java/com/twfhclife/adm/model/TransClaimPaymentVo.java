package com.twfhclife.adm.model;

public class TransClaimPaymentVo extends TransVo{

    private String chainStatus;

    private String receivePaper;

    public String getChainStatus() {
        return chainStatus;
    }

    public void setChainStatus(String chainStatus) {
        this.chainStatus = chainStatus;
    }

    public String getReceivePaper() {
        return receivePaper;
    }

    public void setReceivePaper(String receivePaper) {
        this.receivePaper = receivePaper;
    }
}
