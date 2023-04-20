package com.twfhclife.eservice.api_model;


import com.twfhclife.eservice.web.model.PolicyCountVo;
import com.twfhclife.eservice.web.model.PolicyVo;

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
