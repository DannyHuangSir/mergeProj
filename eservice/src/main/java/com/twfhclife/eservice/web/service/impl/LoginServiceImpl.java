package com.twfhclife.eservice.web.service.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.twfhclife.eservice.web.dao.BxczDao;
import com.twfhclife.generic.api_model.*;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.web.dao.LoginDao;
import com.twfhclife.eservice.web.dao.UserDataDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.AuditLogVo;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.BaseRestClient;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.SsoClient;
import com.twfhclife.generic.model.KeycloakUserSession;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.service.ISendSmsService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

/**
 * 前台登入服務.
 * 
 * @author all
 */
@Service
public class LoginServiceImpl implements ILoginService {

	private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

	@Autowired
	private UserDataDao userDataDao;

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private SsoClient ssoClient;

	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private IMailService mailService;

	@Autowired
	private IRegisterUserService registerUserService;

	@Autowired
	private ISendSmsService smsService;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private MessageTemplateClient messageTemplateClient;

	@Value("${keycloak.default-realm}")
	protected String DEFAULT_REALM;

	@Value("${keycloak.clientId}")
	protected String DEFAULT_CLIENT_ID;

	@Value("${spring.profiles.active}")
	protected String RUNNING_ENV;

	/**
	 * 內部人員類型.
	 *
	 * <pre>
	 * admin: 內部人員
	 * agent: 保代人員
	 * </pre>
	 */
	private static List<String> partnerTypeList = Arrays.asList(new String[] { "admin", "agent" });

	private RestTemplate restTemplate;

	public LoginServiceImpl() {
		//Fix the RestTemplate to be a singleton instance.
		restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;

		// Set the request factory.
		// IMPORTANT: This section I had to add for POST request. Not needed for GET
		int milliseconds = 20*1000;
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(milliseconds);
		httpRequestFactory.setConnectTimeout(milliseconds);
		httpRequestFactory.setReadTimeout(milliseconds);
		restTemplate.setRequestFactory(httpRequestFactory);

		// Add converters
		// Note I use the Jackson Converter, I removed the http form converter
		// because it is not needed when posting String, used for multipart forms.
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}

	/**
	 * 登入.
	 *
	 * @param loginRequestVo LoginRequestVo
	 * @return 回傳登入結果
	 */


	@Override
	public LoginResultVo doLogin(LoginRequestVo loginRequestVo) {
		// 前台登入Request
		String userName = loginRequestVo.getUserId();
		String password = loginRequestVo.getPassword();
		String sessionValidateCode = loginRequestVo.getSessionValidateCode();
		String validateCode = loginRequestVo.getValidateCode();
		String fbId = loginRequestVo.getFbId();
		String fbAccessToken = loginRequestVo.getFbAccessToken();
		String cardSN = loginRequestVo.getCardSN();
		String certb64 = loginRequestVo.getCertb64();
		String grantType = "password"; // 登入驗證方式(預設為密碼驗證)

		// SSO Login Request
		KeycloakLoginRequest loginReq = new KeycloakLoginRequest();
		loginReq.setUsername(userName);
		loginReq.setRealm(DEFAULT_REALM);
		loginReq.setPassword(password);
		loginReq.setClientId(DEFAULT_CLIENT_ID);
		loginReq.setFbId(fbId);
		loginReq.setAccessToken(fbAccessToken);
		loginReq.setMoicaId(cardSN);
		loginReq.setMoicaCert(certb64);
		loginReq.setGrantType(grantType);

		// 登入結果物件
		LoginResultVo loginResultVo = new LoginResultVo();
		loginResultVo.setLoginType(grantType);

		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = null;
		if (StringUtils.isNotEmpty(fbId) && StringUtils.isNotEmpty(fbAccessToken)) {
			// FB登入
			logger.info("Login by FB: fbId={}, token={}", fbId, fbAccessToken);
			loginReq.setGrantType("fb");
			loginResultVo.setLoginType("fb");
		} else if(StringUtils.isNotEmpty(cardSN) && StringUtils.isNotEmpty(certb64)) {
			// 自然人憑證登入
			logger.info("Login by IC Card: cardSN={}, certb64={}", cardSN, certb64);
			loginReq.setGrantType("moica");
			loginResultVo.setLoginType("moica");
		} else {
			logger.info("Login by UserId/Password: userId={}", userName);
			boolean isValidateCodeOk = StringUtils.equals(validateCode, sessionValidateCode);
			loginResultVo.setVerifyValidateCode(isValidateCodeOk);

			// 驗證碼不正確直接回傳
			logger.info("Login by UserId/Password: validateCode result={}", isValidateCodeOk);
			if (!isValidateCodeOk) {
				return loginResultVo;
			}
		}

		if(StringUtils.isBlank(BaseRestClient.getAccessKey())) {
			try {
				String encrypAccessKey = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "eservice_api.accessKey");
				BaseRestClient.setAccessKey(EncryptionUtil.Decrypt(encrypAccessKey));
			} catch(Exception e) {
				logger.error("Set API access key error: ", e);
			}
		}

