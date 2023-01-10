package com.twfhclife.eservice.keycloak.service;

import com.twfhclife.eservice.keycloak.model.KeycloakLoginResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;

import java.util.List;

public interface KeycloakService {
	
	/**
	 * 登入.
	 * 
	 * @param username 使用者帳號
	 * @param password 使用者密碼
	 * @return 回傳KeycloakUser
	 */
	KeycloakUser login(String username, String password);
	
	void logout(String keyCloakUserId);

	public KeycloakLoginResponse validateToken(String accessToken, String refreshToken);

	String createUser(KeycloakUser keycloakUser) throws Exception;

	void updatePassword(KeycloakUser keycloakUser, String password);

	void updateUser(KeycloakUser keycloakUser, FederatedIdentityRepresentation irLink);

	KeycloakUser getUser(String userId);

	KeycloakUser getUserByEmail(String email);
	
	KeycloakUser getUserByUsername(String username);

	List<KeycloakUser> getUsers();
	
	boolean isTokenExpired(String accessToken);
	
	void refreshToken(String refreshToken);

	KeycloakLoginResponse loginEserviceByFbToken(String fbId, String fbToken);

	KeycloakLoginResponse loginByMoica(String moicaId, String ca, String realm, String clientId);

}