package com.twfhclife.eservice.onlineChange.model;

import java.util.Date;

public class SignRecord {

    private Date signStart;
    private Date signEnd;

    private String verifyCode;
    private String verifyMsg;

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
}