		// call sso api login
		apiResponseObj = ssoClient.login(loginReq);
		if (apiResponseObj != null) {
			ReturnHeader returnHeader = apiResponseObj.getReturnHeader();
			KeycloakLoginResponse keycloakLoginResponse = apiResponseObj.getResult();
			if (returnHeader != null) {
				loginResultVo.setReturnCode(returnHeader.getReturnCode());
				loginResultVo.setReturnMsg(returnHeader.getReturnMesg());
			}

			logger.info("Call sso login returnCode={}", loginResultVo.getReturnCode());
			logger.info("Call sso login returnMsg={}", loginResultVo.getReturnMsg());
			if (ReturnHeader.SUCCESS_CODE.equals(loginResultVo.getReturnCode()) && keycloakLoginResponse != null) {
				String keycloakUserId = keycloakLoginResponse.getUserId();
				String sessionState = keycloakLoginResponse.getSessionState();
				logger.info("Find login user[{}] keycloak id={}, sessionState={}", userName, keycloakUserId, sessionState);

				KeycloakUser keycloakUser = keycloakService.getUser(keycloakUserId);
				if (keycloakUser != null) {
					logger.info("Find login keycloakUser Object={}",
							ToStringBuilder.reflectionToString(keycloakUser, ToStringStyle.MULTI_LINE_STYLE));
					keycloakUser.setAccessToken(keycloakLoginResponse.getAccessToken());
					keycloakUser.setRefreshToken(keycloakLoginResponse.getRefreshToken());
					keycloakUser.setSessionState(sessionState);
					loginResultVo.setKeycloakUser(keycloakUser);
				} else {
					logger.info("Unable to find keycloakUser Object with id: {}", keycloakUserId);
				}
			}
		} else {
			//call api login fail, try to login from keycloak
			logger.info("call api login fail, try to login from keycloak");
			logger.error("##### API service fail, please check wso2 or eservice_api! #####");
			if (loginReq.getGrantType().equals("fb") || loginReq.getGrantType().equals("moica")) {
				loginResultVo.setReturnCode("API_ERROR");
				loginResultVo.setReturnMsg("後端系統異常，請使用帳號密碼登入!");
				loginResultVo.setReturnMsg(parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0098"));
			} else {
				KeycloakUser keycloakUser = keycloakService.login(loginReq.getUsername(), loginReq.getPassword());
				loginResultVo.setKeycloakUser(keycloakUser);
			}
		}

		//20180424 CR16 系統登入成功/失敗發送電子郵件至要保人信箱
		if (ApConstants.isNotice && !"dev".equals(RUNNING_ENV)) {
			UsersVo userVo = registerUserService.getUserByAccount(userName);
			if (userVo != null && "member".equals(userVo.getUserType())) {
				boolean isMail = "1".equals(userVo.getMailFlag());
				boolean isSms = "1".equals(userVo.getSmsFlag());
				String isSuccess = loginResultVo.getKeycloakUser() != null ? "成功" : "失敗";
				String loginTime = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");

				HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				String remoteAddr = req.getHeader("X-FORWARDED-FOR");
				if (StringUtils.isEmpty(remoteAddr)) {
					remoteAddr = req.getRemoteAddr();
				}
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("LoginTime", loginTime);
				paramMap.put("LoginStatus", isSuccess);
				paramMap.put("ClientIP", remoteAddr);

				//1.不論成功或失敗皆發送電子郵件/簡訊通知保戶
				if (userVo.getLoginFailCount() == null) {
					userVo.setLoginFailCount(0);
				}
				if (isMail) {
					try {
						List<String> receivers = new ArrayList<String>();
						receivers.add(userVo.getEmail());

						if (userVo.getStatus().equals("enable")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_MAIL-002", receivers, paramMap, "email");
						} else if (userVo.getStatus().equals("locked")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_MAIL-003", receivers, paramMap, "email");
						}
					} catch (Exception e) {
						logger.error("send login notice mail error : ", e);
					}
				}
				if (isSms) {
					try {
						List<String> receivers = new ArrayList<String>();
						receivers.add(userVo.getMobile());
						if (userVo.getStatus().equals("enable")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_SMS-002", receivers, paramMap, "sms");
						} else if (userVo.getStatus().equals("locked")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_SMS-003", receivers, paramMap, "sms");
						}
					} catch (Exception e) {
						logger.error("send login notice SMS error : ", e);
					}
				}

				//2.登入失敗N次帳戶鎖定,需進線客服或臨櫃解鎖方可再次登入,且解鎖後隨機生成新密碼並於登入成功後強制變更密碼,且不可與前N次相同
				UsersVo vo = new UsersVo();
				vo.setUserId(userVo.getUserId());
				if (loginResultVo.getKeycloakUser() != null) { //login success
					if (userVo.getLoginFailCount() > 0 && !userVo.getStatus().equals("locked")) {
						//login success, reset fail count
						vo.setLoginFailCount(0);
						userDao.updateCustInfo(vo);
					}
				} else { //login fail
					vo.setLoginFailCount(userVo.getLoginFailCount() + 1);
					if (vo.getLoginFailCount() == 5) {
						//login fail 5 times, lock account and count + 1
						vo.setStatus("locked");
					}
					userDao.updateCustInfo(vo);
				}
			}
		}

		return loginResultVo;
	}

