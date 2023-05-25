package com.twfhclife.eservice_api.controller;

import java.util.Base64;
import java.util.List;

import javax.validation.Valid;

import com.google.gson.Gson;
import com.twfhclife.eservice.api.elife.domain.BxczLoginRequest;
import com.twfhclife.eservice.api.elife.domain.BxczLoginResponse;
import com.twfhclife.generic.utils.MyJacksonUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice_api.service.IAuthoService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.KeycloakLoginRequest;
import com.twfhclife.generic.domain.KeycloakLoginResponse;
import com.twfhclife.generic.domain.KeycloakLoginResponseByEIN;
import com.twfhclife.generic.domain.KeycloakUserSessionRequest;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.model.KeycloakUserSession;
import com.twfhclife.generic.utils.MyStringUtil;
import com.twfhclife.keycloak.service.KeycloakService;

@RestController
public class SsoController extends BaseController {

	private static final Logger logger = LogManager.getLogger(SsoController.class);

	@Autowired
	KeycloakService keycloakService;

	@Autowired
	IAuthoService authoService;

	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "AA-019", 
			systemEventParams = {
				@SystemEventParam(
					execMethod = "登入",
					execFile = "SSO"
				)
			}))
	@PostMapping(value = "/sso/login", produces = { "application/json" })
	public ResponseEntity<?> login(@Valid @RequestBody KeycloakLoginRequest req) {
		ApiResponseObj<KeycloakLoginResponse> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		KeycloakLoginResponse keycloakLoginResponse = null;
		if (req == null) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "參數不可為空", "", "");
		} else {
			if (!"password,fb,moica,".contains(req.getGrantType() + ",")) {
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "無效的grantType", "", "");
				response.setReturnHeader(returnHeader);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

			if (req.getGrantType().equals("password")) {
				logger.info("User login by username/pwd: " + req.getUsername());
				// 帳密登入
				if (MyStringUtil.isNullOrEmpty(req.getUsername()) || MyStringUtil.isNullOrEmpty(req.getPassword())) {
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "帳號密碼必須輸入", "", "");
				} else {
					keycloakLoginResponse = keycloakService.login(req.getUsername(), req.getPassword(), req.getRealm(),
							req.getClientId());

					if (keycloakLoginResponse.getStatus().equals("SUCCESS")) {
						returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
						response.setResult(keycloakLoginResponse);
					} else if (keycloakLoginResponse.getStatus().equals("FAIL")) {
						returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "帳號/密碼認證失敗，請確認帳號/密碼是否正確輸入", "", "");
					} else {
						returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "認證系統發生錯誤，請稍後再試", "", "");
					}
				}
			} else if (req.getGrantType().equals("fb")) {
				// FB登入
				if (MyStringUtil.isNullOrEmpty(req.getFbId())) {
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "fbId必須輸入", "", "");
				} else if (MyStringUtil.isNullOrEmpty(req.getAccessToken())) {
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "accessToken必須輸入", "", "");
				} else {
					String fbId = req.getFbId();
					String fbToken = req.getAccessToken();
					logger.info("User login by FB: fbId=" + fbId);

					keycloakLoginResponse = keycloakService.loginByFb(fbId, fbToken, req.getRealm(), req.getClientId());
//					keycloakLoginResponse = keycloakService.loginEserviceByFbToken(fbId, fbToken);// 透過交換Token方式登入
					if (keycloakLoginResponse == null) {
						returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "FB未綁定帳號!", "", "");
					} else if (keycloakLoginResponse.getStatus().equals("SUCCESS")) {
						returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
						response.setResult(keycloakLoginResponse);
					} else if (keycloakLoginResponse.getStatus().equals("FAIL")) {
						returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "FB登入失敗!", "", "");
					} else {
						returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "系統發生錯誤，請稍後再試", "", "");
					}
				}
			} else if (req.getGrantType().equals("moica")) {
				// 自然人憑證登入
				if (MyStringUtil.isNullOrEmpty(req.getMoicaId())) {
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "moicaId必須輸入", "", "");
				} else if (MyStringUtil.isNullOrEmpty(req.getMoicaCert())) {
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "moicaCert必須輸入", "", "");
				} else {
					String moicaId = req.getMoicaId();
					logger.info("User login by moica: moicaId=" + moicaId);

					keycloakLoginResponse = keycloakService.loginByMoica(moicaId, "", req.getRealm(),
							req.getClientId());
					if (keycloakLoginResponse == null) {
						returnHeader.setReturnHeader(ReturnHeader.NODATA_CODE, "自然人憑證未綁定帳號!", "", "");
					} else if (keycloakLoginResponse.getStatus().equals("SUCCESS")) {
						returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
						response.setResult(keycloakLoginResponse);
					} else if (keycloakLoginResponse.getStatus().equals("FAIL")) {
						returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "自然人憑證登入失敗!", "", "");
					} else {
						returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "系統發生錯誤，請稍後再試", "", "");
					}
				}
			}

		}

		response.setReturnHeader(returnHeader);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}


