package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyBaseVo;

import java.io.Serializable;

public class PolicyBaseDataResponse implements Serializable {

    private PolicyBaseVo policy;

    public PolicyBaseVo getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyBaseVo policy) {
        this.policy = policy;
    }
}
