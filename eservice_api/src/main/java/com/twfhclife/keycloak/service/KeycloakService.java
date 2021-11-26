package com.twfhclife.keycloak.service;

import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.FederatedIdentityRepresentation;

import com.twfhclife.generic.domain.KeycloakLoginResponse;
import com.twfhclife.generic.domain.KeycloakLoginResponseByEIN;
import com.twfhclife.generic.model.KeycloakUser;
import com.twfhclife.generic.model.KeycloakUserSession;
import com.twfhclife.generic.model.UsersVo;

public interface KeycloakService {
	
	/**
	 * 登入.
	 * 
	 * @param username 使用者帳號
	 * @param password 使用者密碼
	 * @return 回傳KeycloakLoginResponse
	 */
	KeycloakLoginResponse login(String username, String password, String realm, String clientId);

	/**20211013 by 203999 */
	KeycloakLoginResponseByEIN loginByEIN(String username, String password, String realm, String clientId);
	
	KeycloakLoginResponse loginByFb(String fbId, String fbToken, String realm, String clientId);
	
	KeycloakLoginResponse loginByMoica(String moicaId, String ca, String realm, String clientId);
	
	void logout(String realm, String keyCloakUserId);

	String createUser(String realm, KeycloakUser keycloakUser) throws Exception;

	void updatePassword(String realm, String userId, String password);
	public Map<String, Object> resetPwd(String realm, String userId, String password);
	
	void updateUser(String realm, KeycloakUser keycloakUser, FederatedIdentityRepresentation irLink);

	KeycloakUser getUser(String realm, String userId);

	KeycloakUser getUserByEmail(String realm, String email);
	
	KeycloakUser getUserByUsername(String realm, String username);
	
	String getUserIdByUsername(String realm, String username);

	List<KeycloakUser> getUsers(String realm);
	
	boolean isTokenExpired(String accessToken);
	
	UsersVo getUserByFbId(String realm, String fbId);
	
	KeycloakLoginResponse validateToken(String accessToken, String refreshToken, String realm, String clientId);
	
	KeycloakLoginResponse refreshToken(String refreshToken, String realm, String clientId);
	
	List<KeycloakUserSession> getUserSessions(String realm, String userId, String clientId);
	
	boolean deleteUserSession(String realm, List<String> sessionId);

	KeycloakLoginResponse loginEserviceByFbToken(String fbId, String fbToken);
}