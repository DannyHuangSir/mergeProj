package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyPremiumVo;

import java.io.Serializable;

public class PolicyPremiumDataResponse implements Serializable {

    private PolicyPremiumVo policyPremium;

    public PolicyPremiumVo getPolicyPremium() {
        return policyPremium;
    }

    public void setPolicyPremium(PolicyPremiumVo policyPremium) {
        this.policyPremium = policyPremium;
    }
}
