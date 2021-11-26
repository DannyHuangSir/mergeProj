package com.twfhclife.keycloak.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.generic.dao.AuthoDao;
import com.twfhclife.generic.dao.adm.UsersDao;
import com.twfhclife.generic.dao.KeycloakUserDao;
import com.twfhclife.generic.domain.KeycloakLoginResponse;
import com.twfhclife.generic.domain.KeycloakLoginResponseByEIN;
import com.twfhclife.generic.model.KeycloakUser;
import com.twfhclife.generic.model.KeycloakUserSession;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.model.UsersVo;
import com.twfhclife.generic.utils.KeycloakUtil;
import com.twfhclife.generic.utils.MyStringUtil;
import com.twfhclife.keycloak.service.AbstractKeycloakService;
import com.twfhclife.keycloak.service.KeycloakService;


@Component
public class KeycloakServiceImpl extends AbstractKeycloakService implements KeycloakService {

	private static final Logger logger = LogManager.getLogger(KeycloakServiceImpl.class);
	
	@Value("${keycloak.elife-realm}")
	protected String ESERVICE_REALM;
	@Value("${keycloak.elife-clientId}")
	protected String ESERVICE_CLIENT_ID;
	@Value("${keycloak.elife-clientSecret}")
	protected String ESERVICE_CLIENT_SECRET;

	@Autowired
	@Qualifier("apiUsersDao")
	private UsersDao userDao;
	
	@Autowired
	private AuthoDao authoDao;
	
	@Autowired
	KeycloakUtil kutil;
	
	@Autowired
	private KeycloakUserDao KeycloakUserDao;

	/**
	 * 登入.
	 * 
	 * @param username
	 *            使用者帳號
	 * @param password
	 *            使用者密碼
	 * @return 回傳KeycloakUser
	 */
	@Override
	public KeycloakLoginResponse login(String username, String password, String realm, String clientId) {
		KeycloakLoginResponse loginResponse = new KeycloakLoginResponse();
		try {
			String accessToken = "";
			String refreshToken = "";
			Keycloak keycloakClient = getKeycloakClient(realm, clientId, username, password);
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
			
			if(realm.equals("twfhclife")) {
				List<UserVo> pu = new ArrayList<>();
				pu = authoDao.getDeputyUser(userId);
//				if(pu != null && pu.size() > 0) {
					loginResponse.setDeputyOf(pu);
//				}
			}

		} catch (NotAuthorizedException nae) {
			loginResponse.setStatus("FAIL");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			loginResponse.setStatus("ERROR");
		}

		return loginResponse;
	}
	
