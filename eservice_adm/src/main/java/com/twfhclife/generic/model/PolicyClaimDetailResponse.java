package com.twfhclife.generic.model;

import com.google.common.collect.Lists;
import com.twfhclife.adm.model.JdPolicyClaimDetailVo;

import java.io.Serializable;
import java.util.List;

/**
 * @auther lihao
 */
public class PolicyClaimDetailResponse implements Serializable {
    private List<JdPolicyClaimDetailVo> policyClaimDetailVo = Lists.newArrayList();


    public List<JdPolicyClaimDetailVo> getPolicyClaimDetailVo() {
        return policyClaimDetailVo;
    }

    public void setPolicyClaimDetailVo(List<JdPolicyClaimDetailVo> policyClaimDetailVo) {
        this.policyClaimDetailVo = policyClaimDetailVo;
    }
}
