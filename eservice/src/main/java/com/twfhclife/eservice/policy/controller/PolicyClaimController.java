package com.twfhclife.eservice.policy.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_model.PolicyListResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 保單給付資料.
 * 
 * @author alan
 */
@Controller
public class PolicyClaimController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(PolicyClaimController.class);
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	/**
	 * 我的保單清單.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/policyClaim")
	public String policyClaim(@RequestParam("policyNo") String policyNo) {
		try {
			if (!loginCheck()) {
				return "redirect:/login-timeout";
			}
		} catch (Exception e) {
			logger.error("Unable to loginCheck: {}", ExceptionUtils.getStackTrace(e));
		}
		
//		try {
//			
//			String userRocId = getUserRocId();
//			String userId = getUserId();
//			
//			PolicyListResponse policyListResponse = getPolicyListByUser(userId, userRocId);
//			if (policyListResponse != null) {
//				addAttribute("invtPolicyList",  policyListResponse.getInvtPolicyList());
//				addAttribute("benefitPolicyList", policyListResponse.getBenefitPolicyList());
//			}
//		} catch (Exception e) {
//			logger.error("Unable to get data from listing13: {}", ExceptionUtils.getStackTrace(e));
//			addDefaultSystemError();
//		}
		
		/* 計算功能使用次數 */
//		try {
//		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "162");
//		} catch(Exception e) {
//		    logger.debug("Call FunctionUsageAdd error: ", e);
//		}
		
		return "frontstage/policy-claim";
	}
}
