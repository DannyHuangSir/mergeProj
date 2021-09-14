package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.KeycloakUserAddRequest;
import com.twfhclife.generic.api_model.KeycloakUserResetPwdRequest;
import com.twfhclife.generic.api_model.KeycloakUserResponse;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class KeycloakRegisterClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(KeycloakRegisterClient.class);
	
	@Value("${eservice_api.register.url}")
	private String registerUrl;

	@Value("${eservice_api.register.resetpassword.url}")
	private String resetPasswordUrl;

	
	/**
	 * 註冊
	 * @param apiReq KeycloakRegisterUserRequest
	 * @return KeycloakRegisterUserResponse
	 */
	public ApiResponseObj<KeycloakUserResponse> addUser(KeycloakUserAddRequest apiReq){
		ApiResponseObj<KeycloakUserResponse> apiResponseObj = null;
		try{
			apiResponseObj = this.postApiResponse(MyJacksonUtil.object2Json(apiReq)
					, registerUrl, KeycloakUserResponse.class);
		} catch(Exception e) {
			logger.error("Unable to register by eservice_api[addUser]: {}"
					, ExceptionUtils.getStackTrace(e));
		}
		return apiResponseObj;
	}
	
	public ApiResponseObj<?> resetPassword(KeycloakUserResetPwdRequest apiReq) {
		ApiResponseObj<?> apiResponseObj = null;
		try{
			apiResponseObj = this.getPostApiResponseObj(MyJacksonUtil.object2Json(apiReq), resetPasswordUrl);
		} catch(Exception e) {
			logger.error("Unable to register by eservice_api[resetPassword]: {}"
					, ExceptionUtils.getStackTrace(e));
		}
		return apiResponseObj;
	}
}