//  20211013 by 203999
//	@ApiRequest
//	@EventRecordLog(value = @EventRecordParam(
//			eventCode = "AA-019", 
//			systemEventParams = {
//				@SystemEventParam(
//					execMethod = "登入",
//					execFile = "SSO"
//				)
//			}))
	@PostMapping(value = "/sso/login/ein", produces = { "application/json" })
	public ResponseEntity<?> loginByEIN(@Valid @RequestBody KeycloakLoginRequest req) {
		ApiResponseObj<KeycloakLoginResponseByEIN> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		KeycloakLoginResponseByEIN KeycloakLoginResponseByEIN = null;
		if (req == null) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "參數不可為空", "", "");
		} else {
			if (!"password,fb,moica,".contains(req.getGrantType() + ",")) {
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "無效的grantType", "", "");
				response.setReturnHeader(returnHeader);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

			if (req.getGrantType().equals("password")) {
				logger.info("User login by username/pwd: " + req.getUsername());
				// 帳密登入
				if (MyStringUtil.isNullOrEmpty(req.getUsername()) || MyStringUtil.isNullOrEmpty(req.getPassword())) {
					returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "帳號密碼必須輸入", "", "");
				} else {
					KeycloakLoginResponseByEIN = keycloakService.loginByEIN(req.getUsername(), req.getPassword(), req.getRealm(),
							req.getClientId());

					if (KeycloakLoginResponseByEIN.getStatus().equals("SUCCESS")) {
						returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
						response.setResult(KeycloakLoginResponseByEIN);
					} else if (KeycloakLoginResponseByEIN.getStatus().equals("FAIL")) {
						returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "帳號/密碼認證失敗，請確認帳號/密碼是否正確輸入", "", "");
					} else {
						returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "認證系統發生錯誤，請稍後再試", "", "");
					}
				}
			}

		}

		response.setReturnHeader(returnHeader);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
//	@EventRecordLog(value = @EventRecordParam(
//			eventCode = "AA-021", 
//			systemEventParams = {
//				@SystemEventParam(
//					execMethod = "查驗AccessToken合法性",
//					execFile = "SSO"
//				)
//			}))
	@PostMapping(value = "/sso/validatetoken", produces = { "application/json" })
	public ResponseEntity<ApiResponseObj<KeycloakLoginResponse>> validateToken(@RequestBody KeycloakLoginRequest req) {
		ApiResponseObj<KeycloakLoginResponse> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		KeycloakLoginResponse keycloakLoginResponse = null;
		if (req == null) {
			returnHeader.setReturnCode("ERROR");
			returnHeader.setReturnMesg("參數不可為空");
		} else {
			if (MyStringUtil.isNullOrEmpty(req.getAccessToken())) {
				returnHeader.setReturnCode("ERROR");
				returnHeader.setReturnMesg("Access Token cannot empty!");
			} else if (MyStringUtil.isNullOrEmpty(req.getRealm())) {
				returnHeader.setReturnCode("ERROR");
				returnHeader.setReturnMesg("realm cannot empty!");
			} else {

				keycloakLoginResponse = keycloakService.validateToken(req.getAccessToken(), req.getRefreshToken(),
						req.getRealm(), req.getClientId());

				if (keycloakLoginResponse.getStatus().equals("SUCCESS") || keycloakLoginResponse.getStatus().equals("REFRESH")) {
					returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
					response.setResult(keycloakLoginResponse);
				} else if(keycloakLoginResponse.getStatus().equals("INVALID")) {
					returnHeader.setReturnHeader("INVALID", "無效的Token!", "", "");
				} else {
					returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "驗證失敗!", "", "");
				}
			}
		}

		response.setReturnHeader(returnHeader);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	
