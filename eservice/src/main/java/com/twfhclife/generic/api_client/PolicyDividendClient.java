package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyDividendRequest;
import com.twfhclife.generic.api_model.PolicyDividendResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢保單收益分配.
 * 
 * @author all
 */
@Service
public class PolicyDividendClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(PolicyDividendClient.class);

	@Value("${eservice_api.es005.url}")
	private String es005Url;
	
	/**
	 * 查詢保單收益分配.
	 * 
	 * @param apiReq PolicyDividendRequest
	 * @return PolicyDividendResponse
	 */
	public PolicyDividendResponse getPolicyIncomeByPolicyNo(PolicyDividendRequest apiReq) {
		PolicyDividendResponse policyDividendResponse = null;
		try {
			policyDividendResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es005Url,
					PolicyDividendResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyIncomeByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyDividendResponse;
	}

	/**
	 * 查詢保單收益分配.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyDividendResponse getPolicyIncomeByPolicyNo(String userId, String policyNo) {
		PolicyDividendRequest apiReq = new PolicyDividendRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PolicyDividendResponse policyDividendResponse = null;
		try {
			policyDividendResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es005Url,
					PolicyDividendResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyIncomeByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyDividendResponse;
	}
}