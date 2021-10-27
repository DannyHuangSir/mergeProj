package com.twfhclife.eservice.user.service;

import java.util.List;

import com.twfhclife.eservice.user.model.LilipmVo;

/**
 * 要保人服務.
 * 
 * @author all
 */
public interface ILilipmService {
	
	/**
	 * 要保人-查詢.
	 * 
	 * @param lilipmVo LilipmVo
	 * @return 回傳查詢結果
	 */
	public List<LilipmVo> getLilipm(LilipmVo lilipmVo);
	
	/**
	 * 要保人-根據證號查詢.
	 * 
	 * @param rocId 證號
	 * @return 回傳要保人
	 */
	public LilipmVo findByRocId(String rocId);
	
	/**
	 * 要保人-根據保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳要保人
	 */
	public LilipmVo findByPolicyNo(String policyNo);
	
	/**
	 * 查詢要保人聯絡資訊-根據保單號碼清單查詢最近的.
	 * 
	 * @param policyNos 保單號碼清單
	 * @return 回傳要保人
	 */
	public LilipmVo findContactInfoByPolicyNoList(List<String> policyNos);
	
	/**
	 * 取得使用者的所有保單號碼清單.
	 * 
	 * @param rocId 使用者證號
	 * @return 回傳使用者的所有保單號碼
	 */
	public List<String> getUserPolicyNos(String rocId);
	
	/**
	 * 要保人-查詢有效保單.
	 * 
	 * @param lilipmVo LilipmVo
	 * @return 回傳查詢結果
	 */
	public List<LilipmVo> getAliveLilipm(LilipmVo lilipmVo);
	
	/**
	 * 判斷是否保戶(要保人+被保人)
	 * @param rocId
	 * @return 回傳數據條數
	 */
	public int getInsuredUsersByRocId(String rocId);

}