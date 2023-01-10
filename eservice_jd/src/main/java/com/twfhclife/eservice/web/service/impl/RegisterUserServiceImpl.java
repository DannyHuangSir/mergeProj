package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_client.KeycloakRegisterClient;
import com.twfhclife.eservice.api_client.SsoClient;
import com.twfhclife.eservice.api_model.ApiResponseObj;
import com.twfhclife.eservice.api_model.ReturnHeader;
import com.twfhclife.eservice.keycloak.model.KeycloakUserAddRequest;
import com.twfhclife.eservice.keycloak.model.KeycloakUserResetPwdRequest;
import com.twfhclife.eservice.keycloak.model.KeycloakUserResponse;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.MyJacksonUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.RegisterQuestionVo;
import com.twfhclife.eservice.web.model.UserTermVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.*;

@Service
public class RegisterUserServiceImpl implements IRegisterUserService {

	private static final Logger logger = LogManager.getLogger(RegisterUserServiceImpl.class);

	@Value("${keycloak.default-realm}")
	protected String DEFAULT_REALM;

	@Value("${keycloak.clientId}")
	protected String DEFAULT_CLIENT_ID;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private ParameterDao parameterDao;

	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	private KeycloakRegisterClient keycloakRegisterClient;

	@Autowired
	private SsoClient ssoClient;

	@Autowired
	private IParameterService parameterService;

	/**
	 * 用身份證號取得用戶資料
	 *
	 * @param rocId 用戶證號
	 * @return UsersVo
	 */
	@Override
	public UsersVo getUserByRocId(String rocId) {
		return userDao.getUserByRocId(rocId);
	}

