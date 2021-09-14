package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPayerVo;

/**
 * 付款人服務.
 * 
 * @author all
 */
public interface IPolicyPayerService {
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyPayerVo PolicyPayerVo
	 * @return 回傳查詢結果
	 */
	public List<PolicyPayerVo> getPolicyPayerList(PolicyPayerVo policyPayerVo);
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public PolicyPayerVo getPolicyPayer(String policyNo);
}