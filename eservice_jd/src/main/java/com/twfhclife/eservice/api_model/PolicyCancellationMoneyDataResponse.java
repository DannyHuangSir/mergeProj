package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.CancellationMoneyVo;
import com.twfhclife.eservice.web.model.PolicyAmountVo;
import com.twfhclife.eservice.web.model.PolicyVo;

import java.util.List;

public class PolicyCancellationMoneyDataResponse {

    private List<CancellationMoneyVo> cancellationMoneyVos = Lists.newArrayList();

    private PolicyVo policyVo;
    private PolicyAmountVo policyAmountVo;

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

    public PolicyAmountVo getPolicyAmountVo() {
        return policyAmountVo;
    }

    public void setPolicyAmountVo(PolicyAmountVo policyAmountVo) {
        this.policyAmountVo = policyAmountVo;
    }
}
