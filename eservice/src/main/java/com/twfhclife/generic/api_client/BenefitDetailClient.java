package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.BenefitDetailRequest;
import com.twfhclife.generic.api_model.BenefitDetailResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 查詢客戶的保單彙整.
 * 
 * @author all
 */
@Service
public class BenefitDetailClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(BenefitDetailClient.class);

	@Value("${eservice_api.es002.url}")
	private String es002Url;
	
	/**
	 * 查詢客戶的保障彙整.
	 * 
	 * @param apiReq BenefitDetailRequest
	 * @return BenefitDetailResponse
	 */
	public BenefitDetailResponse getPolicyBenefit(BenefitDetailRequest apiReq) {
		BenefitDetailResponse benefitDetailResponse = null;
		try {
			benefitDetailResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es002Url,
					BenefitDetailResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyBenefit]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return benefitDetailResponse;
	}

	/**
	 * 查詢客戶的保障彙整.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	public BenefitDetailResponse getPolicyBenefit(String userId, String rocId) {
		BenefitDetailRequest apiReq = new BenefitDetailRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);

		BenefitDetailResponse benefitDetailResponse = null;
		try {
			benefitDetailResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es002Url,
					BenefitDetailResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyBenefit]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return benefitDetailResponse;
	}
}