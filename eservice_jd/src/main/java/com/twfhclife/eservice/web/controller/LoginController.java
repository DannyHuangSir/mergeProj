package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.MyStringUtil;
import com.twfhclife.eservice.util.ValidateCodeUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;




/**
 * 首頁登入.
 *
 * @author all
 */
@Controller
public class LoginController extends BaseController {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	private IRegisterUserService registerUserService;

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private ILoginService loginService;

	/**
	 * 登入.
	 *
	 * @param loginRequestVo LoginRequestVo
	 * @return
	 */
	@PostMapping("/doLogin")
	public String doLogin(LoginRequestVo loginRequestVo) {
		KeycloakUser keycloakUser = null;
		String loginSuccessPage = "redirect:dashboard";
		String userId = loginRequestVo.getUserId();
		String userType = "";
		try {
			// 取得session驗證碼
			Object validateCodeObj = getSession(ApConstants.LOGIN_VALIDATE_CODE);
			if (validateCodeObj != null) {
				loginRequestVo.setSessionValidateCode((String) validateCodeObj);
			}

			//decrypt pw,retset to loginRequestVo-start
			String encryptPw = loginRequestVo.getPassword();
			String decryptPw = decodeBase64(encryptPw);
			loginRequestVo.setPassword(decryptPw);
			System.out.println("decryptPw="+decryptPw);
			logger.info("decryptPw="+decryptPw);
			//decrypt pw,retset to loginRequestVo-end

			LoginResultVo loginResultVo = loginService.doLogin(loginRequestVo);
			String returnCode = loginResultVo.getReturnCode();
			String returnMsg = loginResultVo.getReturnMsg();
			String loginType = loginResultVo.getLoginType();
			keycloakUser = loginResultVo.getKeycloakUser();
			boolean verifyValidateCode = loginResultVo.isVerifyValidateCode();

			// 驗證是否有綁定FB、自然人憑證及驗證碼是否輸入正確
			if("moica".contains(loginType)) {
				loginType  = "";//disable MMOICA login
			}
			switch (loginType) {
				case "fb":
					// Facebook 登入
					if ("NO_DATA".equals(returnCode)) {
						addSession("fbId", loginRequestVo.getFbId());
						addSession("email", loginRequestVo.getEmail());
						addAttribute("noFbIdUser", true);
						resetVerifyCode();
						return "login";
					}
					if(loginResultVo.getKeycloakUser() != null) {
						userId = loginResultVo.getKeycloakUser().getUsername();
					}
					break;
				case "moica":
					// 自然人憑證登入
					if ("NO_DATA".equals(returnCode)) {
						addSession("cardSn", loginRequestVo.getCardSN());
						addAttribute("noCardSnUser", true);
						resetVerifyCode();
						return "login";
					}


					//需於server side檢核
					boolean checkMoica = false;
					String cardSN = loginRequestVo.getCardSN();
					String sessionCardSN  = "";
					Object strObj = getSession("SESSION_CARDSN");
					if(strObj!=null) {
						sessionCardSN = (String)strObj;
					}
					logger.info("sessionCardSN="+sessionCardSN);
					System.out.println("sessionCardSN="+sessionCardSN);

					if(!StringUtils.isEmpty(cardSN) && !StringUtils.isEmpty(sessionCardSN)
							&& cardSN.equals(sessionCardSN)) {
						checkMoica = true;
					}

					if(!checkMoica) {
						//前端值的驗證和最後傳到SERVER的不一致
						String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0000");
						addAttribute("errorMessage", errorMessage);
						addAuditLog(userId, "0", "");
						return "login";
					}


					if(loginResultVo.getKeycloakUser() != null) {
						userId = loginResultVo.getKeycloakUser().getUsername();
					}
					break;
				case "password":
					// 帳號密碼登入
					if (!verifyValidateCode) {
						addAttribute("errorMessage", "驗證碼不正確");
						resetVerifyCode();
						return "login";
					}
					break;
			}

			UsersVo userDetail = registerUserService.getUserByAccount(userId);
			if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
				if (userDetail != null && userDetail.getStatus().equals("locked")) {
					/*addAttribute("errorMessage", "帳號或密碼不正確");*/
					String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0099");
					addAttribute("errorMessage", errorMessage);
					addAuditLog(userId, "0", loginRequestVo.getEuNationality());
					return "login";
				}

				addSession(UserDataInfo.USER_DETAIL, userDetail);
				addSession(ApConstants.KEYCLOAK_USER, keycloakUser);
				addSession(ApConstants.LOGIN_USER_ID, keycloakUser.getUsername());

				// 設定系統參數
				Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID);
				addAttribute(ApConstants.SYSTEM_CONSTANTS, (Serializable) sysParamMap.get("SYSTEM_CONSTANTS"));
				addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) sysParamMap);
				addSession(ApConstants.PAGE_WORDING, (Serializable) sysParamMap.get("PAGE_WORDING"));
				addSession(ApConstants.SYSTEM_MSG_PARAMETER, (Serializable) sysParamMap.get("SYSTEM_MSG_PARAMETER"));
				addSession(ApConstants.APPLICATION_ITEMS, (Serializable) sysParamMap.get("APPLICATION_ITEMS"));
				addSession(ApConstants.INSURANCE_CLAIM_UPLOADFILE, (Serializable) sysParamMap.get("INSURANCE_CLAIM_UPLOADFILE"));
				addSession(ApConstants.MEDICAL_TREATMENT_UPLOADFILE, (Serializable) sysParamMap.get("MEDICAL_TREATMENT_UPLOADFILE"));
				addSession(ApConstants.RELATION_ITEMS, (Serializable) sysParamMap.get("RELATION_ITEMS"));
				addSession(ApConstants.SEND_COMPANY_ITEMS, (Serializable) sysParamMap.get("SEND_COMPANY_ITEMS"));
				addSession(ApConstants.SEND_COMPANY_ITEMS_CONTACT, (Serializable) sysParamMap.get("SEND_COMPANY_ITEMS_CONTACT"));
				addSession(ApConstants.MEDICAL_COMPANY_ITEMS, (Serializable) sysParamMap.get("MEDICAL_COMPANY_ITEMS"));
				addSession(ApConstants.PAYMENT_METHOD, (Serializable) sysParamMap.get("PAYMENT_METHOD"));
				addSession(ApConstants.INSURANCE_ACCIDENT, (Serializable) sysParamMap.get("INSURANCE_ACCIDENT"));

				// hide listing product code
				Map<String, ParameterVo> paraMap = sysParamMap.get("SYSTEM_CONSTANTS");
				Set<String> keySet = paraMap.keySet();
				ParameterVo vo2 = null;
				for (String key : keySet) {
					if (key.indexOf("HIDE_LISTING") != -1) {
						String mapKey = "hideProdCode" + key.replace("HIDE_LISTING", "").replace("_CODE", "");
						ParameterVo vo = (ParameterVo) paraMap.get(key);
						addSession(mapKey , vo.getParameterValue());
						logger.debug("original key={}, mapKey={}, mapValue={}", key, mapKey, vo.getParameterValue());
					}

					if (key.equals ("DOWN_LISTING_2")) {
						vo2 = (ParameterVo) paraMap.get(key);
						addSession(key , vo2.getParameterValue());
						logger.debug("layout key={},  layou value={}", key, vo2.getParameterValue());
						vo2 = null;
					}
					if (key.indexOf("SHOW_LISTING") != -1) {
						String mapKey = "showProdCode" + key.replace("SHOW_LISTING", "").replace("_CODE", "");
						ParameterVo vo = (ParameterVo) paraMap.get(key);
						addSession(mapKey , vo.getParameterValue());
						logger.debug("original key={}, mapKey={}, mapValue={}", key, mapKey, vo.getParameterValue());
					}

				}

				// 設定登出倒數秒數
				long logutTimeoutSeconds = 0;
				try {
					String timeoutSeconds = sysParamMap.get("SYSTEM_CONSTANTS").get("TIMEOUT_SECONDS").getParameterValue();
					logutTimeoutSeconds = Long.parseLong(timeoutSeconds);
				} catch (Exception e) {
					// 若發生錯誤，取預設值
					logutTimeoutSeconds = ApConstants.LOGUT_TIME_DURATION_SECOND;
				}
				addSession(ApConstants.TIMEOUT_SECONDS, logutTimeoutSeconds);

				if (userDetail != null) {
					Date lastLoginTime = loginService.getLastLoginTime(userId);
					if (lastLoginTime != null) {
						addSession(UserDataInfo.LAST_LOGIN_TIME, lastLoginTime);
					}
					userType = userDetail.getUserType();
				}
				addAttribute("errorMessage", "");
			} else {

				if ("password".equals(loginType) || (userDetail != null && userDetail.getStatus().equals("locked"))) {
					/*addAttribute("errorMessage", "帳號或密碼不正確");*/
					String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0099");
					addAttribute("errorMessage", errorMessage);
				} else {
					if(returnCode.equals("API_ERROR")) {
						addAttribute("errorMessage", returnMsg);
					} else {
						/*addAttribute("errorMessage", "登入失敗");*/
						String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0100");
						addAttribute("errorMessage", errorMessage);
					}
				}
				resetVerifyCode();
				addAuditLog(userId, "0", loginRequestVo.getEuNationality());
				return "login";
			}
		} catch (Exception e) {
			logger.error("Unable to doLogin: {}", ExceptionUtils.getStackTrace(e));
			addAttribute("errorMessage", ApConstants.SYSTEM_ERROR);

			// 清除登入資訊
			if (keycloakUser != null) {
				keycloakService.logout(keycloakUser.getId());
			}

			resetVerifyCode();
			return "login";
		}

		// 20180424 CR16 系統登入成功/失敗發送電子郵件至要保人信箱
		// 3.登入成功後POP UP訊息告知最近三次登入時間及狀態
		if (ApConstants.isNotice && "member".equals(userType)) {
			List<AuditLogVo> auditLogList = loginService.getLastAuditLog(userId, "3");
			addSession("auditLogList", (Serializable) auditLogList);
		}

		// eservice 單一登入控制
		try {
			String keycloakUserId = keycloakUser.getId();
			String nowSessionId = keycloakUser.getSessionState();
			List<String> userSessionList = loginService.getKeycloakUserSessionIdList(keycloakUserId);
			logger.debug("Find user[{}] this login sessionId: {}", userId, nowSessionId);
			logger.debug("Find user[{}] sessionList: {}", userId, userSessionList);

			if (userSessionList != null && userSessionList.size() > 1) {
				boolean hasLogined = false;
				for (String sessionId : userSessionList) {
					logger.debug("Find sessionId: {}", sessionId);
					logger.debug("Find sessionId.equals(nowSessionId): {}", sessionId.equals(nowSessionId));
					if (!sessionId.equals(nowSessionId)) {
						hasLogined = true;
						break;
					}
				}

				logger.debug("User[{}] hasLogined from other browser: {}", userId, hasLogined);
				if (hasLogined) {
					return "confirmLogout";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to getKeycloakUserSessionIdList: {}", ExceptionUtils.getStackTrace(e));
		}

		addAuditLog(userId, "1", loginRequestVo.getEuNationality());
		return loginSuccessPage;
	}

	private void addAuditLog(String userId, String status, String euNationality) {
		// 記錄登入記錄
		try {
			AuditLogVo vo = new AuditLogVo();
			vo.setUserId(userId);
			vo.setLoginStatus(status);
			vo.setClientIp(getClientIp());

			//SR_GDPR_start
			vo.setEuNationality("GDPR="+StringUtils.trimToEmpty(euNationality));
			//SR_GDPR_end

			loginService.addAuditLog(vo);
		} catch (Exception e) {
			logger.error("Unable to add login audit log: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 登出.
	 *
	 * @param request
	 * @return
	 */
	@GetMapping("/doLogout")
	public String doLogout(HttpServletRequest request) {
		try {
			loginService.updateLogoutDate(getUserId());

			Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
			if (KeycloakUserObj != null) {
				KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
				try {
					loginService.doLogout(ApConstants.KEYCLOAK_REALM, keycloakUser.getId());
				} catch (Exception e) {
					logger.error("Unable to logout by api: ", e);
					keycloakService.logout(keycloakUser.getId());
				}
				logger.info(keycloakUser.getUsername()+" logout success!");
			}

			Enumeration<String> em = request.getSession().getAttributeNames();
			while (em.hasMoreElements()) {
				request.getSession().removeAttribute(em.nextElement().toString());
			}
		} catch (Exception e) {
			logger.error("Unable to doLogout: {}", ExceptionUtils.getStackTrace(e));
		}

		resetVerifyCode();
		return "redirect:login";
	}

	/**
	 * 取得圖形驗證碼.
	 *
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/getVerify")
	public void getVerify(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expire", 0);

			ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 20);
			addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());

			vcUtil.write(response.getOutputStream());
		} catch (Exception e) {
			logger.error("Unable to getVerify: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	private void resetVerifyCode() {
		// 設定驗證碼圖示
		ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
		addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
		addAttribute("validateImageBase64", vcUtil.imgToBase64String());
	}

	@RequestMapping("/login-timeout")
	public String logout() {
		logger.debug("force logout");
		return "force_logout";
	}

	@RequestMapping("/confirmLogout")
	public String confirmLogout() {
		return "confirmLogout";
	}

	/**
	 * 登出其他重複登入的session
	 * @return
	 */
	@PostMapping("/logoutOtherUser")
	public String logoutOtherUser() {
		String loginSuccessPage = "redirect:dashboard";
		try {
			KeycloakUser keycloakUser = getLoginUser();
			String userId = getUserId();
			String keycloakUserId = keycloakUser.getId();
			String nowSessionId = keycloakUser.getSessionState();
			List<String> userSessionList = loginService.getKeycloakUserSessionIdList(keycloakUserId);
			logger.debug("logoutOtherUser: Find user[{}] this login sessionId: {}", userId, nowSessionId);
			logger.debug("logoutOtherUser: Find user[{}] sessionList: {}", userId, userSessionList);

			List<String> removeSessionIdList = new ArrayList<>();
			if (userSessionList != null && userSessionList.size() > 0) {
				for (String sessionId : userSessionList) {
					if (!sessionId.equals(nowSessionId)) {
						if (!removeSessionIdList.contains(sessionId)) {
							removeSessionIdList.add(sessionId);
						}
					}
				}
			}
			String returnCode = loginService.logoutOtherUser(removeSessionIdList);
			logger.debug("Remove user[{}] other sessionList[{}] result: {}", userId, removeSessionIdList, returnCode);

			// 判斷是否為內部人員及保代角色
			UsersVo userDetail = registerUserService.getUserByAccount(keycloakUser.getUsername());
			boolean isPartnerRole = loginService.isPartnerRole(userDetail.getUserType());
			if (isPartnerRole) {
				loginSuccessPage = "redirect:partner";
			}

		} catch (Exception e) {
			logger.error("Unable to logoutOtherUser: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return loginSuccessPage;
	}

	/**
	 * 放棄本次登入session
	 * @param request
	 * @return
	 */
	@GetMapping("/abortLogin")
	public String abortLogin(HttpServletRequest request) {
		try {
			String userId = getUserId();
			loginService.updateLogoutDate(userId);

			Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
			if (KeycloakUserObj != null) {
				KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
				String nowSessionId = keycloakUser.getSessionState();

				List<String> removeSessionIdList = new ArrayList<>();
				removeSessionIdList.add(nowSessionId);

				String returnCode = loginService.logoutOtherUser(removeSessionIdList);
				logger.debug("Remove user[{}] sessionList[{}] result: {}", userId, removeSessionIdList, returnCode);
			}

			Enumeration<String> em = request.getSession().getAttributeNames();
			while (em.hasMoreElements()) {
				request.getSession().removeAttribute(em.nextElement().toString());
			}
		} catch (Exception e) {
			logger.error("Unable to abortLogin: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		resetVerifyCode();
		return "redirect:login";
	}

	// FB或自然人登入前先比對驗證碼
	@PostMapping("/preCheckkValidateCode")
	public ResponseEntity<ResponseObj> preCheckkValidateCode(@RequestBody String validateCode, HttpServletRequest request) {
		try {
			boolean isValidateCodeOk = false;
			// 取得session驗證碼
			Object validateCodeObj = getSession(ApConstants.LOGIN_VALIDATE_CODE);
			if (validateCodeObj != null) {
				//System.out.println("input validateCode="+validateCode);
				//System.out.println("session (validateCodeObj)="+(String)validateCodeObj);
				isValidateCodeOk = StringUtils.equals(validateCode, (String) validateCodeObj);
			} else {
				// 設定驗證碼圖示
				ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
				addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
				addAttribute("validateImageBase64", vcUtil.imgToBase64String());
			}

			String message = "";
			if (!isValidateCodeOk) {
				message = "驗證碼不正確";
			}

			this.setResponseObj(ResponseObj.SUCCESS, message, "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	@PostMapping("/setSessioinCardSN")
	public ResponseEntity<ResponseObj> setSessioinCardSN(
			@RequestBody String cardSn,
			HttpServletRequest request) {

		//String afterEncryptCardSN = "";

		try {
			System.out.println("In setSessioinCardSN.");
			logger.info("In setSessioinCardSN,cardSn="+cardSn);

			if(!StringUtils.isEmpty(cardSn)) {
				addSession("SESSION_CARDSN",cardSn.trim());
			}

			this.setResponseObj(ResponseObj.SUCCESS, "", "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());

	}


	/**
	 * 加密自然人憑證卡號
	 * @param sCardSN
	 * @return
	 */
	@PostMapping("/encryptCardSN")
	public Object encryptCardSN(@RequestBody String sCardSN) {
		String afterEncryptCardSN = "";
		try {
			logger.info("Start to enctypt moica cardSN.");
			System.out.println("Start to enctypt moica cardSN.");

			//檢核session validate code是否仍有效


			//加密
			Map<String, Object> dataResult = new HashMap<>();
			if(!StringUtils.isEmpty(sCardSN)) {

				//AES
				afterEncryptCardSN = sCardSN+"ooxx";
				dataResult.put("cardSN", afterEncryptCardSN);

				addSession("CARDSN", sCardSN);

			}

			String message = "";
			this.setResponseObj(ResponseObj.SUCCESS, message, dataResult);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	/**
	 * 加密解密演算法
	 */
	private String decodeBase64(String mi) {

		String mingwen = "";
		if(mi==null || mi.equals("")){
			//donothing.
		}else{
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				byte[] by = decoder.decodeBuffer(mi);
				mingwen = new String(by);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println("解密後[" mingwen "]");
		}
		return mingwen;
	}

}