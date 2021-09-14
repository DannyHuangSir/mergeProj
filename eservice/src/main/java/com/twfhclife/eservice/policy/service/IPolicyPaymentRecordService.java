package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;

/**
 * 繳費服務.
 * 
 * @author all
 */
public interface IPolicyPaymentRecordService {
	
	/**
	 * 繳費-查詢.
	 * 
	 * @param policyPaymentRecordVo PolicyPaymentRecordVo
	 * @return 回傳查詢結果
	 */
	public List<PolicyPaymentRecordVo> getPolicyPaymentRecord(PolicyPaymentRecordVo policyPaymentRecordVo);
	
	/**
	 * 繳費(最近一年)-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public List<PolicyPaymentRecordVo> getPolicyPaymentRecordLastYear(String policyNo);
}