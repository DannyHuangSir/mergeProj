package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyPremiumVo;

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
