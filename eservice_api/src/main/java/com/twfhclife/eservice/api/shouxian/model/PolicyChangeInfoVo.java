package com.twfhclife.eservice.api.shouxian.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyChangeInfoVo extends PolicyVo {

    private List<ChangeInfoVo> changeInfos = Lists.newArrayList();

    public List<ChangeInfoVo> getChangeInfos() {
        return changeInfos;
    }

    public void setChangeInfos(List<ChangeInfoVo> changeInfos) {
        this.changeInfos = changeInfos;
    }
}
