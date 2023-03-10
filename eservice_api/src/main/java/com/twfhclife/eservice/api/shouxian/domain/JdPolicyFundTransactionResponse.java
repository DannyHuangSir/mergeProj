package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.JdFundTransactionVo;

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