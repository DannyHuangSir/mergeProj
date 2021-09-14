package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyDividendVo;

/**
 * 保單收益分配服務.
 * 
 * @author all
 */
public interface IPolicyDividendService {
	
	/**
	 * 保單收益分配-查詢.
	 * 
	 * @param policyDividendVo PolicyDividendVo
	 * @return 回傳查詢結果
	 */
	public List<PolicyDividendVo> getPolicyDividend(PolicyDividendVo policyDividendVo);
}