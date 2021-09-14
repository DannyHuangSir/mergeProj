package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyListVo;

public class OnlineChangePolicyListResponse {

	private List<PolicyListVo> userPolicyList;

	public List<PolicyListVo> getUserPolicyList() {
		return userPolicyList;
	}

	public void setUserPolicyList(List<PolicyListVo> userPolicyList) {
		this.userPolicyList = userPolicyList;
	}
}