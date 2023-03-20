package com.twfhclife.eservice.api.jdzq.domain;

import com.twfhclife.eservice.api.jdzq.model.PolicyClaimDetailVo;

import java.io.Serializable;
import java.util.List;

/**
 * @auther lihao
 */
public class PolicyClaimDetailResponse implements Serializable {

    private List<PolicyClaimDetailVo> policyClaimDetailVo;

    public void setPolicyClaimDetailVo(List<PolicyClaimDetailVo> policyClaimDetailVo) {
        this.policyClaimDetailVo = policyClaimDetailVo;
    }

    public List<PolicyClaimDetailVo> getPolicyClaimDetailVo() {
        return policyClaimDetailVo;
    }
}
