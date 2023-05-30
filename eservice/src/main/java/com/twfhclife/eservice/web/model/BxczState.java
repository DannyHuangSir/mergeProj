package com.twfhclife.eservice.web.model;

public class BxczState {

    public BxczState() {
    }

    public BxczState(String actionId) {
        this.actionId = actionId;
    }

    public BxczState(String actionId, String transNum, String type) {
        this.actionId = actionId;
        this.transNum = transNum;
        this.type = type;
    }

    private String actionId;
    private String transNum;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
