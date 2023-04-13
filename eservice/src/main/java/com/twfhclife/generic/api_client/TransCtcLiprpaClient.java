package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCtcLiprpaRequest;
import com.twfhclife.generic.api_model.TransCtcLiprpaResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcLiprpaClient  extends BaseRestClient {
	private static final Logger logger = LogManager.getLogger(TransCtcLiprpaClient.class);
	
	@Autowired
	private ICtcApiUtilService ctcApiUtilService;
	
	public TransCtcLiprpaResponse getRevokeByLiprpaForInsuSeqNo(TransCtcLiprpaRequest apiReq) {
		TransCtcLiprpaResponse transCtcLiprpaResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REVOKE_BY_LIPRPA);
			
			transCtcLiprpaResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLiprpaResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getRevokeByLiprpaForInsuSeqNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLiprpaResponse;
	}
}
