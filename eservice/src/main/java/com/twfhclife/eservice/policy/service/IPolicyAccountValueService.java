package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;

/**
 * 保單價戶價值服務.
 * 
 * @author all
 */
public interface IPolicyAccountValueService {

	/**
	 * 查詢保單帳戶價值
	 * 
	 * @param policyNo
	 * @return
	 */
	public List<PolicyAccountValueVo> getPolicyAccountValueList(String policyNo);
}
