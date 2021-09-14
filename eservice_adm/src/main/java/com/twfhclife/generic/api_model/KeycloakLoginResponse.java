package com.twfhclife.generic.api_model;

public class KeycloakLoginResponse {

	private String userId;
	private String status;
	private String accessToken;
	private Long expiresIn;
	private String refreshToken;
	private Long refreshExpiresIn;
	private String tokenType;
	private String notBeforePolicy;
	private String sessionState;
	private String loginResultCode;
	private String loginResultMsg;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getRefreshExpiresIn() {
		return refreshExpiresIn;
	}

	public void setRefreshExpiresIn(Long refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getNotBeforePolicy() {
		return notBeforePolicy;
	}

	public void setNotBeforePolicy(String notBeforePolicy) {
		this.notBeforePolicy = notBeforePolicy;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	public String getLoginResultCode() {
		return loginResultCode;
	}

	public void setLoginResultCode(String loginResultCode) {
		this.loginResultCode = loginResultCode;
	}

	public String getLoginResultMsg() {
		return loginResultMsg;
	}

	public void setLoginResultMsg(String loginResultMsg) {
		this.loginResultMsg = loginResultMsg;
	}

	
}
