package com.twfhclife.eservice.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyVo {

    private String policyNo;
    private String policyType;
    private String lipmName;
    private String lipmId;
    private String lipiName;
    private String lipiId;
    private String agent;
    private String status;
    private String effectiveDate;
    private String mainAmount;
    private String lipiTablPrem;
    private String paymentMode;
    private String premYear;
    private String prodMode;

    private String currency;

    private String prodName;

    private String paymentMethod;

    private String endDate;

    private List<PermQueryVo> permQuery = Lists.newArrayList();

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getLipmName() {
        return lipmName;
    }

    public void setLipmName(String lipmName) {
        this.lipmName = lipmName;
    }

    public String getLipmId() {
        return lipmId;
    }

    public void setLipmId(String lipmId) {
        this.lipmId = lipmId;
    }

    public String getLipiName() {
        return lipiName;
    }

    public void setLipiName(String lipiName) {
        this.lipiName = lipiName;
    }

    public String getLipiId() {
        return lipiId;
    }

    public void setLipiId(String lipiId) {
        this.lipiId = lipiId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getMainAmount() {
        return mainAmount;
    }

    public void setMainAmount(String mainAmount) {
        this.mainAmount = mainAmount;
    }

    public String getLipiTablPrem() {
        return lipiTablPrem;
    }

    public void setLipiTablPrem(String lipiTablPrem) {
        this.lipiTablPrem = lipiTablPrem;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPremYear() {
        return premYear;
    }

    public void setPremYear(String premYear) {
        this.premYear = premYear;
    }

    public String getProdMode() {
        return prodMode;
    }

    public void setProdMode(String prodMode) {
        this.prodMode = prodMode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public List<PermQueryVo> getPermQuery() {
        return permQuery;
    }

    public void setPermQuery(List<PermQueryVo> permQuery) {
        this.permQuery = permQuery;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
