package com.twfhclife.eservice.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyPremiumVo extends PolicyVo {

    public List<PremiumVo> getPremiums() {
        return premiums;
    }

    public void setPremiums(List<PremiumVo> premiums) {
        this.premiums = premiums;
    }

    private List<PremiumVo> premiums = Lists.newArrayList();

}
