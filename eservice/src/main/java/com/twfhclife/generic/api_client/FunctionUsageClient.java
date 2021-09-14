package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.FunctionUsageAdd;
import com.twfhclife.generic.api_model.FunctionUsageAddResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 紀錄功能每日使用次數
 * @author Ken Wei
 *
 */
@Service
public class FunctionUsageClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(FunctionUsageClient.class);

	@Value("${eservice_api.function.usage.add.url}")
	private String funcUsageAddUrl;
	
	public void addFunctionUsage(String systemId, String functionId) {
		FunctionUsageAdd functionUsageAdd = new FunctionUsageAdd();
		try {
			logger.info("FunctionUsageClient.addFunctionUsage start. SystemId=" + systemId + ", FunctionId=" + functionId);
			functionUsageAdd.setSysId(ApConstants.SYSTEM_ID);
			functionUsageAdd.setFunctionId(functionId);
			functionUsageAdd.setSystemId(systemId);
			this.postApi(MyJacksonUtil.object2Json(functionUsageAdd), funcUsageAddUrl, FunctionUsageAddResponse.class);
		} catch(Exception e) {
			logger.error("Unable to addFuncitonUsage error: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			logger.info("FunctionUsageClient.addFunctionUsage end. SystemId=" + systemId + ", FunctionId=" + functionId);
		}
	}
}
