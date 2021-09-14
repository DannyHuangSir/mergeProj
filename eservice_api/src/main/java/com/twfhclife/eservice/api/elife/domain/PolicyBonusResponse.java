package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyBonusVo;

public class PolicyBonusResponse {

	private List<PolicyBonusVo> policyBonusVoList;

	public List<PolicyBonusVo> getPolicyBonusVoList() {
		return policyBonusVoList;
	}

	public void setPolicyBonusVoList(List<PolicyBonusVo> policyBonusVoList) {
		this.policyBonusVoList = policyBonusVoList;
	}
	
}