package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyExpireOfPaymentVo;

import java.io.Serializable;

public class PolicyExpireOfPaymentDataResponse implements Serializable {

    private PolicyExpireOfPaymentVo policyExpireOfPayment;

    public PolicyExpireOfPaymentVo getPolicyExpireOfPayment() {
        return policyExpireOfPayment;
    }

    public void setPolicyExpireOfPayment(PolicyExpireOfPaymentVo policyExpireOfPayment) {
        this.policyExpireOfPayment = policyExpireOfPayment;
    }
}
