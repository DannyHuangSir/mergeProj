package com.twfhclife.eservice.onlineChange.service;

import java.math.BigDecimal;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更受益人服務.
 * 
 * @author all
 */
public interface ITransBeneficiaryService {

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 變更受益人主檔-新增.
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return 回傳影響筆數
	 */
	public int insertTransBeneficiary(TransBeneficiaryVo transBeneficiaryVo);
	
	/**
	 * 取得變更後受益人申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public BigDecimal getTransBeneficiaryId(String transNum);
	
	/**
	 * 變更後受益人-明細查詢.
	 * 
	 * @param beneficiaryId 變更受益人主檔id
	 * @return 回傳查詢結果
	 */
	public List<TransBeneficiaryDtlVo> getTransBeneficiaryDtlDetail(BigDecimal beneficiaryId);
	
	/**
	 * 變更前受益人-明細查詢.
	 * 
	 * @param beneficiaryId 變更受益人主檔id
	 * @return 回傳查詢結果
	 */
	public List<TransBeneficiaryOldVo> getTransBeneficiaryOldDetail(BigDecimal beneficiaryId);
}