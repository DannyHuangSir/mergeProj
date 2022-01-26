package com.twfhclife.adm.controller.auth.base;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.service.IDeptMgntService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.generic.util.MenuUtil;

/**
 * 基本資料管理-部門管理.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class DeptMgntController extends BaseController {

	private static final Logger logger = LogManager.getLogger(DeptMgntController.class);

	@Autowired
	private IDeptMgntService deptMgntService;

	/**
	 * 部門管理查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "265",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/deptMgnt")
	public String funcMgnt() {
		return "backstage/auth/base/deptMgnt";
	}

	/**
	 * 取得部門清單.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/deptMgnt/getDepts")
	public ResponseEntity<ResponseObj> getDepts() {
		try {
			List<DepartmentVo> dataList = deptMgntService.getDepts();
			if (!CollectionUtils.isEmpty(dataList)) {
				processSuccess(new MenuUtil().convertDeptAceTree(dataList));
			} else {
				logger.info("{}", "Unable to get all departments");
				processSuccessMsg("請先新增根目錄");
			}
		} catch (Exception e) {
			logger.error("Unable to geDepts: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得上一層部門清單.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/deptMgnt/getParentDepList")
	public ResponseEntity<ResponseObj> getParentDepList(@RequestBody DepartmentVo departmentVo) {
		try {
			processSuccess(deptMgntService.getParentDepList(departmentVo));
		} catch (Exception e) {
			logger.error("Unable to getParentDepList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 新增部門.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.DEPTMGT_002, 
			sqlId = EventCodeConstants.DEPTMGT_002_SQL_ID,
			daoVoParamKey = "departmentVo",
			systemEventParams = {}
			))
	@PostMapping("/deptMgnt/insertDepartment")
	public ResponseEntity<ResponseObj> insertDepartment(@RequestBody DepartmentVo departmentVo) {
		try {
			departmentVo.setCreateUser(getUserId());
			departmentVo.setModifyUser(getUserId());
			
			if (deptMgntService.isDeptNameExist(departmentVo)) {
				processError("部門名稱重覆");
			} else {
				int result = deptMgntService.addDepartment(departmentVo);
				if (result > 0) {
					processSuccessMsg("新增成功");
				} else {
					processError("新增失敗");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to insertDepartment: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 更新部門.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.DEPTMGT_003, 
			sqlId = EventCodeConstants.DEPTMGT_003_SQL_ID,
			daoVoParamKey = "departmentVo",
			systemEventParams = {}
			))
	@PostMapping("/deptMgnt/updateDepartment")
	public ResponseEntity<ResponseObj> updateDepartment(@RequestBody DepartmentVo departmentVo) {
		try {
			departmentVo.setModifyUser(getUserId());
			
			if (deptMgntService.isDeptNameExist(departmentVo)) {
				processError("部門名稱重覆");
			} else {
				int result = deptMgntService.updateDepartment(departmentVo);
				if (result > 0) {
					processSuccessMsg("更新成功");
				} else {
					processError("更新失敗");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to updateDepartment: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 刪除部門.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.DEPTMGT_004, 
			sqlId = EventCodeConstants.DEPTMGT_004_SQL_ID,
			sqlParams = {
					@SqlParam(requestParamkey = "depId", sqlParamkey = "depId") 
			},
			systemEventParams = {}
			))
	@PostMapping("/deptMgnt/deleteDepartment")
	public ResponseEntity<ResponseObj> deleteDepartment(@RequestBody DepartmentVo departmentVo) {
		try {
			int result = deptMgntService.deleteDepartment(departmentVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteDepartment: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
