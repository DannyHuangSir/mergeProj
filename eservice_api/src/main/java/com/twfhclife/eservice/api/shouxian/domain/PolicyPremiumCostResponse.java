package com.twfhclife.eservice.api.shouxian.domain;


import com.twfhclife.eservice.api.shouxian.model.FundPrdtVo;

import java.util.List;

public class PolicyPremiumCostResponse {

	private List<FundPrdtVo> premiumTransactionList;

	public List<FundPrdtVo> getPremiumTransactionList() {
		return premiumTransactionList;
	}

	public void setPremiumTransactionList(List<FundPrdtVo> premiumTransactionList) {
		this.premiumTransactionList = premiumTransactionList;
	}
}