	/**
	 * 登出.
	 *
	 * @param  realm
	 * @param  userId
	 * @return 回傳結果
	 */
	public boolean doLogout(String realm, String userId) {
		boolean result = ssoClient.logout(realm, userId);
		return result;
	}

	/**
	 * 取得登入sessionId.
	 *
	 * @param keyCloakUserId KeyCloak用戶id
	 * @return 回傳登入sessionId
	 */
	@Override
	public List<String> getKeycloakUserSessionIdList(String keyCloakUserId) {
		KeycloakUserSessionRequest apiReq = new KeycloakUserSessionRequest();
		apiReq.setUserId(keyCloakUserId);
		apiReq.setRealm(DEFAULT_REALM);
		apiReq.setClientId(DEFAULT_CLIENT_ID);

		List<String> userSessionIdList = new ArrayList<>();
		List<KeycloakUserSession> userSessionList = ssoClient.getUserSession(apiReq);
		if (!CollectionUtils.isEmpty(userSessionList)) {
			for (KeycloakUserSession keycloakUserSession : userSessionList) {
				if (!userSessionIdList.contains(keycloakUserSession.getSessionId())) {
					userSessionIdList.add(keycloakUserSession.getSessionId());
				}
			}
		}

		return userSessionIdList;
	}

	/**
	 * 登出其他使用者.
	 *
	 * @param  sessionIdList sessionId清單
	 * @return 回傳結果
	 */
	public String logoutOtherUser(List<String> sessionIdList) {
		KeycloakUserSessionRequest apiReq = new KeycloakUserSessionRequest();
		apiReq.setRealm(DEFAULT_REALM);
		apiReq.setClientId(DEFAULT_CLIENT_ID);
		apiReq.setSessionIds(sessionIdList);
		return ssoClient.deleteUserSession(apiReq);
	}

	/**
	 * 取得使用者的保單主約被保險人名稱清單.
	 *
	 * @param rocId 用戶證號
	 * @return 回傳使用者的保單名稱清單
	 */
	@Override
	public List<Map<String, String>> getMainInsuredNameList(String rocId) {
		return userDataDao.getMainInsuredNameList(rocId);
	}

	/**
	 * 取得使用者的保單名稱清單.
	 *
	 * @param rocId 用戶證號
	 * @return 回傳使用者的保單名稱清單
	 */
	@Override
	public List<Map<String, String>> getProductNameList(String rocId) {
		List<Map<String, String>> l = userDataDao.getProductNameList(rocId);
		return l;
	}

	/**
	 * 是否是保代所屬角色.
	 *
	 * @param userType 使用者類型
	 *
	 * @return 回傳使用者的角色ID清單
	 */
	@Override
	public boolean isPartnerRole(String userType) {
		if (StringUtils.isEmpty(userType)) {
			return false;
		}
		return partnerTypeList.contains(userType);
	}


	/**
	 * 新增登入記錄.
	 *
	 * @param auditLogVo AuditLogVo
	 */
	@Override
	public void addAuditLog(AuditLogVo auditLogVo) {
		loginDao.addAuditLog(auditLogVo);
	}

	/**
	 * 更新登出記錄.
	 *
	 * @param userId 用戶名稱
	 */
	@Override
	public void updateLogoutDate(String userId) {
		loginDao.updateLogoutDate(userId);
	}

	/**
	 * 取得最近登入日期.
	 *
	 * @param userId 用戶名稱
	 */
	@Override
	public Date getLastLoginTime(String userId) {
		return loginDao.getLastLoginTime(userId);
	}

