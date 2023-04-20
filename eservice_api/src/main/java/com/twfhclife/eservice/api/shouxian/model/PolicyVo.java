package com.twfhclife.eservice.api.shouxian.model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.elife.domain.AbstractBasePolicyNoDomain;
import com.twfhclife.eservice.api.jdzq.model.PermQueryVo;

import java.util.List;

public class PolicyVo extends AbstractBasePolicyNoDomain {

    private String policyType;
    private String lipmName;
    private String lipmId;
    private String lipiName;
    private String lipiId;
    private String agent;
    private String stat;
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

    private int totalRow;

    private int pageSize;

    private int pageNum;

    private List<PermQueryVo> permQuery = Lists.newArrayList();

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

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
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

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
