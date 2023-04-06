package com.twfhclife.eservice.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyInvtFundVo {

    private PolicyBaseVo policy;
    private List<InvtFundVo> funds = Lists.newArrayList();

    public PolicyBaseVo getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyBaseVo policy) {
        this.policy = policy;
    }

    public List<InvtFundVo> getFunds() {
        return funds;
    }

    public void setFunds(List<InvtFundVo> funds) {
        this.funds = funds;
    }
}
