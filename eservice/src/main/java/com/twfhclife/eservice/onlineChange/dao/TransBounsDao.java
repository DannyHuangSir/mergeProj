package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransBounsVo;

/**
 * 變更紅利選擇權 Dao.
 * 
 * @author all
 */
public interface TransBounsDao {
	
	/**
	 * 變更紅利選擇權-查詢.
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return 回傳查詢結果
	 */
	List<TransBounsVo> getTransBouns(@Param("transBounsVo") TransBounsVo transBounsVo);
	
	/**
	 * 變更紅利選擇權-新增.
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return 回傳影響筆數
	 */
	int insertTransBouns(@Param("transBounsVo") TransBounsVo transBounsVo);
	
	/**
	 * 第4點條件.
	 * 
	 * @param policyNo
	 * @return Y:允許變更, N:不允許變更
	 */
	String checkCondition4(@Param("policyNo") String policyNo);
}