package com.twfhclife.eservice.web.model;

import java.io.Serializable;

public class CaseVo  implements Serializable {

    private String policyNo;
    private String policyType;
    private String appName;
    private String insName;
    private String tBSubmitDate;
    private String bPMCurrentTak;
    private String noteStatus;
    private String noteVerifyResult;

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
        return tBSubmitDate;
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
}
