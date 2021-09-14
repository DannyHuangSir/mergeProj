package com.twfhclife.eservice.policy.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.policy.model.PolicyBonusVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PolicyBonusClient;
import com.twfhclife.generic.api_model.PolicyBonusResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 保單紅利資料.
 * 
 * @author alan
 */
@Controller
public class PolicyBonusController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyBonusController.class);
	
	@Autowired
	private IPolicyBonusService policyBonusService;
	
	@Autowired
	private PolicyBonusClient policyBonusClient;

	/**
	 * 保單紅利資料.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing5")
	public String listing5(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<PolicyBonusVo> policyBonusVoList = null;
			
			// Call api 取得資料
			PolicyBonusResponse policyBonusResponse = policyBonusClient.getPolicyBonusByPolicyNo(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyBonusResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyBonusByPolicyNo]", userId);
				policyBonusVoList = policyBonusResponse.getPolicyBonusVoList();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyBonusByPolicyNo data", userId);
				policyBonusVoList = policyBonusService.findByPolicyNo(policyNo);
			}
			
			String nameBase64 = null;
			if(policyBonusVoList != null && policyBonusVoList.size() > 0 && policyBonusVoList.get(0).getIndividualVo() != null) {
				String name = policyBonusVoList.get(0).getIndividualVo().getName();
				nameBase64 = unicodeService.convertString2Unicode(name);
			}
			addAttribute("nameBase64", nameBase64);
			
			addAttribute("bonusVoList", policyBonusVoList);
		} catch (Exception e) {
			logger.error("Unable to findByPolicyNo from listing5: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/listing5";
	}
}