package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransCreditCardDateVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更信用卡效期服務.
 * 
 * @author all
 */
public interface ITransCreditCardDateService {
	
	/**
	 * 變更信用卡效期-查詢.
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return 回傳查詢結果
	 */
	public List<TransCreditCardDateVo> getTransCreditCardDate(TransCreditCardDateVo transCreditCardDateVo);
	
	/**
	 * 變更信用卡效期-新增.
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return 回傳影響筆數
	 */
	public int insertTransCreditCardDate(TransCreditCardDateVo transCreditCardDateVo);
	
	/**
	 * 變更信用卡效期-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransCreditCardDateVo getTransCreditCardDateDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}