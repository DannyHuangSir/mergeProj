package com.twfhclife.eservice.onlineChange.model;

import java.util.Date;

public class SignRecord {

    private Date signStart;
    private Date signEnd;

    private String verifyCode;
    private String verifyMsg;
    private String actionId;
    private String fileId;

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
}
