package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;

public class UserPolicyAcctValueResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<PolicyAccountValueVo> policyAccountValueList;

	public List<PolicyAccountValueVo> getPolicyAccountValueList() {
		return policyAccountValueList;
	}

	public void setPolicyAccountValueList(List<PolicyAccountValueVo> policyAccountValueList) {
		this.policyAccountValueList = policyAccountValueList;
	}
}