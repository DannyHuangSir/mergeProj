package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyListRequest;
import com.twfhclife.generic.api_model.PolicyListResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 查詢保單清單.
 * 
 * @author all
 */
@Service
public class UserPolicyListClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(UserPolicyListClient.class);

	@Value("${eservice_api.es003.url}")
	private String es003Url;
	
	/**
	 * 查詢保單清單料.
	 * 
	 * @param apiReq PolicyListRequest
	 * @return PolicyListResponse
	 */
	public PolicyListResponse getPolicyListByUser(PolicyListRequest apiReq) {
		PolicyListResponse policyListResponse = null;
		try {
			policyListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es003Url,
					PolicyListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyListByUser]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyListResponse;
	}
	
	/**
	 * 查詢保單清單料.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	public PolicyListResponse getPolicyListByUser(String userId, String rocId) {
		PolicyListRequest apiReq = new PolicyListRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);

		PolicyListResponse policyListResponse = null;
		try {
			policyListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es003Url,
					PolicyListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyListByUser]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyListResponse;
	}
}