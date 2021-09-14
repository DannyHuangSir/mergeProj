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

import com.twfhclife.eservice.api.elife.domain.PolicyDividendRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyDividendResponse;
import com.twfhclife.eservice.policy.model.PolicyDividendVo;
import com.twfhclife.eservice.policy.service.IPolicyDividendService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢保單收益分配.
 * 
 * @author all
 */
@RestController
public class PolicyDividendController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyDividendController.class);

	@Autowired
	private IPolicyDividendService policyDividendService;

	/**
	 * 查詢投資損益及投報率.
	 * 
	 * @param req PolicyDividendRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-005", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyDividendDao.getPolicyDividend",
					execMethod = "查詢投資損益及投報率",
					sqlVoType = "com.twfhclife.eservice.policy.model.PolicyDividendVo",
					sqlVoKey = "policyDividendVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyIncomeByPolicyNo", produces = { "application/json" })
	public ResponseEntity<?> getPolicyIncomeByPolicyNo(@Valid @RequestBody PolicyDividendRequest req) {
		ApiResponseObj<PolicyDividendResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyDividendResponse resp = new PolicyDividendResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				PolicyDividendVo qryVo = new PolicyDividendVo();
				qryVo.setPolicyNo(policyNo);
				
				List<PolicyDividendVo> policyDividendList = policyDividendService.getPolicyDividend(qryVo);
				resp.setPolicyDividendList(policyDividendList);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyIncomeByPolicyNo: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}