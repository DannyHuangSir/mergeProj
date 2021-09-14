package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.RoleFuncAuthVo;

/**
 * 角色功能權限 Dao.
 * 
 * @author all
 */
public interface RoleFuncAuthDao {
	
	/**
	 * 取得角色的功能ID清單.
	 * 
	 * @param roleIds 角色清單
	 * @return 回傳功能清單
	 */
	List<String> getFunctionIdByRoleIdList(@Param("roleIds") List<String> roleIds);
	
	/**
	 * 角色功能權限-查詢.
	 * 
	 * @param roleFuncAuthVo RoleFuncAuthVo
	 * @return 回傳查詢結果
	 */
	List<RoleFuncAuthVo> getRoleFuncAuth(@Param("roleFuncAuthVo") RoleFuncAuthVo roleFuncAuthVo);
	
	/**
	 * 角色功能權限-新增.
	 * 
	 * @param roleFuncAuthVo RoleFuncAuthVo
	 * @return 回傳影響筆數
	 */
	int insertRoleFuncAuth(@Param("roleFuncAuthVo") RoleFuncAuthVo roleFuncAuthVo);
	
	/**
	 * 角色功能權限-刪除.
	 * 
	 * @param roleId 角色代碼
	 * @return 回傳影響筆數
	 */
	int deleteByRoleId(@Param("roleId") String roleId);	
	
	List<Map<String, Object>> getRoleFuncAuthByRoleId(@Param("roleId") String roleId);
}