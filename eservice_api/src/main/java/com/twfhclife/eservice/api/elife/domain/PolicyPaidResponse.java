package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPaidVo;

public class PolicyPaidResponse {

	private List<PolicyPaidVo> policyPaidVoList;

	public List<PolicyPaidVo> getPolicyPaidVoList() {
		return policyPaidVoList;
	}

	public void setPolicyPaidVoList(List<PolicyPaidVo> policyPaidVoList) {
		this.policyPaidVoList = policyPaidVoList;
	}
	
}