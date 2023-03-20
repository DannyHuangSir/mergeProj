package com.twfhclife.generic.model;

import com.twfhclife.adm.model.JdPolicyClaimDetailVo;

import java.io.Serializable;
import java.util.List;

/**
 * @auther lihao
 */
public class PolicyClaimDetailResponse implements Serializable {
    private List<JdPolicyClaimDetailVo> policyClaimDetailVo;


    public List<JdPolicyClaimDetailVo> getPolicyClaimDetailVo() {
        return policyClaimDetailVo;
    }

    public void setPolicyClaimDetailVo(List<JdPolicyClaimDetailVo> policyClaimDetailVo) {
        this.policyClaimDetailVo = policyClaimDetailVo;
    }
}
