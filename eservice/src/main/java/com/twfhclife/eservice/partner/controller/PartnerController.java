package com.twfhclife.eservice.partner.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.partner.service.IPartnerService;
import com.twfhclife.eservice.policy.controller.PolicyDataController;
import com.twfhclife.eservice.policy.model.AgentVo;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;

@Controller
public class PartnerController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PartnerController.class);

	@Autowired
	private IPartnerService partnerService;

	@RequestLog
	@GetMapping("/partner")
	public String partner() {
		try {
			this.removeFromSession(PolicyDataController.ADMIN_QUERY_ROCID);//init
			
			List<AgentVo> agentOptionList = null;
			if ("agent".equals(getUserType())) {
				agentOptionList = partnerService.getAgentOptionList(getAgentCode());
			} else if ("admin".equals(getUserType())) {
				agentOptionList = partnerService.getAgentOptionList(null);
			}
			addAttribute("agentOptionList", agentOptionList);
		} catch (Exception e) {
			logger.error("Unable to findByPolicyNo from listing5: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/partner";
	}

	@RequestLog
	@GetMapping("/partnerEmpty")
	public String partnerEmpty() {
		return "frontstage/partner-empty";
	}

	@RequestLog
	@GetMapping("/partnerResult")
	public String partnerResult() {
		return "frontstage/partner-result";
	}
	
	/**
	 * 取得取得投資損益及投報率清單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/getPartnerPolicyList")
	public ResponseEntity<ResponseObj> getPartnerPolicyList(
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "rocId", required = false) String rocId,
			@RequestParam(value = "agentCode", required = false) String agentCode,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
		try {
			// 保經代的agentCode 使用帳號的前三位
			// 內部人員使用畫面帶入的合作對象代碼
			if ("agent".equals(getUserType())) {
				agentCode = getAgentCode();
			}
			
			int defaultPageSize = 5;
			processSuccess(partnerService.getPartnerPolicyPageList(
					rocId, policyNo, agentCode, pageNum, defaultPageSize));
		} catch (Exception e) {
			logger.error("Unable to getPartnerPolicyList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}