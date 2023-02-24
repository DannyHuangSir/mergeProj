package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyBaseVo;

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
