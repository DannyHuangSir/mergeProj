package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransCancelContractVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 解約申請書套印服務.
 * 
 * @author all
 */
public interface ITransCancelContractService {
	
	/**
	 * 取得解約申請書報表 byte[].
	 * 
	 * @param transCancelContractVo TransCancelContractVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(TransCancelContractVo transCancelContractVo);
	
	/**
	 * 解約申請書套印-查詢.
	 * 
	 * @param transCancelContractVo TransCancelContractVo
	 * @return 回傳查詢結果
	 */
	public List<TransCancelContractVo> getTransCancelContract(TransCancelContractVo transCancelContractVo);
	
	/**
	 * 解約申請書套印-新增.
	 * 
	 * @param transCancelContractVo TransCancelContractVo
	 * @return 回傳影響筆數
	 */
	public int insertTransCancelContract(TransCancelContractVo transCancelContractVo);
	
	/**
	 * 解約申請書套印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransCancelContractVo getTransCancelContractDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
}