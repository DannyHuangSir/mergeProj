package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransPolicyHolderProfileVo;

/**
 * 保戶基本資料更新 Dao.
 * 
 * @author all
 */
public interface TransPolicyHolderProfileDao {
	
	/**
	 * 保戶基本資料更新-查詢.
	 * 
	 * @param TransPolicyHolderProfileVo transPolicyHolderProfileVo
	 * @return 回傳查詢結果
	 */
	List<TransPolicyHolderProfileVo> getPolicyHolderProfileList(@Param("transPolicyHolderProfileVo") TransPolicyHolderProfileVo transPolicyHolderProfileVo);
	
	/**
	 * 保戶基本資料更新-新增.
	 * 
	 * @param TransPolicyHolderProfileVo transPolicyHolderProfileVo
	 * @return 回傳影響筆數
	 */
	int insertPolicyHolderProfile(@Param("transPolicyHolderProfileVo") TransPolicyHolderProfileVo transPolicyHolderProfileVo);

	List<String> getAddCodeByLipmId(@Param("lipmId") String lipmId);
}