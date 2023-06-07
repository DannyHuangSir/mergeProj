package com.twfhclife.eservice.policy.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.FundPrdtVo;
import com.twfhclife.eservice.policy.service.IFundPrdtService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.api_client.TxPremiumClient;
import com.twfhclife.generic.api_model.PolicyPremiumTransactionResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 保費費用.
 * 
 * @author alan
 */
@Controller
public class TxPremiumController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TxPremiumController.class);

	@Autowired
	private IFundPrdtService fundPrdtService;
	
	@Autowired
	private TxPremiumClient txPremiumClient;

	/**
	 * 保費費用.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing7")
	public String listing7() {
		return "frontstage/listing7";
	}
	
	/**
	 * 取得保單的保費費用記錄.
	 * 
	 * @param policyNo 保單號碼
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @return 回傳保單的保費費用記錄
	 */
	@RequestLog
	@PostMapping("/getPremTransList")
	public ResponseEntity<ResponseObj> getPremTransList(@RequestParam("policyNo") String policyNo,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
		try {
			String errorMessage = "";
			int defaultPageSize = 4;
			startDate = (StringUtils.isEmpty(startDate) ? null : startDate);
			endDate = (StringUtils.isEmpty(endDate) ? null : endDate);

			if (startDate != null && endDate != null) {
				if (startDate.compareTo(endDate) > 0) {
					//errorMessage = "結束日期不能小於開始日期";
					errorMessage = getParameterMap("SYSTEM_MSG_PARAMETER").get("E0003").getParameterValue();
				}
			}
			
			if (StringUtils.isEmpty(errorMessage)) {
				String userId = getUserId();
				List<FundPrdtVo> oldPremiumTransactionList = new ArrayList<FundPrdtVo>();
				
				// Call api 取得資料
				PolicyPremiumTransactionResponse policyPremiumTransactionResponse = txPremiumClient
						.getPolicyPremiumTransaction(userId, policyNo, startDate, endDate, pageNum, defaultPageSize);
				// 若無資料，嘗試由內部服務取得資料
				if (policyPremiumTransactionResponse != null) {
					logger.info("Get user[{}] data from eservice_api[getPolicyPremiumTransactionPageList]", userId);
					oldPremiumTransactionList = policyPremiumTransactionResponse.getPremiumTransactionList();
				} else {
					logger.info("Call internal service to get user[{}] getPolicyPremiumTransactionPageList data", userId);
					oldPremiumTransactionList = fundPrdtService.getFundPrdtPageList(policyNo, 
							startDate, endDate, pageNum, defaultPageSize);
				}				
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				List<String> dateAmtList = new ArrayList<String>();
				List<FundPrdtVo> premiumTransactionList = oldPremiumTransactionList.stream().filter( x-> {
					String key = dateFormat.format(x.getPrdtBookDate()) + '-'  + x.getPrdtRcpAmt().toString();
					if(dateAmtList.contains(key)) {
						return false;
					}else {
						dateAmtList.add(key);
						return true;
					}					
				}).collect(Collectors.toList());				
				processSuccess(premiumTransactionList);
			} else {
				processError(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Unable to getPremTransList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}