package com.twfhclife.generic.api_client;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.TransHistoryDetailRequest;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 查詢線上申請明細紀錄.
 * 
 * @author all
 */
@Service
public class TransHistoryDetailClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(TransHistoryDetailClient.class);

	@Value("${eservice_api.es017.url}")
	private String es017Url;
	
	/**
	 * 查詢線上申請明細紀錄.
	 * 
	 * @param apiReq TransHistoryDetailRequest
	 * @return TransHistoryDetailResponses
	 */
	public TransHistoryDetailResponse getTransHistoryDetail(TransHistoryDetailRequest apiReq) {
		TransHistoryDetailResponse transHistoryDetailResponse = null;
		try {
			transHistoryDetailResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es017Url,
					TransHistoryDetailResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransHistoryDetail]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transHistoryDetailResponse;
	}

	/**
	 * 查詢線上申請明細紀錄.
	 * 
	 * @param userId 使用者代號
	 * @param transNums 交易序號
	 * @return TransHistoryDetailResponse
	 */
	public TransHistoryDetailResponse getTransHistoryDetail(String userId, List<String> transNums) {
		TransHistoryDetailRequest apiReq = new TransHistoryDetailRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setTransNums(transNums);
		
		TransHistoryDetailResponse transHistoryDetailResponse = null;
		try {
			transHistoryDetailResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es017Url,
					TransHistoryDetailResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransHistoryDetail]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transHistoryDetailResponse;
	}
}