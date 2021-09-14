package com.twfhclife.eservice.onlineChange.service;

import java.math.BigDecimal;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransReducePolicyDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 減少保險金額服務.
 * 
 * @author all
 */
public interface ITransReducePolicyService {
	
	/**
	 * 減少保險金額-查詢.
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return 回傳查詢結果
	 */
	public List<TransReducePolicyVo> getTransReducePolicy(TransReducePolicyVo transReducePolicyVo);
	
	/**
	 * 減少保險金額-新增.
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return 回傳影響筆數
	 */
	public int insertTransReducePolicy(TransReducePolicyVo transReducePolicyVo);
	
	/**
	 * 取得減少保險金額申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public BigDecimal getTransReducePolicyId(String transNum);

	/**
	 * 減少保險金額-明細查詢.
	 * 
	 * @param transReducePolicyId 減少保險金額申請id
	 * @return 回傳查詢結果
	 */
	public List<TransReducePolicyDtlVo> getTransReducePolicyDtlDetail(BigDecimal transReducePolicyId);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}