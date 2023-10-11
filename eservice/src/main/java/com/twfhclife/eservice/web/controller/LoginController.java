package com.twfhclife.eservice.web.controller;

import java.io.Serializable;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.generic.api_client.BaseRestClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twfhclife.eservice.auth.service.IFuctionAuthService;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_model.PolicyListResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.service.IUnicodeService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.generic.util.ValidateCodeUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

import sun.misc.BASE64Decoder;




/**
 * 首頁登入.
 * 
 * @author all
 */
@Controller
public class LoginController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	private IRegisterUserService registerUserService;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private IFuctionAuthService fuctionAuthService;

	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private IUnicodeService unicodeService;
	
	/**
	 * 登入.
	 * 
	 * @param loginRequestVo LoginRequestVo
	 * @return
	 */
	@PostMapping("/doLogin")
	public String doLogin(LoginRequestVo loginRequestVo) {
		/// 20221003 by 203990
		/// Begin: 算然前端不顯示FB 及 自然人憑證登入的按鈕, 但還是有測試帶入相關資料直接後送做登入的問題, 因此在此清空FB及自然人憑證帶入的相關資料確實關閉登入功能
		loginRequestVo.setFbId("");
		loginRequestVo.setFbAccessToken("");
		loginRequestVo.setCardSN("");
		loginRequestVo.setCertb64("");
		/// End
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
			
			//SR_GDPR
			if("".equals(StringUtils.trimToEmpty(loginRequestVo.getEuNationality()))) {
				String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0134");
				addAttribute("errorMessage", errorMessage);
				addAuditLog(userId, "0", "");
				return "login";
			}
			
			//decrypt pw,retset to loginRequestVo-start
			String encryptPw = loginRequestVo.getPassword();
			String decryptPw = decodeBase64(encryptPw);
			loginRequestVo.setPassword(decryptPw);
			//System.out.println("decryptPw="+decryptPw);
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
				//System.out.println("sessionCardSN="+sessionCardSN);
				
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
//				ParameterVo vo2 = (ParameterVo) paraMap.get("HIDE_LISTING2_CODE");
//				ParameterVo vo6 = (ParameterVo) paraMap.get("HIDE_LISTING6_CODE");
//				ParameterVo vo7 = (ParameterVo) paraMap.get("HIDE_LISTING7_CODE");
//				ParameterVo vo12 = (ParameterVo) paraMap.get("HIDE_LISTING12_CODE");
//				addSession("hideProdCode", vo2.getParameterValue());
//				addSession("hideProdCode6", vo6.getParameterValue());
//				addSession("hideProdCode7", vo7.getParameterValue());
//				addSession("hideProdCode12", vo12.getParameterValue());

				// 取得使用者不能訪問的DIV權限
				List<String> rejectDivList = fuctionAuthService.getRejectDivName(keycloakUser.getId());
				addSession("REJECT_DIV_LIST", (Serializable) rejectDivList);

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
					Map<String, Boolean> identity = new HashMap<String, Boolean>();
					LilipmVo lilipmVo = lilipmService.findByRocId(userDetail.getRocId());
					if (lilipmVo != null && !StringUtils.isEmpty(lilipmVo.getLipmName1())) {
						userDetail.setUserName(lilipmVo.getLipmName1());
						userDetail.setUserNameBase64(unicodeService.convertString2Unicode(lilipmVo.getLipmName1()));
						identity.put("lilipm", true);
					}else {
						identity.put("lilipm", false);
					}
					LilipiVo tempLilipiVo = new LilipiVo();
					tempLilipiVo.setLipiId(userDetail.getRocId());
					List<LilipiVo> lilipiVoList = lilipiService.getLilipi(tempLilipiVo);
					if (lilipiVoList != null && lilipiVoList.size() > 0) {
						identity.put("lilipi", true);
						if(lilipmVo == null) {
							LilipiVo lilipiVo = lilipiVoList.get(0);
							userDetail.setUserName(lilipiVo.getLipiName());
							userDetail.setUserNameBase64(unicodeService.convertString2Unicode(lilipiVo.getLipiName()));
						}
					}else {
						identity.put("lilipi", false);
					}

					logger.info("***userType="+userDetail.getUserType()+"***");
					boolean isPartner = loginService.isPartnerRole(userDetail.getUserType());
					if(isPartner) {//登入者為內部人員及保代角色(admin,agent)時強制給予要／被保人身份
						logger.info("***start force to setting user identity for admin.***");
						identity.put("lilipm", true);
						identity.put("lilipi", true);
						logger.info("***end force to setting user identity for admin.***");
					}

					userDetail.setIdentity(identity);
					logger.info("to setting userIdentity="+Arrays.asList(identity));
					addSession(UserDataInfo.USER_DETAIL, userDetail);

					// 第一次登入需要變更密碼
					if (userDetail.getLastChangPasswordDate() == null) {
						//20190503 check in AuthenticationInterceptor
						//loginSuccessPage = "redirect:changePassword1";
					} else {
						// 判斷是否為內部人員及保代角色
						boolean isPartnerRole = loginService.isPartnerRole(userDetail.getUserType());
						if (isPartnerRole) {
							loginSuccessPage = "redirect:partner";
						}
					}

					// 設定一般及保戶會員的相關資訊
					String userRocId = getUserRocId();
					logger.debug("Find login user's rocId: {}", userRocId);
					if (!StringUtils.isEmpty(userRocId)) {
						List<String> policyNos = lilipmService.getUserPolicyNos(userRocId);
						if (policyNos != null && policyNos.size() > 0) {
							// 使用者的保單號碼清單
							addSession(UserDataInfo.USER_POLICY_NO_LIST, (Serializable) policyNos);
						}
						// 使用者的保單商品名稱
						addSession(UserDataInfo.USER_PRODUCT_NAME_LIST, (Serializable) loginService.getProductNameList(userRocId));
						// 使用者的保單主約被保人清單
						addSession(UserDataInfo.USER_MAIN_INSURED_NAME_LIST, (Serializable) loginService.getMainInsuredNameList(userRocId));
						// 保戶所有保單
						addSession(UserDataInfo.USER_ALL_POLICY_LIST, (Serializable) getUserOnlineChangePolicyList(userId, userRocId));

						PolicyListResponse policyListResponse = getPolicyListByUser(userId, userRocId);
						// 投資型保單
						addSession(UserDataInfo.USER_INVT_POLICY_LIST, (Serializable) policyListResponse.getInvtPolicyList());
						// 保障型保單
						addSession(UserDataInfo.USER_BENEFIT_POLICY_LIST, (Serializable) policyListResponse.getBenefitPolicyList());
					}

					// 取得上次登入時間
					Date lastLoginTime = loginService.getLastLoginTime(userId);
					if (lastLoginTime != null) {
						addSession(UserDataInfo.LAST_LOGIN_TIME, lastLoginTime);
					}
					userType = userDetail.getUserType();
				}

				addAttribute("errorMessage", "");
			} else {

				boolean isOldSystemUser = registerUserService.checkOldSystemUser(userId, loginRequestVo.getPassword());
				if (isOldSystemUser) {
					addAttribute("isOldUser", isOldSystemUser);
					addSession("register_rocId", userId);
					return "login";
				}

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

	/**
	 * 綁定 Facebook ID.
	 *
	 * @param userId
	 * @param password
	 * @return
	 */
	@RequestLog
	@PostMapping("/doBindFbId")
	public String bindFbId(@RequestParam("userId3") String userId, @RequestParam("password3") String password) {
		// 此處userId為userName
		try {
			// 設定系統參數
			Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID);
			addAttribute(ApConstants.PAGE_WORDING, sysParamMap.get("PAGE_WORDING"));
			addAttribute(ApConstants.SYSTEM_CONSTANTS, sysParamMap.get("SYSTEM_CONSTANTS"));
			addAttribute(ApConstants.SYSTEM_MSG_PARAMETER, sysParamMap.get("SYSTEM_MSG_PARAMETER"));

			KeycloakUser keycloakUser = keycloakService.login(userId, password);
			if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
				String fbId = MyStringUtil.nullToString(getSessionStr("fbId"));
				keycloakUser.setFbId(fbId);
				FederatedIdentityRepresentation irLink = new FederatedIdentityRepresentation();
				irLink.setIdentityProvider("facebook");
				irLink.setUserId(fbId);
				irLink.setUserName(userId);
				keycloakService.updateUser(keycloakUser, irLink);
				/*addAttribute("errorMessage", "Facebook帳號綁定成功");*/
				addAttribute("errorMessage", parameterService.getParameterValueByCode(ApConstants.SYSTEM_MSG_PARAMETER, "E0122"));
			} else {
				/*addAttribute("errorMessage", "Facebook帳號綁定失敗");*/
				addAttribute("errorMessage", parameterService.getParameterValueByCode(ApConstants.SYSTEM_MSG_PARAMETER, "E0123"));
			}
		} catch (Exception e) {
			logger.error("Unable to doBindFbId: {}", ExceptionUtils.getStackTrace(e));
			/*addAttribute("errorMessage", "Facebook帳號綁定失敗");*/
			addAttribute("errorMessage", parameterService.getParameterValueByCode(ApConstants.SYSTEM_MSG_PARAMETER, "E0123"));
		}
		resetVerifyCode();
		return "login";
	}

	/**
	 * 綁定自然人憑證.
	 *
	 * @param userId
	 * @param password
	 * @return
	 */
	@RequestLog
	@PostMapping("/doBindCardSn")
	public String bindCardSn(@RequestParam("userId2") String userId, @RequestParam("password2") String password) {
		try {
			// 設定系統參數
			Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID);
			addAttribute(ApConstants.PAGE_WORDING, sysParamMap.get("PAGE_WORDING"));
			addAttribute(ApConstants.SYSTEM_CONSTANTS, sysParamMap.get("SYSTEM_CONSTANTS"));
			addAttribute(ApConstants.SYSTEM_MSG_PARAMETER, sysParamMap.get("SYSTEM_MSG_PARAMETER"));

			KeycloakUser keycloakUser = keycloakService.login(userId, password);
			if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
				String cardSn = MyStringUtil.nullToString(getSessionStr("cardSn"));
				keycloakUser.setCardSn(cardSn);
				keycloakService.updateUser(keycloakUser, null);
				/*addAttribute("errorMessage", "帳號綁定自然人憑證成功");*/
				addAttribute("errorMessage", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0120"));
			} else {
				/*addAttribute("errorMessage", "帳號綁定自然人憑證失敗");*/
				addAttribute("errorMessage", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0121"));
			}
		} catch (Exception e) {
			logger.error("Unable to doBindFbId: {}", ExceptionUtils.getStackTrace(e));
			/*addAttribute("errorMessage", "帳號綁定自然人憑證失敗");*/
			addAttribute("errorMessage", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0121"));
		}
		resetVerifyCode();
		return "login";
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
	@RequestLog
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
	@RequestLog
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
			//System.out.println("In setSessioinCardSN.");
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
	 * @param cardSN
	 * @param request
	 * @return
	 */
	@PostMapping("/encryptCardSN")
	public Object encryptCardSN(@RequestBody String sCardSN) {
		String afterEncryptCardSN = "";
		try {
			logger.info("Start to enctypt moica cardSN.");
			//System.out.println("Start to enctypt moica cardSN.");
			
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


	@Value("${eservice.bxcz.pbs.101.url}")
	private String pbs101url;

	@Value("${eservice.bxcz.login.client_id}")
	private String client_id;

	@PostMapping("/generateBxczLoginUrl")
	public ResponseEntity<ResponseObj> generateBxczLoginUrl(HttpServletRequest request) {
		try {
			String actionId = UUID.randomUUID().toString().replaceAll("-", "");
			addSession("actionId", actionId);
			String eserviceBxczRedirectUri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/bxczDoLogin";
			String url = pbs101url + "?response_type=code&scope=openid&client_id=" + client_id + "&state=" + Base64.getEncoder().encodeToString(new Gson().toJson(new BxczState(actionId)).getBytes())
					+ "&nonce=" + actionId + "&redirect_uri=" + eserviceBxczRedirectUri;
			this.setResponseObj(ResponseObj.SUCCESS, "", url);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	@GetMapping("/autoBxczLogin")
	public String autoBxczLogin(HttpServletRequest request) {
		String actionId = UUID.randomUUID().toString().replaceAll("-", "");
		addSession("actionId", actionId);
		String eserviceBxczRedirectUri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/bxczDoLogin";
		return "redirect:" + pbs101url + "?response_type=code&scope=openid&client_id=" + client_id + "&state=" + Base64.getEncoder().encodeToString(new Gson().toJson(new BxczState(actionId)).getBytes())
				+ "&nonce=" + actionId + "&redirect_uri=" + eserviceBxczRedirectUri;
	}

	@GetMapping("/bxczDoLogin")
	public String bxczDoLogin(BxczRedirectParam param, HttpServletRequest request) {

		String loginSuccessPage = "redirect:dashboard";
		try {
			if (StringUtils.isBlank(param.getCode())) {
				resetVerifyCode();
				addAttribute("errorMessage", "Code不能爲空！");
				return "login";
			}

			if (StringUtils.isBlank(param.getState())) {
				resetVerifyCode();
				addAttribute("errorMessage", "State不能爲空！");
				return "login";
			}

			BxczState bxczState = new Gson().fromJson(new String(Base64.getDecoder().decode(param.getState())), BxczState.class);
			if (bxczState == null || !StringUtils.equals(getSessionStr("actionId"), bxczState.getActionId())) {
				resetVerifyCode();
				addAttribute("errorMessage", "ActionId錯誤！");
				return "login";
			}
			if(StringUtils.isBlank(BaseRestClient.getAccessKey())) {
				try {
					String encrypAccessKey = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "eservice_api.accessKey");
					BaseRestClient.setAccessKey(EncryptionUtil.Decrypt(encrypAccessKey));
				} catch(Exception e) {
					logger.error("Set API access key error: ", e);
				}
			}

			String eserviceBxczRedirectUri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/bxczDoLogin";
			String idToken = loginService.doLoinBxcz(bxczState.getActionId(), param.getCode(), eserviceBxczRedirectUri);
			logger.info("call pbs-102 result id_token is: {}", idToken);
			String rocId = parseIdToken(idToken);
			if (StringUtils.isBlank(rocId)) {
				addAttribute("errorMessage", "login error!");
				return "login";
			}
			addSession("BXCZ_ID_TOKEN", idToken);
			UsersVo userDetail = registerUserService.getBxczUserByRocId(rocId);

			if (userDetail == null) {
				addSession("BXCZ_REGISTER_FLAG", true);
				addSession("rocId", rocId);
				removeFromSession("actionId");
				return "redirect:firstUseStart";
			}
			KeycloakUser keycloakUser = keycloakService.getUser(userDetail.getUserId());
			String userId = userDetail.getUserName();
			if (keycloakUser != null) {
				if (userDetail != null && userDetail.getStatus().equals("locked")) {
					String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0099");
					addAuditLog(userId, "0", "N");
					addAttribute("errorMessage", errorMessage);
					return "login";
				}

				loginService.noitfyUser(userId, keycloakUser);

				addSession(ApConstants.KEYCLOAK_USER, keycloakUser);
				addSession(ApConstants.LOGIN_IN_BXCZ, true);
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
						addSession(mapKey, vo.getParameterValue());
						logger.debug("original key={}, mapKey={}, mapValue={}", key, mapKey, vo.getParameterValue());
					}

					if (key.equals("DOWN_LISTING_2")) {
						vo2 = (ParameterVo) paraMap.get(key);
						addSession(key, vo2.getParameterValue());
						logger.debug("layout key={},  layou value={}", key, vo2.getParameterValue());
						vo2 = null;
					}
					if (key.indexOf("SHOW_LISTING") != -1) {
						String mapKey = "showProdCode" + key.replace("SHOW_LISTING", "").replace("_CODE", "");
						ParameterVo vo = (ParameterVo) paraMap.get(key);
						addSession(mapKey, vo.getParameterValue());
						logger.debug("original key={}, mapKey={}, mapValue={}", key, mapKey, vo.getParameterValue());
					}

				}

				// 取得使用者不能訪問的DIV權限
				List<String> rejectDivList = fuctionAuthService.getRejectDivName(keycloakUser.getId());
				addSession("REJECT_DIV_LIST", (Serializable) rejectDivList);

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
					Map<String, Boolean> identity = new HashMap<String, Boolean>();
					LilipmVo lilipmVo = lilipmService.findByRocId(userDetail.getRocId());
					if (lilipmVo != null && !StringUtils.isEmpty(lilipmVo.getLipmName1())) {
						userDetail.setUserName(lilipmVo.getLipmName1());
						userDetail.setUserNameBase64(unicodeService.convertString2Unicode(lilipmVo.getLipmName1()));
						identity.put("lilipm", true);
					} else {
						identity.put("lilipm", false);
					}
					LilipiVo tempLilipiVo = new LilipiVo();
					tempLilipiVo.setLipiId(userDetail.getRocId());
					List<LilipiVo> lilipiVoList = lilipiService.getLilipi(tempLilipiVo);
					if (lilipiVoList != null && lilipiVoList.size() > 0) {
						identity.put("lilipi", true);
						if (lilipmVo == null) {
							LilipiVo lilipiVo = lilipiVoList.get(0);
							userDetail.setUserName(lilipiVo.getLipiName());
							userDetail.setUserNameBase64(unicodeService.convertString2Unicode(lilipiVo.getLipiName()));
						}
					} else {
						identity.put("lilipi", false);
					}

					logger.info("***userType=" + userDetail.getUserType() + "***");
					boolean isPartner = loginService.isPartnerRole(userDetail.getUserType());
					if (isPartner) {//登入者為內部人員及保代角色(admin,agent)時強制給予要／被保人身份
						logger.info("***start force to setting user identity for admin.***");
						identity.put("lilipm", true);
						identity.put("lilipi", true);
						logger.info("***end force to setting user identity for admin.***");
					}

					userDetail.setIdentity(identity);
					logger.info("to setting userIdentity=" + Arrays.asList(identity));
					addSession(UserDataInfo.USER_DETAIL, userDetail);

					// 第一次登入需要變更密碼
					if (userDetail.getLastChangPasswordDate() == null) {
						//20190503 check in AuthenticationInterceptor
						//loginSuccessPage = "redirect:changePassword1";
					} else {
						// 判斷是否為內部人員及保代角色
						boolean isPartnerRole = loginService.isPartnerRole(userDetail.getUserType());
						if (isPartnerRole) {
							loginSuccessPage = "redirect:partner";
						}
					}

					// 設定一般及保戶會員的相關資訊
					String userRocId = getUserRocId();
					logger.debug("Find login user's rocId: {}", userRocId);
					if (!StringUtils.isEmpty(userRocId)) {
						List<String> policyNos = lilipmService.getUserPolicyNos(userRocId);
						if (policyNos != null && policyNos.size() > 0) {
							// 使用者的保單號碼清單
							addSession(UserDataInfo.USER_POLICY_NO_LIST, (Serializable) policyNos);
						}
						// 使用者的保單商品名稱
						addSession(UserDataInfo.USER_PRODUCT_NAME_LIST, (Serializable) loginService.getProductNameList(userRocId));
						// 使用者的保單主約被保人清單
						addSession(UserDataInfo.USER_MAIN_INSURED_NAME_LIST, (Serializable) loginService.getMainInsuredNameList(userRocId));
						// 保戶所有保單
						addSession(UserDataInfo.USER_ALL_POLICY_LIST, (Serializable) getUserOnlineChangePolicyList(userId, userRocId));

						PolicyListResponse policyListResponse = getPolicyListByUser(userId, userRocId);
						// 投資型保單
						addSession(UserDataInfo.USER_INVT_POLICY_LIST, (Serializable) policyListResponse.getInvtPolicyList());
						// 保障型保單
						addSession(UserDataInfo.USER_BENEFIT_POLICY_LIST, (Serializable) policyListResponse.getBenefitPolicyList());
					}

					// 取得上次登入時間
					Date lastLoginTime = loginService.getLastLoginTime(userId);
					if (lastLoginTime != null) {
						addSession(UserDataInfo.LAST_LOGIN_TIME, lastLoginTime);
					}
				}

				addAttribute("errorMessage", "");
				addAuditLog(userId, "1", "N");
				removeFromSession("actionId");

				if (ApConstants.isNotice && "member".equals(userDetail.getUserType())) {
					List<AuditLogVo> auditLogList = loginService.getLastAuditLog(userId, "3");
					addSession("auditLogList", (Serializable) auditLogList);
				}

				return loginSuccessPage;
			}
		} catch (Exception e) {
			logger.error("Unable to doLogin: {}", ExceptionUtils.getStackTrace(e));
			addAttribute("errorMessage", ApConstants.SYSTEM_ERROR);
			resetVerifyCode();
		}
		return "login";
	}

	private String parseIdToken(String idToken) throws Exception {

		if (StringUtils.isBlank(idToken)) {
			return null;
		}

		String[] split_string = idToken.split("\\.");
		String base64EncodedHeader = split_string[0];
		String base64EncodedBody = split_string[1];

		logger.debug("~~~~~~~~~ JWT Header ~~~~~~~");
		String header = new String(Base64.getUrlDecoder().decode(base64EncodedHeader));
		logger.debug("JWT Header : " + header);


		logger.debug("~~~~~~~~~ JWT Body ~~~~~~~");
		String body = new String(Base64.getUrlDecoder().decode(base64EncodedBody));
		logger.debug("JWT Body : " + body);
		return MyJacksonUtil.readValue(body, "/userId");
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