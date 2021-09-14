package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.MaturityVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 滿期服務.
 * 
 * @author all
 */
public interface IMaturityService {
	
	/**
	 * 取得滿期報表 byte[].
	 * 
	 * @param maturityVo MaturityVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(MaturityVo maturityVo);
	
	/**
	 * 新增申請主檔.
	 * 
	 * @param maturityVo MaturityVo
	 * @return 回傳影響筆數
	 */
	public int insertTransMaturity(MaturityVo maturityVo);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}