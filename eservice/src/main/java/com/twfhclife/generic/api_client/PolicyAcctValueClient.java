package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyAcctValueRequest;
import com.twfhclife.generic.api_model.PolicyAcctValueResponse;
import com.twfhclife.generic.api_model.PolicyPaymentRecordResponse;
import com.twfhclife.generic.api_model.UserPolicyAcctValueRequest;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢保單帳戶價值.
 * 
 * @author all
 */
@Service
public class PolicyAcctValueClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(PolicyAcctValueClient.class);

	@Value("${eservice_api.es009.url}")
	private String es009Url;

	@Value("${eservice_api.es010.url}")
	private String es010Url;
	
	/**
	 * 以保單號碼查詢保單帳戶價值.
	 * 
	 * @param apiReq PolicyAcctValueRequest
	 * @return PolicyAcctValueResponse
	 */
	public PolicyAcctValueResponse getPolicyValueByPolicyNo(PolicyAcctValueRequest apiReq) {
		PolicyAcctValueResponse policyAcctValueResponse = null;
		try {
			policyAcctValueResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es009Url,
					PolicyAcctValueResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyValueByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyAcctValueResponse;
	}

	/**
	 * 以保單號碼查詢保單帳戶價值.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyAcctValueResponse getPolicyValueByPolicyNo(String userId, String policyNo) {
		PolicyAcctValueRequest apiReq = new PolicyAcctValueRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PolicyAcctValueResponse policyAcctValueResponse = null;
		try {
			policyAcctValueResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es009Url,
					PolicyAcctValueResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyValueByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyAcctValueResponse;
	}
	
	/**
	 * 以要保人身分證字號為條件查詢保單帳戶價值.
	 * 
	 * @param apiReq UserPolicyAcctValueRequest
	 * @return UserPolicyAcctValueResponse
	 */
	public PolicyPaymentRecordResponse getPolicyValueByRocId(UserPolicyAcctValueRequest apiReq) {
		PolicyPaymentRecordResponse userPolicyAcctValueResponse = null;
		try {
			userPolicyAcctValueResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es010Url,
					PolicyPaymentRecordResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyValueByRocId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return userPolicyAcctValueResponse;
	}

	/**
	 * 以要保人身分證字號為條件查詢保單帳戶價值.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	public PolicyPaymentRecordResponse getPolicyValueByRocId(String userId, String rocId) {
		UserPolicyAcctValueRequest apiReq = new UserPolicyAcctValueRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);

		PolicyPaymentRecordResponse userPolicyAcctValueResponse = null;
		try {
			userPolicyAcctValueResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es010Url,
					PolicyPaymentRecordResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyValueByRocId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return userPolicyAcctValueResponse;
	}
}