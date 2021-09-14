package com.twfhclife.generic.api_model;

public class KeycloakLoginRequest {

	private String username;
	private String password;
	private String fbId;
	private String moicaId;// cardSN
	private String accessToken;
	private String refreshToken;
	private String moicaCert;// X509
	private String grantType;// password, fb, moica
	private String clientId;// eservice, eservice_adm, eform
	private String realm;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getMoicaId() {
		return moicaId;
	}

	public void setMoicaId(String moicaId) {
		this.moicaId = moicaId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getMoicaCert() {
		return moicaCert;
	}

	public void setMoicaCert(String moicaCert) {
		this.moicaCert = moicaCert;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

}
