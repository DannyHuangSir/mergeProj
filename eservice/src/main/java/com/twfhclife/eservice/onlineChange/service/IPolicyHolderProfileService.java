package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransPolicyHolderProfileVo;

/**
 * 保戶基本資料更新服務.
 * 
 * @author all
 */
public interface IPolicyHolderProfileService {
	
	/**
	 * 保戶基本資料更新-查詢.
	 * 
	 * @param TransPolicyHolderProfileVo transPolicyHolderProfileVo
	 * @return 回傳查詢結果
	 */
	public List<TransPolicyHolderProfileVo> getPolicyHolderProfileList(TransPolicyHolderProfileVo transPolicyHolderProfileVo);
	
	/**
	 * 保戶基本資料更新-新增.
	 * 
	 * @param TransPolicyHolderProfileVo transPolicyHolderProfileVo
	 * @return 回傳影響筆數
	 */
	public int insertPolicyHolderProfile(TransPolicyHolderProfileVo transPolicyHolderProfileVo);
	
	/**
	 * 保戶基本資料更新-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransPolicyHolderProfileVo getPolicyHolderProfileDetail(String transNum);
	
	public List<String> getAddCodeByLipmId(String lipmId);
}