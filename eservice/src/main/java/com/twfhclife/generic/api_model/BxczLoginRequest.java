package com.twfhclife.generic.api_model;

import java.io.Serializable;

public class BxczLoginRequest implements Serializable {

    private String actionId;
    private String grant_type;
    private String code;
    private String redirect_uri;

    public BxczLoginRequest(String actionId, String grant_type, String code, String redirect_uri) {
        this.actionId = actionId;
        this.grant_type = grant_type;
        this.code = code;
        this.redirect_uri = redirect_uri;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
