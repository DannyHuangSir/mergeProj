package com.twfhclife.adm.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @auther lihao
 */
public class JdPolicyClaimReqVo implements Serializable {
    private String agentCode;

    private String branchCode;

    private String pSalesCode;

    private String policyNo;

    private String appName;

    private String appID;

    private String insName;

    private String insID;

    private String policyTypeName;

    private String tBSubmitDate;

    private String tBStartTime;

    private String tBEndTime;

    private String policyActiveDate;

    private String policyActiveStartTime;

    private String policyActiveEndTime;
    private String noteStatus;
    private String bpmcurrenttak;

    private String noteVerifyResult;

    private String depName;

    private List<String> column;

    private List<String>  columnName;

    private String categoryCode;

    private String categoryName;

    private String parameterName;

    private String parameterCode;

    private String brokerID;

    private String typeCode;

    private String accDocNo;

    private String caseReceviceDate;

    private String printDate;

    private String brokerName;

    private String agentSalesID;

    private String agentSalesName;

    private String accUnit;

    private String reiewResult;

    private String appCountry;

    private String appPolicyAge;

    private String insAge;

    private String policyAmountNTD;

    private String piTablPremTmsS;

    private String payType;

    private String permYear;

    private String nebkBranchCode;

    private String nebkBranchName;

    private String policyNoteContent;

    private String policyType;

    private String pTypeCode;

    private String pTypeName;

    private String manager;

    private String reviewResult;

    private String policyFee;

    private String userName;

    private String lipiAge;

    private String itemContent;

    private String noteDate;

    private String dueDate;

    private String noteKey;

    private String contentMemo;
    private List<String> selectColumn = Lists.newArrayList();
    private List<String> unSelectColumn = Lists.newArrayList();
    private List<String> selectColumnName = Lists.newArrayList();
    private List<String> unSelectColumnName = Lists.newArrayList();

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNoteKey() {
        return noteKey;
    }

    public void setNoteKey(String noteKey) {
        this.noteKey = noteKey;
    }

    public String getContentMemo() {
        return contentMemo;
    }

    public void setContentMemo(String contentMemo) {
        this.contentMemo = contentMemo;
    }


    public String getBranchCode() {
        return branchCode;
    }

