package com.twfhclife.generic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.adm.domain.Role;
import com.twfhclife.eservice.api.adm.domain.RoleFuncAuthVo;
import com.twfhclife.eservice.api.adm.domain.Systems;
import com.twfhclife.eservice.api.adm.model.FunctionDivEntity;
import com.twfhclife.eservice.api.adm.model.FunctionItemEntity;
import com.twfhclife.eservice.api.adm.model.FunctionVoEntity;
import com.twfhclife.generic.model.UserAuthVoEntity;
import com.twfhclife.generic.model.UserRepresentationEntity;
import com.twfhclife.generic.model.UserVo;

/**
 * Definite authorization DAO.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public interface AuthoDao {

	/**
	 * userId必須傳入,sysId可以為null
	 * 
	 * @param userId
	 * @param sysId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getFuncDivAuthByUser(@Param("userId") String userId, @Param("sysId") String sysId)
			throws Exception;
	
	List<Map<String, Object>> getMenuList(@Param("userId") String userId, @Param("sysId") String sysId, @Param("isAdmin") String isAdmin)
			throws Exception;

	/**
	 * userId必須傳入,sysId可以為null
	 * 
	 * @param userId
	 * @param sysId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getFuncDivAuths(@Param("userId") String userId, @Param("sysId") String sysId)
			throws Exception;

	/**
	 * 以使用者帳號查詢所有權限資料.
	 * 
	 * @param userId
	 * @param sysId
	 * @return List<Function>
	 * @throws Exception
	 */
	List<FunctionVoEntity> getAuthInfoByUser(@Param("userId") String userId, @Param("sysId") String sysId)
			throws Exception;

	/**
	 * 查詢使用者可使用的系統清單.
	 * 
	 * @param userId
	 * @return List<SystemsVo>
	 * @throws Exception
	 */
	List<Systems> getSysListByUser(@Param("userId") String userId) throws Exception;

	/**
	 * 查詢系統功能清單.
	 * 
	 * @param sysId
	 * @param parentFuncId
	 * @param functionName
	 * @return List<FunctionVoEntity>
	 * @throws Exception
	 */
	List<FunctionVoEntity> getFunListBySys(@Param("sysId") String sysId, @Param("parentFuncId") String parentFuncId,
			@Param("functionName") String functionName) throws Exception;

	/**
	 * 查詢角色清單.
	 * 
	 * @param roleNameQuery
	 * @return List<Role>
	 * @throws Exception
	 */
	List<Role> getRoleListByRoleName(@Param("roleNameQuery") String roleNameQuery) throws Exception;

	/**
	 * 查詢功能清單.
	 * 
	 * @return List<FunctionEntity>
	 * @throws Exception
	 */
	List<FunctionVoEntity> getAllFunction() throws Exception;

	/**
	 * 查詢角色功能權限.
	 * 
	 * @param roleId
	 * @param roleName
	 * @param sysId
	 * @return List<Function>
	 * @throws Exception
	 */
	List<FunctionVoEntity> getRoleFunctionAuthByRole(@Param("roleId") String roleId, @Param("roleName") String roleName,
			@Param("sysId") String sysId) throws Exception;

	/**
	 * 查詢使用者的系統功能權限.
	 * 
	 * @param userId
	 * @param sysId
	 * @return List<FunctionEntity>
	 * @throws Exception
	 */
	List<FunctionVoEntity> getSysFunctionAuthByUser(@Param("userId") String userId, @Param("sysId") String sysId)
			throws Exception;

	/**
	 * 新增功能項目.
	 * 
	 * @param functionItem
	 * @return Integer
	 * @throws Exception
	 */
	int insertFuncItem(@Param("entity") FunctionItemEntity entity) throws Exception;

	/**
	 * 新增功能分類項目.
	 * 
	 * @param functionDivs
	 * @return Integer
	 * @throws Exception
	 */
	int insertFuncDiv(@Param("entity") FunctionDivEntity entity) throws Exception;

	/**
	 * 修改功能項目.
	 * 
	 * @param request
	 * @return Integer
	 * @throws Exception
	 */
	int updateFunctionItem(@Param("entity") FunctionItemEntity request) throws Exception;

	/**
	 * 修改功能分類項目.
	 * 
	 * @param request
	 * @return Integer
	 * @throws Exception
	 */
	int updateFunctionDiv(@Param("entity") FunctionDivEntity request) throws Exception;

	/**
	 * 刪除功能項目.
	 * 
	 * @param functionId
	 * @return Integer
	 * @throws Exception
	 */
	int deleteFuncItem(@Param("functionId") Integer functionId) throws Exception;

	/**
	 * 刪除功能分類項目.
	 * 
	 * @param functionId
	 * @return Integer
	 * @throws Exception
	 */
	int deleteFuncDiv(@Param("functionId") Integer functionId) throws Exception;

	/**
	 * 刪除角色.
	 * 
	 * @param roleId
	 * @return Integer
	 * @throws Exception
	 */
	int deleteRoleFuncAuth(@Param("roleId") String roleId) throws Exception;

	/**
	 * 新增角色.
	 * 
	 * @param roleFuncAuth
	 * @return Integer
	 * @throws Exception
	 */
	int insertRoleFuncAuth(@Param("entity") RoleFuncAuthVo roleFuncAuth) throws Exception;

	/**
	 * 取得使用者帳號清單.
	 * 
	 * @param sysId
	 * @return List<UserRepresentationEntity>
	 * @throws Exception
	 */
	List<UserRepresentationEntity> getSystemUsers(@Param("sysId") String sysId) throws Exception;

	/**
	 * 取得所有帳號權限報表資料.
	 * 
	 * @param userId
	 * @return List<UserAuth>
	 * @throws Exception
	 */
	List<UserAuthVoEntity> getUsersAuthReport(@Param("userId") String userId) throws Exception;

	List<UserVo> getDeputyUser(@Param("userId") String userId) throws Exception;
}
