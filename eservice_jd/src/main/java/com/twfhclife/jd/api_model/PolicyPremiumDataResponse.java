package com.twfhclife.jd.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.jd.web.model.PolicyBaseVo;
import com.twfhclife.jd.web.model.PremiumVo;

import java.io.Serializable;
import java.util.List;

public class PolicyPremiumDataResponse implements Serializable {

    private PolicyBaseVo policyBase;

    public List<PremiumVo> getPremiums() {
        return premiums;
    }

    public void setPremiums(List<PremiumVo> premiums) {
        this.premiums = premiums;
    }

    private List<PremiumVo> premiums = Lists.newArrayList();

    public PolicyBaseVo getPolicyBase() {
        return policyBase;
    }

    public void setPolicyBase(PolicyBaseVo policyBase) {
        this.policyBase = policyBase;
    }
}
