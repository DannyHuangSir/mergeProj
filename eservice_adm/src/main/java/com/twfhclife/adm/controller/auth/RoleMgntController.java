package com.twfhclife.adm.controller.auth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.RoleMgntVo;
import com.twfhclife.adm.model.RoleVo;
import com.twfhclife.adm.service.IRoleMgntService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 權限管理-角色權限維護.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class RoleMgntController extends BaseController {

	private static final Logger logger = LogManager.getLogger(RoleMgntController.class);

	@Autowired
	private IRoleMgntService roleMgntService;

	/**
	 * 角色權限查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "192",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/roleMgnt")
	public String roleMgnt() {
		return "backstage/auth/roleMgnt";
	}

	/**
	 * 更新角色權限
	 * 
	 * @param roleMrgntVo RoleMgntVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/roleMgnt/updateRole")
	public ResponseEntity<ResponseObj> updateRole(@RequestBody RoleMgntVo roleMrgntVo) {
		try {
			roleMgntService.updateRole(roleMrgntVo, getUserId());
			processSuccessMsg("更新成功");
		} catch (Exception e) {
			logger.error("Unable to updateRole: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@RequestMapping("/roleMgnt/downloadRoleFuncAuth")
	public void downloadRoleFuncAuth(@RequestParam("sysId") String sysId, @RequestParam("roleName") String roleName,
			HttpServletResponse response) {
		RoleVo roleVo = new RoleVo();
		roleVo.setRoleName(roleName);
		roleVo.setSystemId(sysId);
		
		String userName = getUserId();
		String keyCloakUserId = getLoginUser().getId();
		
		List<Map<String, Object>> result = roleMgntService.getRoleFuncAuth(roleVo, userName, keyCloakUserId);

		// 開始準備產生csv
		String DATE_FORMAT = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String timeStr = sdf.format(c1.getTime());
		String csvFileName = "exportRoleFunctions_"+timeStr+".csv";// 預設的檔名

		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setCharacterEncoding("utf-8");
		response.setHeader(headerKey, headerValue);
		CSVPrinter csvFilePrinter = null;

		// 定義標題列 SYS_ID, Role_ID, ROLE_NAME, FUNCTION_ID, FUNCTION_NAME
		Object[] headers = { "系統別", "角色ID", "角色名稱", "功能別", "功能名稱" };
		try {
			//Create the CSVFormat object with "\n" as a record delimiter

			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			response.getWriter().print('\ufeff'); //write BOM
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			//寫入header
			csvFilePrinter.printRecord(headers);

			// 寫入List每一行資料
			for (Map<String, Object> roleMap : result) {
				csvFilePrinter.printRecord(roleMap.get("SYS_ID"), roleMap.get("ROLE_ID"), 
						roleMap.get("ROLE_NAME"), roleMap.get("FUNCTION_ID"), roleMap.get("FUNCTION_NAME"));
			}
			csvFilePrinter.flush();
			csvFilePrinter.close();
		} catch (Exception e) {
			logger.error("Unable to downloadRoleFuncAuth: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}