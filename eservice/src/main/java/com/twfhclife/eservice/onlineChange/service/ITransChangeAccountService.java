package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransChangeAccountVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 匯款帳號變更服務.
 * 
 * @author all
 */
public interface ITransChangeAccountService {
	
	/**
	 * 匯款帳號變更-查詢.
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return 回傳查詢結果
	 */
	public List<TransChangeAccountVo> getTransChangeAccount(TransChangeAccountVo transChangeAccountVo);
	
	/**
	 * 匯款帳號變更-新增.
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return 回傳影響筆數
	 */
	public int insertTransChangeAccount(TransChangeAccountVo transChangeAccountVo);
	
	/**
	 * 匯款帳號變更-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransChangeAccountVo getTransChangeAccountDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}