package com.twfhclife.jd.web.model;

import java.io.Serializable;

public class NoteNotifyVo implements Serializable {

    private String policyNo;
    private String appName;
    private String insName;
    private String dueDate;
    private String agentCode;
    private String agentBranchM;
    private String pSalesCode;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getpSalesCode() {
        return pSalesCode;
    }

    public void setpSalesCode(String pSalesCode) {
        this.pSalesCode = pSalesCode;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentBranchM() {
        return agentBranchM;
    }

    public void setAgentBranchM(String agentBranchM) {
        this.agentBranchM = agentBranchM;
    }
}
