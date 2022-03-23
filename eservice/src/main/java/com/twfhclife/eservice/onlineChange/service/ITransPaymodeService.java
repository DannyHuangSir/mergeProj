package com.twfhclife.eservice.onlineChange.service;

import java.math.BigDecimal;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更繳別服務.
 * 
 * @author all
 */
public interface ITransPaymodeService {
	
	/**
	 * 變更繳別-查詢.
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return 回傳查詢結果
	 */
	public List<TransPaymodeVo> getTransPaymode(TransPaymodeVo transPaymodeVo);
	
	/**
	 * 變更繳別-新增.
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return 回傳影響筆數
	 */
	public int insertTransPaymode(TransPaymodeVo transPaymodeVo);
	
	/**
	 * 變更繳別-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransPaymodeVo getTransPaymodeDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 取得保單繳別由短變長判斷 LIPM_TRED_TMS 欄位.
	 * ex: 月繳 -> 半年繳
	 * @param policyList
	 */
	public Integer getPolicyPayMethodChange(String policyNo);

	BigDecimal getPolicyPayOld(String policyNo);
}