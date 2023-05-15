package com.twfhclife.jd.keycloak.model;

import java.io.Serializable;
import java.util.List;

public class KeycloakUserSessionRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String realm;
	private String userId;
	private String clientId;
	private List<String> sessionIds;

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<String> getSessionIds() {
		return sessionIds;
	}

	public void setSessionIds(List<String> sessionIds) {
		this.sessionIds = sessionIds;
	}

}
