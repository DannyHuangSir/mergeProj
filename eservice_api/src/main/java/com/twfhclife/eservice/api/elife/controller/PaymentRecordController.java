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

import com.twfhclife.eservice.api.elife.domain.PolicyPaymentRecordRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyPaymentRecordResponse;
import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;
import com.twfhclife.eservice.policy.service.IPolicyPaymentRecordService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢繳費紀錄.
 * 
 * @author all
 */
@RestController
public class PaymentRecordController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PaymentRecordController.class);
	
	@Autowired
	private IPolicyPaymentRecordService paymentRecordService;

	/**
	 * 查詢繳費紀錄.
	 * 
	 * @param req PolicyPaymentRecordRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-006", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyPaymentRecordDao.getPolicyPaymentRecordLastYear",
					execMethod = "查詢最近2年繳費紀錄",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyPaymentRecord", produces = { "application/json" })
	public ResponseEntity<?> getPolicyPaymentRecord(@Valid @RequestBody PolicyPaymentRecordRequest req) {
		ApiResponseObj<PolicyPaymentRecordResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyPaymentRecordResponse resp = new PolicyPaymentRecordResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				List<PolicyPaymentRecordVo> paymentRecordList = paymentRecordService.getPolicyPaymentRecordLastYear(policyNo);
				resp.setPaymentRecordList(paymentRecordList);
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyPaymentRecord: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}