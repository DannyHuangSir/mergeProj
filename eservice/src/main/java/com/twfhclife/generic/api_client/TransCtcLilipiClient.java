package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCtcLilipiRequest;
import com.twfhclife.generic.api_model.TransCtcLilipiResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcLilipiClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(TransCtcLilipiClient.class);
	
	@Autowired
	private ICtcApiUtilService ctcApiUtilService;
	
	
	/**
	 * 取得被保險人身份證與生日
	 * @param apiReq
	 * @return
	 */
	public TransCtcLilipiResponse getLipiDataByPolicyNo(TransCtcLilipiRequest apiReq) {
		TransCtcLilipiResponse transCtcLilipiResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REGISTER_USER_LIPIDATA);
			transCtcLilipiResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLilipiResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getLipiDataByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLilipiResponse;
	}
}
