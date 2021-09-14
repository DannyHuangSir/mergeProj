package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.FundPrdtVo;

public class PolicyPremiumTransactionResponse {

	private List<FundPrdtVo> premiumTransactionList;

	public List<FundPrdtVo> getPremiumTransactionList() {
		return premiumTransactionList;
	}

	public void setPremiumTransactionList(List<FundPrdtVo> premiumTransactionList) {
		this.premiumTransactionList = premiumTransactionList;
	}
}