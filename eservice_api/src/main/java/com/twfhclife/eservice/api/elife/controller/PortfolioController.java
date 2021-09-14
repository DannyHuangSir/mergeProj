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

import com.twfhclife.eservice.api.elife.domain.PortfolioRequest;
import com.twfhclife.eservice.api.elife.domain.PortfolioResponse;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單查詢投資損益及投報率.
 * 
 * @author all
 */
@RestController
public class PortfolioController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PortfolioController.class);
	
	@Autowired
	private IPortfolioService portfolioService;

	/**
	 * 以保單號碼查詢保單收益分配.
	 * 
	 * @param req PortfolioRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-011", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PortfolioDao.getPortfolioList",
					execMethod = "以保單號碼查詢保單收益分配",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyRateOfReturn", produces = { "application/json" })
	public ResponseEntity<?> getPolicyRateOfReturn(@Valid @RequestBody PortfolioRequest req) {
		ApiResponseObj<PortfolioResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PortfolioResponse resp = new PortfolioResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				String riskLevelName = portfolioService.getRiskLevelName(policyNo);
				resp.setRiskLevelName(riskLevelName);
				
				List<PortfolioVo> portfolioList = portfolioService.getPortfolioList(policyNo, null);
				resp.setPortfolioList(portfolioList);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyRateOfReturn: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}