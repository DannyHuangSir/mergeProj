package com.twfhclife.adm.service;

import com.twfhclife.adm.model.UserRoleVo;

import java.util.List;
import java.util.Map;

/**
 * 人員角色管理服務.
 * 
 * @author all
 */
public interface IJdUserRoleService {
	
	/**
	 * 人員角色管理-分頁查詢.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳人員角色管理-分頁查詢結果
	 */
	public List<Map<String, Object>> getUserRolePageList(UserRoleVo userRoleVo);

	/**
	 * 人員角色管理-查詢結果總筆數.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳總筆數
	 */
	public int getUserRolePageTotal(UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-查詢.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳查詢結果
	 */
	List<UserRoleVo> getUserRole(UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-新增.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	public int insertUserRole(UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-更新.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	public int updateUserRole(UserRoleVo userRoleVo);
	
	/**
	 * 人員角色管理-刪除.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	public int deleteUserRole(UserRoleVo userRoleVo);

	int countUserRole(String userId);
}