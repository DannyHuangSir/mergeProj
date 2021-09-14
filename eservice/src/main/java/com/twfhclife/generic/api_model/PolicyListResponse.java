package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyListVo;

public class PolicyListResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

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