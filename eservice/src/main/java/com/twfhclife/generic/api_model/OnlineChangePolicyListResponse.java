package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyListVo;

public class OnlineChangePolicyListResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<PolicyListVo> userPolicyList;

	public List<PolicyListVo> getUserPolicyList() {
		return userPolicyList;
	}

	public void setUserPolicyList(List<PolicyListVo> userPolicyList) {
		this.userPolicyList = userPolicyList;
	}
}