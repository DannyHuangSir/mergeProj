package com.twfhclife.eservice.onlineChange.model;

public class TransCashPaymentVo extends AbstractOnlineChangeModelBean {

    private Long id;

    private String transNum;

    private String policyNo;

    private String allocation;

    private String preAllocation;

    private String authType;

    private String userPassword;

    private static final long serialVersionUID = 1L;

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

    public String getAllocation() {
        return allocation;
    }

    public void setAllocation(String allocation) {
        this.allocation = allocation;
    }

    public String getPreAllocation() {
        return preAllocation;
    }

    public void setPreAllocation(String preAllocation) {
        this.preAllocation = preAllocation;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}