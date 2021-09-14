package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransValuePrintVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 保單價值列印服務.
 * 
 * @author all
 */
public interface ITransValuePrintService {
	
	/**
	 * 保單價值列印-查詢.
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return 回傳查詢結果
	 */
	public List<TransValuePrintVo> getTransValuePrint(TransValuePrintVo transValuePrintVo);
	
	/**
	 * 保單價值列印-新增.
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return 回傳影響筆數
	 */
	public int insertTransValuePrint(TransValuePrintVo transValuePrintVo);
	
	/**
	 * 保單價值列印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransValuePrintVo getTransValuePrintDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}