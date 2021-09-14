package com.twfhclife.eservice.policy.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;
import com.twfhclife.eservice.policy.service.IPolicyPaymentRecordService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PaymentRecordClient;
import com.twfhclife.generic.api_model.PolicyPaymentRecordResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 繳費紀錄.
 * 
 * @author alan
 */
@Controller
public class PaymentRecordController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PaymentRecordController.class);
	
	@Autowired
	private IPolicyPaymentRecordService paymentRecordService;
	
	@Autowired
	private PaymentRecordClient paymentRecordClient;
	
	/**
	 * 繳費紀錄.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing3")
	public String listing3(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<PolicyPaymentRecordVo> paymentRecordList;
			
			// Call api 取得資料
			PolicyPaymentRecordResponse policyPaymentRecordResponse = paymentRecordClient.getPolicyPaymentRecord(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyPaymentRecordResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyPaymentRecord]", userId);
				paymentRecordList = policyPaymentRecordResponse.getPaymentRecordList();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyPaymentRecord data", userId);
				paymentRecordList = paymentRecordService.getPolicyPaymentRecordLastYear(policyNo);
			}
			addAttribute("paymentRecordList", paymentRecordList);
		} catch (Exception e) {
			logger.error("Unable to getPolicyPaymentRecordLastYear from listing3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/listing3";
	}
}
