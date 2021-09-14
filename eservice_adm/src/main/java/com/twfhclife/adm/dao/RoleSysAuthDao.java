package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.RoleSysAuthVo;

/**
 * 角色系統權限 Dao.
 * 
 * @author all
 */
public interface RoleSysAuthDao {
	
	/**
	 * 角色系統權限-查詢.
	 * 
	 * @param roleSysAuthVo RoleSysAuthVo
	 * @return 回傳查詢結果
	 */
	List<RoleSysAuthVo> getRoleSysAuth(@Param("roleSysAuthVo") RoleSysAuthVo roleSysAuthVo);
	
	/**
	 * 角色系統權限-新增.
	 * 
	 * @param roleSysAuthVo RoleSysAuthVo
	 * @return 回傳影響筆數
	 */
	int insertRoleSysAuth(@Param("roleSysAuthVo") RoleSysAuthVo roleSysAuthVo);

	/**
	 * 角色系統權限-刪除.
	 * 
	 * @param roleId 角色代碼
	 * @return 回傳影響筆數
	 */
	int deleteByRoleId(@Param("roleId") String roleId);
}