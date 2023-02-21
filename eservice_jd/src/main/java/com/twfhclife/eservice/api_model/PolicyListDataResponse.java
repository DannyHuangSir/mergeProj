package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.PolicyVo;

import java.io.Serializable;
import java.util.List;

public class PolicyListDataResponse implements Serializable {

    private List<PolicyVo> policyList = Lists.newArrayList();

    public List<PolicyVo> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<PolicyVo> policyList) {
        this.policyList = policyList;
    }
}
