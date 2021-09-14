package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.PolicyFundTransactionRequest;
import com.twfhclife.generic.api_model.PolicyFundTransactionResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 以保單號碼查詢交易歷史記錄.
 * 
 * @author all
 */
@Service
public class TxLogClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(TxLogClient.class);

	@Value("${eservice_api.es013.url}")
	private String es013Url;
	
	/**
	 * 以保單號碼查詢交易歷史記錄（分頁）.
	 * 
	 * @param apiReq PolicyFundTransactionRequest
	 * @return PolicyFundTransactionResponse
	 */
	public PolicyFundTransactionResponse getPolicyFundTransaction(PolicyFundTransactionRequest apiReq) {
		PolicyFundTransactionResponse policyFundTransactionResponse = null;
		try {
			policyFundTransactionResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es013Url,
					PolicyFundTransactionResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyFundTransaction]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyFundTransactionResponse;
	}

	/**
	 * 以保單號碼查詢交易歷史記錄（分頁）.
	 * 
	 * @param userId 使用者代號
	 * @param policyNo 保單號碼
	 * @param transType 交易種類
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return PolicyFundTransactionResponse
	 */
	public PolicyFundTransactionResponse getPolicyFundTransaction(String userId, String policyNo,
			String transType, String startDate, String endDate, int pageNum, int pageSize) {
		PolicyFundTransactionRequest apiReq = new PolicyFundTransactionRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyNo(policyNo);
		apiReq.setTransType(transType);
		apiReq.setStartDate(startDate);
		apiReq.setEndDate(endDate);
		apiReq.setPageNum(pageNum);
		apiReq.setPageSize(pageSize);

		PolicyFundTransactionResponse policyFundTransactionResponse = null;
		try {
			policyFundTransactionResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es013Url,
					PolicyFundTransactionResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyFundTransaction]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyFundTransactionResponse;
	}
}