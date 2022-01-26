package com.twfhclife.eservice.policy.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.service.IFundTransactionService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.api_client.TxLogClient;
import com.twfhclife.generic.api_model.PolicyFundTransactionResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.DateUtil;

/**
 * 交易歷史記錄.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class TxLogController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TxLogController.class);

	@Autowired
	private IFundTransactionService fundTransactionService;
	
	@Autowired
	private TxLogClient txLogClient;

	/**
	 * 交易歷史記錄.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing10")
	public String listing10() {
		return "frontstage/listing10";
	}

	/**
	 * 取得交易歷史記錄.
	 * 
	 * @param policyNo 保單號碼
	 * @param systemId 系統別
	 * @param currencyCategoryCode 幣別參數類型代碼
	 * @param trCode 交易類別
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @return 回傳保單的保費費用記錄
	 */
	@RequestLog
	@PostMapping("/getTxLogList")
	public ResponseEntity<ResponseObj> getTxLogList(@RequestParam("policyNo") String policyNo,
			@RequestParam(value = "trCode", required = false) String trCode,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
		try {
			String errorMessage = "";
			int defaultPageSize = 4;
			trCode = (StringUtils.isEmpty(trCode) ? null : trCode);
			
			if (startDate != null && endDate != null) {
				if (startDate.compareTo(endDate) > 0) {
					//errorMessage = "結束日期不能小於開始日期";
					errorMessage = getParameterMap("SYSTEM_MSG_PARAMETER").get("E0003").getParameterValue();
				}
			}
			
			if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
				Date d = new Date();
				d.setYear(d.getYear() - 2);
				startDate = DateUtil.formatDateTime(d, "yyyy/MM/dd");
				endDate = DateUtil.formatDateTime(new Date(), "yyyy/MM/dd");
			}
			
			if (StringUtils.isEmpty(errorMessage)) {
				String userId = getUserId();
				List<FundTransactionVo> fundTransactionList = null;
				
				// Call api 取得資料
				PolicyFundTransactionResponse policyFundTransactionResponse = txLogClient
						.getPolicyFundTransaction(userId, policyNo, trCode, startDate, endDate, pageNum, defaultPageSize);
				// 若無資料，嘗試由內部服務取得資料
				if (policyFundTransactionResponse != null) {
					logger.info("Get user[{}] data from eservice_api[getPolicyFundTransactionPageList]", userId);
					fundTransactionList = policyFundTransactionResponse.getFundTransactionList();
				} else {
					logger.info("Call internal service to get user[{}] getPolicyFundTransactionPageList data", userId);
					fundTransactionList = fundTransactionService.getFundTransactionPageList(policyNo, trCode, startDate,
							endDate, pageNum, defaultPageSize);
				}
				processSuccess(fundTransactionList);
			} else {
				processError(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Unable to getTxLogList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
