package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPaidVo;

public class PolicyPaidResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<PolicyPaidVo> policyPaidVoList;

	public List<PolicyPaidVo> getPolicyPaidVoList() {
		return policyPaidVoList;
	}

	public void setPolicyPaidVoList(List<PolicyPaidVo> policyPaidVoList) {
		this.policyPaidVoList = policyPaidVoList;
	}
	
}