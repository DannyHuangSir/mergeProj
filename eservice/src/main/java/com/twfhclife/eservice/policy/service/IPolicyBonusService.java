package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyBonusVo;

/**
 * 保單紅利服務.
 * 
 * @author all
 */
public interface IPolicyBonusService {
	
	/**
	 * 保單紅利-查詢.
	 * 
	 * @param policyBonusVo PolicyBonusVo
	 * @return 回傳查詢結果
	 */
	public List<PolicyBonusVo> getPolicyBonus(PolicyBonusVo policyBonusVo);
	
	/**
	 * 保單紅利-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public List<PolicyBonusVo> findByPolicyNo(String policyNo);
	
	/**
	 * 查詢紅利選擇權.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳紅利選擇權
	 */
	public String getBounsTake(String policyNo);
}