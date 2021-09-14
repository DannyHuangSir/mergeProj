package com.twfhclife.eservice.policy.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.policy.model.PolicyDividendVo;
import com.twfhclife.eservice.policy.service.IPolicyDividendService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PolicyDividendClient;
import com.twfhclife.generic.api_model.PolicyDividendResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 保單收益分配.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class PolicyDividendController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyDividendController.class);

	@Autowired
	private IPolicyDividendService policyDividendService;
	
	@Autowired
	private PolicyDividendClient policyDividendClient;

	/**
	 * 保單收益分配.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing6_2")
	public String listing6_2() {
		return "frontstage/listing6-2";
	}
	
	/**
	 * 取得保單收益分配記錄.
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/getPolicyDividendList")
	public ResponseEntity<ResponseObj> getPolicyDividendList(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<PolicyDividendVo> policyDividendList = null;
			
			// Call api 取得資料
			PolicyDividendResponse policyDividendResponse = policyDividendClient.getPolicyIncomeByPolicyNo(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyDividendResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyIncomeByPolicyNo]", userId);
				policyDividendList = policyDividendResponse.getPolicyDividendList();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyIncomeByPolicyNo data", userId);
				PolicyDividendVo qryVo = new PolicyDividendVo();
				qryVo.setPolicyNo(policyNo);
				policyDividendList = policyDividendService.getPolicyDividend(qryVo);
			}
			processSuccess(policyDividendList);
		} catch (Exception e) {
			logger.error("Unable to getPolicyDividendList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}