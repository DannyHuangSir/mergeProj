package com.twfhclife.adm.controller;

import java.io.Serializable;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twfhclife.generic.util.ValidateCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.bridge.MessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.twfhclife.adm.model.FunctionVo;
import com.twfhclife.adm.model.LoginLogVo;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.service.ILoginService;
import com.twfhclife.adm.service.IParameterService;
import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.SsoClient;
import com.twfhclife.generic.api_model.KeycloakLoginRequest;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.CookieUtil;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

/**
 * 登入.
 * 
 * @author alan
 *
 */
@Controller
@EnableAutoConfiguration
public class LoginController extends BaseController {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	private SsoClient ssoClient;

	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private IParameterService parameterService;

	@RequestLog
	@RequestMapping("/login")
	public String login(String userId) {
		return "login";
	}
	
	@RequestMapping("/login-timeout")
	public String logout() {
		logger.debug("force logout");
		return "force_logout";
	}

	@RequestLog
	@PostMapping("/doLogin")
	public String doLogin(@Param("username") String username, @Param("password") String password,
						  @Param("validateCode") String validateCode, HttpServletRequest request, HttpServletResponse response) {
		KeycloakUser keycloakUser = null;
		boolean loginSucsess = true;
		String processUuid = UUID.randomUUID().toString();
		String returnCode = null;
		String returnMsg = null;
		try {
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				addAttribute("errorMessage", "請輸入正確的帳號/密碼");
				resetVerifyCode();
				loginSucsess = false;
			}

			String sessionValidateCode = (String)request.getSession().getAttribute(ApConstants.LOGIN_VALIDATE_CODE);
			if (!StringUtils.equals(validateCode, sessionValidateCode)) {
				addAttribute("errorMessage", "請輸入正確的驗證碼");
				resetVerifyCode();
				loginSucsess = false;
			}
			
			if (loginSucsess) {
				KeycloakLoginRequest req = new KeycloakLoginRequest();
				req.setUsername(username);
				req.setPassword(password);
				req.setGrantType("password");
				req.setRealm(ssoClient.ADM_REALM);
				req.setClientId(ssoClient.ADM_CLIENT);
				
				logger.info("Login eservice_adm with userId: {}", username);
				
				if(this.isApiMode()) {
					if(StringUtils.isBlank(ssoClient.ESERVICE_API_SECRET)) {
						String encryAccessKey = parameterService.getParameterValueByCode("eservice", "eservice_api.accessKey");
						ssoClient.setEserviceAccessKey(EncryptionUtil.Decrypt(encryAccessKey));
					}
					KeycloakLoginResponse apiResponse = ssoClient.login(req);// Call login api
					if(apiResponse != null) {
						returnCode = apiResponse.getLoginResultCode();
						returnMsg = apiResponse.getLoginResultMsg();
						if(MyStringUtil.isNotNullOrEmpty(returnCode) && returnCode.equals("SUCCESS")) {
							keycloakUser = new KeycloakUser();
							String accessToken = apiResponse.getAccessToken();
							String refreshToken = apiResponse.getRefreshToken();
							
							keycloakUser = keycloakService.getUser(ssoClient.ADM_REALM, apiResponse.getUserId());
							keycloakUser.setAccessToken(accessToken);
							keycloakUser.setRefreshToken(refreshToken);
							keycloakUser.setUserId(apiResponse.getUserId());
						}
					}
				} else {
					keycloakUser = keycloakService.login(username, password);
				}
				
//				logger.info("Get keycloakUser: {}", keycloakUser);
				
				if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
//					logger.info("{} AccessToken: {}", username, keycloakUser.getAccessToken());
					// TODO 登入記錄及檢核

					List<FunctionVo> menuList = loginService.getMenuList(username, keycloakUser.getId());
					Map<String, String> roleMap = loginService.getRoleName(keycloakUser.getId());
					
					addSession(ApConstants.KEYCLOAK_USER, keycloakUser);
					addSession(ApConstants.MENU_LIST, menuList);
					addSession(ApConstants.DIV_AUTH_MAP, loginService.getDivAuthMap());//用於DIV權限控制
					
					String loginShowName = (StringUtils.isEmpty(keycloakUser.getLastName()) ? keycloakUser.getUsername() : keycloakUser.getLastName());
					addSession(ApConstants.LOGIN_USER_ID, keycloakUser.getUsername());
					addSession(ApConstants.LOGIN_KEYCLOAK_USER_ID, keycloakUser.getId());
					addSession(ApConstants.LOGIN_USER_ROLE_NAME, roleMap.get("name"));
					addSession(ApConstants.ROLE_CODE_00, roleMap.get("key").contains(ApConstants.ROLE_CODE_00));
					addSession(ApConstants.ROLE_CODE_01, roleMap.get("key").contains(ApConstants.ROLE_CODE_01));
					addSession(ApConstants.ROLE_CODE_02, roleMap.get("key").contains(ApConstants.ROLE_CODE_02));
					addSession(ApConstants.LOGIN_SHOW_NAME, loginShowName);
					addSession(ApConstants.LOGIN_PROCESS_UUID, processUuid);
					// 設定系統參數
					Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID_ESERVICE);
					addSession(ApConstants.PAGE_WORDING, (Serializable) sysParamMap.get("PAGE_WORDING"));
					addSession(ApConstants.APPLICATION_ITEMS, (Serializable) sysParamMap.get("APPLICATION_ITEMS"));
					addSession(ApConstants.INSURANCE_CLAIM_UPLOADFILE, (Serializable) sysParamMap.get("INSURANCE_CLAIM_UPLOADFILE"));
					addSession(ApConstants.RELATION_ITEMS, (Serializable) sysParamMap.get("RELATION_ITEMS"));
					addSession(ApConstants.SEND_COMPANY_ITEMS, (Serializable) sysParamMap.get("SEND_COMPANY_ITEMS"));
					addSession(ApConstants.SEND_COMPANY_ITEMS_CONTACT, (Serializable) sysParamMap.get("SEND_COMPANY_ITEMS_CONTACT"));
					addSession(ApConstants.PAYMENT_METHOD, (Serializable) sysParamMap.get("PAYMENT_METHOD"));
					addSession(ApConstants.INSURANCE_ACCIDENT, (Serializable) sysParamMap.get("INSURANCE_ACCIDENT"));
					addSession(ApConstants.ONLINE_CHANGE_STATUS, (Serializable) sysParamMap.get("ONLINE_CHANGE_STATUS"));

					Map<String, ParameterVo> online_change_status = sysParamMap.get("ONLINE_CHANGE_STATUS");
					String [] str = {"1","2","3","5","6","7"};
					Map<String, ParameterVo> hashMap =new HashMap<>();
					for (String s : str) {
						ParameterVo parameterVo = online_change_status.get(s);
						if (parameterVo != null) {
							hashMap.put(s,parameterVo);
						}
					}
					logger.info("ApConstants.CONTENT_INFO_CHANGE_STATUS  log: {}", hashMap);
					addSession(ApConstants.CONTENT_INFO_CHANGE_STATUS, (Serializable) hashMap);



					addAttribute("errorMessage", "");
					CookieUtil.addCookie(response, CookieUtil.SSOToken, keycloakUser.getAccessToken(), null);
//					String t = CookieUtil.getCookieValue(request, CookieUtil.SSOToken);
//					logger.debug("t="+t);
				} else {
					if(returnCode != null && returnMsg!= null && !returnCode.equals("SUCCESS")) {
						addAttribute("errorMessage", returnMsg);
					} else {
						addAttribute("errorMessage", "登入失敗");
					}
					resetVerifyCode();
					loginSucsess = false;
				}
			}
		} catch (Exception e) {
			logger.error("Unable to login: {}", ExceptionUtils.getStackTrace(e));
			addAttribute("errorMessage", "系統發生錯誤，請重試或聯絡系統管理員。");
			
			// 清除登入資訊
			if (keycloakUser != null) {
//				 keycloakService.logout(keycloakUser.getId());
				ssoClient.logout(keycloakUser.getId());
			}
			loginSucsess = false;
		}
		
		String indexViewName = "redirect:index";
		String loginStatus = "1"; // 成功
		if (!loginSucsess) {
			indexViewName = "login";
			loginStatus = "0"; // 失敗
		}
		
		// 記錄登入記錄
		try {
			if (keycloakUser != null) {
				LoginLogVo vo = new LoginLogVo();
				vo.setId(processUuid);
				vo.setUserId(keycloakUser.getUserId());
				vo.setUserName(username);
				vo.setLoginStatus(loginStatus);
				vo.setSystemId("eservice_adm");
				vo.setClientIp(getClientIp());
				loginService.addLoginLog(vo);
			}
		} catch (Exception e) {
			logger.error("Unable to add login audit log: {}", ExceptionUtils.getStackTrace(e));
		}
		
		return indexViewName;
	}

	@RequestLog
	@GetMapping("/doLogout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {
		try {
			Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
			if (KeycloakUserObj != null) {
				 KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
				 
				 if(this.isApiMode()) {
					 ssoClient.logout(keycloakUser.getId());// Call logout api
				 } else {
					 keycloakService.logout(keycloakUser.getId());
				 }
			}
			
			Object processUuid = request.getSession().getAttribute(ApConstants.LOGIN_PROCESS_UUID);
			if (processUuid != null) {
				LoginLogVo vo = new LoginLogVo();
				vo.setId(processUuid.toString());
				loginService.updateLogoutLog(vo);
			}
			
			Enumeration<String> em = request.getSession().getAttributeNames();
			while (em.hasMoreElements()) {
				request.getSession().removeAttribute(em.nextElement().toString());
			}
			CookieUtil.delCookie(request, response, CookieUtil.SSOToken);
		} catch (Exception e) {
			logger.error("Unable to logout: {}", ExceptionUtils.getStackTrace(e));
		}
		
		return "redirect:login";
	}

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
}
