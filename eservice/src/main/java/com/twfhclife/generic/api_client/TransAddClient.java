package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 送出線上申請明細.
 * 
 * @author all
 */
@Service
public class TransAddClient extends BaseRestClient {
	
	private static final Logger logger = LogManager.getLogger(TransAddClient.class);

	@Value("${eservice_api.es018.url}")
	private String es018Url;
	
	/**
	 * 送出線上申請明細.
	 * 
	 * @param apiReq TransAddRequest
	 * @return TransAddResponses
	 */
	public TransAddResponse addTransRequest(TransAddRequest apiReq) {
		TransAddResponse transAddResponse = null;
		try {
			transAddResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es018Url,
					TransAddResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransAdd]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transAddResponse;
	}
}