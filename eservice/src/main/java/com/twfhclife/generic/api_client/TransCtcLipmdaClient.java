package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCtcLipmdaRequest;
import com.twfhclife.generic.api_model.TransCtcLipmdaResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcLipmdaClient extends BaseRestClient{
	
	private static final Logger logger = LogManager.getLogger(TransHistoryDetailClient.class);

	@Autowired
	private ICtcApiUtilService ctcApiUtilService;

	
	public TransCtcLipmdaResponse getTransCtcLipmdaDetail(TransCtcLipmdaRequest apiReq) {
		TransCtcLipmdaResponse transCtcLipmdaResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_LIPMDA);
			transCtcLipmdaResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLipmdaResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCtcLipmdaDetail]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLipmdaResponse;
	}
}
