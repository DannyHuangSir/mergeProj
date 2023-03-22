package com.twfhclife.generic.api_client;

import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.KeycloakLoginRequest;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
public class SsoClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(SsoClient.class);

	@Value("${eservice_api.sso.login.url}")
	private String LOGIN_URI;
	@Value("${eservice_api.sso.logout.url}")
	private String LOGOUT_URI;
	@Value("${eservice_api.sso.validatetoken.url}")
	private String VALIDATE_TOKEN_URI;

	public KeycloakLoginResponse login(KeycloakLoginRequest requestBody) {
		KeycloakLoginResponse keycloakLoginResponse = null;
		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = new ApiResponseObj<KeycloakLoginResponse>();
		ReturnHeader returnHeader = null;
		String url = LOGIN_URI;//"http://220.133.126.209:8084/eservice_api/sso/login";//for test
//		String url = "http://192.168.1.3:8243/jdzq/1.0/sso/login";
		logger.debug("invoke login post api: url="+url+", requestBody="+MyJacksonUtil.object2Json(requestBody));
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<KeycloakLoginRequest> entity = new HttpEntity<>(requestBody, headers);

		ParameterizedTypeReference<ApiResponseObj<KeycloakLoginResponse>> typeRef = new ParameterizedTypeReference<ApiResponseObj<KeycloakLoginResponse>>() {
		};
		ResponseEntity<ApiResponseObj<KeycloakLoginResponse>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, typeRef);

		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof ApiResponseObj) {
				apiResponseObj = (ApiResponseObj<KeycloakLoginResponse>) obj;
				keycloakLoginResponse = apiResponseObj.getResult();
				returnHeader = apiResponseObj.getReturnHeader();
				if(keycloakLoginResponse == null) {
					keycloakLoginResponse = new KeycloakLoginResponse();
				}
				keycloakLoginResponse.setLoginResultCode(returnHeader.getReturnCode());
				keycloakLoginResponse.setLoginResultMsg(returnHeader.getReturnMesg());
			} else {
				return null;
			}
		}
		logger.info("login result = " + returnHeader.getReturnCode());
		return keycloakLoginResponse;
	}

	public boolean logout(String userId) {
		ApiResponseObj<?> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = null;

		String url = LOGOUT_URI;
		url = url.replace("{realm}", this.ADM_REALM).replace("{userId}", userId.trim());
		// url="http://220.133.126.209:8084/eservice_api/sso/logout/twfhclife/88319e3a-6eb4-4360-ab41-90da80740cac";
		logger.debug("invoke logout get api: url="+url);
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ParameterizedTypeReference<ApiResponseObj<?>> typeRef = new ParameterizedTypeReference<ApiResponseObj<?>>() {
		};
		ResponseEntity<ApiResponseObj<?>> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, typeRef);

		if (!this.checkResponseStatus(responseEntity)) {
			return false;
		}
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		Object obj = responseEntity.getBody();
		if (obj != null) {
			if (obj instanceof ApiResponseObj) {
				apiResponseObj = (ApiResponseObj) obj;
			}
		}

		if (apiResponseObj == null || apiResponseObj.getReturnHeader() == null) {
			return false;
		}
		returnHeader = apiResponseObj.getReturnHeader();
		if ("SUCCESS".equals(returnHeader.getReturnCode())) {
			logger.info("logout result = " + returnHeader.getReturnCode());
			return true;
		} else {
			return false;
		}
	}
	
	public KeycloakLoginResponse validateToken(String accessToken, String refreshToken, String realm, String clientId) {
		KeycloakLoginRequest requestBody = new KeycloakLoginRequest();
		requestBody.setAccessToken(accessToken);
		requestBody.setRefreshToken(refreshToken);
		requestBody.setRealm(realm);
		requestBody.setClientId(clientId);
		
		KeycloakLoginResponse keycloakLoginResponse = null;
		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = new ApiResponseObj<KeycloakLoginResponse>();
		ReturnHeader returnHeader = null;
		String url = VALIDATE_TOKEN_URI;//"http://220.133.126.209:8084/eservice_api/sso/validatetoken";//for test
		logger.debug("invoke validateToken post api: url="+url);
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<KeycloakLoginRequest> entity = new HttpEntity<>(requestBody, headers);

		ParameterizedTypeReference<ApiResponseObj<KeycloakLoginResponse>> typeRef = new ParameterizedTypeReference<ApiResponseObj<KeycloakLoginResponse>>() {
		};
		ResponseEntity<ApiResponseObj<KeycloakLoginResponse>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, typeRef);

		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof ApiResponseObj) {
				apiResponseObj = (ApiResponseObj<KeycloakLoginResponse>) obj;
				keycloakLoginResponse = apiResponseObj.getResult();
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
		logger.info("login result = " + returnHeader.getReturnCode());
		return keycloakLoginResponse;
	}

	public static void main(String[] args) {
		SsoClient sso = new SsoClient();
		KeycloakLoginRequest req = new KeycloakLoginRequest();
		req.setUsername("davidyu");
		req.setRealm("twfhclife");
		req.setPassword("12qwaszx");
		req.setClientId("eservice_adm");
		req.setGrantType("password");
		KeycloakLoginResponse res = (KeycloakLoginResponse) sso.login(req);
		System.out.println("KeycloakLoginResponse token=" + res.getAccessToken());
		// boolean res = sso.logout("");
		// System.out.println("KeycloakLoginResponse result=" + res);
	}
}
