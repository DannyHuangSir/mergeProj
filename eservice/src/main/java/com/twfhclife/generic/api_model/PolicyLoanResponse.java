package com.twfhclife.generic.api_model;

import java.io.Serializable;

import com.twfhclife.eservice.policy.model.PolicyExtraVo;

public class PolicyLoanResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private PolicyExtraVo policyExtraVo;

	public PolicyExtraVo getPolicyExtraVo() {
		return policyExtraVo;
	}

	public void setPolicyExtraVo(PolicyExtraVo policyExtraVo) {
		this.policyExtraVo = policyExtraVo;
	}
}