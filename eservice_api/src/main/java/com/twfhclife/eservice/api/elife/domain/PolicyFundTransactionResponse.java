package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.FundTransactionVo;

public class PolicyFundTransactionResponse {

	private List<FundTransactionVo> fundTransactionList;

	public List<FundTransactionVo> getFundTransactionList() {
		return fundTransactionList;
	}

	public void setFundTransactionList(List<FundTransactionVo> fundTransactionList) {
		this.fundTransactionList = fundTransactionList;
	}
}