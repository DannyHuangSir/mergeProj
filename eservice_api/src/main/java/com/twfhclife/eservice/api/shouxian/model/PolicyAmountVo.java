package com.twfhclife.eservice.api.shouxian.model;

import java.math.BigDecimal;

public class PolicyAmountVo {

    private String policyDate;
    private BigDecimal policyVal;

    public String getPolicyDate() {
        return policyDate;
    }

    public void setPolicyDate(String policyDate) {
        this.policyDate = policyDate;
    }

    public BigDecimal getPolicyVal() {
        return policyVal;
    }

    public void setPolicyVal(BigDecimal policyVal) {
        this.policyVal = policyVal;
    }
}
