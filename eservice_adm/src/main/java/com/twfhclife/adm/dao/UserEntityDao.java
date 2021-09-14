package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.UserEntityVo;

/**
 * 人員管理 Dao.
 * 
 * @author all
 */
public interface UserEntityDao {
	
	/**
	 * 人員管理-分頁查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳人員管理-分頁查詢結果
	 */
	List<Map<String, Object>> getUserEntityPageList(@Param("userEntityVo") UserEntityVo userEntityVo,
			@Param("adminUserFlag") String adminUserFlag);

	/**
	 * 人員管理-查詢結果總筆數.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳總筆數
	 */
	int getUserEntityPageTotal(@Param("userEntityVo") UserEntityVo userEntityVo,
			@Param("adminUserFlag") String adminUserFlag);
	
	/**
	 * 人員管理-查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return 回傳查詢結果
	 */
	List<UserEntityVo> getUserEntity(@Param("userEntityVo") UserEntityVo userEntityVo);
}