package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.PaymentRecordVo;
import com.twfhclife.eservice.web.model.PolicyBaseVo;

import java.io.Serializable;
import java.util.List;

public class PolicyPaymentRecordDataResponse implements Serializable {

    private PolicyBaseVo policyBaseVo;
    private List<PaymentRecordVo> paymentRecords = Lists.newArrayList();

    public PolicyBaseVo getPolicyBaseVo() {
        return policyBaseVo;
    }

    public void setPolicyBaseVo(PolicyBaseVo policyBaseVo) {
        this.policyBaseVo = policyBaseVo;
    }

    public List<PaymentRecordVo> getPaymentRecords() {
        return paymentRecords;
    }

    public void setPaymentRecords(List<PaymentRecordVo> paymentRecords) {
        this.paymentRecords = paymentRecords;
    }
}
