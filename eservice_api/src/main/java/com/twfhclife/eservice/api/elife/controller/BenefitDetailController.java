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

import com.twfhclife.eservice.api.elife.domain.BenefitDetailRequest;
import com.twfhclife.eservice.api.elife.domain.BenefitDetailResponse;
import com.twfhclife.eservice.dashboard.service.IDashboardService;
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
 * 查詢客戶的保單彙整.
 * 
 * @author all
 */
@RestController
public class BenefitDetailController extends BaseController {

	private static final Logger logger = LogManager.getLogger(BenefitDetailController.class);
	
	@Autowired
	private IDashboardService dashboardService;
	
	@Autowired
	private IPolicyListService policyListService;

	/**
	 * 查詢客戶的保障總覽.
	 * 
	 * @param req BenefitDetailRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-002", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyListDao.getBenefitPolicyList",
					execMethod = "查詢保障型保單",
					execFile = "保障型保單資料", 
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				)
			}))
	@PostMapping(value = "/getPolicyBenefit", produces = { "application/json" })
	public ResponseEntity<?> getPolicyBenefit(@Valid @RequestBody BenefitDetailRequest req) {
		ApiResponseObj<BenefitDetailResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		BenefitDetailResponse resp = new BenefitDetailResponse();
		
		try {
			String rocId = req.getPolicyHolderId();
			if (!StringUtils.isEmpty(rocId)) {
				// 取得保障型商品
				List<PolicyListVo> benefitPolicyList = policyListService.getBenefitPolicyList(rocId);
				if (benefitPolicyList != null) {
					// 取得保障型的被保人資料
					resp.setBenefitInsuredData(dashboardService.getBenefitInsuredData(benefitPolicyList));
				}
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyBenefit: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}