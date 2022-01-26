package com.twfhclife.eservice.onlineChange.controller;

import java.security.InvalidParameterException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeNoteUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.ValidateUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

/**
 * 修改密碼
 * (無保單)
 */
@Controller
@EnableAutoConfiguration
public class ChangePasswordController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ChangePasswordController.class);
	
	@Autowired
	private IRegisterUserService registerUaerService;
	
	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@RequestLog
	@GetMapping("/changePassword1")
	public String changePassword1() {
		try {
			//備註
			String changePassword = getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE, OnlineChangeNoteUtil.CHANGE_PD_NOTE1_CODE);
			addAttribute("changePasswordNote", changePassword);
			UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
			//TODO: add error message parameter
			if (userDetail.getLastChangPasswordDate() == null) {
				addAttribute("errorMessage", "為確保您的帳號安全，請立即變更密碼");
			}
		} catch (Exception e) {
			logger.error("changeInfo1 error! {}", e);
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "492");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/changePassword/change-password1";
	}
	
	/**
	 * 密碼修改
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestLog
	@PostMapping("/changePassword1/updatePassword")
	public ResponseEntity<ResponseObj> updatePassword(@RequestBody UsersVo users) {
		String message = "";
		try {
			String userId = "";
			String loginUserType = getUserType();
			if ("member".equals(loginUserType)) {
				userId = getUserId();
			} else {
				//admin or agent
				UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				userId = userDetail.getUserId();
			}

			KeycloakUser keycloakUser = keycloakService.login(userId, users.getPassword());
			if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
				if (ValidateUtil.isPwd(users.getNewPassword())) {
					registerUaerService.updatePassword(userId, users.getNewPassword());
				} else {
					/*message = "請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";*/
					message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0024");
				}
			}else{
				/*message = "密碼錯誤!";*/
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0118");
			}
			this.setResponseObj(ResponseObj.SUCCESS, message, null);
		} catch (InvalidParameterException e) {
			this.setResponseObj(ResponseObj.ERROR, e.getMessage(), null);
		} catch (Exception e) {
			logger.error("updatePassword error! {}", e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@GetMapping("/changePasswordSuccess")
	public String changePasswordSuccess() {
		try {
			//備註
			String changePasswordSuccess = getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE, OnlineChangeNoteUtil.CHANGE_PD_SUCCESS_CODE);
			SuccessMsg("changePassword", changePasswordSuccess);
			
			String loginUserType = getUserType();
			if ("member".equals(loginUserType)) {
				//userId = getUserId();
				UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				userDetail.setLastChangPasswordDate(Calendar.getInstance().getTime());
			} else {
				//admin or agent
				UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				userDetail.setLastChangPasswordDate(Calendar.getInstance().getTime());
			}
		} catch (Exception e) {
			logger.error("changePasswordSuccess error! {}", e);
		}
		return "frontstage/onlineChange/changePassword/change-password-success";
	}
}
