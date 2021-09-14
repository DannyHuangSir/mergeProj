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

import com.twfhclife.eservice.api.elife.domain.PolicyPremiumTransactionRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyPremiumTransactionResponse;
import com.twfhclife.eservice.policy.model.FundPrdtVo;
import com.twfhclife.eservice.policy.service.IFundPrdtService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢保單保費費用.
 * 
 * @author all
 */
@RestController
public class TxPremiumController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TxPremiumController.class);

	@Autowired
	private IFundPrdtService fundPrdtService;

	/**
	 * 以保單號碼查詢保單保費費用.
	 * 
	 * @param req PolicyPremiumTransactionRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-012", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.FundPrdtDao.getFundPrdtPageList",
					execMethod = "查詢保單保費費用",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
						@SqlParam(requestParamkey = "startDate", sqlParamkey = "startDate"),
						@SqlParam(requestParamkey = "endDate", sqlParamkey = "endDate"),
						@SqlParam(requestParamkey = "pageNum", sqlParamkey = "pageNum"),
						@SqlParam(requestParamkey = "pageSize", sqlParamkey = "pageSize")
					}
				)
			}))
	@PostMapping(value = "/getPolicyPremiumTransaction", produces = { "application/json" })
	public ResponseEntity<?> getPolicyPremiumTransaction(
			@Valid @RequestBody PolicyPremiumTransactionRequest req) {
		ApiResponseObj<PolicyPremiumTransactionResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyPremiumTransactionResponse resp = new PolicyPremiumTransactionResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			String startDate = req.getStartDate();
			String endDate = req.getEndDate();
			Integer pageNum = req.getPageNum();
			Integer pageSize = req.getPageSize();
			
			if (!StringUtils.isEmpty(policyNo)) {
				List<FundPrdtVo> premiumTransactionList = fundPrdtService.
						getFundPrdtPageList(policyNo, startDate, endDate, pageNum, pageSize);
				resp.setPremiumTransactionList(premiumTransactionList);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyPremiumTransaction: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}