	@Override
	public List<AuditLogVo> getLastAuditLog(String userId, String row) {
		return loginDao.getLastAuditLog(userId, row);
	}

	@Value("${eservice.bxcz.pbs.102.url}")
	private String pbs102url;
	@Value("${eservice.bxcz.login.client_id}")
	private String clientId;
	@Value("${eservice.bxcz.login.client_secret}")
	private String clientSecret;

	@Autowired
	private BxczDao bxczDao;
    @Override
	public String doLoinBxcz(String code, String redirectUri) {
		try {

			MultiValueMap<String, String> headerMap = new HttpHeaders();
			headerMap.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
			headerMap.add("Content-Type", "application/json;charset=UTF-8");

			BxczLoginRequest bxczLoginRequest = new BxczLoginRequest();
			bxczLoginRequest.setCode(code);
			bxczLoginRequest.setGrant_type("authorization_code");
			bxczLoginRequest.setRedirect_uri(redirectUri);

			HttpEntity<BxczLoginRequest> entity = new HttpEntity<>(bxczLoginRequest, headerMap);

			ResponseEntity<BxczLoginResponse> resp = restTemplate.exchange(pbs102url,
					HttpMethod.POST, entity, BxczLoginResponse.class);
			logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(resp));

			if (!this.checkResponseStatus(resp)) {
				return null;
			}

			BxczLoginResponse obj = resp.getBody();
			bxczDao.insertBxczApiLog("call", "PBS-102", new Gson().toJson(bxczLoginRequest), new Gson().toJson(obj));
			if (obj != null) {
				return parseIdToken(obj.getId_token());
			}
		} catch (Exception e) {
			logger.error("doLoinBxcz: " + e);
			return null;
		}
		return null;
	}

	@Override
	public void noitfyUser(String userId, KeycloakUser keycloakUser) {
		if (ApConstants.isNotice && !"dev".equals(RUNNING_ENV)) {
			UsersVo userVo = registerUserService.getUserByAccount(userId);
			if (userVo != null && "member".equals(userVo.getUserType())) {
				boolean isMail = "1".equals(userVo.getMailFlag());
				boolean isSms = "1".equals(userVo.getSmsFlag());
				String isSuccess = keycloakUser != null ? "成功" : "失敗";
				String loginTime = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");

				HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				String remoteAddr = req.getHeader("X-FORWARDED-FOR");
				if (StringUtils.isEmpty(remoteAddr)) {
					remoteAddr = req.getRemoteAddr();
				}
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("LoginTime", loginTime);
				paramMap.put("LoginStatus", isSuccess);
				paramMap.put("ClientIP", remoteAddr);

				//1.不論成功或失敗皆發送電子郵件/簡訊通知保戶
				if (userVo.getLoginFailCount() == null) {
					userVo.setLoginFailCount(0);
				}
				if (isMail) {
					try {
						List<String> receivers = new ArrayList<String>();
						receivers.add(userVo.getEmail());

						if (userVo.getStatus().equals("enable")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_MAIL-002", receivers, paramMap, "email");
						} else if (userVo.getStatus().equals("locked")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_MAIL-003", receivers, paramMap, "email");
						}
					} catch (Exception e) {
						logger.error("send login notice mail error : ", e);
					}
				}
				if (isSms) {
					try {
						List<String> receivers = new ArrayList<String>();
						receivers.add(userVo.getMobile());
						if (userVo.getStatus().equals("enable")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_SMS-002", receivers, paramMap, "sms");
						} else if (userVo.getStatus().equals("locked")) {
							messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_SMS-003", receivers, paramMap, "sms");
						}
					} catch (Exception e) {
						logger.error("send login notice SMS error : ", e);
					}
				}

				//2.登入失敗N次帳戶鎖定,需進線客服或臨櫃解鎖方可再次登入,且解鎖後隨機生成新密碼並於登入成功後強制變更密碼,且不可與前N次相同
				UsersVo vo = new UsersVo();
				vo.setUserId(userVo.getUserId());
				if (keycloakUser != null) { //login success
					if (userVo.getLoginFailCount() > 0 && !userVo.getStatus().equals("locked")) {
						//login success, reset fail count
						vo.setLoginFailCount(0);
						userDao.updateCustInfo(vo);
					}
				} else { //login fail
					vo.setLoginFailCount(userVo.getLoginFailCount() + 1);
					if (vo.getLoginFailCount() == 5) {
						//login fail 5 times, lock account and count + 1
						vo.setStatus("locked");
					}
					userDao.updateCustInfo(vo);
				}
			}
		}
	}

	private boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
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
}