package com.twfhclife.jd.api_model;


import com.twfhclife.jd.web.model.FundPrdtVo;

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