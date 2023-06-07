package com.twfhclife.generic.api_model;

import java.util.List;

import com.twfhclife.eservice.web.model.UsersVo;

public class Result {
	private String userId;
	private String status;
	private String accessToken;
	private String expiresIn;
	private String refreshToken;
	private String refreshExpiresIn;
	private String tokenType;
	private String sessionState;
	private List<UsersVo> deputyOf;
	
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
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getRefreshExpiresIn() {
		return refreshExpiresIn;
	}
	public void setRefreshExpiresIn(String refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getSessionState() {
		return sessionState;
	}
	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}
	public List<UsersVo> getDeputyOf() {
		return deputyOf;
	}
	public void setDeputyOf(List<UsersVo> deputyOf) {
		this.deputyOf = deputyOf;
	}
}
