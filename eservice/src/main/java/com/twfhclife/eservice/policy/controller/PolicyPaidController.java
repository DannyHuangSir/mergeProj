package com.twfhclife.eservice.policy.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.PolicyPaidVo;
import com.twfhclife.eservice.policy.service.IPolicyPaidService;
import com.twfhclife.generic.api_client.PolicyPaidClient;
import com.twfhclife.generic.api_model.PolicyPaidResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 保單給付資料.
 * 
 * @author alan
 */
@Controller
public class PolicyPaidController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyPaidController.class);
	
	@Autowired
	private IPolicyPaidService policyPaidService;
	
	@Autowired
	private PolicyPaidClient policyPaidClient;

	/**
	 * 保單給付資料.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing5_2")
	public String listing52(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<PolicyPaidVo> policyPaidVoList = null;
			
			// Call api 取得資料
			PolicyPaidResponse policyPaidResponse = policyPaidClient.getPolicyPaidByPolicyNo(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyPaidResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyPaidByPolicyNo]", userId);
				policyPaidVoList = policyPaidResponse.getPolicyPaidVoList();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyPaidByPolicyNo data", userId);
				policyPaidVoList = policyPaidService.findByPolicyNo(policyNo);
			}

			addAttribute("paidVoList", policyPaidVoList);
		} catch (Exception e) {
			logger.error("Unable to findByPolicyNo from listing5-2: ", e);
			addDefaultSystemError();
		}
		return "frontstage/listing5-2";
	}
}