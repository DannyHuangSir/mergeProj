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

import com.twfhclife.eservice.api.elife.domain.PolicyBonusRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyBonusResponse;
import com.twfhclife.eservice.policy.model.PolicyBonusVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢保單紅利資料.
 * 
 * @author all
 */
@RestController
public class PolicyBonusController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyBonusController.class);
	
	@Autowired
	private IPolicyBonusService policyBonusService;

	/**
	 * 查詢保單紅利資料.
	 * 
	 * @param req PolicyBonusRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-008", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyBonusDao.findByPolicyNo",
					execMethod = "查詢保單紅利",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyBonusByPolicyNo", produces = { "application/json" })
	public ResponseEntity<?> getPolicyBonusByPolicyNo(@Valid @RequestBody PolicyBonusRequest req) {
		ApiResponseObj<PolicyBonusResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyBonusResponse resp = new PolicyBonusResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				List<PolicyBonusVo> policyBonusVoList = policyBonusService.findByPolicyNo(policyNo);
				resp.setPolicyBonusVoList(policyBonusVoList);
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