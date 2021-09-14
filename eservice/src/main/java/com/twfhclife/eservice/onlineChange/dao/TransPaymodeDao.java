package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;

/**
 * 變更繳別 Dao.
 * 
 * @author all
 */
public interface TransPaymodeDao {
	
	/**
	 * 變更繳別-查詢.
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return 回傳查詢結果
	 */
	List<TransPaymodeVo> getTransPaymode(@Param("transPaymodeVo") TransPaymodeVo transPaymodeVo);
	
	/**
	 * 變更繳別-新增.
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return 回傳影響筆數
	 */
	int insertTransPaymode(@Param("transPaymodeVo") TransPaymodeVo transPaymodeVo);
}