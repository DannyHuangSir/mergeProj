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

import com.twfhclife.eservice.api.elife.domain.PolicyFundTransactionRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyFundTransactionResponse;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.service.IFundTransactionService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢交易歷史記錄.
 * 
 * @author all
 */
@RestController
public class TxLogController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TxLogController.class);

	@Autowired
	private IFundTransactionService fundTransactionService;

	/**
	 * 以保單號碼查詢交易歷史記錄(分頁).
	 * 
	 * @param req PolicyFundTransactionRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-013", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.FundTransactionDao.getFundTransactionPageList",
					execMethod = "以保單號碼查詢交易歷史記錄",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
						@SqlParam(requestParamkey = "transType", sqlParamkey = "trCode"),
						@SqlParam(requestParamkey = "startDate", sqlParamkey = "startDate"),
						@SqlParam(requestParamkey = "endDate", sqlParamkey = "endDate"),
						@SqlParam(requestParamkey = "pageNum", sqlParamkey = "pageNum"),
						@SqlParam(requestParamkey = "pageSize", sqlParamkey = "pageSize")
					}
				)
			}))
	@PostMapping(value = "/getPolicyFundTransaction", produces = { "application/json" })
	public ResponseEntity<?> getPolicyFundTransaction(
			@Valid @RequestBody PolicyFundTransactionRequest req) {
		ApiResponseObj<PolicyFundTransactionResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyFundTransactionResponse resp = new PolicyFundTransactionResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			String transType = req.getTransType();
			String startDate = req.getStartDate();
			String endDate = req.getEndDate();
			Integer pageNum = req.getPageNum();
			Integer pageSize = req.getPageSize();
			
			if (!StringUtils.isEmpty(policyNo)) {
				List<FundTransactionVo> fundTransactionList = fundTransactionService.
						getFundTransactionPageList(policyNo, transType, startDate, endDate, pageNum, pageSize);
				resp.setFundTransactionList(fundTransactionList);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyFundTransaction: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}