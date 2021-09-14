package com.twfhclife.generic.domain;

import java.util.List;

import com.twfhclife.generic.model.UserVo;

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
	private List<UserVo> deputyOf;//身為誰的代理人

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

	public List<UserVo> getDeputyOf() {
		return deputyOf;
	}

	public void setDeputyOf(List<UserVo> deputyOf) {
		this.deputyOf = deputyOf;
	}

}