	@Override
	public String checkRegister(UsersVo user) {
		try {
			UsersVo returnUser = userDao.getUserByRocId(user.getRocId());
			if (returnUser != null) {
				//身分證號已存在
				logger.info("RocId("+user.getRocId()+") has been registered by username:"+user.getUserName());
				return "rocid";
			}
			/*
			KeycloakUser kuser = keycloakService.getUserByEmail(user.getEmail());
			if (kuser != null && kuser.getUsername() != null && !user.getEmail().equals("")) {
				logger.info("Email("+user.getEmail()+") has been registered by username:"+user.getUserName());
				//email已存在
				return "email";
			}
			*/

			if(StringUtils.isBlank(BaseRestClient.getAccessKey())) {
				try {
					String encrypAccessKey = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "eservice_api.accessKey");
					BaseRestClient.setAccessKey(EncryptionUtil.Decrypt(encrypAccessKey));
				} catch(Exception e) {
					logger.error("Set API access key error: ", e);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public List<ParameterVo> getTerms() {
		return parameterDao.getParameterByCategoryCode("eservice", ApConstants.REGISTER_TERM_PARAMETER_CATEGORY_CODE);
	}

	@Override
	public boolean getPolicyByRocId(String policyNo, String rocId) {
		String policy = userDao.getPolicyByRocId(rocId, policyNo);
		if (policy == null) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Map<String, String> registerUserData(UsersVo user) {
		logger.info("registerUserData user: {}", MyJacksonUtil.object2Json(user));

		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("userId", "");
		retMap.put("error", "");

		String userId = null;
		boolean keycloakCreated = false;

		// call API to register
		KeycloakUserAddRequest registerReq = new KeycloakUserAddRequest();
		registerReq.setUsername(user.getUserId().toLowerCase()); // 不分大小寫，統一轉小寫
		registerReq.setPassword(user.getPassword());
		registerReq.setRocId(user.getRocId());
		registerReq.setUserType(user.getUserType());
		registerReq.setMobile(user.getMobile());
		registerReq.setEmail(user.getEmail());
		registerReq.setFbId(user.getFbId());
		registerReq.setCardSn(user.getMoicaId());
		registerReq.setCreateUser(user.getUserId());

		ApiResponseObj<KeycloakUserResponse> apiResponse = keycloakRegisterClient.addUser(registerReq);
		if(apiResponse != null) {
			ReturnHeader header = apiResponse.getReturnHeader();
			try {
				KeycloakUserResponse registerRes = apiResponse.getResult();
				if(registerRes != null
						&& StringUtils.isNotBlank(registerRes.getUserId())) {
					userId = registerRes.getUserId();
					retMap.put("userId", registerRes.getUserId());
					keycloakCreated = true;

					// set online_flag='Y'
					userDao.updateOnlineFlagY(user.getUserId().toLowerCase());
				} else {
					keycloakCreated = false;
					retMap.put("error", header.getReturnCode());
					retMap.put("errorMsg", header.getReturnMesg());
				}
			} catch(Exception e) {
				// 原邏輯: 用戶名稱重複 (非錯誤故不寫error log) 409, 其他 400
				// API 回傳類型不同，無法判斷
				keycloakCreated = false;
				logger.error("Error occured in keycloakRegisterClient.addUser():", e);
				retMap.put("error", header.getReturnCode());
				retMap.put("errorMsg", header.getReturnMesg());
			}
		} else {
			keycloakCreated = false;
			retMap.put("error", "API");
		}

		// TODO:save term
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request.getSession().getAttribute(ApConstants.USER_TERM_REGISTER) != null) {
			Date termDate = (Date) request.getSession().getAttribute(ApConstants.USER_TERM_REGISTER);
			UserTermVo userTerm = new UserTermVo();
			userTerm.setUserId(user.getUserId().toLowerCase());
			userTerm.setAcceptDate(termDate);
			userTerm.setTermName(ApConstants.USER_TERM_REGISTER);
			userTerm.setMemo("rocId=" + user.getRocId() + ", userType=" + user.getUserType() + ", mobile=" + user.getMobile() + ", email=" + user.getEmail());
			userDao.createUserTerm(userTerm);
		}

		return retMap;
	}

	public UsersVo getUserByAccount(String account) {
		UsersVo user = userDao.getUserByAccount(account);
		return user;
	}

	public void updatePassword(String account, String password) throws Exception {
		// call keycloak
		KeycloakUserResetPwdRequest apiReq = new KeycloakUserResetPwdRequest();
		apiReq.setUsername(account);
		apiReq.setNewPassword(password);
		ApiResponseObj<?> resp = keycloakRegisterClient.resetPassword(apiReq);
		ReturnHeader returnHeader = resp.getReturnHeader();
		if (ReturnHeader.SUCCESS_CODE.equals(returnHeader.getReturnCode())) {
			userDao.updatePassword(account);
		} else {
			if (returnHeader.getReturnMesg().indexOf("Invalid password: must not be equal to any of last") != -1) {
				int end = returnHeader.getReturnMesg().indexOf(" passwords.");
				String msg = "密碼不可與前" + returnHeader.getReturnMesg().substring(end-1, end) + "次相同";
				throw new InvalidParameterException(msg);
			}
		}
	}

	public UsersVo getUserByFbId(String fbId) {
		UsersVo uservo = null;
		try {
			uservo = userDao.getUserByFbId(DEFAULT_REALM, fbId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return uservo;
	}

	public UsersVo getUserByCardSn(String cardSN) {
		UsersVo uservo = null;
		try {
			uservo = userDao.getUserByCardSn(DEFAULT_REALM, cardSN);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return uservo;
	}

	@Override
	public boolean checkOldSystemUser(String rocId, String password) {
		return userDao.checkOldSystemUser(rocId, password) > 0;
	}

	@Override
	public List<UsersVo> getMailPhoneByRocid(String rocId) {
		return userDao.getMailPhoneByRocid(rocId);
	}

	@Override
	public UsersVo getMailPhoneByRocidPolicyNo(String rocId, String policyNo) {
		return userDao.getMailPhoneByRocidPolicyNo(rocId, policyNo);
	}

}
