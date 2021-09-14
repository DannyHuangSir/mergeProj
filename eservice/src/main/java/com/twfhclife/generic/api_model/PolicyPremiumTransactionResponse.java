package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.FundPrdtVo;

public class PolicyPremiumTransactionResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<FundPrdtVo> premiumTransactionList;

	public List<FundPrdtVo> getPremiumTransactionList() {
		return premiumTransactionList;
	}

	public void setPremiumTransactionList(List<FundPrdtVo> premiumTransactionList) {
		this.premiumTransactionList = premiumTransactionList;
	}
}