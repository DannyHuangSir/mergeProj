package com.twfhclife.adm.controller.auth.base;

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
import com.twfhclife.adm.model.RoleVo;
import com.twfhclife.adm.service.IRoleService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 基本資料管理-使用者角色管理.
 * 
 * @author all
 */
@Controller
public class RoleController extends BaseController {

	private static final Logger logger = LogManager.getLogger(RoleController.class);

	@Autowired
	private IRoleService roleService;
	
	/**
	 * 使用者角色管理頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "302",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/role")
	public String loginRecord() {
		return "backstage/auth/base/role";
	}

	/**
	 * 取得使用者角色管理查詢結果.
	 * 
	 * @param roleVo RoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/role/getRoleDetail")
	public ResponseEntity<PageResponseObj> getRoleDetail(@RequestBody RoleVo vo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			String userName = getUserId();
			String keyCloakUserId = getLoginUser().getId();
			
			// Note: RoleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(roleService.getRolePageList(vo, userName, keyCloakUserId));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(roleService.getRolePageTotal(vo, userName, keyCloakUserId));
			// 設定jqGrid 目前頁數
			pageResp.setPage(vo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getRoleDetail: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 使用者角色管理-新增.
	 * 
	 * @param roleVo RoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/role/insertRole")
	public ResponseEntity<ResponseObj> insertRole(@RequestBody RoleVo roleVo) {
		try {
			roleVo.setCreateUser(getUserId());
			roleVo.setModifyUser(getUserId());
			
			int result = roleService.insertRole(roleVo);
			if (result > 0) {
				processSuccessMsg("新增成功");
			} else {
				processError("新增失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to insertRole: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 使用者角色管理-更新.
	 * 
	 * @param roleVo RoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/role/updateRole")
	public ResponseEntity<ResponseObj> updateRole(@RequestBody RoleVo roleVo) {
		try {
			roleVo.setModifyUser(getUserId());
			int result = roleService.updateRole(roleVo);
			if (result > 0) {
				processSuccessMsg("更新成功");
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to updateRole: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 使用者角色管理-刪除.
	 * 
	 * @param roleVo RoleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/role/deleteRole")
	public ResponseEntity<ResponseObj> deleteRole(@RequestBody RoleVo roleVo) {
		try {
			int result = roleService.deleteRole(roleVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteRole: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}