package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyIncomeDistributionVo;

import java.io.Serializable;

public class PolicyIncomeDistributionDataResponse implements Serializable {

    public PolicyIncomeDistributionVo getIncomeDistribution() {
        return incomeDistribution;
    }

    public void setIncomeDistribution(PolicyIncomeDistributionVo incomeDistribution) {
        this.incomeDistribution = incomeDistribution;
    }

    private PolicyIncomeDistributionVo incomeDistribution;

}
