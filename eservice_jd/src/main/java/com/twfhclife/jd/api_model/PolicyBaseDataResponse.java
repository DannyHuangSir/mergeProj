package com.twfhclife.jd.api_model;

import com.twfhclife.jd.web.model.PolicyBaseVo;

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
