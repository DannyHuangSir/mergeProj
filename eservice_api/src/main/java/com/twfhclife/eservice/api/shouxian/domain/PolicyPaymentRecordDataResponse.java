package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyPaymentRecordVo;
import com.twfhclife.eservice.api.shouxian.model.PolicySafeGuardVo;

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
