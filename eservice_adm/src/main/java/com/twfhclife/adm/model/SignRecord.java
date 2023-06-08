package com.twfhclife.adm.model;

import java.util.Date;

public class SignRecord {

    private Date signStart;
    private Date signEnd;
    private String verifyCode;
    private String verifyMsg;
    private String actionId;
    private String fileId;
    private String idVerifyStatus;
    private Date idVerifyTime;
    private Date idVerifyType;
    private String signStatus;
    private Date signTime;
    private String signDownload;
    private String transNum;

    public Date getIdVerifyType() {
        return idVerifyType;
    }

    public void setIdVerifyType(Date idVerifyType) {
        this.idVerifyType = idVerifyType;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getSignDownload() {
        return signDownload;
    }

    public void setSignDownload(String signDownload) {
        this.signDownload = signDownload;
    }

    public String getIdVerifyStatus() {
        return idVerifyStatus;
    }

    public void setIdVerifyStatus(String idVerifyStatus) {
        this.idVerifyStatus = idVerifyStatus;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public Date getSignStart() {
        return signStart;
    }

    public void setSignStart(Date signStart) {
        this.signStart = signStart;
    }

    public Date getSignEnd() {
        return signEnd;
    }

    public void setSignEnd(Date signEnd) {
        this.signEnd = signEnd;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Date getIdVerifyTime() {
        return idVerifyTime;
    }

    public void setIdVerifyTime(Date idVerifyTime) {
        this.idVerifyTime = idVerifyTime;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }
}
