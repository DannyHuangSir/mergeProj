package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyPremiumTransactionRequest;
import com.twfhclife.generic.api_model.PolicyPremiumTransactionResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢保單保費費用.
 * 
 * @author all
 */
@Service
public class TxPremiumClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(TxPremiumClient.class);

	@Value("${eservice_api.es012.url}")
	private String es012Url;
	
	/**
	 * 以保單號碼查詢保單保費費用.
	 * 
	 * @param apiReq PolicyPremiumTransactionRequest
	 * @return PolicyPremiumTransactionResponse
	 */
	public PolicyPremiumTransactionResponse getPolicyPremiumTransaction(PolicyPremiumTransactionRequest apiReq) {
		PolicyPremiumTransactionResponse policyPremiumTransactionResponse = null;
		try {
			policyPremiumTransactionResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es012Url,
					PolicyPremiumTransactionResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyPremiumTransaction]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyPremiumTransactionResponse;
	}

	/**
	 * 以保單號碼查詢保單保費費用.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return PolicyPremiumTransactionResponse
	 */
	public PolicyPremiumTransactionResponse getPolicyPremiumTransaction(String userId, String policyNo,
			String startDate, String endDate, int pageNum, int pageSize) {
		PolicyPremiumTransactionRequest apiReq = new PolicyPremiumTransactionRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);
		apiReq.setStartDate(startDate);
		apiReq.setEndDate(endDate);
		apiReq.setPageNum(pageNum);
		apiReq.setPageSize(pageSize);

		PolicyPremiumTransactionResponse policyPremiumTransactionResponse = null;
		try {
			policyPremiumTransactionResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es012Url,
					PolicyPremiumTransactionResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyPremiumTransaction]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyPremiumTransactionResponse;
	}
}