package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransValuePrintVo;

/**
 * 保單價值列印 Dao.
 * 
 * @author all
 */
public interface TransValuePrintDao {
	
	/**
	 * 保單價值列印-查詢.
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return 回傳查詢結果
	 */
	List<TransValuePrintVo> getTransValuePrint(@Param("transValuePrintVo") TransValuePrintVo transValuePrintVo);
	
	/**
	 * 保單價值列印-新增.
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return 回傳影響筆數
	 */
	int insertTransValuePrint(@Param("transValuePrintVo") TransValuePrintVo transValuePrintVo);
}