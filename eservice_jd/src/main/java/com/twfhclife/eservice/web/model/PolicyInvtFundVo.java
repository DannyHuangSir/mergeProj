package com.twfhclife.eservice.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyInvtFundVo {

    private PolicyVo policy;
    private List<InvtFundVo> funds = Lists.newArrayList();

    public PolicyVo getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyVo policy) {
        this.policy = policy;
    }

    public List<InvtFundVo> getFunds() {
        return funds;
    }

    public void setFunds(List<InvtFundVo> funds) {
        this.funds = funds;
    }
}
