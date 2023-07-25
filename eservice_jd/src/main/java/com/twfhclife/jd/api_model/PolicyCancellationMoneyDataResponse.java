package com.twfhclife.jd.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.jd.web.model.CancellationMoneyVo;
import com.twfhclife.jd.web.model.PolicyAmountVo;
import com.twfhclife.jd.web.model.PolicyBaseVo;

import java.util.List;

public class PolicyCancellationMoneyDataResponse {

    private List cancellationMoneyVos = Lists.newArrayList();

    private PolicyBaseVo policyVo;
    private PolicyAmountVo policyAmountVo;

    private Boolean invest = false;

    public Boolean getInvest() {
        return invest;
    }

    public void setInvest(Boolean invest) {
        this.invest = invest;
    }

    public List getCancellationMoneyVos() {
        return cancellationMoneyVos;
    }

    public void setCancellationMoneyVos(List cancellationMoneyVos) {
        this.cancellationMoneyVos = cancellationMoneyVos;
    }

    public PolicyBaseVo getPolicyVo() {
        return policyVo;
    }

    public void setPolicyVo(PolicyBaseVo policyVo) {
        this.policyVo = policyVo;
    }

    public PolicyAmountVo getPolicyAmountVo() {
        return policyAmountVo;
    }

    public void setPolicyAmountVo(PolicyAmountVo policyAmountVo) {
        this.policyAmountVo = policyAmountVo;
    }
}
