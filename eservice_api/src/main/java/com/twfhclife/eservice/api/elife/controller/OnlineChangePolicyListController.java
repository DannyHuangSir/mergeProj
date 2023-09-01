package com.twfhclife.eservice.api.elife.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.elife.domain.OnlineChangePolicyListRequest;
import com.twfhclife.eservice.api.elife.domain.OnlineChangePolicyListResponse;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.ApConstants;

/**
 * 以保戶證號及申請類別查詢保單清單.
 * 
 * @author all
 */
@RestController
public class OnlineChangePolicyListController extends BaseController {

	private static final Logger logger = LogManager.getLogger(OnlineChangePolicyListController.class);
	
	@Autowired
	private IPolicyListService policyListService;

	/**
	 * 以保戶證號及申請類別查詢保單清單.
	 * 
	 * @param req OnlineChangePolicyListRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-015", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyListDao.getUserPolicyList",
					execMethod = "以保戶證號查詢保單清單",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				)
			}))
	@PostMapping(value = "/getOnlineChangePolicyList", produces = { "application/json" })
	public ResponseEntity<?> getOnlineChangePolicyList(@Valid @RequestBody OnlineChangePolicyListRequest req) {
		ApiResponseObj<OnlineChangePolicyListResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		OnlineChangePolicyListResponse resp = new OnlineChangePolicyListResponse();
	
		try {
			String rocId = req.getPolicyHolderId();
			if (!StringUtils.isEmpty(rocId)) {
				List<PolicyListVo> userPolicyList = new ArrayList<PolicyListVo>();
				// 判斷是否為保單理賠
				if (req.getTypeName() != null &&
						(ApConstants.INSURANCE_CLAIM.equals(req.getTypeName()) || ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE.equals(req.getTypeName()))) {
					userPolicyList = policyListService.getUserPolicyListByRocId(rocId);
				}else {
					userPolicyList = policyListService.getUserPolicyList(rocId);
				}
				resp.setUserPolicyList(userPolicyList);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getOnlineChangePolicyList: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}