package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;

public class PolicyPaymentRecordResponse {

	/**
	 * 繳費紀錄
	 */
	private List<PolicyPaymentRecordVo> paymentRecordList;

	public List<PolicyPaymentRecordVo> getPaymentRecordList() {
		return paymentRecordList;
	}

	public void setPaymentRecordList(List<PolicyPaymentRecordVo> paymentRecordList) {
		this.paymentRecordList = paymentRecordList;
	}
}