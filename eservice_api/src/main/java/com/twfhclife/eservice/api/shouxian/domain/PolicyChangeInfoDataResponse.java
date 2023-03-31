package com.twfhclife.eservice.api.shouxian.domain;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.shouxian.model.ChangeInfoVo;
import com.twfhclife.eservice.api.shouxian.model.PolicyBaseVo;

import java.io.Serializable;
import java.util.List;

public class PolicyChangeInfoDataResponse implements Serializable {

    private List<ChangeInfoVo> changeInfos = Lists.newArrayList();

    public List<ChangeInfoVo> getChangeInfos() {
        return changeInfos;
    }

    public void setChangeInfos(List<ChangeInfoVo> changeInfos) {
        this.changeInfos = changeInfos;
    }

    private PolicyBaseVo policyBase;

    public PolicyBaseVo getPolicyBase() {
        return policyBase;
    }

    public void setPolicyBase(PolicyBaseVo policyBase) {
        this.policyBase = policyBase;
    }
}
