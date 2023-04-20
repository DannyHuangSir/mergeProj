package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyCountVo;
import com.twfhclife.eservice.api.shouxian.model.PolicyVo;
import com.twfhclife.generic.domain.PageResponseObj;

import java.io.Serializable;

public class PolicyListDataResponse extends PageResponseObj<PolicyVo> implements Serializable  {

    private PolicyCountVo policyCountVo;

    public PolicyCountVo getPolicyCountVo() {
        return policyCountVo;
    }

    public void setPolicyCountVo(PolicyCountVo policyCountVo) {
        this.policyCountVo = policyCountVo;
    }
}