	/**20211013 by 203999 */
	@Override
	public KeycloakLoginResponseByEIN loginByEIN(String username, String password, String realm, String clientId) {
		KeycloakLoginResponseByEIN loginResponse = new KeycloakLoginResponseByEIN();
		try {
			String accessToken = "";
			String refreshToken = "";
			Keycloak keycloakClient = getKeycloakClient(realm, clientId, username, password);
			AccessTokenResponse accessTokenResponse = keycloakClient.tokenManager().getAccessToken();
			accessToken = accessTokenResponse.getToken();
			refreshToken = accessTokenResponse.getRefreshToken();
			if (MyStringUtil.isNullOrEmpty(accessToken)) {
				loginResponse.setStatus("FAIL");
			} else {
				loginResponse.setStatus("SUCCESS");
			}
			
			loginResponse.setRocId("");
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

				UserRepresentation user = findByUsername(realm, username);
				if (user.getAttributes() != null) {
					user.getAttributes().forEach((k, v) -> {
						if ("rocId".equals(k)) {
							loginResponse.setRocId(v.get(0));
						}
					});
				}

			} else {
				loginResponse.setStatus(MyStringUtil.objToStr(resultMap.get("error")));
			}
			
			if(realm.equals("twfhclife")) {
				List<UserVo> pu = new ArrayList<>();
				pu = authoDao.getDeputyUser(userId);
//				if(pu != null && pu.size() > 0) {
					loginResponse.setDeputyOf(pu);
//				}
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
	public KeycloakLoginResponse loginByFb(String fbId, String fbToken, String realm, String clientId) {
		KeycloakLoginResponse loginResponse = new KeycloakLoginResponse();
		try {
			UsersVo findUser = getUserByFbId(fbId);
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
	
	/**
	 * ExchangeToken版本
	 */
	@Override
	public KeycloakLoginResponse loginEserviceByFbToken(String fbId, String fbToken) {
		KeycloakLoginResponse loginResponse = new KeycloakLoginResponse();
		try {
//			UsersVo findUser = getUserByFbId(fbId);
//			if(findUser == null) {
//				return null;
//			}
			
			Map<String, Object> result = kutil.exchangeFbToken(ESERVICE_REALM, ESERVICE_CLIENT_ID, ESERVICE_CLIENT_SECRET, fbToken);
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
			
			Map resultMap = kutil.validateToken(loginResponse.getAccessToken(), ESERVICE_REALM);
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
			uservo = userDao.getUserByFbId(ESERVICE_REALM, fbId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uservo;
	}
	
	public UsersVo getUserByMoicaId(String moicaId) {
		UsersVo uservo = null;
		try {
			uservo = userDao.getUserByCardSn(ESERVICE_REALM, moicaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uservo;
	}
	
	@Override
	public KeycloakLoginResponse validateToken(String accessToken, String refreshToken, String realm, String clientId) {
		KeycloakLoginResponse loginResponse = new KeycloakLoginResponse();
		try {
			Map resultMap = kutil.validateToken(accessToken, realm);
			String userId = MyStringUtil.objToStr(resultMap.get("userId"));
			String error = MyStringUtil.objToStr(resultMap.get("error_description"));
			if(MyStringUtil.isNotNullOrEmpty(userId)) {
				loginResponse.setUserId(userId);
				loginResponse.setStatus("SUCCESS");
			} else {
				loginResponse.setStatus("INVALID");
			}

			//當access token不合法時，嘗試用refresh token取得新的accessToken
			if(KeycloakUtil.VALIDATE_TOKEN_ERROR_MSG.equals(error) && MyStringUtil.isNotNullOrEmpty(refreshToken)) {
				// Refresh Token
				KeycloakLoginResponse refreshResult = refreshToken(refreshToken, realm, clientId);
				if(refreshResult.getStatus().equals("SUCCESS")) {
					loginResponse = refreshResult;
					Map tmpMap = kutil.validateToken(refreshResult.getAccessToken(), realm);
					userId = MyStringUtil.objToStr(tmpMap.get("userId"));
					loginResponse.setUserId(userId);
					loginResponse.setStatus("REFRESH");
				} else {
					loginResponse.setStatus("INVALID");
				}
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
	public KeycloakLoginResponse refreshToken(String refreshToken, String realm, String clientId) {
		KeycloakLoginResponse response = new KeycloakLoginResponse();
		try {
			Map<String, Object> result = kutil.refreshToken(refreshToken, realm, clientId);
			String resultStatus = (String) result.get("result");
			if("true".equals(resultStatus) && MyStringUtil.isNotNullOrEmpty(result.get("access_token"))) {
				response.setStatus("SUCCESS");
				response.setAccessToken(MyStringUtil.objToStr(result.get("access_token")));
				response.setRefreshToken(MyStringUtil.objToStr(result.get("refresh_token")));
				response.setExpiresIn(MyStringUtil.convertLong(result.get("expires_in")));
				response.setRefreshExpiresIn(MyStringUtil.convertLong(result.get("refresh_expires_in")));
				response.setTokenType(MyStringUtil.objToStr(result.get("token_type")));
			} else {
				response.setStatus("FAIL");
			}
		} catch (NotAuthorizedException nae) {
			response.setStatus("FAIL");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setStatus("ERROR");
		}

		return response;
	}

	@Override
	public void logout(String realm, String keyCloakUserId) {
		logger.info("Logout user (id=" + keyCloakUserId + ")");
		logoutByUserId(realm, keyCloakUserId);
	}
	
	@Override
	public List<KeycloakUserSession> getUserSessions(String realm, String userId, String clientId) {
		List<KeycloakUserSession> result = new ArrayList<>();
		try {
			result = kutil.getUserSessions(realm, userId, clientId);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}
	
	@Override
	public boolean deleteUserSession(String realm, List<String> sessionIds) {
		boolean result = false;
		try {
			for(String sessionId : sessionIds) {
				result = kutil.deleteSession(realm, sessionId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}

	@Override
	public KeycloakUser getUser(String realm, String userId) {
		UserRepresentation user = findByUserId(realm, userId);
		KeycloakUser keycloakUser = new KeycloakUser();
		convertKeycloakUser(user, keycloakUser);
		return keycloakUser;
	}

	@Override
	public KeycloakUser getUserByEmail(String realm, String email) {
		UserRepresentation user = findUserByEmail(realm, email);
		if (user == null) {
			return null;
		}
		return getUser(realm, user.getId());
	}

	@Override
	public KeycloakUser getUserByUsername(String realm, String username) {
		UserRepresentation user = findByUsername(realm, username);
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
	public String getUserIdByUsername(String realm, String username) {
		return findIdByUsername(realm, username);
	}

	@Override
	public List<KeycloakUser> getUsers(String realm) {
		List<KeycloakUser> keycloakUsers = new ArrayList<>();
		List<UserRepresentation> users = getRealmUsers(realm);
		users.forEach(user -> {
			KeycloakUser keycloakUser = new KeycloakUser();
			convertKeycloakUser(user, keycloakUser);
			keycloakUsers.add(keycloakUser);
		});

		return keycloakUsers;
	}

	@Override
	public String createUser(String realm, KeycloakUser keycloakUser) throws Exception {
		String userUuid = null;
		Response response = addUser(realm, keycloakUser);
		final int status = response.getStatus();
		if (status == HttpStatus.CREATED.value()) {
			// 新增成功
			userUuid = getCreatedUserId(response);
		} else if (status == HttpStatus.CONFLICT.value()) {
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
	public void updateUser(String realm, KeycloakUser keycloakUser, FederatedIdentityRepresentation irLink) {
		UserRepresentation user = findByUserId(realm, keycloakUser.getId());
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
			// update時綁定FB帳戶
			List<FederatedIdentityRepresentation> irList = new ArrayList<>();
			irList.add(irLink);
			user.setFederatedIdentities(irList);
		}
		
		updateUser(realm, keycloakUser.getId(), user);
	}

	@Override
	public void updatePassword(String realm, String userId, String password) {
		resetPassword(realm, userId, password);
		// 需同時更新user attribute
		UserRepresentation user = findByUserId(realm, userId);
//		CredentialRepresentation newCredential = new CredentialRepresentation();
//		newCredential.setType(CredentialRepresentation.PASSWORD);
//		newCredential.setValue(password);
//		newCredential.setTemporary(false);
//		user.setCredentials(Arrays.asList(newCredential));	
		Map<String, List<String>> attributes = user.getAttributes();
		try {
			attributes.put("idp", Arrays.asList(new String[] { EncryptionUtil.Encrypt(password) }));
		} catch (Exception e) {
			logger.error("EncryptionUtil error." + e.getMessage());
 		}
		user.setAttributes(attributes);
		updateUser(realm, userId, user);
 	}

	
	@Override
	public Map<String, Object> resetPwd(String realm, String userId, String password) {
//		resetPassword(realm, userId, password);
		Map<String, Object> map = kutil.resetPwdByRest(realm, userId, password);
		if(map != null) {
			if(map.get("result").equals("true")) {
				// 需同時更新user attribute
				UserRepresentation user = findByUserId(realm, userId);

				Map<String, List<String>> attributes = user.getAttributes();
				try {
					attributes.put("idp", Arrays.asList(new String[] { EncryptionUtil.Encrypt(password) }));
				} catch (Exception e) {
					logger.error("EncryptionUtil error." + e.getMessage());
				}
				user.setAttributes(attributes);
				updateUser(realm, userId, user);
			} if(map.get("error") != null && map.get("error").equals("invalidPasswordHistoryMessage")) {
				logger.info("invalidPasswordHistoryMessage: error="+map.get("error")+", error_description="+map.get("error_description"));
				//密碼不可與過去重複
				//map.put("error","invalidPasswordHistoryMessage");
				//map.put("error_message","密碼不可與最近3次相同");
				//resultString={"error":"invalidPasswordHistoryMessage","error_description":"Invalid password: must not be equal to any of last 3 passwords."}
			}
		}
		
		return map;
	}

	@Override
	public boolean isTokenExpired(String accessToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UsersVo getUserByFbId(String realm, String fbId) {
		// TODO Auto-generated method stub
		return null;
	}

}
