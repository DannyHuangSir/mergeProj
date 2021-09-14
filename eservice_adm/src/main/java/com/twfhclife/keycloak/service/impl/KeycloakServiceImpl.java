package com.twfhclife.keycloak.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.SsoClient;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.AbstractKeycloakService;
import com.twfhclife.keycloak.service.KeycloakService;

@Component
public class KeycloakServiceImpl extends AbstractKeycloakService implements KeycloakService {
	
	private static final Logger logger = LogManager.getLogger(KeycloakServiceImpl.class);

	@Value("${keycloak.adm-realm}")
	protected String admRealm;

	@Value("${keycloak.adm-clientId}")
	protected String admClientId;
	
	@Autowired
	private SsoClient ssoClient;

	/**
	 * 登入.
	 * 
	 * @param username 使用者帳號
	 * @param password 使用者密碼
	 * @return 回傳KeycloakUser
	 */
	@Override
	@RequestLog
	public KeycloakUser login(String username, String password) {
		KeycloakUser keycloakUser = new KeycloakUser();
		try {
			Keycloak keycloakClient = getKeycloakClient(admRealm, admClientId, username, password);
			String accessToken = getAccessTokenString(keycloakClient);
			String refreshToken = getRefreshTokenString(keycloakClient);
			
			// TODO 檢查accessToken
			if (StringUtils.isEmpty(accessToken)) {
				// TODO
				return null;
			}
			
			UserRepresentation user = findByUsername(admRealm, username);
			keycloakUser.setAccessToken(accessToken);
			keycloakUser.setRefreshToken(refreshToken);
			// TODO 理論上應該只會有一筆，需要檢查做例外處理
			if (user == null) {
				// TODO
				return null;
			}
			
			convertKeycloakUser(user, keycloakUser);
		} catch (Exception e) {
			logger.error("Unable to login: {}", ExceptionUtils.getStackTrace(e));
		}
		
		return keycloakUser;
	}
	
	@Override
	@RequestLog
	public KeycloakLoginResponse validateToken(String accessToken, String refreshToken) {
		KeycloakLoginResponse apiResponse = null;
		try {
			apiResponse = ssoClient.validateToken(accessToken, refreshToken, admRealm, admClientId);//Call API
		} catch (Exception e) {
			logger.error("Unable to validateToken: {}", ExceptionUtils.getStackTrace(e));
		}
		
		return apiResponse;
	}

	@Override
	public void logout(String keyCloakUserId) {
		logger.info("Logout user (id="+keyCloakUserId+")");
		logoutByUserId(admRealm, keyCloakUserId);
	}

	@Override
	public KeycloakUser getUser(String realm, String userId) {
		UserRepresentation user = findByUserId(realm, userId);
		KeycloakUser keycloakUser = new KeycloakUser();
		convertKeycloakUser(user, keycloakUser);
		return keycloakUser;
	}

	@Override
	public KeycloakUser getUserByEmail(String email) {
		UserRepresentation user = findUserByEmail(admRealm, email);
		if (user == null) {
			return null;
		}
		return getUser(admRealm, user.getId());
	}

	@Override
	public List<KeycloakUser> getUsers() {
		List<KeycloakUser>  keycloakUsers = new ArrayList<>();
		List<UserRepresentation> users = getRealmUsers(admRealm);
		users.forEach(user -> {
			KeycloakUser keycloakUser = new KeycloakUser();
			convertKeycloakUser(user, keycloakUser);
			keycloakUsers.add(keycloakUser);
		});
		
		return keycloakUsers;
	}

	@Override
	public String createUser(String relam, KeycloakUser keycloakUser) {
		// TODO 檢查帳號、電話、信箱唯一性
		Response response = addUser(relam, keycloakUser);
		final int status = response.getStatus();

		if (status != HttpStatus.CREATED.value()) {
			// TODO error handle
		}

		return getCreatedUserId(response);
	}

	@Override
	public void updateUser(KeycloakUser keycloakUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePassword(KeycloakUser keycloakUser, String password, String realm) {
		if (StringUtils.isEmpty(realm)) {
			realm = admRealm;
		}
		resetPassword(realm, keycloakUser.getId(), password);
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

	@Override
	public int deleteUser(String relam, String id) {
		Response response = removeUser(relam, id);
		final int status = response.getStatus();
		logger.info("deleteUser status: {}", status);
		if (status == 204) {
			return 1;
		}
		return 0;
	}
}
