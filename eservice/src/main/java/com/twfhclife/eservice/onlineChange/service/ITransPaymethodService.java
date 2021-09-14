package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransPaymethodVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更收費管道服務.
 * 
 * @author all
 */
public interface ITransPaymethodService {
	
	/**
	 * 變更收費管道-查詢.
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return 回傳查詢結果
	 */
	public List<TransPaymethodVo> getTransPaymethod(TransPaymethodVo transPaymethodVo);
	
	/**
	 * 變更收費管道-新增.
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return 回傳影響筆數
	 */
	public int insertTransPaymethod(TransPaymethodVo transPaymethodVo);
	
	/**
	 * 變更收費管道-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransPaymethodVo getTransPaymethodDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}