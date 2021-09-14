package com.twfhclife.adm.controller.common;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.service.IAuthorizationService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;

/**
 * 共用選單資料控制器.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class RoleTreeController extends BaseController {

	private static final Logger logger = LogManager.getLogger(OptionController.class);

	@Autowired
	private IAuthorizationService authorizationService;

	/**
	 * 取得使用者角色全部功能清單.
	 * 
	 * @param vo UserEntityVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/getUserFunctionAuth")
	public ResponseEntity<ResponseObj> getUserFunctionAuth(@RequestParam("roleIds") List<String> roleIds) {
		try {
			processSuccess(authorizationService.getRoleTree(roleIds, true));
		} catch (Exception e) {
			logger.error("Unable to getUserFunctionAuth: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得使用者功能清單(依據角色代碼).
	 * 
	 * @param vo UserEntityVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/getFunctionAuthByRoleId")
	public ResponseEntity<ResponseObj> getFunctionAuthByRoleId(@RequestParam("roleId") String roleId) {
		try {
			processSuccess(authorizationService.getRoleTree(Arrays.asList(roleId), false));
		} catch (Exception e) {
			logger.error("Unable to getUserFunctionAuth: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}