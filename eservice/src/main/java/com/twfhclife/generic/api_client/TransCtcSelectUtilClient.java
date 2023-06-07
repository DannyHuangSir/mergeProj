package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.LicohilResponse;
import com.twfhclife.generic.api_model.PolicyDetailRequest;
import com.twfhclife.generic.api_model.PolicyDetailResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDetailResponse;
import com.twfhclife.generic.api_model.TransCtcSelectUtilRequest;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcSelectUtilClient extends BaseRestClient {
	private static final Logger logger = LogManager.getLogger(TransCtcSelectUtilClient.class);

	@Autowired
	private ICtcApiUtilService ctcApiUtilService;
	
	public TransCtcSelectDataResponse getTransCtcSelectDataByLipmId(TransCtcSelectUtilRequest apiReq) {
		TransCtcSelectDataResponse transCtcSelectDataResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_SELECT_DATA);
			transCtcSelectDataResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcSelectDataResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCtcSelectDataByLipmId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcSelectDataResponse;
	}
	


	public TransCtcSelectDetailResponse getTransCtcSelectDetailByLipmInsuSeqNo(TransCtcSelectUtilRequest apiReq) {
		TransCtcSelectDetailResponse transCtcSelectDetailResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_SELECT_DETAIL);
			transCtcSelectDetailResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcSelectDetailResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCtcSelectDetailByLipmId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcSelectDetailResponse;
	}
	
	
	public TransCtcSelectDataAddCodeResponse getTransCtcSelectDataAddCode(TransCtcSelectUtilRequest apiReq) {
		TransCtcSelectDataAddCodeResponse transCtcSelectDataAddCodeResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_SELECT_DATA_ADD_CODE);
			transCtcSelectDataAddCodeResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcSelectDataAddCodeResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCtcSelectDetailByLipmId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcSelectDataAddCodeResponse;
	}
	
	public PolicyDetailResponse getPolicyDataByRocId(PolicyDetailRequest apiReq) {
		PolicyDetailResponse policyDetailResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_POLICY_DATA);
			policyDetailResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					PolicyDetailResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyDataByRocId]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return policyDetailResponse;
	}
	
	public LicohilResponse getLicohiByPolicyNo(PolicyDetailRequest apiReq) {
		LicohilResponse licohilResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_INDIVIDUAL_CHOOSE_LICOHI);
			licohilResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					LicohilResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getLicohiByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return licohilResponse;
	}
	
	public LicohilResponse getLilipmByPolicyNo(PolicyDetailRequest apiReq) {
		LicohilResponse licohilResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_INDIVIDUAL_CHOOSE_LILIPM);
			licohilResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					LicohilResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getLilipmByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return licohilResponse;
	}
}
