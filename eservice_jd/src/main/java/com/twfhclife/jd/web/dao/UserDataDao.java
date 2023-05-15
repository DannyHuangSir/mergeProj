package com.twfhclife.jd.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 使用者資訊 Dao.
 * 
 * @author all
 */
public interface UserDataDao {
	
	/**
	 * 取得使用者的保單名稱清單.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳使用者的保單名稱清單
	 */
	List<Map<String, String>> getProductNameList(@Param("rocId") String rocId);
	
	/**
	 * 取得使用者的保單主約被保險人名稱清單.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳使用者的保單名稱清單
	 */
	List<Map<String, String>> getMainInsuredNameList(@Param("rocId") String rocId);
}
