package com.twfhclife.eservice.api.jdzq.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class NotePdfVo implements Serializable {

    private String policyNo;
    private String appName;
    private String insName;
    private String agentCode;
    private String agentName;
    private String branchDesc;
    private String policyTypeName;
    private String manageDate;
    private String processorName;
    private String extNumber;
    private String accDocNo;
    private String agentSalesName;
    private String agentSalesID;

    private String dueDate;
    private String noteVerifyMemo;
    private List<NoteItemVo> noteItems = Lists.newArrayList();

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

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBranchDesc() {
        return branchDesc;
    }

    public void setBranchDesc(String branchDesc) {
        this.branchDesc = branchDesc;
    }

    public String getPolicyTypeName() {
        return policyTypeName;
    }

    public void setPolicyTypeName(String policyTypeName) {
        this.policyTypeName = policyTypeName;
    }

    public String getManageDate() {
        return manageDate;
    }

    public void setManageDate(String manageDate) {
        this.manageDate = manageDate;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public String getExtNumber() {
        return extNumber;
    }

    public void setExtNumber(String extNumber) {
        this.extNumber = extNumber;
    }

    public String getAccDocNo() {
        return accDocNo;
    }

    public void setAccDocNo(String accDocNo) {
        this.accDocNo = accDocNo;
    }

    public String getAgentSalesName() {
        return agentSalesName;
    }

    public void setAgentSalesName(String agentSalesName) {
        this.agentSalesName = agentSalesName;
    }

    public String getAgentSalesID() {
        return agentSalesID;
    }

    public void setAgentSalesID(String agentSalesID) {
        this.agentSalesID = agentSalesID;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNoteVerifyMemo() {
        return noteVerifyMemo;
    }

    public void setNoteVerifyMemo(String noteVerifyMemo) {
        this.noteVerifyMemo = noteVerifyMemo;
    }

    public List<NoteItemVo> getNoteItems() {
        return noteItems;
    }

    public void setNoteItems(List<NoteItemVo> noteItems) {
        this.noteItems = noteItems;
    }
}
