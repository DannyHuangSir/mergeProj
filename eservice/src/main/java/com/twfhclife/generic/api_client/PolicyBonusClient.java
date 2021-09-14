package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyBonusRequest;
import com.twfhclife.generic.api_model.PolicyBonusResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢保單紅利資料.
 * 
 * @author all
 */
@Service
public class PolicyBonusClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(PolicyBonusClient.class);

	@Value("${eservice_api.es008.url}")
	private String es008Url;
	
	/**
	 * 查詢保單紅利資料.
	 * 
	 * @param apiReq PolicyBonusRequest
	 * @return PolicyBonusResponse
	 */
	public PolicyBonusResponse getPolicyBonusByPolicyNo(PolicyBonusRequest apiReq) {
		PolicyBonusResponse policyBonusResponse = null;
		try {
			policyBonusResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es008Url,
					PolicyBonusResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyBonusByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyBonusResponse;
	}

	/**
	 * 查詢保單紅利資料.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyBonusResponse getPolicyBonusByPolicyNo(String userId, String policyNo) {
		PolicyBonusRequest apiReq = new PolicyBonusRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PolicyBonusResponse policyBonusResponse = null;
		try {
			policyBonusResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es008Url,
					PolicyBonusResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyBonusByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyBonusResponse;
	}
}