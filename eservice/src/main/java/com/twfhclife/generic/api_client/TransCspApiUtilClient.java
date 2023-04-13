package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.TransCsp002DataRequest;
import com.twfhclife.generic.api_model.TransCsp002DataResponse;
import com.twfhclife.generic.api_model.TransCspApiUtilRequest;
import com.twfhclife.generic.api_model.TransCspApiUtilResponse;
import com.twfhclife.generic.service.ICspApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class TransCspApiUtilClient extends BaseRestClient{
	private static final Logger logger = LogManager.getLogger(TransCspApiUtilClient.class);
	
	@Autowired
	private ICspApiUtilService cspApiUtilService;
	
	public TransCspApiUtilResponse getTransCsp001Api(TransCspApiUtilRequest apiReq) {
		TransCspApiUtilResponse transCspApiUtilResponse = null;
		try {			
			String url = cspApiUtilService.getApiUrl(OnlineChangeUtil.CSP_001_DETAIL);
			transCspApiUtilResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url , TransCspApiUtilResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCsp001Api]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCspApiUtilResponse;
	}
	
	public TransCsp002DataResponse getTransCsp002Api(TransCsp002DataRequest apiReq) {
		TransCsp002DataResponse transCsp002DataResponse = null;
		try {			
			String url = cspApiUtilService.getApiUrl(OnlineChangeUtil.CSP_002_DETAIL);
			transCsp002DataResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url , TransCsp002DataResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getTransCsp001Api]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return transCsp002DataResponse;
	}
}