    public String getpSalesCode() {
        return pSalesCode;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppID() {
        return appID;
    }

    public String getInsName() {
        return insName;
    }

    public String getInsID() {
        return insID;
    }

    public String getPolicyTypeName() {
        return policyTypeName;
    }

    public String gettBSubmitDate() {
        return tBSubmitDate;
    }

    public String gettBStartTime() {
        return tBStartTime;
    }

    public String gettBEndTime() {
        return tBEndTime;
    }

    public String getPolicyActiveDate() {
        return policyActiveDate;
    }

    public String getPolicyActiveStartTime() {
        return policyActiveStartTime;
    }

    public String getPolicyActiveEndTime() {
        return policyActiveEndTime;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public String getBpmcurrenttak() {
        return bpmcurrenttak;
    }

    public String getNoteVerifyResult() {
        return noteVerifyResult;
    }

    public String getDepName() {
        return depName;
    }

    public List<String> getColumn() {
        return column;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterCode() {
        return parameterCode;
    }

    public String getBrokerID() {
        return brokerID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getAccDocNo() {
        return accDocNo;
    }

    public String getCaseReceviceDate() {
        return caseReceviceDate;
    }

    public String getPrintDate() {
        return printDate;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public String getAgentSalesID() {
        return agentSalesID;
    }

    public String getAgentSalesName() {
        return agentSalesName;
    }

    public String getAccUnit() {
        return accUnit;
    }

    public String getReiewResult() {
        return reiewResult;
    }

    public String getAppCountry() {
        return appCountry;
    }

    public String getAppPolicyAge() {
        return appPolicyAge;
    }

    public String getInsAge() {
        return insAge;
    }

    public String getPolicyAmountNTD() {
        return policyAmountNTD;
    }

    public String getPiTablPremTmsS() {
        return piTablPremTmsS;
    }

    public String getPayType() {
        return payType;
    }

    public String getPermYear() {
        return permYear;
    }

    public String getNebkBranchCode() {
        return nebkBranchCode;
    }

    public String getNebkBranchName() {
        return nebkBranchName;
    }

    public String getPolicyNoteContent() {
        return policyNoteContent;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getpTypeCode() {
        return pTypeCode;
    }

    public String getpTypeName() {
        return pTypeName;
    }

    public String getManager() {
        return manager;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public String getPolicyFee() {
        return policyFee;
    }

    public String getUserName() {
        return userName;
    }

    public String getLipiAge() {
        return lipiAge;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setpSalesCode(String pSalesCode) {
        this.pSalesCode = pSalesCode;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public void setInsID(String insID) {
        this.insID = insID;
    }

    public void setPolicyTypeName(String policyTypeName) {
        this.policyTypeName = policyTypeName;
    }

    public void settBSubmitDate(String tBSubmitDate) {
        this.tBSubmitDate = tBSubmitDate;
    }

    public void settBStartTime(String tBStartTime) {
        this.tBStartTime = tBStartTime;
    }

    public void settBEndTime(String tBEndTime) {
        this.tBEndTime = tBEndTime;
    }

    public void setPolicyActiveDate(String policyActiveDate) {
        this.policyActiveDate = policyActiveDate;
    }

    public void setPolicyActiveStartTime(String policyActiveStartTime) {
        this.policyActiveStartTime = policyActiveStartTime;
    }

    public void setPolicyActiveEndTime(String policyActiveEndTime) {
        this.policyActiveEndTime = policyActiveEndTime;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }

    public void setBpmcurrenttak(String bpmcurrenttak) {
        this.bpmcurrenttak = bpmcurrenttak;
    }

    public void setNoteVerifyResult(String noteVerifyResult) {
        this.noteVerifyResult = noteVerifyResult;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setParameterCode(String parameterCode) {
        this.parameterCode = parameterCode;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setAccDocNo(String accDocNo) {
        this.accDocNo = accDocNo;
    }

    public void setCaseReceviceDate(String caseReceviceDate) {
        this.caseReceviceDate = caseReceviceDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public void setAgentSalesID(String agentSalesID) {
        this.agentSalesID = agentSalesID;
    }

    public void setAgentSalesName(String agentSalesName) {
        this.agentSalesName = agentSalesName;
    }

    public void setAccUnit(String accUnit) {
        this.accUnit = accUnit;
    }

    public void setReiewResult(String reiewResult) {
        this.reiewResult = reiewResult;
    }

    public void setAppCountry(String appCountry) {
        this.appCountry = appCountry;
    }

    public void setAppPolicyAge(String appPolicyAge) {
        this.appPolicyAge = appPolicyAge;
    }

    public void setInsAge(String insAge) {
        this.insAge = insAge;
    }

    public void setPolicyAmountNTD(String policyAmountNTD) {
        this.policyAmountNTD = policyAmountNTD;
    }

    public void setPiTablPremTmsS(String piTablPremTmsS) {
        this.piTablPremTmsS = piTablPremTmsS;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setPermYear(String permYear) {
        this.permYear = permYear;
    }

    public void setNebkBranchCode(String nebkBranchCode) {
        this.nebkBranchCode = nebkBranchCode;
    }

    public void setNebkBranchName(String nebkBranchName) {
        this.nebkBranchName = nebkBranchName;
    }

    public void setPolicyNoteContent(String policyNoteContent) {
        this.policyNoteContent = policyNoteContent;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public void setpTypeCode(String pTypeCode) {
        this.pTypeCode = pTypeCode;
    }

    public void setpTypeName(String pTypeName) {
        this.pTypeName = pTypeName;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public void setPolicyFee(String policyFee) {
        this.policyFee = policyFee;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLipiAge(String lipiAge) {
        this.lipiAge = lipiAge;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
    public String getAgentCode() {
        return agentCode;
    }

    public List<String> getSelectColumn() {
        return selectColumn;
    }

    public void setSelectColumn(List<String> selectColumn) {
        this.selectColumn = selectColumn;
    }

    public List<String> getUnSelectColumn() {
        return unSelectColumn;
    }

    public void setUnSelectColumn(List<String> unSelectColumn) {
        this.unSelectColumn = unSelectColumn;
    }

    public List<String> getSelectColumnName() {
        return selectColumnName;
    }

    public void setSelectColumnName(List<String> selectColumnName) {
        this.selectColumnName = selectColumnName;
    }

    public List<String> getUnSelectColumnName() {
        return unSelectColumnName;
    }

    public void setUnSelectColumnName(List<String> unSelectColumnName) {
        this.unSelectColumnName = unSelectColumnName;
    }
}
