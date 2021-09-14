package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.RoleDivAuthVo;

/**
 * 角色DIV REJECT權限 Dao.
 * 
 * @author all
 */
public interface RoleDivAuthDao {
	
	/**
	 * 取得角色DIV REJECT的divId清單.
	 * 
	 * @param roleIds 角色清單
	 * @return 回傳功能清單
	 */
	List<String> getDivIdByRoleIdList(@Param("roleIds") List<String> roleIds);
	
	/**
	 * 角色DIV REJECT權限-查詢.
	 * 
	 * @param roleDivAuthVo RoleDivAuthVo
	 * @return 回傳查詢結果
	 */
	List<RoleDivAuthVo> getRoleDivAuth(@Param("roleDivAuthVo") RoleDivAuthVo roleDivAuthVo);
	
	/**
	 * 角色DIV REJECT權限-新增.
	 * 
	 * @param roleDivAuthVo RoleDivAuthVo
	 * @return 回傳影響筆數
	 */
	int insertRoleDivAuth(@Param("roleDivAuthVo") RoleDivAuthVo roleDivAuthVo);

	/**
	 * 角色DIV REJECT權限-刪除.
	 * 
	 * @param roleId 角色代碼
	 * @return 回傳影響筆數
	 */
	int deleteByRoleId(@Param("roleId") String roleId);
	
	/**
	 * 依UserId取得不可使用的DivId清單
	 * @param userId
	 * @return List<RoleDivAuthVo>
	 */
	List<RoleDivAuthVo> getRoleDivAuthByUserId(@Param("userId") String userId);
}