package com.twfhclife.keycloak.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.common.util.Time;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.generic.dao.KeycloakUserDao;
import com.twfhclife.generic.model.KeycloakUser;

public abstract class AbstractKeycloakService {
	private static final Logger logger = LogManager.getLogger(AbstractKeycloakService.class);
	
	@Value("${keycloak.auth-server-url}")
	protected String KEYCLOAK_URL;

	@Value("${keycloak.admin-realm}")
	protected String ADMIN_REALM;

	@Value("${keycloak.admin-user}")
	protected String ADMIN_USER;

	@Value("${keycloak.admin-pwd}")
	protected String ADMIN_PWD;

	@Value("${keycloak.admin-security-client}")
	protected String ADMIN_SECURITY_CLIENT;

	@Value("${keycloak.minTokenValidity}")
	protected String DEFAULT_MIN_VALIDITY;
	
	@Autowired
	private KeycloakUserDao keycloakUserDao;

	private KeycloakBuilder clientWithPasswordCredentials(String realm, String clientId, String username,
			String password) {
		return KeycloakBuilder.builder()
				.serverUrl(KEYCLOAK_URL)
				.realm(realm)
				.clientId(clientId)
				.username(username)
				.password(password)
				.grantType(OAuth2Constants.PASSWORD);
	}

	private KeycloakBuilder adminWithPasswordCredentials() {
		logger.debug("Keycloak adminWithPasswordCredentials KEYCLOAK_URL="+KEYCLOAK_URL+", ADMIN_REALM="+ADMIN_REALM+",ADMIN_SECURITY_CLIENT="+ADMIN_SECURITY_CLIENT);
		return KeycloakBuilder.builder()
				.serverUrl(KEYCLOAK_URL)
				.realm(ADMIN_REALM)
				.clientId(ADMIN_SECURITY_CLIENT)
				.username(ADMIN_USER)
				.password(ADMIN_PWD);
	}

	protected Keycloak getKeycloakClient(String realm, String clientId, String username, String password) {
		return clientWithPasswordCredentials(realm, clientId, username, password).build();
	}

	private Keycloak getKeycloakAdmin() {
		return adminWithPasswordCredentials().build();
	}
	
	protected boolean logoutByUserId(String realm, String userId) {
		UserResource userResource = getKeycloakAdmin().realm(realm).users().get(userId);
		if (userResource == null) {
			return false;
		}
		
		userResource.logout();
		return true;
	}

	protected UserRepresentation findByUsername(String relam, String username) {
		UserRepresentation user = keycloakUserDao.findByUserName(relam, username);
		Map<String, List<String>> attributes = new HashMap<>();
		if (user != null) {
			List<Map<String, String>> attrs = keycloakUserDao.findByUserAttributes(user.getId());
			
			if (attrs != null) {
				Map<String, String> map = new HashMap<String, String>();
				attrs.forEach(userAttr -> {
					userAttr.forEach((k, v) -> {
						map.put(k, v);
					});
					attributes.put(map.get("NAME"), Arrays.asList(new String[] { map.get("VALUE") }));
				});
				user.setAttributes(attributes);
			}
		}
		/*
		List<UserRepresentation> users = getKeycloakAdmin().realm(relam).users().search(username, null, null, null, 0, 1);
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}
		*/
		return user;
	}
	
	protected String findIdByUsername(String relam, String username) {
		String userId = null;
		UserRepresentation user = keycloakUserDao.findByUserName(relam, username);
		if (user != null) {
			userId = user.getId();
		}

		return userId;
	}

