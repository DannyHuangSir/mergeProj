package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.api_client.KeycloakRegisterClient;
import com.twfhclife.eservice.api_client.SsoClient;
import com.twfhclife.eservice.api_model.ApiResponseObj;
import com.twfhclife.eservice.api_model.ReturnHeader;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.model.KeycloakUserAddRequest;
import com.twfhclife.eservice.keycloak.model.KeycloakUserResponse;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.MyJacksonUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.ParameterVo;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public List<ParameterVo> getTerms() {
		return parameterDao.getParameterByCategoryCode("eservice", ApConstants.REGISTER_TERM_PARAMETER_CATEGORY_CODE);
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

	public String updatePassword(String account, String password) throws Exception {
		KeycloakUser user = keycloakService.getUserByUsername(account);
		if(user == null) {
			return "使用者帳號不存在";
		} else {
			Map<String, Object> resultmap = keycloakService.resetPwd(DEFAULT_REALM, user.getId(), password);
			String result = resultmap.get("result").toString();
			if(result.contentEquals("true")) {
				logger.debug("update password success!");
				userDao.updatePassword(account);
				logger.debug("update last password date success!");
			} else {
				if(resultmap.get("error_description") != null && resultmap.get("error_description").toString().indexOf("Invalid password: must not be equal to any of last") != -1) {
					int end = resultmap.get("error_description").toString().indexOf(" passwords.");
					return "密碼不可與前" + resultmap.get("error_description").toString().substring(end-1, end) + "次相同";
				} else {
					return "error";
				}
			}
		}
		return "";
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
    public int incLoginFailCount(String userId) {
        return userDao.incLoginFailCount(userId);
    }

	@Override
	public int updateLoginSuccess(String userId) {
		return userDao.updateLoginSuccess(userId);
	}

	@Override
	public String changePassword(String account, String password) {
		KeycloakUser user = keycloakService.getUserByUsername(account);
		if(user == null) {
			return "使用者帳號不存在";
		} else {
			Map<String, Object> resultmap = keycloakService.resetPwd(DEFAULT_REALM, user.getId(), password);
			String result = resultmap.get("result").toString();
			if(result.contentEquals("true")) {
				logger.debug("update password success!");
				userDao.changePassword(account);
				logger.debug("update last password date success!");
			} else {
				if(resultmap.get("error_description") != null) {
					return resultmap.get("error_description").toString();
				} else {
					return "error";
				}
			}
		}
		return "密碼變更成功";
	}
}
