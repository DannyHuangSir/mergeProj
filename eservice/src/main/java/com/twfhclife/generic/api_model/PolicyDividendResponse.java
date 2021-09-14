package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyDividendVo;

public class PolicyDividendResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<PolicyDividendVo> policyDividendList;

	public List<PolicyDividendVo> getPolicyDividendList() {
		return policyDividendList;
	}

	public void setPolicyDividendList(List<PolicyDividendVo> policyDividendList) {
		this.policyDividendList = policyDividendList;
	}
}