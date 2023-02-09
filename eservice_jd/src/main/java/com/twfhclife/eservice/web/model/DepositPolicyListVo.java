package com.twfhclife.eservice.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class DepositPolicyListVo extends PolicyListVo {

    private List<PortfolioVo> portfolioVoList = Lists.newArrayList();

    public List<PortfolioVo> getPortfolioVoList() {
        return portfolioVoList;
    }

    public void setPortfolioVoList(List<PortfolioVo> portfolioVoList) {
        this.portfolioVoList = portfolioVoList;
    }
}
