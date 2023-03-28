package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.ExpireOfPaymentVo;
import com.twfhclife.eservice.web.model.PolicyBaseVo;

import java.io.Serializable;
import java.util.List;

public class PolicyExpireOfPaymentDataResponse implements Serializable {

    private PolicyBaseVo policyBase;
    private List<ExpireOfPaymentVo> payments = Lists.newArrayList();

    public List<ExpireOfPaymentVo> getPayments() {
        return payments;
    }

    public void setPayments(List<ExpireOfPaymentVo> payments) {
        this.payments = payments;
    }

    public PolicyBaseVo getPolicyBase() {
        return policyBase;
    }

    public void setPolicyBase(PolicyBaseVo policyBase) {
        this.policyBase = policyBase;
    }
}
