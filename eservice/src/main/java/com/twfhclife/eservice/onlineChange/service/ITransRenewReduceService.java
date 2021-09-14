package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransRenewReduceVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更展期定期保險/減額繳清保險服務.
 * 
 * @author all
 */
public interface ITransRenewReduceService {
	
	/**
	 * 變更展期定期保險/減額繳清保險-新增.
	 * 
	 * @param transRenewReduceVo TransRenewReduceVo
	 * @return 回傳影響筆數
	 */
	public int insertTransRenewReduce(TransRenewReduceVo transRenewReduceVo);
	
	/**
	 * 變更展期定期保險/減額繳清保險-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransRenewReduceVo getTransRenewReduceDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 * @param userId 使用者ID
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList, String userId);
}