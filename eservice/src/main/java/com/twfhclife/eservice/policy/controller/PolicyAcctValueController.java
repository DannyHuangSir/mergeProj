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

import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;
import com.twfhclife.eservice.policy.service.IPolicyAccountValueService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PolicyAcctValueClient;
import com.twfhclife.generic.api_model.PolicyAcctValueResponse;
import com.twfhclife.generic.controller.BaseController;

/**
 * 保單帳戶價值.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class PolicyAcctValueController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyAcctValueController.class);

	@Autowired
	private IPolicyAccountValueService policyAccountValueService;
	
	@Autowired
	private PolicyAcctValueClient policyAcctValueClient;
	
	/**
	 * 保單帳戶價值.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing6")
	public String listing6() {
		return "frontstage/listing6";
	}
	
	/**
	 * 取得保單帳戶價值.
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/getPolicyAcctValueList")
	public ResponseEntity<ResponseObj> getPolicyAcctValueList(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<PolicyAccountValueVo> policyAccountValueList = null;
			
			// Call api 取得資料
			PolicyAcctValueResponse policyAcctValueResponse = policyAcctValueClient.getPolicyValueByPolicyNo(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyAcctValueResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyValueByPolicyNo]", userId);
				policyAccountValueList = policyAcctValueResponse.getPolicyAccountValueList();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyValueByPolicyNo data", userId);
				policyAccountValueList = policyAccountValueService.getPolicyAccountValueList(policyNo);
			}
			processSuccess(policyAccountValueList);
		} catch (Exception e) {
			logger.error("Unable to getPolicyAcctValueList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}