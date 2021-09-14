package com.twfhclife.eservice.onlineChange.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twfhclife.eservice.onlineChange.model.ClaimVo;
import com.twfhclife.eservice.onlineChange.service.IClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;

/**
 * 線上申請-理賠申請書套印(保單為單選)
 */
@Controller
public class ClaimController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(ClaimController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private IClaimService claimService;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	@RequestLog
	@GetMapping("/claim1")
	public String claim1() {
		try {
			if(!checkCanUseOnlineChange()) {
				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				addSystemError(message);
				return "redirect:apply1";
			}
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<PolicyListVo> policyList = getUserOnlineChangePolicyList(userId, userRocId);
			
			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.CLAIM_PARAMETER_CODE);
				claimService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CLAIM_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from claim1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "487");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/claim/claim1";
	}

	@RequestLog
	@PostMapping("/claim2")
	public String claim2(ClaimVo claimVo) {
		try {
			// 要保人
			LilipmVo lilipmVo = lilipmService.findByPolicyNo(claimVo.getPolicyNo());
			if (lilipmVo != null) {
				claimVo.setCustomerName(lilipmVo.getLipmName1());
			}
			// 被保人
			LilipiVo lilipiVo = lilipiService.findByPolicyNo(claimVo.getPolicyNo());
			if (lilipiVo != null) {
				claimVo.setInsuredName(lilipiVo.getLipiName());
				claimVo.setRocId(lilipiVo.getNoHiddenLipiId());
				claimVo.setBirthDate(DateUtil.formatDateTime(lilipiVo.getLipiBirth(), DateUtil.FORMAT_WEST_DATE));
			}
			
			addAttribute("claimVo", claimVo);
		} catch (Exception e) {
			logger.error("Unable to init from claim2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/claim/claim2";
	}

	@RequestLog
	@PostMapping("/claim3")
	public String claim3(ClaimVo claimVo) {
		try {
			addAttribute("claimVo", claimVo);
		} catch (Exception e) {
			logger.error("Unable to init from claim3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/claim/claim3";
	}
	
	@RequestLog
	@RequestMapping(value = "/downloadClaimPDF")
	public @ResponseBody HttpEntity<byte[]> downloadClaimPDF(ClaimVo claimVo) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=理賠申請書-%s.pdf", claimVo.getPolicyNo());
			
			document = claimService.getPDF(claimVo);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);
		} catch (Exception e) {
			logger.error("Unable to get data from downloadClaimPDF: {}", ExceptionUtils.getStackTrace(e));
		}

		return new HttpEntity<byte[]>(document, header);
	}
}