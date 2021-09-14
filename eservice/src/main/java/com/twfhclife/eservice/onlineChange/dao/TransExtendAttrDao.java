package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;

/**
 * 線上申請額外資料 Dao.
 * 
 * @author all
 */
public interface TransExtendAttrDao {
	
	/**
	 * 查詢線上申請額外資料.
	 * 
	 * @param transNum String
	 * @return 回傳查詢結果
	 */
	List<TransExtendAttrVo> getTransExtendsByTransNum(@Param("transNum") String transNum);
	
	/**
	 * 新增線上申請額外資料.
	 * 
	 * @param transExtendAttrVo
	 * @return
	 */
	int insertTransExtendAttr(@Param("transExtendAttrVo") TransExtendAttrVo transExtendAttrVo);
	
}