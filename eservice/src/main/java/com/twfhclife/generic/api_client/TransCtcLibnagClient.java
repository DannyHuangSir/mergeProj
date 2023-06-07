package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCtcLibnagRequest;
import com.twfhclife.generic.api_model.TransCtcLibnagResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCtcLibnagClient  extends BaseRestClient {
	private static final Logger logger = LogManager.getLogger(TransCtcLibnagClient.class);
	
	@Autowired
	private ICtcApiUtilService ctcApiUtilService;
	
	/**
	 * 
	 * @param apiReq
	 * @return
	 */
	public TransCtcLibnagResponse getRevokeByLibnagForBnagInsuSeqNo(TransCtcLibnagRequest apiReq) {
		TransCtcLibnagResponse transCtcLibnagResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REVOKE_BY_LIBNAG);
			transCtcLibnagResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLibnagResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getRevokeByLibnagForBnagInsuSeqNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLibnagResponse;
	}
	/**
	 * 加入會員-取得要保人出生日期
	 * @param apiReq
	 * @return
	 */
	public TransCtcLibnagResponse getBirthByPolicyNo(TransCtcLibnagRequest apiReq) {
		TransCtcLibnagResponse transCtcLibnagResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REGISTER_USER_BIRTH);
			transCtcLibnagResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					TransCtcLibnagResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getBirthByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCtcLibnagResponse;
	}
}
