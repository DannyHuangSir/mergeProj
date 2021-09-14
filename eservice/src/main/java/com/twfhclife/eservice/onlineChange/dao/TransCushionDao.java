package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransCushionVo;

/**
 * 自動墊繳選擇權 Dao.
 * 
 * @author all
 */
public interface TransCushionDao {
	
	/**
	 * 自動墊繳選擇權-查詢.
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return 回傳查詢結果
	 */
	List<TransCushionVo> getTransCushion(@Param("transCushionVo") TransCushionVo transCushionVo);
	
	/**
	 * 自動墊繳選擇權-新增.
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return 回傳影響筆數
	 */
	int insertTransCushion(@Param("transCushionVo") TransCushionVo transCushionVo);
}