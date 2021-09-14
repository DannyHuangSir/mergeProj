package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransSurrenderVo;

/**
 * 紅利提領申請書 Dao.
 * 
 * @author all
 */
public interface TransSurrenderDao {
	
	/**
	 * 紅利提領申請書-查詢.
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳查詢結果
	 */
	List<TransSurrenderVo> getTransSurrender(@Param("transSurrenderVo") TransSurrenderVo transSurrenderVo);
	
	/**
	 * 紅利提領申請書-新增.
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳影響筆數
	 */
	int insertTransSurrender(@Param("transSurrenderVo") TransSurrenderVo transSurrenderVo);
}