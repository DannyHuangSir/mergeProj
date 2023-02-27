package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyExpireOfPaymentVo;

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
