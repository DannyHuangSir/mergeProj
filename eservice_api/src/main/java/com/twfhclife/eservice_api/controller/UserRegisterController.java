package com.twfhclife.eservice_api.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice_api.service.IUserRegisterService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.KeycloakUserAddRequest;
import com.twfhclife.generic.domain.KeycloakUserResponse;
import com.twfhclife.generic.domain.ResetPwdRequest;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.MyStringUtil;
import com.twfhclife.keycloak.service.KeycloakService;

@RestController
public class UserRegisterController extends BaseController {

	private static final Logger logger = LogManager.getLogger(UserRegisterController.class);

	@Autowired
	IUserRegisterService userRegisterService;

	@Autowired
	KeycloakService keycloakService;

	/**
	 * 新增使用者帳號
	 * 
	 * @param req
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "AA-023", 
			systemEventParams = {
				@SystemEventParam(
//					sqlId = "com.twfhclife.generic.dao.adm.UsersDao.createUser",
					execMethod = "eservice用戶註冊",
					execFile = "SSO"
				)
			}))
	@PostMapping(value = "/elife/user/create", produces = { "application/json" })
	public ResponseEntity<?> createUser(@Valid @RequestBody KeycloakUserAddRequest req,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<KeycloakUserResponse> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		KeycloakUserResponse keycloakUserResponse = new KeycloakUserResponse();
		String message = "";

		if (SECRET_REQUIRE && !this.validateSecret(secret)) {
			this.setErrorMessages("不合法的調用");
			Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
			return ResponseEntity.badRequest().body(error);
		}

		if (req == null) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "參數不可為空", "", "");
		} else {
			if (MyStringUtil.isNullOrEmpty(req.getUsername())) {
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "無效的grantType", "", "");
				response.setReturnHeader(returnHeader);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

			UserVo userVo = new UserVo();
			userVo.setUsername(req.getUsername());
			userVo.setPassword(req.getPassword());
			userVo.setFirstName(req.getName());
			userVo.setEmail(req.getEmail());
			userVo.setMobile(req.getMobile());
			userVo.setRocId(req.getRocId());
			userVo.setRealmId(req.getRealm());
			userVo.setFirstName(req.getCardSn());
			userVo.setFbId(req.getFbId());
			userVo.setMoicaId(req.getCardSn());
			userVo.setUserType(req.getUserType());
			Map<String, String> retMap = userRegisterService.createEserviceUser(userVo);

			String userId = retMap.get("userId");
			String error = retMap.get("error");
			if (MyStringUtil.isNotNullOrEmpty(error)) {
				message = "建立新用戶時發生錯誤，請重試或聯絡系統管理員";
				if (error.equals("409")) {
					// 用戶名稱重複
					logger.info("[Register error] User existed in Keycloak!");
					message = "用戶名稱已被使用，請改用其他名稱";

				} else if (error.equals("400")) {
					// 新增Keycloak User錯誤
					logger.error("[Register error] Keycloak Create user error!");
				} else if (error.equals("SQL")) {
					// 新增至DB錯誤
					logger.error("[Register error] Insert User data into DB fail!");
				}
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, message, "", "");
			} else {
				if (userId == null) {
					message = "建立新用戶時發生錯誤，請重試或聯絡系統管理員";
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, message, "", "");
				} else {
					req.setClientId("elife");//for event sysId
					keycloakUserResponse.setUserId(userId);
					response.setResult(keycloakUserResponse);
					returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				}
			}
		}

		response.setReturnHeader(returnHeader);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * eservice修改密碼
	 * 
	 * @param req
	 * @param request
	 * @return
	 */
	@ApiRequest
	@PostMapping("/elife/password/reset")
	public ResponseEntity<?> updateEservicePassword(@Valid @RequestBody ResetPwdRequest req,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj response = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		String message = "";
		if (SECRET_REQUIRE && !this.validateSecret(secret)) {
			this.setErrorMessages("不合法的調用");
			Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
			return ResponseEntity.badRequest().body(error);
		}
		
		try {
			message = userRegisterService.resetEservicePassword(req);
			if (MyStringUtil.isNotNullOrEmpty(message)) {
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, message, "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			}
		} catch (ClientErrorException e) {
            Response apiresponse = e.getResponse();
            String errMsg = getClientErrorMessage(apiresponse);
            logger.info("ClientErrorException in updateEservicePassword(): " + errMsg);
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, errMsg, "", "");
            apiresponse.close();
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		}

		response.setReturnHeader(returnHeader);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 修改密碼
	 * 
	 * @param newPassword
	 * @param request
	 * @return
	 */
	// @PostMapping("/password/reset")
	// public ResponseEntity<ApiResponseObj> updatePassword(@RequestBody
	// ResetPwdRequest req, HttpServletRequest request) {
	// ApiResponseObj response = new ApiResponseObj();
	// ReturnHeader returnHeader = new ReturnHeader();
	// String message = "";
	// if (ValidateUtil.isPwd(req.getNewPassword())) {
	// if(MyStringUtil.isNotNullOrEmpty(req.getOldPassword())) {
	// //透過舊密碼驗證身分
	//
	// } else if(MyStringUtil.isNotNullOrEmpty(req.getAccessToken())) {
	//
	// } else {
	// // 無法驗證身分，不可修改密碼
	// }
	// String resMsg = userRegisterService.resetEservicePassword(req.getUsername(),
	// req.getNewPassword());
	// if (MyStringUtil.isNotNullOrEmpty(resMsg)) {
	// returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, resMsg, "", "");
	// } else {
	// returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
	// }
	// } else {
	// message = "請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";
	// returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, message, "", "");
	// }
	//
	// response.setReturnHeader(returnHeader);
	// return ResponseEntity.status(HttpStatus.OK).body(response);
	// }
	
	private String getClientErrorMessage(Response response) {
        Object entity = response.getEntity();
        String errorMessage = "";
        if (entity instanceof ErrorRepresentation)
            errorMessage = ((ErrorRepresentation) entity).getErrorMessage();
        else if (entity instanceof InputStream)
            errorMessage = new BufferedReader(new InputStreamReader((InputStream)entity)).lines().collect(Collectors.joining("\n"));
        else if (entity != null)
            errorMessage = entity.toString();
        return errorMessage;
    }
}
