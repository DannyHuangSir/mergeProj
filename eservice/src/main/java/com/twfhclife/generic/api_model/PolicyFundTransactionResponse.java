package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.FundTransactionVo;

public class PolicyFundTransactionResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<FundTransactionVo> fundTransactionList;

	public List<FundTransactionVo> getFundTransactionList() {
		return fundTransactionList;
	}

	public void setFundTransactionList(List<FundTransactionVo> fundTransactionList) {
		this.fundTransactionList = fundTransactionList;
	}
}