package com.twfhclife.eservice.api.shouxian.model;

import com.google.common.collect.Lists;

import java.util.List;

public class PolicyExpireOfPaymentVo extends PolicyVo {


    private List<ExpireOfPaymentVo> payments = Lists.newArrayList();

    public List<ExpireOfPaymentVo> getPayments() {
        return payments;
    }

    public void setPayments(List<ExpireOfPaymentVo> payments) {
        this.payments = payments;
    }
}
