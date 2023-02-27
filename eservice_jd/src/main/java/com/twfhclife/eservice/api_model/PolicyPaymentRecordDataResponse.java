package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyPaymentRecordVo;

import java.io.Serializable;

public class PolicyPaymentRecordDataResponse implements Serializable {

    private PolicyPaymentRecordVo policyPaymentRecord;

    public PolicyPaymentRecordVo getPolicyPaymentRecord() {
        return policyPaymentRecord;
    }

    public void setPolicyPaymentRecord(PolicyPaymentRecordVo policyPaymentRecord) {
        this.policyPaymentRecord = policyPaymentRecord;
    }
}
