package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.TransHistoryListRequest;
import com.twfhclife.generic.api_model.TransHistoryListResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 查詢線上申請紀錄.
 * 
 * @author all
 */
@Service
public class TransHistoryListClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(TransHistoryListClient.class);

	@Value("${eservice_api.es016.url}")
	private String es016Url;
	
	/**
	 * 查詢線上申請紀錄.
	 * 
	 * @param apiReq TransHistoryListRequest
	 * @return TransHistoryListResponses
	 */
	public TransHistoryListResponse getTransHistoryList(TransHistoryListRequest apiReq) {
		TransHistoryListResponse transHistoryListResponse = null;
		try {
			transHistoryListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es016Url,
					TransHistoryListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransHistoryList]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transHistoryListResponse;
	}

	/**
	 * 查詢線上申請紀錄.
	 * 
	 * @param userId 使用者代號
	 * @param transStatus 交易狀態
	 * @param transType 交易種類
	 * @param policyNo 保單號碼
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return TransHistoryListResponse
	 */
	public TransHistoryListResponse getTransHistoryList(String userId, String transStatus, String transType,
			String policyNo, String startDate, String endDate, int pageNum, int pageSize) {
		TransHistoryListRequest apiReq = new TransHistoryListRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setTransStatus(transStatus);
		apiReq.setTransType(transType);
		apiReq.setPolicyNo(policyNo);
		apiReq.setStartDate(startDate);
		apiReq.setEndDate(endDate);
		apiReq.setPageNum(pageNum);
		apiReq.setPageSize(pageSize);

		TransHistoryListResponse transHistoryListResponse = null;
		try {
			transHistoryListResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es016Url,
					TransHistoryListResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransHistoryList]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transHistoryListResponse;
	}

}