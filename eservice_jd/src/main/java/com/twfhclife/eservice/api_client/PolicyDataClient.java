package com.twfhclife.eservice.api_client;

import com.twfhclife.eservice.api_model.PolicyDataRequest;
import com.twfhclife.eservice.api_model.PolicyDataResponse;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.MyJacksonUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 查詢保單明細資料.
 * 
 * @author all
 */
@Service
public class PolicyDataClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(PolicyDataClient.class);

	@Value("${eservice_api.es004.url}")
	private String es004Url;
	
	/**
	 * 查詢保單明細資料.
	 * 
	 * @param apiReq PolicyDataRequest
	 * @return PolicyDataResponse
	 */
	public PolicyDataResponse getPolicyDetail(PolicyDataRequest apiReq) {
		PolicyDataResponse policyDataResponse = null;
		try {
			policyDataResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es004Url,
					PolicyDataResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyDetail]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyDataResponse;
	}

	/**
	 * 查詢保單明細資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PolicyDataResponse getPolicyDetail(String policyNo) {
		PolicyDataRequest apiReq = new PolicyDataRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setPolicyNo(policyNo);
		apiReq.setUserId(ApConstants.SYSTEM_ID_JD);

		PolicyDataResponse policyDataResponse = null;
		try {
			policyDataResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es004Url,
					PolicyDataResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyDetail]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyDataResponse;
	}
}