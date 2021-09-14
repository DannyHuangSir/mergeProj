package com.twfhclife.eservice.policy.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.policy.model.RcdcChangeVo;
import com.twfhclife.eservice.policy.service.IRcdcChangeService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;

/**
 * 契約變更資料.
 * 
 * @author alan
 */
@Controller
public class RcdcChangeController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyPaidController.class);
	
	@Autowired
	private IRcdcChangeService rcdcChangeService;
	
//	@Autowired
//	private RcdcChangeClient rcdcChangeClient;

	/**
	 * 保單紅利資料.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing5_3")
	public String listing53(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<RcdcChangeVo> rcdcChangeVoList = null;
			
			// 由內部服務取得資料
			logger.info("Call internal service to get user[{}] getRcdcChangeByPolicyNo data", userId);
			rcdcChangeVoList = rcdcChangeService.findByPolicyNo(policyNo);

			addAttribute("rcdcChangeVoList", rcdcChangeVoList);
		} catch (Exception e) {
			logger.error("Unable to findByPolicyNo from listing5-3: ", e);
			addDefaultSystemError();
		}
		return "frontstage/listing5-3";
	}
}