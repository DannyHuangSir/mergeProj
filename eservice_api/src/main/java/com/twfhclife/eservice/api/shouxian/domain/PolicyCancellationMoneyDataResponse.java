package com.twfhclife.eservice.api.shouxian.domain;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.shouxian.model.CancellationMoneyVo;
import com.twfhclife.eservice.api.shouxian.model.PolicyVo;

import java.util.List;

public class PolicyCancellationMoneyDataResponse {

    private List<CancellationMoneyVo> cancellationMoneyVos = Lists.newArrayList();

    private PolicyVo policyVo;

    public List<CancellationMoneyVo> getCancellationMoneyVos() {
        return cancellationMoneyVos;
    }

    public void setCancellationMoneyVos(List<CancellationMoneyVo> cancellationMoneyVos) {
        this.cancellationMoneyVos = cancellationMoneyVos;
    }

    public PolicyVo getPolicyVo() {
        return policyVo;
    }

    public void setPolicyVo(PolicyVo policyVo) {
        this.policyVo = policyVo;
    }
}
