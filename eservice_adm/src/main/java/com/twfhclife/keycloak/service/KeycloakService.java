package com.twfhclife.keycloak.service;

import java.util.List;

import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.keycloak.model.KeycloakUser;

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
	
	String createUser(String relam, KeycloakUser keycloakUser);

	int deleteUser(String relam, String id);

	void updatePassword(KeycloakUser keycloakUser, String password, String realm);

	void updateUser(KeycloakUser keycloakUser);

	KeycloakUser getUser(String realm, String userId);

	KeycloakUser getUserByEmail(String email);

	List<KeycloakUser> getUsers();
	
	boolean isTokenExpired(String accessToken);
	
	void refreshToken(String refreshToken);

}