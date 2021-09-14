package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyLoanRequest;
import com.twfhclife.generic.api_model.PolicyLoanResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢保單貸款資料.
 * 
 * @author all
 */
@Service
public class PolicyLoanClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(PolicyLoanClient.class);

	@Value("${eservice_api.es007.url}")
	private String es007Url;
	
	/**
	 * 查詢保單明細資料.
	 * 
	 * @param apiReq PolicyLoanRequest
	 * @return PolicyLoanResponse
	 */
	public PolicyLoanResponse getPolicyloanByPolicyNo(PolicyLoanRequest apiReq) {
		PolicyLoanResponse policyLoanResponse = null;
		try {
			policyLoanResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es007Url,
					PolicyLoanResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyloanByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyLoanResponse;
	}

	/**
	 * 查詢保單明細資料.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyLoanResponse getPolicyloanByPolicyNo(String userId, String policyNo) {
		PolicyLoanRequest apiReq = new PolicyLoanRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PolicyLoanResponse policyLoanResponse = null;
		try {
			policyLoanResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es007Url,
					PolicyLoanResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyloanByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyLoanResponse;
	}
}