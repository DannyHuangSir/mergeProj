package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransBounsVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更紅利選擇權服務.
 * 
 * @author all
 */
public interface ITransBounsService {
	
	/**
	 * 變更紅利選擇權-查詢.
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return 回傳查詢結果
	 */
	public List<TransBounsVo> getTransBouns(TransBounsVo transBounsVo);
	
	/**
	 * 變更紅利選擇權-新增.
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return 回傳影響筆數
	 */
	public int insertTransBouns(TransBounsVo transBounsVo);
	
	/**
	 * 變更紅利選擇權-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransBounsVo getTransBounsDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}