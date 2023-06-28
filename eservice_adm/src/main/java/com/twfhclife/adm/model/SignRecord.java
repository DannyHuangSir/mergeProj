package com.twfhclife.adm.model;

import java.util.Date;

public class SignRecord {

    private Date signStart;
    private Date signEnd;
    private String verifyCode;
    private String verifyMsg;
    private String actionId;
    private String signFileId;
    private String idVerifyStatus;
    private Date idVerifyTime;
    private String idVerifyType;
    private String signStatus;
    private Date signTime;
    private String signDownload;
    private String transNum;
    private String seNo;

    public String getSeNo() {
        return seNo;
    }

    public void setSeNo(String seNo) {
        this.seNo = seNo;
    }

    public String getIdVerifyType() {
        return idVerifyType;
    }

    public void setIdVerifyType(String idVerifyType) {
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

    public String getSignFileId() {
        return signFileId;
    }

    public void setSignFileId(String signFileId) {
        this.signFileId = signFileId;
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
