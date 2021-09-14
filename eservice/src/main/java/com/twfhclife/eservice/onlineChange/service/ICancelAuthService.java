package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.CancelAuthVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 終止授權.
 * 
 * @author all
 */
public interface ICancelAuthService {
	
	/**
	 * 新增申請主檔.
	 * 
	 * @param cancelAuthVo CancelAuthVo
	 * @return 回傳影響筆數
	 */
	public int insertTransCancelAuth(CancelAuthVo cancelAuthVo);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}