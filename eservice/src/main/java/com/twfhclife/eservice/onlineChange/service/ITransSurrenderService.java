package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransSurrenderVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 紅利提領申請書套印服務.
 * 
 * @author all
 */
public interface ITransSurrenderService {
	
	/**
	 * 取得紅利提領申請書報表 byte[].
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(TransSurrenderVo transSurrenderVo);
	
	/**
	 * 紅利提領申請書套印-查詢.
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳查詢結果
	 */
	public List<TransSurrenderVo> getTransSurrender(TransSurrenderVo transSurrenderVo);
	
	/**
	 * 紅利提領申請書套印-新增.
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳影響筆數
	 */
	public int insertTransSurrender(TransSurrenderVo transSurrenderVo);
	
	/**
	 * 紅利提領申請書套印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransSurrenderVo getTransSurrenderDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}