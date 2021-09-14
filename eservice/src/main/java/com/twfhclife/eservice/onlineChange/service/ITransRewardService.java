package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransRewardVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更增值回饋分享金領取方式服務.
 * 
 * @author all
 */
public interface ITransRewardService {
	
	/**
	 * 變更增值回饋分享金領取方式-查詢.
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return 回傳查詢結果
	 */
	public List<TransRewardVo> getTransReward(TransRewardVo transRewardVo);
	
	/**
	 * 變更增值回饋分享金領取方式-新增.
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return 回傳影響筆數
	 */
	public int insertTransReward(TransRewardVo transRewardVo);
	
	/**
	 * 變更增值回饋分享金領取方式-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransRewardVo getTransRewardDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}