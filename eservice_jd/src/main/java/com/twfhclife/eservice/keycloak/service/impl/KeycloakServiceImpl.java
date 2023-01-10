package com.twfhclife.eservice.keycloak.service.impl;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.api_client.SsoClient;
import com.twfhclife.eservice.keycloak.model.KeycloakLoginRequest;
import com.twfhclife.eservice.keycloak.model.KeycloakLoginResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.service.AbstractKeycloakService;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.KeycloakUtil;
import com.twfhclife.eservice.util.MyStringUtil;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.UsersVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class KeycloakServiceImpl extends AbstractKeycloakService implements KeycloakService {
	
	private static final Logger logger = LogManager.getLogger(KeycloakServiceImpl.class);

	@Value("${keycloak.default-realm}")
	protected String DEFAULT_REALM;

	@Value("${keycloak.clientId}")
	protected String DEFAULT_CLIENT_ID;
	
	@Value("${keycloak.clientSecret}")
	protected String ESERVICE_CLIENT_SECRET;
	
	@Autowired
	SsoClient ssoClient;
	@Autowired
	KeycloakUtil kutil;
	@Autowired
	private UsersDao userDao;

	/**
	 * 登入.
	 * 
	 * @param username 使用者帳號
	 * @param password 使用者密碼
	 * @return 回傳KeycloakUser
	 */
	@Override
	public KeycloakUser login(String username, String password) {
		KeycloakUser keycloakUser = new KeycloakUser();
		try {
			Keycloak keycloakClient = getKeycloakClient(DEFAULT_REALM, DEFAULT_CLIENT_ID, username, password);
			String accessToken = getAccessTokenString(keycloakClient);

			// TODO 檢查accessToken
			if (StringUtils.isEmpty(accessToken)) {
				// TODO
				return null;
			}
			
			UserRepresentation user = findByUsername(DEFAULT_REALM, username);
			keycloakUser.setAccessToken(accessToken);
			
			// TODO 理論上應該只會有一筆，需要檢查做例外處理
			if (user == null) {
				// TODO
				return null;
			}
			
			convertKeycloakUser(user, keycloakUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
			
		return keycloakUser;
	}
	
	/**
	 * ExchangeToken版本
	 */
	@Override
	public KeycloakLoginResponse loginEserviceByFbToken(String fbId, String fbToken) {
		KeycloakLoginResponse loginResponse = new KeycloakLoginResponse();
		try {
			UsersVo findUser = getUserByFbId(fbId);
			if(findUser == null) {
				return null;
			}
			
			Map<String, Object> result = kutil.exchangeFbToken(DEFAULT_REALM, DEFAULT_CLIENT_ID, ESERVICE_CLIENT_SECRET, fbToken);
			String resultStatus = (String) result.get("loginResult");
			if("true".equals(resultStatus) && MyStringUtil.isNotNullOrEmpty(result.get("access_token"))) {
				loginResponse.setStatus("SUCCESS");
				loginResponse.setAccessToken(MyStringUtil.objToStr(result.get("access_token")));
				loginResponse.setRefreshToken(MyStringUtil.objToStr(result.get("refresh_token")));
				loginResponse.setExpiresIn(MyStringUtil.convertLong(result.get("expires_in")));
				loginResponse.setRefreshExpiresIn(MyStringUtil.convertLong(result.get("refresh_expires_in")));
				loginResponse.setTokenType(MyStringUtil.objToStr(result.get("token_type")));
				loginResponse.setSessionState(MyStringUtil.objToStr(result.get("session_state")));
			} else {
				loginResponse.setStatus("FAIL");
			}
			
			Map resultMap = kutil.validateToken(loginResponse.getAccessToken(), DEFAULT_REALM);
			String userId = MyStringUtil.objToStr(resultMap.get("userId"));
			if(MyStringUtil.isNotNullOrEmpty(userId)) {
				loginResponse.setUserId(userId);
			} else {
				loginResponse.setStatus(MyStringUtil.objToStr(resultMap.get("error")));
			}
			
		} catch (NotAuthorizedException nae) {
			loginResponse.setStatus("FAIL");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			loginResponse.setStatus("ERROR");
		}

		return loginResponse;
	}
	
	@Override
	public KeycloakLoginResponse loginByMoica(String moicaId, String ca, String realm, String clientId) {
		KeycloakLoginResponse loginResponse = new KeycloakLoginResponse();
		try {
			UsersVo findUser = getUserByMoicaId(moicaId);
			if(findUser == null) {
				return null;
			}
			String username = findUser.getUserName();
			String idp = EncryptionUtil.Decrypt(findUser.getPassword());
			
			String accessToken = "";
			String refreshToken = "";
			Keycloak keycloakClient = getKeycloakClient(realm, clientId, username, idp);
			AccessTokenResponse accessTokenResponse = keycloakClient.tokenManager().getAccessToken();
			accessToken = accessTokenResponse.getToken();
			refreshToken = accessTokenResponse.getRefreshToken();
			if (MyStringUtil.isNullOrEmpty(accessToken)) {
				loginResponse.setStatus("FAIL");
			} else {
				loginResponse.setStatus("SUCCESS");
			}
			loginResponse.setAccessToken(accessToken);
			loginResponse.setRefreshToken(refreshToken);
			loginResponse.setExpiresIn(accessTokenResponse.getExpiresIn());
			loginResponse.setRefreshExpiresIn(accessTokenResponse.getRefreshExpiresIn());
			loginResponse.setTokenType(accessTokenResponse.getTokenType());
			loginResponse.setSessionState(accessTokenResponse.getSessionState());

			Map resultMap = kutil.validateToken(accessToken, realm);
			String userId = MyStringUtil.objToStr(resultMap.get("userId"));
			if(MyStringUtil.isNotNullOrEmpty(userId)) {
				loginResponse.setUserId(userId);
			} else {
				loginResponse.setStatus(MyStringUtil.objToStr(resultMap.get("error")));
			}
			
		} catch (NotAuthorizedException nae) {
			loginResponse.setStatus("FAIL");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			loginResponse.setStatus("ERROR");
		}

		return loginResponse;
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
	
	public UsersVo getUserByMoicaId(String moicaId) {
		UsersVo uservo = null;
		try {
			uservo = userDao.getUserByCardSn(DEFAULT_REALM, moicaId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return uservo;
	}

	@Override
	public void logout(String keyCloakUserId) {
		logger.info("Logout user (id="+keyCloakUserId+")");
		logoutByUserId(DEFAULT_REALM, keyCloakUserId);
	}
	
	@Override
	public KeycloakLoginResponse validateToken(String accessToken, String refreshToken) {
		KeycloakLoginResponse apiResponse = null;
		try {
			KeycloakLoginRequest requestBody = new KeycloakLoginRequest();
			requestBody.setAccessToken(accessToken);
			requestBody.setRefreshToken(refreshToken);
			requestBody.setRealm(DEFAULT_REALM);
			requestBody.setClientId(DEFAULT_CLIENT_ID);
			
			apiResponse = ssoClient.validateToken(requestBody);//Call API
		} catch (Exception e) {
			logger.error("Unable to validateToken: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		
		return apiResponse;
	}

	@Override
	public KeycloakUser getUser(String userId) {
		UserRepresentation user = findByUserId(DEFAULT_REALM, userId);
		KeycloakUser keycloakUser = new KeycloakUser();
		convertKeycloakUser(user, keycloakUser);
		return keycloakUser;
	}

	@Override
	public KeycloakUser getUserByEmail(String email) {
		UserRepresentation user = findUserByEmail(DEFAULT_REALM, email);
		if (user == null) {
			return null;
		}
		return getUser(user.getId());
	}
	
	@Override
	public KeycloakUser getUserByUsername(String username) {
		UserRepresentation user = findByUsername(DEFAULT_REALM, username);
		KeycloakUser keycloakUser = new KeycloakUser();
		
		// TODO 理論上應該只會有一筆，需要檢查做例外處理
		if (user == null) {
			// TODO
			return null;
		}
		convertKeycloakUser(user, keycloakUser);
		return keycloakUser;
	}

	@Override
	public List<KeycloakUser> getUsers() {
		List<KeycloakUser> keycloakUsers = new ArrayList<>();
		List<UserRepresentation> users = getRealmUsers(DEFAULT_REALM);
		users.forEach(user -> {
			KeycloakUser keycloakUser = new KeycloakUser();
			convertKeycloakUser(user, keycloakUser);
			keycloakUsers.add(keycloakUser);
		});
		
		return keycloakUsers;
	}

	@Override
	public String createUser(KeycloakUser keycloakUser) throws Exception {
		// TODO 檢查帳號、電話、信箱唯一性
		
		String userUuid = null;
		Response response = addUser(DEFAULT_REALM, keycloakUser);
		final int status = response.getStatus();
		if (status == HttpStatus.CREATED.value()) {
			// 新增成功
			userUuid = getCreatedUserId(response);
		} else if(status == HttpStatus.CONFLICT.value()) {
			// User重複
			logger.info("Keycloak createUser status=409 conflict.");
			throw new Exception("409");
		} else {
			logger.info("Keycloak createUser error.");
			throw new Exception("400");
		}

		return userUuid;
	}

	@Override
	public void updateUser(KeycloakUser keycloakUser, FederatedIdentityRepresentation irLink) {
		UserRepresentation user = findByUserId(DEFAULT_REALM, keycloakUser.getId());
		Map<String, List<String>> attributes = user.getAttributes();
		if (!StringUtils.isEmpty(keycloakUser.getMobile())) {
			attributes.put("mobile", Arrays.asList(new String[] { keycloakUser.getMobile() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getRocId())) {
			attributes.put("rocId", Arrays.asList(new String[] { keycloakUser.getRocId() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getFbId())) {
			attributes.put("fb_id", Arrays.asList(new String[] { keycloakUser.getFbId() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getCardSn())) {
			attributes.put("card_sn", Arrays.asList(new String[] { keycloakUser.getCardSn() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getEmail())) {
			attributes.put("email", Arrays.asList(new String[] { keycloakUser.getEmail() }));
		}
		user.setAttributes(attributes);
		
		if(irLink != null) {
			List<FederatedIdentityRepresentation> irList = user.getFederatedIdentities();
			if(irList == null) {
				irList = new ArrayList<>();
			}
			irList.add(irLink);
			user.setFederatedIdentities(irList);
		}
		
		updateUser(DEFAULT_REALM, keycloakUser.getId(), user);
	}

	@Override
	public void updatePassword(KeycloakUser keycloakUser, String password) {
		resetPassword(DEFAULT_REALM, keycloakUser.getId(), password);
		//同時更新user attribute
		UserRepresentation user = findByUserId(DEFAULT_REALM, keycloakUser.getId());
		Map<String, List<String>> attributes = user.getAttributes();
		try {
			attributes.put("idp", Arrays.asList(new String[] { EncryptionUtil.Encrypt(password) }));
		} catch (Exception e) {
			logger.error("EncryptionUtil error."+e.getMessage());
		}
		user.setAttributes(attributes);
		updateUser(DEFAULT_REALM, keycloakUser.getId(), user);
	}

	@Override
	public boolean isTokenExpired(String accessToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshToken(String refreshToken) {
		// TODO Auto-generated method stub
		
	}
}
