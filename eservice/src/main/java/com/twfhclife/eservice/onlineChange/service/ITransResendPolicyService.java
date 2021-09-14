package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransResendPolicyVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 補發保單服務.
 * 
 * @author all
 */
public interface ITransResendPolicyService {
	
	/**
	 * 補發保單-查詢.
	 * 
	 * @param transResendPolicyVo TransResendPolicyVo
	 * @return 回傳查詢結果
	 */
	public List<TransResendPolicyVo> getTransResendPolicy(TransResendPolicyVo transResendPolicyVo);
	
	/**
	 * 補發保單-新增.
	 * 
	 * @param transResendPolicyVo TransResendPolicyVo
	 * @return 回傳影響筆數
	 */
	public int insertTransResendPolicy(TransResendPolicyVo transResendPolicyVo);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);

	public TransResendPolicyVo getTransResendPolicyDetail(String transNum);
}