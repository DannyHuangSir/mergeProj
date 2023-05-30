package com.twfhclife.eservice.web.model;

public class BxczState {

    public BxczState() {
    }

    public BxczState(String actionId) {
        this.actionId = actionId;
    }

    public BxczState(String actionId, String transNum) {
        this.actionId = actionId;
        this.transNum = transNum;
    }

    private String actionId;
    private String transNum;

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
}
