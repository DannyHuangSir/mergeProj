package com.twfhclife.eservice_api.service;

import com.twfhclife.eservice.api.adm.domain.*;
import com.twfhclife.eservice.api.adm.model.DepartmentVo;
import com.twfhclife.eservice.api.adm.model.JobTitleVo;
import com.twfhclife.eservice.api.elife.domain.BxczLoginRequest;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.model.UserVo;

import java.util.List;
import java.util.Map;

/**
 * Definite authorization service.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public interface IAuthoService {
	
	List<Map<String, Object>> getFuncDivAuthByUser(UserFuncAuthReqVo vo) throws Exception;

	/**
	 * 以使用者帳號查詢所有權限資料.
	 * 
	 * @param userId
	 * @param sysId
	 * @return List<Function>
	 * @throws Exception
	 */
	List<Function> getAuthInfoByUser(String userId, String sysId) throws Exception;

	/**
	 * 查詢使用者可使用的系統清單.
	 * 
	 * @param userId
	 * @return List<Systems>
	 * @throws Exception
	 */
	List<Systems> getSysListByUser(String userId) throws Exception;

	/**
	 * 查詢系統功能清單.
	 * 
	 * @param sysId
	 * @return List<Function>
	 * @throws Exception
	 */
	List<Function> getFunListBySys(String sysId) throws Exception;

	/**
	 * 查詢角色清單.
	 * 
	 * @param roleNameQuery
	 * @return List<Role>
	 * @throws Exception
	 */
	List<Role> getRoleListByRoleName(String roleNameQuery) throws Exception;
	
	/**
	 * 查詢角色功能權限.
	 * 
	 * @param roleId
	 * @param roleName
	 * @param sysId
	 * @return List<Function>
	 * @throws Exception
	 */
	List<Function> getRoleFunctionAuthByRole(String roleId, String roleName, String sysId) throws Exception;
	
	/**
	 * 查詢使用者的系統功能權限.
	 * 
	 * @param userId
	 * @param sysId
	 * @return List<Function>
	 * @throws Exception
	 */
	List<Function> getSysFunctionAuthByUser(String userId, String sysId) throws Exception;
	
	/**
	 * 新增功能項目.
	 * 
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	boolean addFuncItem(FuncItemReqObj request) throws Exception;
	
	/**
	 * 新增或修改功能清單.
	 * 
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	boolean addFuncList(FuncListReqObj request) throws Exception;
	
	/**
	 * 修改功能項目.
	 * 
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	boolean updateFunctionItem(FuncItemReqObj request) throws Exception;
	
	/**
	 * 刪除功能項目.
	 * 
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	boolean deleteFuncItem(Function request) throws Exception;
	
	/**
	 * 修改角色權限.
	 * 
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	boolean updateRoleFuncAuth(RoleFuncAuthReqObj request) throws Exception;
	
	/**
	 * 取得使用者帳號清單.
	 * 
	 * @param sysId
	 * @return List<UserRepresentation>
	 * @throws Exception
	 */
	List<UserRepresentation> getSystemUsers(String sysId) throws Exception;
	
	/**
	 * 取得所有帳號權限報表資料.
	 * 
	 * @param userId
	 * @return List<UserAuth>
	 * @throws Exception
	 */
	List<UserAuth> getUsersAuthReport(String userId) throws Exception;

	List<DepartmentVo> getDepartmentsByUser(String userId) throws Exception;
	
	List<JobTitleVo> getJobTitles() throws Exception;
	
	List<DepartmentVo> getAllDepartments() throws Exception;
	
	List<UserVo> getUserByDepTitle(String realm, String depId, String titleId, String userId) throws Exception;

    ApiResponseObj<String> doPostPbs102(BxczLoginRequest req);
}
