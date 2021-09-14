package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;

public class PolicyPaymentRecordResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

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