package com.twfhclife.eservice.api.elife.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.twfhclife.eservice.api.elife.domain.PolicyListRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyListResponse;
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

/**
 * 查詢保單清單.
 * 
 * @author all
 */
@RestController
public class UserPolicyListController extends BaseController {

	private static final Logger logger = LogManager.getLogger(UserPolicyListController.class);
	
	@Autowired
	private IPolicyListService policyListService;

	/**
	 * 查詢保單清單料.
	 * 
	 * @param req PolicyListRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-003", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyListDao.getInvtPolicyList",
					execMethod = "查詢投資型保單",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyListDao.getBenefitPolicyList",
					execMethod = "查詢保障型保單",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				)
			}))
	@PostMapping(value = "/getPolicyListByUser", produces = { "application/json" })
	public ResponseEntity<?> getPolicyListByUser(@Valid @RequestBody PolicyListRequest req) {
		ApiResponseObj<PolicyListResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyListResponse resp = new PolicyListResponse();
	
		try {
			String rocId = req.getPolicyHolderId();
			if (!StringUtils.isEmpty(rocId)) {
				
				// 投資型保單
				List<PolicyListVo> invtPolicyList = policyListService.getInvtPolicyList(rocId);
				resp.setInvtPolicyList(invtPolicyList);

				// 保障型保單
				List<PolicyListVo> benefitPolicyList = policyListService.getBenefitPolicyList(rocId);
				resp.setBenefitPolicyList(benefitPolicyList);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyListByUser: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}