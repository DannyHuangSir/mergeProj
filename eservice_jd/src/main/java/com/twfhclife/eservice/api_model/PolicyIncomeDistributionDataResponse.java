package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.IncomeDistributionVo;
import com.twfhclife.eservice.web.model.PolicyBaseVo;

import java.io.Serializable;
import java.util.List;

public class PolicyIncomeDistributionDataResponse implements Serializable {

    private PolicyBaseVo policyBase;

    private List<IncomeDistributionVo> incomeDistributions = Lists.newArrayList();

    public List<IncomeDistributionVo> getIncomeDistributions() {
        return incomeDistributions;
    }

    public void setIncomeDistributions(List<IncomeDistributionVo> incomeDistributions) {
        this.incomeDistributions = incomeDistributions;
    }

    public PolicyBaseVo getPolicyBase() {
        return policyBase;
    }

    public void setPolicyBase(PolicyBaseVo policyBase) {
        this.policyBase = policyBase;
    }
}
