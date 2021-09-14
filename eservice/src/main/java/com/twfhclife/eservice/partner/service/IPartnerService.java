package com.twfhclife.eservice.partner.service;

import java.util.List;

import com.twfhclife.eservice.partner.model.PartnerVo;
import com.twfhclife.eservice.policy.model.AgentVo;

public interface IPartnerService {

	/**
	 * 取得保代下及內部人員的所有保單清單.
	 * 
	 * @param rocId 要保人身份證字號
	 * @param policyNo 保單號碼
	 * @param agentCode 代理人代碼
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保代下的使用者的所有保單清單
	 */
	public List<PartnerVo> getPartnerPolicyPageList(String rocId, String policyNo, String agentCode, int pageNum,
			int pageSize);

	/**
	 * 取得合作對象.
	 * 
	 * @param agentCode 代理人代碼
	 * @return 回傳合作對象
	 */
	public List<AgentVo> getAgentOptionList(String agentCode);
}