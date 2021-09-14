package com.twfhclife.eservice.policy.service;

import com.twfhclife.eservice.policy.model.PolicyPaidVo;
import com.twfhclife.eservice.policy.model.PolicyReversalVo;

import java.util.List;

/**
 * 保單給付服務.
 * 
 * @author all
 */
public interface IPolicyReversalService {

	/**
	 * 撥回-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public List<PolicyReversalVo> findByPolicyNo(String policyNo);
	
}