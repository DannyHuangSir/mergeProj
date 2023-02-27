package com.twfhclife.eservice.api.shouxian.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyPaymentRecordVo extends PolicyVo {

    private List<PaymentRecordVo> paymentRecords = Lists.newArrayList();

    public List<PaymentRecordVo> getPaymentRecords() {
        return paymentRecords;
    }

    public void setPaymentRecords(List<PaymentRecordVo> paymentRecords) {
        this.paymentRecords = paymentRecords;
    }
}
