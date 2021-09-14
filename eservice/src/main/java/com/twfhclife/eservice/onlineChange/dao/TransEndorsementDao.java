package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransEndorsementVo;

/**
 * 批註書 Dao.
 * 
 * @author all
 */
public interface TransEndorsementDao {
	
	/**
	 * 查詢批註書內容.
	 * 
	 * @param transNum String
	 * @return 回傳查詢結果
	 */
	List<TransEndorsementVo> getTransEndorsementByTransNum(@Param("transNum") String transNum);
	
}