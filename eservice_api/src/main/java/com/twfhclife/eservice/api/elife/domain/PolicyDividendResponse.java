package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyDividendVo;

public class PolicyDividendResponse {

	private List<PolicyDividendVo> policyDividendList;

	public List<PolicyDividendVo> getPolicyDividendList() {
		return policyDividendList;
	}

	public void setPolicyDividendList(List<PolicyDividendVo> policyDividendList) {
		this.policyDividendList = policyDividendList;
	}
}