package com.twfhclife.eservice.api.adm.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.adm.domain.AuthoReqObj;
import com.twfhclife.eservice.api.adm.domain.FuncListReqObj;
import com.twfhclife.eservice.api.adm.domain.Function;
import com.twfhclife.eservice.api.adm.domain.Role;
import com.twfhclife.eservice.api.adm.domain.RoleFuncAuthReqObj;
import com.twfhclife.eservice.api.adm.domain.SystemUserRequestVo;
import com.twfhclife.eservice.api.adm.domain.Systems;
import com.twfhclife.eservice.api.adm.domain.UserAuth;
import com.twfhclife.eservice.api.adm.domain.UserFuncAuthReqVo;
import com.twfhclife.eservice.api.adm.domain.UserRepresentation;
import com.twfhclife.eservice.api.adm.model.DepartmentVo;
import com.twfhclife.eservice.api.adm.model.JobTitleVo;
import com.twfhclife.eservice.api.adm.model.UserEntityVo;
import com.twfhclife.eservice.api.adm.service.IUserMgntService;
import com.twfhclife.eservice_api.service.IAuthoService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.PageResponseObj;
import com.twfhclife.generic.domain.ResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.domain.UserDepartmentRequest;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyStringUtil;

/**
 * Definite authorization controller.
 * 
 * @author tcMarcus
 * @version 1.0
 */
@RestController
//@RequestMapping("autho")
public class AuthoController extends BaseController {
	
	private static final Logger logger = LogManager.getLogger(AuthoController.class);
	
	@Autowired
	private IAuthoService authoService;
	
	@Autowired
	private IUserMgntService userMgntService;

//	@Autowired
//	public AuthoController(IAuthoService authoService) {
//
//		this.authoService = authoService;
//	}
	
