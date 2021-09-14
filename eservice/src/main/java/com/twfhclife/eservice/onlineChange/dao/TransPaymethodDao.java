package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransPaymethodVo;

/**
 * 變更收費管道 Dao.
 * 
 * @author all
 */
public interface TransPaymethodDao {
	
	/**
	 * 變更收費管道-查詢.
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return 回傳查詢結果
	 */
	List<TransPaymethodVo> getTransPaymethod(@Param("transPaymethodVo") TransPaymethodVo transPaymethodVo);
	
	/**
	 * 變更收費管道-新增.
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return 回傳影響筆數
	 */
	int insertTransPaymethod(@Param("transPaymethodVo") TransPaymethodVo transPaymethodVo);
}