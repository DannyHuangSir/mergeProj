package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.web.model.PolicyPayerVo;

import java.util.List;

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