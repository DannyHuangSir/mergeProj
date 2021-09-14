package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.FunctionDivVo;

/**
 * 功能 DIV 區塊 Dao.
 * 
 * @author all
 */
public interface FunctionDivDao {
	
	/**
	 * 功能 DIV 區塊-查詢.
	 * 
	 * @param functionDivVo FunctionDivVo
	 * @return 回傳查詢結果
	 */
	List<FunctionDivVo> getFunctionDiv(@Param("functionDivVo") FunctionDivVo functionDivVo);
	
	/**
	 * 取得所有功能 DIV 區塊.
	 * 
	 * @return 回傳查詢結果
	 */
	List<FunctionDivVo> findAll();
	
	/**
	 * 功能 DIV 區塊-新增.
	 * 
	 * @param functionDivVo FunctionDivVo
	 * @return 回傳影響筆數
	 */
	int insertFunctionDiv(@Param("functionDivVo") FunctionDivVo functionDivVo);

	/**
	 * 功能 DIV 區塊-刪除.
	 * 
	 * @param functionId 功能代碼
	 * @return 回傳影響筆數
	 */
	int deleteByFunId(@Param("functionId") String functionId);
}