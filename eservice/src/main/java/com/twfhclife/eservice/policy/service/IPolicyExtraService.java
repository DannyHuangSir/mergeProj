package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyExtraVo;

/**
 * 保單貸款服務.
 * 
 * @author all
 */
public interface IPolicyExtraService {
	
	/**
	 * 保單貸款-查詢.
	 * 
	 * @param policyExtraVo PolicyExtraVo
	 * @return 回傳查詢結果
	 */
	public List<PolicyExtraVo> getPolicyExtra(PolicyExtraVo policyExtraVo);
	
	/**
	 * 保單貸款-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public PolicyExtraVo findByPolicyNo(String policyNo);

	/**
	 * 取得使用者的資產與負債.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	public PolicyExtraVo getUserAllLoanData(String rocId);
}