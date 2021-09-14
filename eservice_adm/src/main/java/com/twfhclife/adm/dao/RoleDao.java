package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.RoleVo;

/**
 * 使用者角色管理 Dao.
 * 
 * @author all
 */
public interface RoleDao {
	
	/**
	 * 取得使用者角色管理查詢結果.
	 * 
	 * @param roleVo RoleVo
	 * @param keyCloakUserId keyCloak user UUID
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳使用者角色管理查詢結果
	 */
	List<Map<String, Object>> getRolePageList(@Param("roleVo") RoleVo roleVo,
			@Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag);

	/**
	 * 取得使用者角色管理查詢結果總筆數.
	 * 
	 * @param roleVo RoleVo
	 * @param keyCloakUserId keyCloak user UUID
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳總筆數
	 */
	int getRolePageTotal(@Param("roleVo") RoleVo roleVo, @Param("keyCloakUserId") String keyCloakUserId,
			@Param("adminUserFlag") String adminUserFlag);

	/**
	 * 使用者角色管理-查詢.
	 * 
	 * @param roleVo RoleVo
	 * @param keyCloakUserId keyCloak user UUID
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳查詢結果
	 */
	List<RoleVo> getRole(@Param("roleVo") RoleVo roleVo,
			@Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag);

	/**
	 * 使用者角色管理-依照使用者權限查詢.
	 * 
	 * @param keyCloakUserId keyCloak user UUID
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳查詢結果
	 */
	List<RoleVo> getRoleByAuth(@Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag);
	
	/**
	 * 使用者角色管理-新增.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	int insertRole(@Param("roleVo") RoleVo roleVo);
	
	/**
	 * 使用者角色管理-更新.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	int updateRole(@Param("roleVo") RoleVo roleVo);

	/**
	 * 使用者角色管理-刪除.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	int deleteRole(@Param("roleVo") RoleVo roleVo);
}

