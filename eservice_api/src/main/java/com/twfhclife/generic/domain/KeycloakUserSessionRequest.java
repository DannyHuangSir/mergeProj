package com.twfhclife.generic.domain;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class KeycloakUserSessionRequest {

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
