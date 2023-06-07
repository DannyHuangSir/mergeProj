package com.twfhclife.eservice.web.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.twfhclife.generic.api_model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
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
import com.twfhclife.generic.service.IUnicodeService;
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
	
	@Autowired
	private IUnicodeService unicodeService;

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

	@Resource(name = "baseRestClient")
	private BaseRestClient baseRestClient;
    @Override
	public String doLoinBxcz(String code, String redirectUri, String state) {
		return baseRestClient.postApiResponse(new Gson().toJson(new BxczLoginRequest("authorization_code", code, redirectUri, state)), pbs102url, String.class).getResult();
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

	@Override
	public List<PolicyListVo> mappingPolicyList(List<PolicyDetailVo> policyDetailList) {
		List<PolicyListVo> policyList = new ArrayList<PolicyListVo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		policyList = policyDetailList.stream().map(x->{
			PolicyListVo vo = new PolicyListVo();
			PolicyExtraVo policyExtraVo = new PolicyExtraVo();
			try {
				vo.setRocId(x.getLipiRocid());
				vo.setPolicyNo(x.getLipmInsuNo());
				vo.setGroupNo(x.getLipmInsuGrpNo());
				vo.setSeqNo(x.getLipmInsuSeqNo());
				vo.setStatus(x.getLipmSt());
				if(StringUtils.isNotEmpty(x.getLipiInsuBegDate())) {
					vo.setEffectiveDate(sdf.parse(x.getLipiInsuBegDate()));
				}

				if(StringUtils.isNotEmpty(x.getLipiInsuEndDate())) {
					vo.setExpireDate(sdf.parse(x.getLipiInsuEndDate()));
				}

				if(StringUtils.isNotEmpty(x.getLipmTredPaabDate())) {
					vo.setPaidToDate(sdf.parse(x.getLipmTredPaabDate()));
				}
				vo.setPaymentMethod(x.getLipmRcpMethCode());
				vo.setPaymentMode(x.getLipmRcpCode());
				vo.setPolicyType(x.getLipmInsuType());
				if(StringUtils.isNotEmpty(x.getLipiMainAmt())) {
					vo.setMainAmount(new BigDecimal(x.getLipiMainAmt()));
				}
				if(StringUtils.isNotEmpty(x.getLipiPremYear())) {
					vo.setPremYear(new BigDecimal(x.getLipiPremYear()));
				}
				if(StringUtils.isNotEmpty(x.getPaprPaabAmt())) {
					vo.setUnpaidAmount(new BigDecimal(x.getPaprPaabAmt()));
				}
				vo.setCurrency(x.getProdCurrency());
				vo.setAgentCode(x.getLipmAgenCode());
				vo.setAgentNaCode(x.getLipmAgenNaCode());
				vo.setAgentCodeBranch(x.getLipmAgenCodeBranch());
				vo.setCustomerName(x.getLipmName1());
				vo.setCustomerNameBase64(unicodeService.convertString2Unicode(x.getLipmName1()));
				vo.setMainInsuredName(x.getLipiName());
				vo.setMainInsuredNameBase64(unicodeService.convertString2Unicode(x.getLipiName()));
				if(StringUtils.isNotEmpty(x.getPaprPaabAmt())) {
					vo.setPaidAmount(new BigDecimal(x.getPaprPaabAmt()));
				}
				if(StringUtils.isEmpty(x.getLipiInsuEndDate())){
					vo.setExpiredFlag("N");
				}else{
					int dates = DateUtil.getDateInterval(x.getLipiInsuEndDate().substring(0,10));
					if(365 + dates  >= 0) {
						vo.setExpiredFlag("Y");
					}else {
						vo.setExpiredFlag("N");
					}
				}
				vo.setProdIsFatca(x.getProdIsFatca());
				vo.setProdType(x.getProdType());
				vo.setProductName(x.getSettChName());
				vo.setPolicyListType(x.getPolicyListType());

				policyExtraVo.setPolicyNo(x.getLipmInsuNo());

				if(StringUtils.isNotEmpty(x.getLomsAmt())) {
					policyExtraVo.setLoanAmount(new BigDecimal(x.getLomsAmt()));
				}
				if(StringUtils.isNotEmpty(x.getVal1PaprAmt())) {
					policyExtraVo.setRemainLoanValue(new BigDecimal(x.getVal1PaprAmt()));
				}
				if(StringUtils.isNotEmpty(x.getRat1Rate())) {
					policyExtraVo.setRoanRate(new BigDecimal(x.getRat1Rate()));
				}
				if(StringUtils.isNotEmpty(x.getAutrSubPrem())) {
					policyExtraVo.setAplAmount(new BigDecimal(x.getAutrSubPrem()));
				}
				policyExtraVo.setAutoRcpMk(x.getLipmAutoRcpMk());
				policyExtraVo.setMethAnnuPay(x.getLipiMethAnnuPay());
				policyExtraVo.setInmsBankCode(x.getInmsBankCode());
				policyExtraVo.setInmsBankBranchCode(x.getInmsBankBranchCode());
				policyExtraVo.setInmsAccountNo(x.getInmsAccountNo());
				if(StringUtils.isNotEmpty(x.getVal1ReseAmt())) {
					policyExtraVo.setVal1ReseAmt(new BigDecimal(x.getVal1ReseAmt()));
				}
				if(StringUtils.isNotEmpty(x.getVal1CancAmt())) {
					policyExtraVo.setVal1CancAmt(new BigDecimal(x.getVal1CancAmt()));
				}
				vo.setPolicyExtraVo(policyExtraVo);
			} catch (ParseException e) {
				logger.info("*** LOGIN  MAPPING POLICY DATA LIST ERROR: ***" , e);
			}
			return vo;
		}).collect(Collectors.toList());
		return policyList;
	}

	@Override
	public List<String> getpolicyInvestmentType() {
		return loginDao.getpolicyInvestmentType();
	}

}