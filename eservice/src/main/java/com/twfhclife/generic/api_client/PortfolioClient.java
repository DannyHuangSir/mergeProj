package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PortfolioResponse;
import com.twfhclife.generic.api_model.PortfolioRequest;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單查詢投資損益及投報率.
 * 
 * @author all
 */
@Service
public class PortfolioClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(PortfolioClient.class);

	@Value("${eservice_api.es011.url}")
	private String es011Url;
	
	/**
	 * 查詢查詢投資損益及投報率.
	 * 
	 * @param apiReq PortfolioRequest
	 * @return PortfolioResponse
	 */
	public PortfolioResponse getPolicyRateOfReturn(PortfolioRequest apiReq) {
		PortfolioResponse policyLoanResponse = null;
		try {
			policyLoanResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es011Url,
					PortfolioResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyRateOfReturn]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyLoanResponse;
	}

	/**
	 * 查詢查詢投資損益及投報率.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @return
	 */
	public PortfolioResponse getPolicyRateOfReturn(String userId, String policyNo) {
		PortfolioRequest apiReq = new PortfolioRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);

		PortfolioResponse policyLoanResponse = null;
		try {
			policyLoanResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es011Url,
					PortfolioResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyRateOfReturn]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyLoanResponse;
	}
}