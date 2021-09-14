package com.twfhclife.eservice.policy.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PolicyLoanClient;
import com.twfhclife.generic.api_model.PolicyLoanResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 保單貸款資料.
 * 
 * @author alan
 */
@Controller
public class PolicyLoanController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyLoanController.class);
	
	@Autowired
	private IPolicyExtraService policyExtraService;
	
	@Autowired
	private PolicyLoanClient policyLoanClient;

	/**
	 * 保單貸款資料.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing4")
	public String listing4(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			PolicyExtraVo policyExtraVo = null;
			
			// Call api 取得資料
			PolicyLoanResponse policyLoanResponse = policyLoanClient.getPolicyloanByPolicyNo(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyLoanResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyloanByPolicyNo]", userId);
				policyExtraVo = policyLoanResponse.getPolicyExtraVo();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyloanByPolicyNo data", userId);
				policyExtraVo = policyExtraService.findByPolicyNo(policyNo);
			}
			addAttribute("loanVo", policyExtraVo);
		} catch (Exception e) {
			logger.error("Unable to findByPolicyNo from listing4: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/listing4";
	}
}
