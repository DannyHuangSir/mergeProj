package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyPaymentRecordRequest;
import com.twfhclife.generic.api_model.PolicyPaymentRecordResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢繳費紀錄.
 * 
 * @author all
 */
@Service
public class PaymentRecordClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(PaymentRecordClient.class);

	@Value("${eservice_api.es006.url}")
	private String es006Url;
	
	/**
	 * 查詢繳費紀錄.
	 * 
	 * @param apiReq PolicyPaymentRecordRequest
	 * @return PolicyPaymentRecordResponse
	 */
	public PolicyPaymentRecordResponse getPolicyPaymentRecord(PolicyPaymentRecordRequest apiReq) {
		PolicyPaymentRecordResponse policyPaymentRecordResponse = null;
		try {
			policyPaymentRecordResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es006Url,
					PolicyPaymentRecordResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyPaymentRecord]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyPaymentRecordResponse;
	}

	/**
	 * 查詢繳費紀錄.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyPaymentRecordResponse getPolicyPaymentRecord(String userId, String policyNo) {
		PolicyPaymentRecordRequest apiReq = new PolicyPaymentRecordRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PolicyPaymentRecordResponse policyPaymentRecordResponse = null;
		try {
			policyPaymentRecordResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es006Url,
					PolicyPaymentRecordResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyPaymentRecord]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyPaymentRecordResponse;
	}
}