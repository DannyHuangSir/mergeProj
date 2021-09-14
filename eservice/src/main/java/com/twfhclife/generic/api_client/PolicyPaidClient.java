package com.twfhclife.generic.api_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyPaidRequest;
import com.twfhclife.generic.api_model.PolicyPaidResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢保單紅利資料.
 * 
 * @author all
 */
@Service
public class PolicyPaidClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(PolicyPaidClient.class);

	@Value("${eservice_api.es019.url}")
	private String es019Url;
	
	/**
	 * 查詢保單紅利資料.
	 * 
	 * @param apiReq PolicyBonusRequest
	 * @return PolicyBonusResponse
	 */
	public PolicyPaidResponse getPolicyPaidByPolicyNo(PolicyPaidRequest apiReq) {
		PolicyPaidResponse policyPaiodResponse = null;
		try {
			policyPaiodResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es019Url,
					PolicyPaidResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyPaidByPolicyNo]:",e);
		}
		return policyPaiodResponse;
	}

	/**
	 * 查詢保單紅利資料.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyPaidResponse getPolicyPaidByPolicyNo(String userId, String policyNo) {
		PolicyPaidRequest apiReq = new PolicyPaidRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PolicyPaidResponse policyPaidResponse = null;
		try {
			policyPaidResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es019Url,
					PolicyPaidResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyPaidByPolicyNo]: ", e);
		}
		return policyPaidResponse;
	}
}