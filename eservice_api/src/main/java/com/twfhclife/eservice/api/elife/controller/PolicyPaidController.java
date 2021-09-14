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

import com.twfhclife.eservice.api.elife.domain.PolicyPaidRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyPaidResponse;
import com.twfhclife.eservice.policy.model.PolicyPaidVo;
import com.twfhclife.eservice.policy.service.IPolicyPaidService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢保單給付資料.
 * 
 * @author all
 */
@RestController
public class PolicyPaidController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyPaidController.class);
	
	@Autowired
	private IPolicyPaidService policyPaidService;

	/**
	 * 查詢保單紅利資料.
	 * 
	 * @param req PolicyBonusRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-019", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyPaidDao.findByPolicyNo",
					execMethod = "查詢保單給付",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyPaidByPolicyNo", produces = { "application/json" })
	public ResponseEntity<?> getPolicyPaidByPolicyNo(@Valid @RequestBody PolicyPaidRequest req) {
		ApiResponseObj<PolicyPaidResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyPaidResponse resp = new PolicyPaidResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				List<PolicyPaidVo> policyPaidVoList = policyPaidService.findByPolicyNo(policyNo);
				resp.setPolicyPaidVoList(policyPaidVoList);
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyBonusByPolicyNo: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}