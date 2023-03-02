package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.FundTransactionVo;

import java.util.List;

public class PolicyFundTransactionResponse {

	private List<FundTransactionVo> fundTransactionList;

	public List<FundTransactionVo> getFundTransactionList() {
		return fundTransactionList;
	}

	public void setFundTransactionList(List<FundTransactionVo> fundTransactionList) {
		this.fundTransactionList = fundTransactionList;
	}
}