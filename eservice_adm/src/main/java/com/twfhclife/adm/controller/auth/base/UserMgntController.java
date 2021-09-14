package com.twfhclife.adm.controller.auth.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.UserDepartmentVo;
import com.twfhclife.adm.model.UserDeputyVo;
import com.twfhclife.adm.model.UserEntityVo;
import com.twfhclife.adm.model.UserRoleVo;
import com.twfhclife.adm.service.IAuthorizationService;
import com.twfhclife.adm.service.IUserDepartmentService;
import com.twfhclife.adm.service.IUserDeputyService;
import com.twfhclife.adm.service.IUserMgntService;
import com.twfhclife.adm.service.IUserRoleService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyStringUtil;

/**
 * 人員管理.
 * 
 * @author all
 */
@Controller
public class UserMgntController extends BaseController {

	private static final Logger logger = LogManager.getLogger(UserMgntController.class);

	@Autowired
	private IAuthorizationService authorizationService;

	@Autowired
	private IUserMgntService userMgntService;

	@Autowired
	private IUserDepartmentService userDepartmentService;

	@Autowired
	private IUserRoleService userRoleService;
	
	@Autowired
	private IUserDeputyService userDeputyService;
	
	/**
	 * 人員管理頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "303",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/userMgnt")
	public String loginRecord() {
		return "backstage/auth/base/userMgnt";
	}

	/**
	 * 取得人員管理查詢結果.
	 * 
	 * @param vo UserEntityVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/getUserEntityPageList")
	public ResponseEntity<PageResponseObj> getUserEntityPageList(@RequestBody UserEntityVo userEntityVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserEntityVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			userEntityVo.setId(getLoginUser().getId());
			List<Map<String, Object>> userAuthList = userMgntService.getUserEntityPageList(getUserId(), userEntityVo);
			
			// 設定使用者的系統跟角色權限清單
			authorizationService.setUserSystemRoleAuth(userAuthList);
			
			pageResp.setRows(userAuthList);
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(userMgntService.getUserEntityPageTotal(getUserId(), userEntityVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(userEntityVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + userEntityVo.getRows() - 1) / userEntityVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getUserEntityPageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 取得人員部門管理查詢結果.
	 * 
	 * @param vo UserDepartmentVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/getUserDepartmentPageList")
	public ResponseEntity<PageResponseObj> getUserDepartmentPageList(@RequestBody UserDepartmentVo userDepartmentVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserDepartmentVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(userDepartmentService.getUserDepartmentPageList(userDepartmentVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(userDepartmentService.getUserDepartmentPageTotal(userDepartmentVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(userDepartmentVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + userDepartmentVo.getRows() - 1) / userDepartmentVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getUserDepartmentPageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 人員部門管理-新增.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/insertUserDepartment")
	public ResponseEntity<ResponseObj> insertUserDepartment(@RequestBody UserDepartmentVo userDepartmentVo) {
		try {
			int result = userDepartmentService.insertUserDepartment(userDepartmentVo);
			if (result > 0) {
				processSuccessMsg("新增成功");
			} else {
				processError("新增失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to insertUserDepartment: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 人員部門管理-刪除.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/deleteUserDepartment")
	public ResponseEntity<ResponseObj> deleteUserDepartment(@RequestBody UserDepartmentVo userDepartmentVo) {
		try {
			int result = userDepartmentService.deleteUserDepartment(userDepartmentVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteUserDepartment: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得人員角色管理查詢結果.
	 * 
	 * @param vo UserRoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/getUserRolePageList")
	public ResponseEntity<PageResponseObj> getUserRolePageList(@RequestBody UserRoleVo userRoleVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserRoleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(userRoleService.getUserRolePageList(userRoleVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(userRoleService.getUserRolePageTotal(userRoleVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(userRoleVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + userRoleVo.getRows() - 1) / userRoleVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getUserRolePageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 人員角色管理-新增.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/insertUserRole")
	public ResponseEntity<ResponseObj> insertUserRole(@RequestBody UserRoleVo userRoleVo) {
		try {
			int result = userRoleService.insertUserRole(userRoleVo);
			if (result > 0) {
				processSuccessMsg("新增成功");
			} else {
				processError("新增失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to insertUserRole: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 人員角色管理-刪除.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/deleteUserRole")
	public ResponseEntity<ResponseObj> deleteUserRole(@RequestBody UserRoleVo userRoleVo) {
		try {
			int result = userRoleService.deleteUserRole(userRoleVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteUserRole: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 取得人員代理人查詢結果
	 * @param vo userDeputyVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/getUserDeputyPageList")
	public ResponseEntity<PageResponseObj> getUserDeputyList(@RequestBody UserDeputyVo userDeputyVo){
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserDeputyVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(userDeputyService.getUserDeputyPageList(userDeputyVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(userDeputyService.getUserDeputyPageTotal(userDeputyVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(userDeputyVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + userDeputyVo.getRows() - 1) / userDeputyVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch(Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getUserRolePageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
	
	/**
	 * 人員管理-新增代理人
	 * @param userDeputyVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/addUserDeputy")
	public ResponseEntity<ResponseObj> addUserDeputy(@RequestBody UserDeputyVo userDeputyVo){
		try {
			if(StringUtils.isEmpty(userDeputyVo.getUserId())
					|| StringUtils.isEmpty(userDeputyVo.getDeputyId())) {
				processError("代理人設置不正確，請確認");
			} else {
				int result = userDeputyService.insertUserDeputy(userDeputyVo);
				if(result > 0) {
					processSuccessMsg("新增成功");
				} else {
					processError("新增失敗");
				}
			}
		} catch(Exception e) {
			logger.error("Unable to addUserDeputy: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@PostMapping("/userMgnt/getUserCanDeputy")
	public ResponseEntity<ResponseObj> getUserCanDeputyList(@RequestBody UserDeputyVo userDeputyVo){
		try {
			if(StringUtils.isEmpty(userDeputyVo.getUserId())) {
				processError("沒有選擇使用者，請確認");
			} else {
				Object resultData = userDeputyService.getCanDeputyUser(userDeputyVo);
				if(resultData == null) {
					processError("目前沒有可以使用的代理人");
				} else {
					processSuccess(resultData);
				}
			}
		} catch(Exception e) {
			logger.error("Unable to getUserCanDeputyList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 刪除代理人
	 * @param userDeputyVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/userMgnt/deleteUserDeputy")
	public ResponseEntity<ResponseObj> deleteUserDeputy(@RequestBody UserDeputyVo userDeputyVo){
		try {
			if(MyStringUtil.isNotNullOrEmpty(userDeputyVo.getId()) && userDeputyVo.getId().matches("[0-9]+")) {
				Integer intId = Integer.parseInt(userDeputyVo.getId());
				int result = userDeputyService.deleteDeputyById(intId);
				if(result > 0) {
					processSuccessMsg("刪除成功");
				} else {
					processError("刪除失敗");
				}
			} else {
				processError("刪除失敗");
			}
		} catch(Exception e) {
			logger.error("Unable to deleteDeputy: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
