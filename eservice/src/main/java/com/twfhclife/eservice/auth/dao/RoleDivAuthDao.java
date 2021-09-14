package com.twfhclife.eservice.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 角色拒絕訪問DIV Dao.
 * 
 * @author all
 */
public interface RoleDivAuthDao {
	
	/**
	 * 取得拒絕訪問的div 名稱清單.
	 * 
	 * @param keycloakUserId 使用者ID
	 * @return 回傳查詢結果
	 */
	List<String> getRejectDivName(@Param("userId") String keycloakUserId);
}