package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCtcLilipmRequest;
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcLilipmClient extends BaseRestClient {
	private static final Logger logger = LogManager.getLogger(TransCtcLilipmClient.class);

	@Autowired
	private ICtcApiUtilService ctcApiUtilService;
	
	public TransCtcLilipmResponse getTransCtcLilipm(TransCtcLilipmRequest apiReq) {
		TransCtcLilipmResponse transCtcLilipmResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_LILIPM);
			transCtcLilipmResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLilipmResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCtcLilipm]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLilipmResponse;
	}
	
	public TransCtcLilipmResponse getTransCtcLilipmDetail(TransCtcLilipmRequest apiReq) {
		TransCtcLilipmResponse transCtcLilipmResponse = null;
		try {
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_LILIPM_DETAIL);
			transCtcLilipmResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLilipmResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCtcLilipmDetail]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLilipmResponse;
	}
	
	public TransCtcLilipmResponse getRevokeByLilipmForLipmInsuSeqNo(TransCtcLilipmRequest apiReq) {
		TransCtcLilipmResponse transCtcLilipmResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REVOKE_BY_LILIPM);
			transCtcLilipmResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLilipmResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getRevokeByLilipmForLipmInsuSeqNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLilipmResponse;
	}
	
	public TransCtcLilipmResponse getRevokeByLilipmForSeqNoAndAginRecCode(TransCtcLilipmRequest apiReq) {
		TransCtcLilipmResponse transCtcLilipmResponse = null;
		try {						
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REVOKE_BY_LILIPM_AGIN_REC_CODE);
			transCtcLilipmResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLilipmResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getRevokeByLilipmForSeqNoAndAginRecCode]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLilipmResponse;
	}
	
	public TransCtcLilipmResponse getOnlineTrialDetail(TransCtcLilipmRequest apiReq) {
		TransCtcLilipmResponse transCtcLilipmResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_ONLINE_TRIAL_DETAIL);
			transCtcLilipmResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLilipmResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getRevokeByLilipmForSeqNoAndAginRecCode]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLilipmResponse;
	}
}