	protected UserRepresentation findUserByEmail(String realm, String email) {
		logger.debug("Keycloak findUserByEmail realm="+realm+", email="+email);
		List<UserRepresentation> users = getKeycloakAdmin().realm(realm).users().search(null, null, null, email, 0, 1);
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}
		return users.get(0);
	}

	protected UserRepresentation findByUserId(String relam, String userId) {
		return getKeycloakAdmin().realm(relam).users().get(userId).toRepresentation();
	}

	protected List<UserRepresentation> getRealmUsers(String relam) {
		return getKeycloakAdmin().realm(relam).users().list();
	}

	protected Response addUser(String relam, KeycloakUser keycloakUser) throws Exception {
		UserRepresentation user = new UserRepresentation();
		BeanUtils.copyProperties(keycloakUser, user, "attributes");
		user.setEnabled(true);
		user.setEmailVerified(true);
		user.setCredentials(Arrays.asList(getCredentials(keycloakUser)));

		// 設定Attribute
		Map<String, List<String>> attributes = new HashMap<>();
		if (!StringUtils.isEmpty(keycloakUser.getMobile())) {
			attributes.put("mobile", Arrays.asList(new String[] { keycloakUser.getMobile() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getRocId())) {
			attributes.put("rocId", Arrays.asList(new String[] { keycloakUser.getRocId() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getPassword())) {
			attributes.put("idp", Arrays.asList(new String[] { EncryptionUtil.Encrypt(keycloakUser.getPassword()) }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getFbId())) {
			attributes.put("fb_id", Arrays.asList(new String[] { keycloakUser.getFbId() }));
		}
		if (!StringUtils.isEmpty(keycloakUser.getCardSn())) {
			attributes.put("card_sn", Arrays.asList(new String[] { keycloakUser.getCardSn() }));
		}
		//
		if (!StringUtils.isEmpty(keycloakUser.getEmail())) {
			attributes.put("email", Arrays.asList(new String[] { keycloakUser.getEmail() }));
		}

		// reset email：因信箱在keycloak會唯一，故更改放在attributes裡
		user.setEmail(null);
		user.setEmailVerified(true);
		user.setAttributes(attributes);

		if (!StringUtils.isEmpty(keycloakUser.getFbId())) {
			// 註冊時綁定FB帳戶
			List<FederatedIdentityRepresentation> irList = user.getFederatedIdentities();
			if(irList == null) {
				irList = new ArrayList<>();
			}
			FederatedIdentityRepresentation irLink = new FederatedIdentityRepresentation();
			irLink.setIdentityProvider("facebook");
			irLink.setUserId(keycloakUser.getFbId());
			irLink.setUserName(keycloakUser.getUsername());
			irList.add(irLink);
			user.setFederatedIdentities(irList);
		}
		
		return getKeycloakAdmin().realm(relam).users().create(user);
	}

	protected void resetPassword(String relam, String id, String password) {
		CredentialRepresentation newCredential = new CredentialRepresentation();
		newCredential.setType(CredentialRepresentation.PASSWORD);
		newCredential.setValue(password);
		newCredential.setTemporary(false);
		getKeycloakAdmin().realm(relam).users().get(id).resetPassword(newCredential);
	}
	
	protected void updateUser(String relam, String id, UserRepresentation ur) {
		  getKeycloakAdmin().realm(relam).users().get(id).update(ur);
	}
	
	/**
	 * 搜尋使用者帳號
	 * @param queryStr
	 */
	public UserRepresentation searchUser(String relam, String queryStr) {
		// 1.先用admin帳戶取得權限
		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);
		List<UserRepresentation> users = kc.realm(relam).users().search(queryStr, 0, 10);
		if(users != null && users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	protected String getAccessTokenString(Keycloak keycloak) {
		AccessTokenResponse tokenResponse = getAccessTokenResponse(keycloak);
		return tokenResponse == null ? null : tokenResponse.getToken();
	}
	
	protected String getRefreshTokenString(Keycloak keycloak) {
		AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
		return tokenResponse == null ? null : tokenResponse.getRefreshToken();
	}

	private AccessTokenResponse getAccessTokenResponse(Keycloak keycloak) {
		try {
			return keycloak.tokenManager().getAccessToken();
		} catch (Exception ex) {
			return null;
		}
	}

	private synchronized boolean tokenExpired(long minTokenValidity, long expirationTime) {
		return (Time.currentTime() + minTokenValidity) >= expirationTime;
	}

	protected CredentialRepresentation getCredentials(KeycloakUser keycloakUser) {
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(keycloakUser.getPassword());
		credential.setTemporary(keycloakUser.isTemporary());
		return credential;
	}

	protected String getCreatedUserId(Response response) {
		String locationHeader = response.getHeaderString("Location");
		String userId = locationHeader.replaceAll(".*/(.*)$", "$1");
		return userId;
	}
	
	protected void convertKeycloakUser(UserRepresentation user, KeycloakUser keycloakUser) {
		BeanUtils.copyProperties(user, keycloakUser);
		if (user.getAttributes() != null) {
			user.getAttributes().forEach((k, v) -> {
				if ("mobile".equals(k)) {
					keycloakUser.setMobile(v.get(0));
				}
				if ("rocId".equals(k)) {
					keycloakUser.setRocId(v.get(0));
				}
				if ("fb_id".equals(k)) {
					keycloakUser.setFbId(v.get(0));
				}
				if ("card_sn".equals(k)) {
					keycloakUser.setCardSn(v.get(0));
				}
			});
		}
	}
}
