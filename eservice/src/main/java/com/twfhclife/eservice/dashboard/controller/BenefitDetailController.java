package com.twfhclife.eservice.dashboard.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.twfhclife.eservice.dashboard.service.IDashboardService;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.generic.api_client.BenefitDetailClient;
import com.twfhclife.generic.api_model.BenefitDetailResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 我的保障.
 * 
 * @author alan
 */
@Controller
public class BenefitDetailController extends BaseController {

	private static final Logger logger = LogManager.getLogger(BenefitDetailController.class);
	
	@Autowired
	private IPolicyListService policyListService;

	@Autowired
	private IDashboardService dashboardService;
	
	@Autowired
	private BenefitDetailClient benefitDetailClient;

	/**
	 * 我的保障頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/detail1")
	public String detail1() {
		try {
			String userRocId = getUserRocId();
			String userId = getUserId();
			Map<String, List<CoverageVo>> benefitInsuredData = null; // 保障型的被保人資料
			
			if (!StringUtils.isEmpty(userRocId)) {
				// Call api 取得資料
				BenefitDetailResponse benefitDetailResponse = benefitDetailClient.getPolicyBenefit(userId, userRocId);
				// 若無資料，嘗試由內部服務取得資料
				if (benefitDetailResponse != null) {
					logger.info("Get user[{}] data from eservice_api[getPolicyBenefit]", userId);
					benefitInsuredData = benefitDetailResponse.getBenefitInsuredData();
				} else {
					logger.info("Call internal service to get user[{}] getPolicyBenefit data", userId);
					
					// 取得保障型商品
					List<PolicyListVo> benefitPolicyList = policyListService.getBenefitPolicyList(userRocId);
					if (benefitPolicyList != null) {
						benefitInsuredData = dashboardService.getBenefitInsuredData(benefitPolicyList);
					}
				}
				
				if (benefitInsuredData != null) {
					Iterator<String> it = benefitInsuredData.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						benefitInsuredData.get(key).get(0).setInsuredNameBase64(unicodeService.convertString2Unicode(key));
					}
				}
				
				addAttribute("benefitInsuredData", benefitInsuredData);
			}
		} catch (Exception e) {
			logger.error("Unable to init from dashboard detail1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/detail1";
	}
}
