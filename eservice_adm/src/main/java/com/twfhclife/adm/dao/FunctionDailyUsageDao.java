package com.twfhclife.adm.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 功能使用統計 dao 
 * @author Ken Wei
 *
 */
public interface FunctionDailyUsageDao {

	/**
	 * 更新功能使用次數，固定 +1，以天為單位累計
	 * @param funcId
	 * @param systemId
	 * @return
	 */
	public int updateFunctionDailyUsage(@Param("funcId")Integer funcId, @Param("systemId")String systemId);
	
	/**
	 * 新增功能使用次數，預設次數 1 且自動帶入 sysdate
	 * @param funcId
	 * @param systemId
	 * @return
	 */
	public int insertFunctionDailyUsage(@Param("funcId")Integer funcId, @Param("systemId")String systemId);
}
