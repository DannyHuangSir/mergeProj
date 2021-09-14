package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.RoleVo;

/**
 * 使用者角色管理服務.
 * 
 * @author all
 */
public interface IRoleService {
	
	/**
	 * 取得使用者角色管理查詢結果.
	 * 
	 * @param roleVo RoleVo
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳使用者角色管理查詢結果
	 */
	public List<Map<String, Object>> getRolePageList(RoleVo roleVo, String userName, String keyCloakUserId);

	/**
	 * 取得使用者角色管理查詢結果總筆數.
	 * 
	 * @param roleVo RoleVo
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳總筆數
	 */
	public int getRolePageTotal(RoleVo roleVo, String userName, String keyCloakUserId);
	
	/**
	 * 使用者角色管理-查詢.
	 * 
	 * @param roleVo RoleVo
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳查詢結果
	 */
	public List<RoleVo> getRole(RoleVo roleVo, String userName, String keyCloakUserId);

	/**
	 * 使用者角色管理-依照使用者權限查詢.
	 * 
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID.
	 * @return 回傳查詢結果
	 */
	public List<RoleVo> getRoleByAuth(String userName, String keyCloakUserId);
	
	/**
	 * 使用者角色管理-新增.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	public int insertRole(RoleVo roleVo);
	
	/**
	 * 使用者角色管理-更新.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	public int updateRole(RoleVo roleVo);
	
	/**
	 * 使用者角色管理-刪除.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	public int deleteRole(RoleVo roleVo);
}