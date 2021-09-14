package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransCushionVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 自動墊繳選擇權服務.
 * 
 * @author all
 */
public interface ITransCushionService {
	
	/**
	 * 自動墊繳選擇權-查詢.
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return 回傳查詢結果
	 */
	public List<TransCushionVo> getTransCushion(TransCushionVo transCushionVo);
	
	/**
	 * 自動墊繳選擇權-新增.
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return 回傳影響筆數
	 */
	public int insertTransCushion(TransCushionVo transCushionVo);
	
	/**
	 * 自動墊繳選擇權-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransCushionVo getTransCushionDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}