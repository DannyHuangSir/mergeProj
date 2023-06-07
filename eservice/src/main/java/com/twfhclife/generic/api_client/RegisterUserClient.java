package com.twfhclife.generic.api_client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.generic.api_model.RegisterUserRequest;
import com.twfhclife.generic.api_model.RegisterUserResponse;
import com.twfhclife.generic.service.ICtcApiUtilService;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class RegisterUserClient extends BaseRestClient{

	private static final Logger logger = LogManager.getLogger(RegisterUserClient.class);
	
	@Autowired
	private ICtcApiUtilService ctcApiUtilService;

	/**
	 * 加入會員-查詢電話、信箱
	 * @param apiReq
	 * @return
	 */
	public RegisterUserResponse getUserMailPhoneByRocid(RegisterUserRequest apiReq) {
		RegisterUserResponse registerUserResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REGISTER_USER_MAILPHONE);
			registerUserResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					RegisterUserResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getUserMailPhoneByRocid]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return registerUserResponse;
	}
	
	/**
	 * 加入會員-驗證保單是否存在
	 * @param apiReq
	 * @return
	 */
	public RegisterUserResponse CheckLipmInsuNoByPolicyNo(RegisterUserRequest apiReq) {
		RegisterUserResponse registerUserResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REGISTER_USER_CHECK_POLICYNO);
			registerUserResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					RegisterUserResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[CheckLipmInsuNoByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return registerUserResponse;
	}	
	
	/**
	 * 加入會員-取得台銀保單貸款狀態
	 * @param apiReq
	 * @return
	 */
	public RegisterUserResponse getLilomsAmtByPolicyNo(RegisterUserRequest apiReq) {
		RegisterUserResponse registerUserResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REGISTER_USER_CHECK_POLICYNO);
			registerUserResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					RegisterUserResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getLilomsAmtByPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return registerUserResponse;
	}	
	
	/**
	 * 加入會員-取得更新保單對應的電話號碼
	 * @param apiReq
	 * @return
	 */
	public RegisterUserResponse getUserMailPhoneByRocidAndPolicyNo(RegisterUserRequest apiReq) {
		RegisterUserResponse registerUserResponse = null;
		try {			
			String url = ctcApiUtilService.getApiUrl(OnlineChangeUtil.CTC_REGISTER_USER_USERMAILPHONE);
			registerUserResponse = this.postApi(MyJacksonUtil.object2Json(apiReq), url,
					RegisterUserResponse.class);
		} catch (Exception e) {
			logger.error("Unable to get data from eservice_api[getUserMailPhoneByRocidAndPolicyNo]: {}",
					ExceptionUtils.getStackTrace(e));
		}
		return registerUserResponse;
	}
	
}
