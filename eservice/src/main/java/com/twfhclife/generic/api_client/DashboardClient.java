package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.DashboardRequest;
import com.twfhclife.generic.api_model.DashboardResponse;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 保障總覽相關資料控制器.
 * 
 * @author all
 */
@Service
public class DashboardClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(DashboardClient.class);

	@Value("${eservice_api.es001.url}")
	private String es001Url;
	
	/**
	 * 查詢客戶的保障總覽.
	 * 
	 * @param apiReq DashboardRequest
	 * @return DashboardResponse
	 */
	public DashboardResponse getPolicyDashBoard(DashboardRequest apiReq) {
		DashboardResponse dashboardResponse = null;
		try {
			dashboardResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es001Url,
					DashboardResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyDashBoard]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return dashboardResponse;
	}

	/**
	 * 查詢客戶的保障總覽.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	public DashboardResponse getPolicyDashBoard(String userId, String rocId) {
		DashboardRequest apiReq = new DashboardRequest();
		apiReq.setSysId(ApConstants.SYSTEM_ID);
		apiReq.setUserId(userId);
		apiReq.setPolicyHolderId(rocId);

		DashboardResponse dashboardResponse = null;
		try {
			dashboardResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), es001Url,
					DashboardResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getPolicyDashBoard]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return dashboardResponse;
	}
}
