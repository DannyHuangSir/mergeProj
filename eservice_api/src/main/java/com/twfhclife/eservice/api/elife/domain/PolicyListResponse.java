package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyListVo;

public class PolicyListResponse {

	/**
	 * 投資型保單
	 */
	private List<PolicyListVo> invtPolicyList;

	/**
	 * 投資型保單
	 */
	private List<PolicyListVo> benefitPolicyList;

	public List<PolicyListVo> getInvtPolicyList() {
		return invtPolicyList;
	}

	public void setInvtPolicyList(List<PolicyListVo> invtPolicyList) {
		this.invtPolicyList = invtPolicyList;
	}

	public List<PolicyListVo> getBenefitPolicyList() {
		return benefitPolicyList;
	}

	public void setBenefitPolicyList(List<PolicyListVo> benefitPolicyList) {
		this.benefitPolicyList = benefitPolicyList;
	}
}