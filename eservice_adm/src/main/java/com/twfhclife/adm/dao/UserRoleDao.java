package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.UserRoleVo;

/**
 * 人員角色管理 Dao.
 * 
 * @author all
 */
public interface UserRoleDao {
	
	/**
	 * 人員角色管理-分頁查詢.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳人員角色管理-分頁查詢結果
	 */
	List<Map<String, Object>> getUserRolePageList(@Param("userRoleVo") UserRoleVo userRoleVo);

	/**
	 * 人員角色管理-查詢結果總筆數.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳總筆數
	 */
	int getUserRolePageTotal(@Param("userRoleVo") UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-查詢.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳查詢結果
	 */
	List<UserRoleVo> getUserRole(@Param("userRoleVo") UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-新增.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	int insertUserRole(@Param("userRoleVo") UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-更新.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	int updateUserRole(@Param("userRoleVo") UserRoleVo userRoleVo);

	/**
	 * 人員角色管理-刪除.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	int deleteUserRole(@Param("userRoleVo") UserRoleVo userRoleVo);
}

