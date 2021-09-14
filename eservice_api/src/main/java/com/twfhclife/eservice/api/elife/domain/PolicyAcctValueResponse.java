package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;

public class PolicyAcctValueResponse {

	private List<PolicyAccountValueVo> policyAccountValueList;

	public List<PolicyAccountValueVo> getPolicyAccountValueList() {
		return policyAccountValueList;
	}

	public void setPolicyAccountValueList(List<PolicyAccountValueVo> policyAccountValueList) {
		this.policyAccountValueList = policyAccountValueList;
	}
}