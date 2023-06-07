package com.twfhclife.generic.api_model;

import java.util.List;

public class PolicyDetailRequest {
	
	private String rocId;
	
	private List<String> policyInvestmentType;

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public List<String> getPolicyInvestmentType() {
		return policyInvestmentType;
	}

	public void setPolicyInvestmentType(List<String> policyInvestmentType) {
		this.policyInvestmentType = policyInvestmentType;
	}
	
}
