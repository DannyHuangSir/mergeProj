package com.twfhclife.jd.api_client;

import com.twfhclife.jd.api_model.ApiResponseObj;
import com.twfhclife.jd.keycloak.model.KeycloakLoginRequest;
import com.twfhclife.jd.keycloak.model.KeycloakLoginResponse;
import com.twfhclife.jd.keycloak.model.KeycloakUserSession;
import com.twfhclife.jd.keycloak.model.KeycloakUserSessionRequest;
import com.twfhclife.jd.util.MyJacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SsoClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(SsoClient.class);

	@Value("${eservice_api.sso.login.url}")
	private String ssoLoginUrl;

	@Value("${eservice_api.sso.logout.url}")
	private String ssoLogoutUrl;

	@Value("${eservice_api.sso.validatetoken.url}")
	private String ssoValidatetokenUrl;

	@Value("${eservice_api.sso.session.get.url}")
	private String ssoGetSessionUrl;

	@Value("${eservice_api.sso.session.remove.url}")
	private String ssoRemoveSessionUrl;
	
	/**
	 * 登入.
	 * 
	 * @param apiReq KeycloakLoginRequest
	 * @return KeycloakLoginResponse
	 */
	public ApiResponseObj<KeycloakLoginResponse> login(KeycloakLoginRequest apiReq) {
		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = null;
		try {
			String json = MyJacksonUtil.object2Json(apiReq);
			//logger.debug("# login json string = " + json);
			apiResponseObj = this.postApiResponse(json, ssoLoginUrl, KeycloakLoginResponse.class);
		} catch (Exception e) {
			logger.error("Unable to login from eservice_api: {}", ExceptionUtils.getStackTrace(e));
		}
		return apiResponseObj;
	}
	
//	public ApiResponseObj<KeycloakLoginResponse> exchangeToken(KeycloakLoginRequest apiReq) {
//		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = null;
//		try {
//			apiResponseObj = this.postApiResponse(MyJacksonUtil.object2Json(apiReq), ssoLoginUrl, KeycloakLoginResponse.class);
//		} catch (Exception e) {
//			logger.error("Unable to login from eservice_api: {}", ExceptionUtils.getStackTrace(e));
//		}
//		return apiResponseObj;
//	}
	
	public KeycloakLoginResponse validateToken(KeycloakLoginRequest apiReq) {
		String json = MyJacksonUtil.object2Json(apiReq);
		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = this.postApiResponse(json, 
				ssoValidatetokenUrl, KeycloakLoginResponse.class);
		return (apiResponseObj == null ? null : apiResponseObj.getResult());
	}

	public boolean logout(String realm, String userId) {
		String logoutUrl = String.format("%s/%s", ssoLogoutUrl, StringUtils.trimToEmpty(realm));
		logoutUrl = String.format("%s/%s", logoutUrl, StringUtils.trimToEmpty(userId));
		
//		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = this.getApiResponse(logoutUrl,
//				KeycloakLoginResponse.class);
//		if (apiResponseObj == null) {
//			return false;
//		}
//
//		ReturnHeader returnHeader = apiResponseObj.getReturnHeader();
//		if (returnHeader == null) {
//			return false;
//		}
//		
//		logger.info("UserId[{}] logout returnCode = {}", logoutUrl, returnHeader.getReturnCode());
//		return ("SUCCESS".equals(returnHeader.getReturnCode()));
		
		String returnCode = this.getGetApiReturnCode(logoutUrl);		
		logger.info("UserId[{}] logout returnCode = {}", userId, returnCode);
		return ("SUCCESS".equals(returnCode));
	}

	@SuppressWarnings("unchecked")
	public List<KeycloakUserSession> getUserSession(KeycloakUserSessionRequest apiReq) {
		List<KeycloakUserSession> userSessionList = this.postApi(MyJacksonUtil.object2Json(apiReq), ssoGetSessionUrl,
				new ArrayList<KeycloakUserSession>().getClass());
		return userSessionList;
	}

	public String deleteUserSession(KeycloakUserSessionRequest apiReq) {
		String returnCode = this.getPostApiReturnCode(MyJacksonUtil.object2Json(apiReq), ssoRemoveSessionUrl);
		return returnCode;
	}
}