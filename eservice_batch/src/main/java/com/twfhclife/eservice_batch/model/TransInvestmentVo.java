package com.twfhclife.eservice_batch.model;

public class TransInvestmentVo {

    private Long id;

    private String transNum;

    private String policyNo;

    private String invtNo;

    private Short distributionRatio;

    private Short preDistributionRatio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getInvtNo() {
        return invtNo;
    }

    public void setInvtNo(String invtNo) {
        this.invtNo = invtNo;
    }

    public Short getDistributionRatio() {
        return distributionRatio;
    }

    public void setDistributionRatio(Short distributionRatio) {
        this.distributionRatio = distributionRatio;
    }

    public Short getPreDistributionRatio() {
        return preDistributionRatio;
    }

    public void setPreDistributionRatio(Short preDistributionRatio) {
        this.preDistributionRatio = preDistributionRatio;
    }
}