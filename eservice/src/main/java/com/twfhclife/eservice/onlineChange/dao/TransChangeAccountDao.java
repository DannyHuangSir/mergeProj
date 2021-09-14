package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransChangeAccountVo;

/**
 * 匯款帳號變更 Dao.
 * 
 * @author all
 */
public interface TransChangeAccountDao {
	
	/**
	 * 匯款帳號變更-查詢.
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return 回傳查詢結果
	 */
	List<TransChangeAccountVo> getTransChangeAccount(@Param("transChangeAccountVo") TransChangeAccountVo transChangeAccountVo);
	
	/**
	 * 匯款帳號變更-新增.
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return 回傳影響筆數
	 */
	int insertTransChangeAccount(@Param("transChangeAccountVo") TransChangeAccountVo transChangeAccountVo);
}