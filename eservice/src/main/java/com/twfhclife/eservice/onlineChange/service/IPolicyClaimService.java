package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 理賠申請書套印服務.
 * 
 * @author all
 */
public interface IPolicyClaimService {
	
	/**
	 * 取得理賠申請書報表 byte[].
	 * 
	 * @param claimVo ClaimVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(TransInsuranceClaimVo claimVo);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}