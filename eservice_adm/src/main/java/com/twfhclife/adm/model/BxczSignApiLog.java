package com.twfhclife.adm.model;

import java.util.Date;

public class BxczSignApiLog {

    private Long id;
    private String apiType;
    private String apiName;
    private String apiRespCode;
    private String apiRespMsg;
    private String actionId;
    private String transNum;
    private Date callStartTime;
    private Date callEndTime;

    public BxczSignApiLog(String apiType, String apiName, String apiRespCode, String apiRespMsg, String actionId, String transNum, Date callStartTime, Date callEndTime) {
        this.apiType = apiType;
        this.apiName = apiName;
        this.apiRespCode = apiRespCode;
        this.apiRespMsg = apiRespMsg;
        this.actionId = actionId;
        this.transNum = transNum;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
    }

    public BxczSignApiLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiRespCode() {
        return apiRespCode;
    }

    public void setApiRespCode(String apiRespCode) {
        this.apiRespCode = apiRespCode;
    }

    public String getApiRespMsg() {
        return apiRespMsg;
    }

    public void setApiRespMsg(String apiRespMsg) {
        this.apiRespMsg = apiRespMsg;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public Date getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Date getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Date callEndTime) {
        this.callEndTime = callEndTime;
    }
}
