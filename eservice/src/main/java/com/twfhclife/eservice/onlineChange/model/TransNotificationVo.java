package com.twfhclife.eservice.onlineChange.model;

import com.google.common.collect.Lists;

import java.util.List;

public class TransNotificationVo {

    private String policyNo;

    private List<String> invtNos = Lists.newArrayList();

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public List<String> getInvtNos() {
        return invtNos;
    }

    public void setInvtNos(List<String> invtNos) {
        this.invtNos = invtNos;
    }
}
