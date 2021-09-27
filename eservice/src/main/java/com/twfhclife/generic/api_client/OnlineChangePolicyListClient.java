package com.twfhclife.generic.api_client;

import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.OnlineChangePolicyListRequest;
import com.twfhclife.generic.api_model.OnlineChangePolicyListResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保戶證號查詢保單清單.
 * 
 * @author all
 */
@Service
public class OnlineChangePolicyListClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(OnlineChangePolicyListClient.class);

	@Value("${eservice_api.es015.url}")
	private String es015Url;
	
	/**
	 * 以保戶證號查詢保單清單.
	 * 
	 * @param apiReq OnlineChangePolicyListRequest
	 * @return OnlineChangePolicyListResponse
	 */
	public OnlineChangePolicyListResponse getOnlineChangePolicyList(OnlineChangePolicyListRequest apiReq) {
		OnlineChangePolicyListResponse onlineChangePolicyListResponse = null;
		try {
			onlineChangePolicyListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es015Url,
					OnlineChangePolicyListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getOnlineChangePolicyList]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return onlineChangePolicyListResponse;
	}
	
	/**
	 * 以保戶證號查詢保單清單.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	public OnlineChangePolicyListResponse getOnlineChangePolicyList(String userId, String rocId) {
		OnlineChangePolicyListRequest apiReq = new OnlineChangePolicyListRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);

		OnlineChangePolicyListResponse onlineChangePolicyListResponse = null;
		try {
			onlineChangePolicyListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es015Url,
					OnlineChangePolicyListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getOnlineChangePolicyList]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return onlineChangePolicyListResponse;
	}
	
	/**
	 * 以保戶證號查詢保單清單(保單理賠).
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	public OnlineChangePolicyListResponse getUserOnlineChangePolicyListByRocId(String userId, String rocId) {
		OnlineChangePolicyListRequest apiReq = new OnlineChangePolicyListRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);
		apiReq.setTypeName(ApConstants.INSURANCE_CLAIM);
		OnlineChangePolicyListResponse onlineChangePolicyListResponse = null;
		try {
			onlineChangePolicyListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es015Url,
					OnlineChangePolicyListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getUserOnlineChangePolicyListByRocId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return onlineChangePolicyListResponse;
	}



	public OnlineChangePolicyListResponse getUserOnlineChangePolicyToMedicalListByRocId(String userId, String rocId) {

		OnlineChangePolicyListRequest apiReq = new OnlineChangePolicyListRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);
		apiReq.setTypeName(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
		OnlineChangePolicyListResponse onlineChangePolicyListResponse = null;
		try {
			onlineChangePolicyListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es015Url,
					OnlineChangePolicyListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getUserOnlineChangePolicyListByRocId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return onlineChangePolicyListResponse;
	}
}