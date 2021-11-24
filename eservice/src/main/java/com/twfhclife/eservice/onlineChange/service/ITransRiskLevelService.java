package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;

/**
 * 變更風險屬性服務.
 * 
 * @author all
 */
public interface ITransRiskLevelService {
	
	/**
	 * 變更風險屬性-查詢.
	 * 
	 * @param TransRiskLevelVo transRiskLevelVo
	 * @return 回傳查詢結果
	 */
	public List<TransRiskLevelVo> getTransRiskLevelList(TransRiskLevelVo transRiskLevelVo);
	
	/**
	 * 變更風險屬性-新增.
	 * 
	 * @param TransRiskLevelVo transRiskLevelVo
	 * @return 回傳影響筆數
	 */
	public int insertTransRiskLevel(TransRiskLevelVo transRiskLevelVo);
	
	/**
	 * 變更風險屬性-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransRiskLevelVo getTransRiskLevelDetail(String transNum);
	
	/**
	 * 取得投資風險屬性代碼.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資風險屬性
	 */
	public String getRiskLevelCode(String policyNo);

	/***
	 * 获取用户风险属性
	 * @param rocId
	 * @return
	 */
	public String getUserRiskAttr(String rocId);

	String computeRiskLevel(Integer score);

	boolean checkRiskLevelExpire(String userRocId);
}