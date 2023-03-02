package com.twfhclife.eservice.api.shouxian.model;

import com.google.common.collect.Lists;
import java.util.List;

public class PolicyIncomeDistributionVo {

    private PolicyVo policy;
    private List<IncomeDistributionVo> incomeDistributions = Lists.newArrayList();

    public PolicyVo getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyVo policy) {
        this.policy = policy;
    }

    public List<IncomeDistributionVo> getIncomeDistributions() {
        return incomeDistributions;
    }

    public void setIncomeDistributions(List<IncomeDistributionVo> incomeDistributions) {
        this.incomeDistributions = incomeDistributions;
    }
}
