package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.FunctionItemVo;
import com.twfhclife.adm.model.SystemsVo;

/**
 * 功能維護 Dao.
 * 
 * @author all
 */
public interface FunctionItemDao {
	
	/**
	 * 取得目前有設定的系統別.
	 * 
	 * @return 回傳系統別
	 */
	List<String> getUnionSysId();
	
	/**
	 * 根據系統別取得所有功能.
	 * 
	 * @param sysId 系統id
	 * @return 回傳該系統的所有功能.
	 */	
	List<FunctionItemVo> getAllFuncBySysId(@Param("sysId") String sysId);
	
	/**
	 * 新增系統
	 * 
	 * @param systemsVo SystemsVo
	 * @return 回傳影響筆數
	 */
	int addSystem(@Param("systemsVo") SystemsVo systemsVo);
	
	/**
	 * 新增功能節點.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	int addFunctionItem(@Param("functionVo") FunctionItemVo functionVo);
	
	/**
	 * 更新功能節點.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	int updateFunctionItem(@Param("functionVo") FunctionItemVo functionVo);
	
	/**
	 * 刪除功能節點.
	 * 
	 * @param functionId 功能ID
	 * @return 回傳影響筆數
	 */
	int deleteFunctionItem(@Param("functionId") String functionId);
}