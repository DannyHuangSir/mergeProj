package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransAnnuityMethodVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更年金給付方式服務.
 * 
 * @author all
 */
public interface ITransAnnuityMethodService {
	
	/**
	 * 變更年金給付方式-查詢.
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return 回傳查詢結果
	 */
	public List<TransAnnuityMethodVo> getTransAnnuityMethod(TransAnnuityMethodVo transAnnuityMethodVo);
	
	/**
	 * 變更年金給付方式-新增.
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return 回傳影響筆數
	 */
	public int insertTransAnnuityMethod(TransAnnuityMethodVo transAnnuityMethodVo);
	
	/**
	 * 變更年金給付方式-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransAnnuityMethodVo getTransAnnuityMethodDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 取得外部舊保證期間
	 * @param policyNo
	 * @return String
	 */
	public String getOldGuarTerm(@Param("policyNo") String policyNo);
}