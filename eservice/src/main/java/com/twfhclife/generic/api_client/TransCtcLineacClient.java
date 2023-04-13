package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCtcLineacRequest;
import com.twfhclife.generic.api_model.TransCtcLineacResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcLineacClient  extends BaseRestClient{
	
	private static final Logger logger = LogManager.getLogger(TransCtcLineacClient.class);

	@Autowired
	private ICtcApiUtilService ctcApiUtilService;

	
	public TransCtcLineacResponse getRevokeByLineacForNeacInsuNo(TransCtcLineacRequest apiReq) {
		TransCtcLineacResponse transCtcLineacResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REVOKE_BY_LINEAC);
			transCtcLineacResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLineacResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getRevokeByLineacForNeacInsuNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLineacResponse;
	}
}