//	@EventRecordLog(value = @EventRecordParam(
//			eventCode = "AA-020", 
//			systemEventParams = {
//				@SystemEventParam(
//					execMethod = "更新Token",
//					execFile = "SSO"
//				)
//			}))
	@PostMapping(value = "/sso/refreshtoken", produces = { "application/json" })
	public ResponseEntity<ApiResponseObj<KeycloakLoginResponse>> refreshToken(@RequestBody KeycloakLoginRequest req) {
		ApiResponseObj<KeycloakLoginResponse> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		KeycloakLoginResponse keycloakLoginResponse = null;
		if (req == null) {
			returnHeader.setReturnCode("ERROR");
			returnHeader.setReturnMesg("參數不可為空");
		} else {
			if (MyStringUtil.isNullOrEmpty(req.getRefreshToken())) {
				returnHeader.setReturnCode("ERROR");
				returnHeader.setReturnMesg("refreshToken is null!");
			} else if (MyStringUtil.isNullOrEmpty(req.getRealm())) {
				returnHeader.setReturnCode("ERROR");
				returnHeader.setReturnMesg("realm is null!");
			} else if (MyStringUtil.isNullOrEmpty(req.getClientId())) {
				returnHeader.setReturnCode("ERROR");
				returnHeader.setReturnMesg("clientId is null!");
			} else {
				keycloakLoginResponse = keycloakService.refreshToken(req.getRefreshToken(), req.getRealm(),
						req.getClientId());

				if (keycloakLoginResponse != null && keycloakLoginResponse.getStatus().equals("SUCCESS")) {
					returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
					response.setResult(keycloakLoginResponse);
				} else {
					returnHeader.setReturnCode("FAIL");
					returnHeader.setReturnMesg("帳號認證失敗!");
				}
			}
		}

		response.setReturnHeader(returnHeader);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "AA-022", 
			systemEventParams = {
				@SystemEventParam(
					execMethod = "登出",
					execFile = "SSO"
				)
			}))
	@GetMapping("/sso/logout/{realm}/{userId}")
	public ResponseEntity<?> logout(@PathVariable("realm") String realm, @PathVariable("userId") String userId) {
		ApiResponseObj response = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (realm == null || userId == null) {
				returnHeader.setReturnCode(ReturnHeader.ERROR_CODE);
				returnHeader.setReturnMesg("Invalid input parameters!");
			} else {
				//用userId反查事件紀錄需要使用的帳號
				try {
					List<KeycloakUserSession> list = keycloakService.getUserSessions(realm, userId, null);
					response.setResult(list != null && list.size() > 0 ? list.get(0).getUsername() : "unknown");
				} catch (Exception e) {
					response.setResult("unknown");
				}
				returnHeader.setReturnCode(ReturnHeader.SUCCESS_CODE);
				keycloakService.logout(realm, userId);
			}
		} catch (Exception e) {
			logger.error("Unable to doLogout: {}", ExceptionUtils.getStackTrace(e));
		}
		response.setReturnHeader(returnHeader);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 查詢user登入的session
	 * @param req
	 * @return
	 */
	@ApiRequest
//	@EventRecordLog(value = @EventRecordParam(
//			eventCode = "AA-025", 
//			systemEventParams = {
//				@SystemEventParam(
//					execMethod = "查詢Keycloak使用者的登入session",
//					execFile = "SSO"
//				)
//			}))
	@PostMapping("/sso/user-sessions")
	public ResponseEntity<?> getUserSession(@Valid @RequestBody KeycloakUserSessionRequest req) {
		ApiResponseObj response = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (MyStringUtil.isNullOrEmpty(req.getRealm()) || MyStringUtil.isNullOrEmpty(req.getUserId())) {
				returnHeader.setReturnCode(ReturnHeader.ERROR_CODE);
				returnHeader.setReturnMesg("Invalid input parameters!");
			} else {
				List<KeycloakUserSession> list = keycloakService.getUserSessions(req.getRealm(), req.getUserId(), req.getClientId());
				returnHeader.setReturnCode(ReturnHeader.SUCCESS_CODE);
				response.setResult(list);
			}
		} catch (Exception e) {
			logger.error("Unable to doLogout: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnCode(ReturnHeader.ERROR_CODE);
			returnHeader.setReturnMesg(e.getMessage());
		} finally {
			response.setReturnHeader(returnHeader);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	/**
	 * 刪除指定的user-session
	 * @param req
	 * @return
	 */
	@ApiRequest
//	@EventRecordLog(value = @EventRecordParam(
//			eventCode = "AA-026", 
//			systemEventParams = {
//				@SystemEventParam(
//					execMethod = "登出指定session",
//					execFile = "SSO"
//				)
//			}))
	@PostMapping("/sso/user-sessions/remove")
	public ResponseEntity<?> deleteUserSession(@Valid @RequestBody KeycloakUserSessionRequest req) {
		ApiResponseObj response = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (req.getSessionIds() == null || req.getSessionIds().size() == 0) {
				returnHeader.setReturnCode(ReturnHeader.ERROR_CODE);
				returnHeader.setReturnMesg("Invalid input parameters!");
			} else {
				boolean result = keycloakService.deleteUserSession(req.getRealm(), req.getSessionIds());
				returnHeader.setReturnCode(ReturnHeader.SUCCESS_CODE);
				
			}
		} catch (Exception e) {
			logger.error("Unable to doLogout: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnCode(ReturnHeader.ERROR_CODE);
			returnHeader.setReturnMesg(e.getMessage());
		} finally {
			response.setReturnHeader(returnHeader);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/lia-ids/oauth2/token", produces = { "application/json" })
	public ResponseEntity<ApiResponseObj<String>> oauth2Token(@RequestBody BxczLoginRequest req) {
		return ResponseEntity.status(HttpStatus.OK).body(authoService.doPostPbs102(req));
	}

}
