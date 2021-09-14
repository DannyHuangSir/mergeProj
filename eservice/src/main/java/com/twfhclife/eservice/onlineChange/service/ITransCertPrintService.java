package com.twfhclife.eservice.onlineChange.service;

import java.util.List;
import java.util.Optional;

import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 投保證明列印服務.
 * 
 * @author all
 */
public interface ITransCertPrintService {
	
	/**
	 * 投保證明列印-查詢.
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return 回傳查詢結果
	 */
	public List<TransCertPrintVo> getTransCertPrint(TransCertPrintVo transCertPrintVo);
	
	/**
	 * 投保證明列印-新增.
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @param transExtendAttrList List<TransExtendAttrVo>
	 * @return 回傳影響筆數
	 */
	public int insertTransCertPrint(TransCertPrintVo transCertPrintVo, List<TransExtendAttrVo> transExtendAttrList);
	
	/**
	 * 投保證明列印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransCertPrintVo getTransCertPrintDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 投保證明列印-PDF匯出
	 * @return
	 */
	public Optional<byte[]> prepareCertPrint(TransCertPrintVo transCertPrintVo);
}