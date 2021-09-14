package com.twfhclife.keycloak.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.keycloak.representations.idm.UserRepresentation;

public class KeycloakUser extends UserRepresentation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String accessToken;
	private String refreshToken;
	private String password;
	private String mobile;
	private String rocId;
	private String fbId;
	private String cardSn;
	private String sessionState;
	private boolean temporary = false;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getCardSn() {
		return cardSn;
	}

	public void setCardSn(String cardSn) {
		this.cardSn = cardSn;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
