package com.twfhclife.eservice.api.shouxian.model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.elife.domain.AbstractBasePolicyNoDomain;

import java.util.List;

public class PolicyBaseVo extends AbstractBasePolicyNoDomain {

    private String policyType;
    private String lipmName;
    private String lipmId;
    private String lipiName;
    private String lipmTelH;
    private String lipiId;
    private String agent;
    private String status;
    private String effectiveDate;
    private String mainAmount;
    private String lipiTablPrem;
    private String paymentMode;
    private String premYear;
    private String prodMode;

    private String lipmPhone;

    private String lipmMail;
    private String lipmAddr;
    private String lipiBirth;

    private String lipmBirth;
    private String lipiAge;
    private String lipiSex;

    private String prodName;

    private String paymentMethod;
    private String beginDate;
    private String endDate;

    private String pmdaEpoMk;
    private String pmdaPiTel;

    private String currency;
    private String printDate;
    private String assnDate;
    private String pmdaMail;
    private String assnTrDate;
    private String assnStatus;
    private String lipmGp;
    private String autoMk;

    private String branchName;
    private String charAt1;
    private String charAt2;
    private String bankCode;

    private String riskLevel;

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAutoMk() {
        return autoMk;
    }

    public void setAutoMk(String autoMk) {
        this.autoMk = autoMk;
    }

    public String getLipmGp() {
        return lipmGp;
    }

    public void setLipmGp(String lipmGp) {
        this.lipmGp = lipmGp;
    }

    public String getAssnDate() {
        return assnDate;
    }

    public void setAssnDate(String assnDate) {
        this.assnDate = assnDate;
    }

    public String getPmdaMail() {
        return pmdaMail;
    }

    public void setPmdaMail(String pmdaMail) {
        this.pmdaMail = pmdaMail;
    }

    public String getAssnTrDate() {
        return assnTrDate;
    }

    public void setAssnTrDate(String assnTrDate) {
        this.assnTrDate = assnTrDate;
    }

    public String getAssnStatus() {
        return assnStatus;
    }

    public void setAssnStatus(String assnStatus) {
        this.assnStatus = assnStatus;
    }

    private List<PolicyBeneficiaryVo> beneficiaries = Lists.newArrayList();

    private List<String> serialNums = Lists.newArrayList();


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

    public String getLipmTelH() {
        return lipmTelH;
    }

    public void setLipmTelH(String lipmTelH) {
        this.lipmTelH = lipmTelH;
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

    public String getPmdaPiTel() {
        return pmdaPiTel;
    }

    public void setPmdaPiTel(String pmdaPiTel) {
        this.pmdaPiTel = pmdaPiTel;
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

    public List<String> getSerialNums() {
        return serialNums;
    }

    public void setSerialNums(List<String> serialNums) {
        this.serialNums = serialNums;
    }

    public List<PolicyBeneficiaryVo> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<PolicyBeneficiaryVo> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public String getLipmPhone() {
        return lipmPhone;
    }

    public void setLipmPhone(String lipmPhone) {
        this.lipmPhone = lipmPhone;
    }

    public String getLipmMail() {
        return lipmMail;
    }

    public void setLipmMail(String lipmMail) {
        this.lipmMail = lipmMail;
    }

    public String getLipmAddr() {
        return lipmAddr;
    }

    public void setLipmAddr(String lipmAddr) {
        this.lipmAddr = lipmAddr;
    }

    public String getLipiBirth() {
        return lipiBirth;
    }

    public void setLipiBirth(String lipiBirth) {
        this.lipiBirth = lipiBirth;
    }

    public String getLipiAge() {
        return lipiAge;
    }

    public void setLipiAge(String lipiAge) {
        this.lipiAge = lipiAge;
    }

    public String getLipiSex() {
        return lipiSex;
    }

    public void setLipiSex(String lipiSex) {
        this.lipiSex = lipiSex;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPmdaEpoMk() {
        return pmdaEpoMk;
    }

    public void setPmdaEpoMk(String pmdaEpoMk) {
        this.pmdaEpoMk = pmdaEpoMk;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getLipmBirth() {
        return lipmBirth;
    }

    public void setLipmBirth(String lipmBirth) {
        this.lipmBirth = lipmBirth;
    }

    public String getCharAt1() {
        return charAt1;
    }

    public void setCharAt1(String charAt1) {
        this.charAt1 = charAt1;
    }

    public String getCharAt2() {
        return charAt2;
    }

    public void setCharAt2(String charAt2) {
        this.charAt2 = charAt2;
    }

    public PolicyBaseVo() {
    }
}