	@ApiRequest
	@PostMapping(value = "/user/function-auth", produces = { "application/json" })
	public ResponseEntity<ApiResponseObj<List<Map<String, Object>>>> getUserFunctionsAuth(
			@Valid @RequestBody UserFuncAuthReqVo req) {
		ApiResponseObj<List<Map<String, Object>>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();

		List<Map<String, Object>> res = null;
		try {
			if(MyStringUtil.isNullOrEmpty(req.getUserId()) && MyStringUtil.isNullOrEmpty(req.getUserName())) {
				logger.error("/user/function-auth: userId & userName cannot null!");
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "userId 或 userName 請擇一輸入!", "", "");
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			if(req.getIsAdmin() != null && req.getIsAdmin().equals("Y")) {
				logger.debug("/user/function-auth invoked by admin");
//				userId = null; //20181005 增加判斷user為admin時，不篩選user role的權限(SQL中遇到userId=null時會屏蔽該條件)
			}
			res = authoService.getFuncDivAuthByUser(req);

			if (res == null || res.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料，請確認該使用者是否已設定角色權限?", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(res);
			}
		} catch (Exception e) {
			// ERROR
			logger.error(e.getMessage(), e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	/**
	 * 以使用者帳號查詢所有權限資料.
	 * <p>
	 * Restful RUL : /autho/getauthinfobyuser
	 * 
	 * @param request
	 *            userId, sysId
	 * @return ResponseEntity<ResponseObj>
	 */
//	@ApiRequest
//	@PostMapping(value = "/autho/user/menulist", produces = { "application/json" })
//	public ResponseEntity<ResponseObj> getAuthInfoByUser(@RequestBody AuthoReqObj request) {
//
//		ResponseEntity<ResponseObj> response = null;
//		List<Function> functions = null;
//
//		try {
//			// Invoke DB process
//			functions = authoService.getAuthInfoByUser(request.getUserId(), request.getSysId());
//			List<FunctionVo> menuList = loginService.getMenuList(username, keycloakUser.getId());
//			// Handling data not found condition
//			response = handlingDataNotFound(functions);
//
//			// Setting successful response
//			if (response == null) {
//				response = createSuccessResp(functions);
//			}
//		} catch (Exception e) {
//			response = doExceptionHandling(e);
//		}
//
//		return response;
//	}

	/**
	 * 查詢使用者可使用的系統清單.
	 * <p>
	 * Restful RUL : /autho/getsyslistbyuser
	 * 
	 * @param request
	 *            userId
	 * @return ResponseEntity<ResponseObj>
	 */
	@PostMapping(value = "/user/sys-auth", produces = { "application/json" })
	public ResponseEntity<ApiResponseObj<List<Systems>>> getSysListByUser(@Valid @RequestBody UserFuncAuthReqVo req) {
		ApiResponseObj<List<Systems>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();

		List<Systems> systems = null;

		try {
			// Invoke DB process
			systems = authoService.getSysListByUser(req.getUserId());
			if (systems == null || systems.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(systems);
			}

		} catch (Exception e) {
			// ERROR
			logger.error(e.getMessage(), e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 查詢系統功能清單.
	 * <p>
	 * Restful RUL : /autho/getfunlistbysys
	 * 
	 * @param request
	 *            sysId
	 * @return ResponseEntity<ResponseObj>
	 */
	@PostMapping(value = "/system/functions", produces = { "application/json" })
	public ResponseEntity<?> getFunListBySys(@RequestBody AuthoReqObj request) {
		ApiResponseObj<List<Function>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();

		List<Function> functions = null;

		try {
			// Invoke DB process
			functions = authoService.getFunListBySys(request.getSysId());
			if (functions == null || functions.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(functions);
			}
		} catch (Exception e) {
			// ERROR
			logger.error(e.getMessage(), e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 查詢角色清單.
	 * <p>
	 * Restful RUL : /autho/getrolelistbyrolename
	 * 
	 * @param request
	 *            roleNameQuery
	 * @return ResponseEntity<ResponseObj>
	 */
	@PostMapping(value = "/role/searches", produces = { "application/json" })
	public ResponseEntity<?> getRoleListByRoleName(@RequestBody AuthoReqObj request) {
		ApiResponseObj<List<Role>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		List<Role> roles = null;

		try {
			// Invoke DB process
			roles = authoService.getRoleListByRoleName(request.getRoleNameQuery());
			if (roles == null || roles.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(roles);
			}
		} catch (Exception e) {
			// ERROR
			logger.error(e.getMessage(), e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 查詢角色功能權限.
	 * <p>
	 * Restful RUL : /role/authinfo
	 * 
	 * @param request
	 *            roleId, roleName, sysId
	 * @return ResponseEntity<ResponseObj>
	 */
	@PostMapping(value = "/role/authinfo", produces = { "application/json" })
	public ResponseEntity<?> getRoleFunctionAuthByRole(@RequestBody AuthoReqObj request) {
		ApiResponseObj<List<Function>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		List<Function> functions = null;

		try {
			// Invoke DB process
			functions = authoService.getRoleFunctionAuthByRole(request.getRoleId(), request.getRoleName(),
					request.getSysId());
			if (functions == null || functions.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(functions);
			}
		} catch (Exception e) {
			// ERROR
			logger.error(e.getMessage(), e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 查詢使用者的系統功能權限.
	 * <p>
	 * Restful RUL : /autho/getsysfunctionauthbyuser
	 * 
	 * @param request
	 *            userId, sysId
	 * @return ResponseEntity<ResponseObj>
	 */
//	@PostMapping(value = "/getsysfunctionauthbyuser", produces = { "application/json" })
//	public ResponseEntity<ResponseObj> getSysFunctionAuthByUser(@RequestBody AuthoReqObj request) {
//
//		ResponseEntity<ResponseObj> response = null;
//		List<Function> functions = null;
//
//		try {
//			// Invoke DB process
//			functions = authoService.getSysFunctionAuthByUser(request.getUserId(), request.getSysId());
//
//			// Handling data not found condition
//			response = handlingDataNotFound(functions);
//
//			// Setting successful response
//			if (response == null) {
//				response = createSuccessResp(functions);
//			}
//		} catch (Exception e) {
//			response = doExceptionHandling(e);
//		}
//
//		return response;
//	}

	/**
	 * 新增功能項目.
	 * <p>
	 * Restful RUL : /autho/addfuncitem
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
//	@PostMapping(value = "/addfuncitem", produces = { "application/json" })
//	public ResponseEntity<ResponseObj> addFuncItem(@RequestBody FuncItemReqObj request) {
//
//		ResponseEntity<ResponseObj> response = null;
//
//		try {
//			// Invoke DB process
//			boolean isSuccess = authoService.addFuncItem(request);
//
//			// Setting successful response
//			if (isSuccess) {
//				response = createSuccessResp(null);
//			}
//		} catch (Exception e) {
//			response = doExceptionHandling(e);
//		}
//
//		return response;
//	}

	/**
	 * 新增或修改功能清單.
	 * <p>
	 * Restful RUL : /autho/addfunclist
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
	@PostMapping(value = "/addfunclist", produces = { "application/json" })
	public ResponseEntity<ResponseObj> addFuncList(@RequestBody FuncListReqObj request) {

		ResponseEntity<ResponseObj> response = null;

		try {
			// Invoke DB process
			boolean isSuccess = authoService.addFuncList(request);

			// Setting successful response
			if (isSuccess) {
				response = createSuccessResp(null);
			}
		} catch (Exception e) {
			response = doExceptionHandling(e);
		}

		return response;
	}

	/**
	 * 修改功能項目.
	 * <p>
	 * Restful RUL : /autho/updatefunctionitem
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
//	@PostMapping(value = "/updatefunctionitem", produces = { "application/json" })
//	public ResponseEntity<ResponseObj> updateFunctionItem(@RequestBody FuncItemReqObj request) {
//
//		ResponseEntity<ResponseObj> response = null;
//
//		try {
//			// Invoke DB process
//			boolean isSuccess = authoService.updateFunctionItem(request);
//
//			// Setting successful response
//			if (isSuccess) {
//				response = createSuccessResp(null);
//			}
//		} catch (Exception e) {
//			response = doExceptionHandling(e);
//		}
//
//		return response;
//	}

	/**
	 * 刪除功能項目.
	 * <p>
	 * Restful RUL : /autho/deletefuncitem
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
//	@ApiRequest
//	@PostMapping(value = "/deletefuncitem", produces = { "application/json" })
//	public ResponseEntity<ResponseObj> deleteFuncItem(@RequestBody Function request) {
//
//		ResponseEntity<ResponseObj> response = null;
//
//		try {
//			// Invoke DB process
//			boolean isSuccess = authoService.deleteFuncItem(request);
//
//			// Setting successful response
//			if (isSuccess) {
//				response = createSuccessResp(null);
//			}
//		} catch (Exception e) {
//			response = doExceptionHandling(e);
//		}
//
//		return response;
//	}

	/**
	 * 修改角色權限.
	 * <p>
	 * Restful RUL : /autho/updaterolefuncauth
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
	@ApiRequest
	@PostMapping(value = "/updaterolefuncauth", produces = { "application/json" })
	public ResponseEntity<ResponseObj> updateRoleFuncAuth(@RequestBody RoleFuncAuthReqObj request) {

		ResponseEntity<ResponseObj> response = null;

		try {
			// Invoke DB process
			boolean isSuccess = authoService.updateRoleFuncAuth(request);

			// Setting successful response
			if (isSuccess) {
				response = createSuccessResp(null);
			}
		} catch (Exception e) {
			response = doExceptionHandling(e);
		}

		return response;
	}

	@ApiRequest
	@PostMapping(value = "/system/users", produces = { "application/json" })
	public ResponseEntity<?> getSystemUsers(@Valid @RequestBody SystemUserRequestVo vo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<PageResponseObj<Map<String, Object>>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PageResponseObj<Map<String, Object>> pageResp = new PageResponseObj<>();
		String realm = null;
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/system/users:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getUserId());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			if(vo.getIsAdmin() == null) {
				vo.setIsAdmin(false);
			}
			UserEntityVo userEntityVo = new UserEntityVo();
			if (vo.getSysId().equals("eservice")) {
				realm = "elife";
			} else {
				realm = "twfhclife";
			}
			userEntityVo.setRealmId(realm);
			userEntityVo.setId(vo.getUserId());
			userEntityVo.setUsername(vo.getUserNameQuery());
//			List<Map<String, Object>> userAuthList = userMgntService.getUserEntityPageList(userEntityVo, vo.getIsAdmin());

			// 查詢當頁資料集合
			List<Map<String, Object>> resultList = userMgntService.getUserEntityPageList(userEntityVo, vo.getIsAdmin());
			pageResp.setPageData(resultList);
			
			// 設定使用者的系統跟角色權限清單
			//authorizationService.setUserSystemRoleAuth(userAuthList);
						
			// 資料總筆數
			int totalRecords = userMgntService.getUserEntityPageTotal(userEntityVo, vo.getIsAdmin());
			pageResp.setTotalRecords(totalRecords);

			if (vo.getRows() != null && vo.getPage() != null) {
				// 分頁查詢
				// 目前頁數
				pageResp.setPage(vo.getPage());
				// 總頁數
				pageResp.setTotalPage((pageResp.getTotalRecords() + vo.getRows() - 1) / vo.getRows());
			} else {
				// 查詢全部
				// 目前頁數
				pageResp.setPage(1);
				// 總頁數
				pageResp.setTotalPage(1);
			}

			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setResult(pageResp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getParameterPageList: {}", ExceptionUtils.getStackTrace(e));
			//Map<String, Object> error = Collections.singletonMap("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	/**
	 * 取得使用者帳號清單.
	 * <p>
	 * Restful RUL : /autho/getsystemusers
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
	@ApiRequest
	@PostMapping(value = "/getsystemusers", produces = { "application/json" })
	public ResponseEntity<ResponseObj> getSystemUsers(@RequestBody AuthoReqObj request) {

		ResponseEntity<ResponseObj> response = null;
		List<UserRepresentation> userRepresentations = null;

		try {
			// Invoke DB process
			userRepresentations = authoService.getSystemUsers(request.getSysId());

			// Handling data not found condition
			response = handlingDataNotFound(userRepresentations);

			// Setting successful response
			if (response == null) {
				response = createSuccessResp(userRepresentations);
			}
		} catch (Exception e) {
			response = doExceptionHandling(e);
		}

		return response;
	}

	/**
	 * 取得所有帳號權限報表資料.
	 * <p>
	 * Restful RUL : /autho/getusersauthreport
	 * 
	 * @param request
	 * @return ResponseEntity<ResponseObj>
	 */
	@ApiRequest
	@PostMapping(value = "/getusersauthreport", produces = { "application/json" })
	public ResponseEntity<ResponseObj> getUsersAuthReport(@RequestBody AuthoReqObj request) {

		ResponseEntity<ResponseObj> response = null;
		List<UserAuth> userAuths = null;

		try {
			// Invoke DB process
			userAuths = authoService.getUsersAuthReport(request.getUserId());

			// Handling data not found condition
			response = handlingDataNotFound(userAuths);

			// Setting successful response
			if (response == null) {
				response = createSuccessResp(userAuths);
			}
		} catch (Exception e) {
			response = doExceptionHandling(e);
		}

		return response;
	}

	/**
	 * 查詢所有職位清單
	 * @param secret
	 * @return
	 */
	@ApiRequest
	@GetMapping(value = "/autho/jobtitles", produces = { "application/json" })
	public ResponseEntity<?> getJobTitles(
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<List<JobTitleVo>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		List<JobTitleVo> titleList = null;
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/autho/jobtitles:" + ApConstants.INVALID_SECRET);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			// Invoke DB process
			titleList = authoService.getJobTitles();

			// Handling data not found condition
			if (titleList == null || titleList.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(titleList);
			}

		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	

	/**
	 * 查詢user的部門
	 * (查詢有權限的部門，故下層部門也會被撈出)
	 * (userId傳入all則可查詢全部部門)
	 * @param userId
	 * @param secret
	 * @return
	 */
	@ApiRequest
	@GetMapping(value = "/autho/departments/{userId}", produces = { "application/json" })
	public ResponseEntity<?> getDepartmentsByUser(
			@PathVariable("userId") String userId,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<List<DepartmentVo>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		List<DepartmentVo> depList = null;
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/autho/departments/{userId}:" + ApConstants.INVALID_SECRET + ", userId=" + userId);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			
			if(userId.equals("all")) {
				logger.debug("API autho/departments invoked: query all departments.");
				depList = authoService.getAllDepartments();
			} else {
				logger.debug("API autho/departments invoked: query by userId=" + userId);
				depList = authoService.getDepartmentsByUser(userId);
			}

			// Handling data not found condition
			if (depList == null || depList.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(depList);
			}

		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 以depId/titleId/userId查詢員工
	 * @param request
	 * @param secret
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/autho/department/title/user", produces = { "application/json" })
	public ResponseEntity<?> getUserByDepTitle(@RequestBody UserDepartmentRequest request,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<List<UserVo>> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		List<UserVo> userList = null;
		try {
			if (MyStringUtil.isNullOrEmpty(request.getDepId()) && MyStringUtil.isNullOrEmpty(request.getTitleId())
					&& MyStringUtil.isNullOrEmpty(request.getUserId())) {
				this.setErrorMessages("depId/titleId/userId須擇一輸入");
				logger.error("/autho/department/title/user:" + this.errorMessages);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/autho/department/title/user:" + ApConstants.INVALID_SECRET);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			// Invoke DB process
			userList = authoService.getUserByDepTitle(ApConstants.REALM_TWFHCLIFE, request.getDepId(),
					request.getTitleId(), request.getUserId());

			// Handling data not found condition
			if (userList == null || userList.size() == 0) {
				returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "查無資料", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				response.setResult(userList);
			}

		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
