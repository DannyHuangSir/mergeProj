package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyPaidVo;

/**
 * 保單給付服務.
 * 
 * @author all
 */
public interface IPolicyPaidService {
	
	/**
	 * 保單給付-查詢.
	 * 
	 * @param policyBonusVo PolicyBonusVo
	 * @return 回傳查詢結果
	 */
	public List<PolicyPaidVo> getPolicyPaid(PolicyPaidVo policyPaidVo);
	
	/**
	 * 保單給付-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public List<PolicyPaidVo> findByPolicyNo(String policyNo);
	
}