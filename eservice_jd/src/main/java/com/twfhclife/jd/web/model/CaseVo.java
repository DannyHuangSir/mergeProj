package com.twfhclife.jd.web.model;

import com.twfhclife.jd.util.DateUtil;

import java.io.Serializable;

public class CaseVo implements Serializable {

    private String mainKey;
    private String policyNo;
    private String policyType;
    private String appName;
    private String appId;
    private String paymentMode;
    private String appBirth;
    private String insName;
    private String insId;
    private String insBirth;
    private String tBSubmitDate;
    private String bPMCurrentTak;
    private String noteStatus;
    private String noteVerifyResult;
    private String printDate;
    private String policyActiveDate;
    private String transPayDate;
    private String manager;
    private String stampResult;
    private String reviewResult;
    private String applyDate;
    private String accDocNo;
    private String payType;
    private String policyAmountNTD;
    private String policyAmount;
    private String prmeNewOldSW;
    private String pSalesName;

    public String getMainKey() {
        return mainKey;
    }

    public void setMainKey(String mainKey) {
        this.mainKey = mainKey;
    }

    public String getPrmeNewOldSW() {
        return prmeNewOldSW;
    }

    public void setPrmeNewOldSW(String prmeNewOldSW) {
        this.prmeNewOldSW = prmeNewOldSW;
    }


    public String getPolicyAmountNTD() {
        return policyAmountNTD;
    }

    public void setPolicyAmountNTD(String policyAmountNTD) {
        this.policyAmountNTD = policyAmountNTD;
    }

    public String getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(String policyAmount) {
        this.policyAmount = policyAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAppBirth() {
        return DateUtil.westToTwDate(appBirth);
    }

    public void setAppBirth(String appBirth) {
        this.appBirth = appBirth;
    }

    public String getInsBirth() {
        return DateUtil.westToTwDate(insBirth);
    }

    public void setInsBirth(String insBirth) {
        this.insBirth = insBirth;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public String gettBSubmitDate() {
        return DateUtil.westToTwDate(tBSubmitDate);
    }

    public void settBSubmitDate(String tBSubmitDate) {
        this.tBSubmitDate = tBSubmitDate;
    }

    public String getbPMCurrentTak() {
        return bPMCurrentTak;
    }

    public void setbPMCurrentTak(String bPMCurrentTak) {
        this.bPMCurrentTak = bPMCurrentTak;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }

    public String getNoteVerifyResult() {
        return noteVerifyResult;
    }

    public void setNoteVerifyResult(String noteVerifyResult) {
        this.noteVerifyResult = noteVerifyResult;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getPrintDate() {
        return DateUtil.westToTwDate(printDate);
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getPolicyActiveDate() {
        return DateUtil.westToTwDate(policyActiveDate);
    }

    public void setPolicyActiveDate(String policyActiveDate) {
        this.policyActiveDate = policyActiveDate;
    }

    public String getTransPayDate() {
        return DateUtil.westToTwDate(transPayDate);
    }

    public void setTransPayDate(String transPayDate) {
        this.transPayDate = transPayDate;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getStampResult() {
        return stampResult;
    }

    public void setStampResult(String stampResult) {
        this.stampResult = stampResult;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getApplyDate() {
        return DateUtil.westToTwDate(applyDate);
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getAccDocNo() {
        return accDocNo;
    }

    public void setAccDocNo(String accDocNo) {
        this.accDocNo = accDocNo;
    }

    public String getpSalesName() {
        return pSalesName;
    }

    public void setpSalesName(String pSalesName) {
        this.pSalesName = pSalesName;
    }
}
