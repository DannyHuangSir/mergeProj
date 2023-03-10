package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.JdFundTransactionVo;

import java.util.List;

public class JdPolicyFundTransactionResponse {

	private List<JdFundTransactionVo> fundTransactionList;

	public List<JdFundTransactionVo> getFundTransactionList() {
		return fundTransactionList;
	}

	public void setFundTransactionList(List<JdFundTransactionVo> fundTransactionList) {
		this.fundTransactionList = fundTransactionList;
	}
}