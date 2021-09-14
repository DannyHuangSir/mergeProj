package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyBonusVo;

public class PolicyBonusResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<PolicyBonusVo> policyBonusVoList;

	public List<PolicyBonusVo> getPolicyBonusVoList() {
		return policyBonusVoList;
	}

	public void setPolicyBonusVoList(List<PolicyBonusVo> policyBonusVoList) {
		this.policyBonusVoList = policyBonusVoList;
	}

	
}