package com.twfhclife.eservice.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicySafeGuardVo extends PolicyVo {

    private List<SafeGuardVo> safeGuards = Lists.newArrayList();

    public List<SafeGuardVo> getSafeGuards() {
        return safeGuards;
    }

    public void setSafeGuards(List<SafeGuardVo> safeGuards) {
        this.safeGuards = safeGuards;
    }
}
