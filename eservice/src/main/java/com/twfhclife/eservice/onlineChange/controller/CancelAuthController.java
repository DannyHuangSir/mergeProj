package com.twfhclife.eservice.onlineChange.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.twfhclife.eservice.onlineChange.model.CancelAuthVo;
import com.twfhclife.eservice.onlineChange.service.ICancelAuthService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 線上申請-終止授權(保單為多選).
 * 
 * @author all
 */
@Controller
public class CancelAuthController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(CancelAuthController.class);
	
	@Autowired
	private ITransService transService;

	@Autowired
	private ICancelAuthService cancelAuthService;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@RequestLog
	@GetMapping("/cancelAuth1")
	public String cancelAuth1() {
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
						userId, TransTypeUtil.CANCEL_AUTH_ACCOUNT_PARAMETER_CODE);
				cancelAuthService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CANCEL_AUTH_ACCOUNT_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from cancelAuth1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "484");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/cancelAuth/cancel-auth1";
	}

	@RequestLog
	@PostMapping("/cancelAuth2")
	public String cancelAuth1(CancelAuthVo cancelAuthVo) {
		try {
			// 發送驗證碼
			sendAuthCode("cancelAuth");
			addAttribute("cancelAuthVo", cancelAuthVo);
		} catch (Exception e) {
			logger.error("Unable to init from cancelAuth2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cancelAuth/cancel-auth2";
	}

	@RequestLog
	@PostMapping("/cancelAuthSuccess")
	public String cancelAuthSuccess(CancelAuthVo cancelAuthVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = cancelAuthVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.CANCEL_AUTH_ACCOUNT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = cancelAuthVo.getAuthenticationNum();
				String msg = checkAuthCode("cancelAuth", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:cancelAuth2";
				}
				
				// 設定使用者
				String userId = getUserId();
				cancelAuthVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.CANCEL_AUTH_ACCOUNT_PARAMETER_CODE);
				apiReq.setCancelAuthVo(cancelAuthVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransCancelAuth data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					cancelAuthVo.setTransNum(transNum);
					
					int result = cancelAuthService.insertTransCancelAuth(cancelAuthVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:cancelAuth2";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from cancelAuthSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:cancelAuth2";
		}
		return "frontstage/onlineChange/cancelAuth/cancel-auth-success";
	}
}