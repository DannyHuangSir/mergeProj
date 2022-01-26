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
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransResendPolicyVo;
import com.twfhclife.eservice.onlineChange.service.ITransResendPolicyService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;
import com.twfhclife.generic.util.MyStringUtil;

/**
 * 線上申請-補發保單(保單為單選).
 * 
 * @author all
 */
@Controller
public class TransResendPolicyController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(TransResendPolicyController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ILilipmService lilipmService;

	@Autowired
	private ITransResendPolicyService transResendPolicyService;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	private final static String PAY_ACCT_PREFIX = "4399";
	
	@RequestLog
	@GetMapping("/policyResend1")
	public String policyResend1() {
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
						userId, TransTypeUtil.POLICY_RESEND_PARAMETER_CODE);
				transResendPolicyService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.POLICY_RESEND_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from policyResend1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "477");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/policyResend/policy-resend1";
	}

	@RequestLog
	@PostMapping("/policyResend2")
	public String policyResend1(TransResendPolicyVo transResendPolicyVo) {
		try {
			List<String> policyNoList = transResendPolicyVo.getPolicyNoList();
			if (policyNoList != null && policyNoList.size() > 0) {
				String policyNo = policyNoList.get(0);

				// 戶籍地址、收費地址
				LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
				if (lilipmVo != null) {
					addAttribute("addr", lilipmVo.getLipmAddr());
					addAttribute("charAddr", lilipmVo.getLipmCharAddr());
				}
			}
			
			addAttribute("transResendPolicyVo", transResendPolicyVo);
		} catch (Exception e) {
			logger.error("Unable to init from policyResend2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyResend/policy-resend2";
	}

	@RequestLog
	@PostMapping("/policyResend3")
	public String policyResend3(TransResendPolicyVo transResendPolicyVo) {
		try {
			// 發送驗證碼
			sendAuthCode("policyResend");
			addAttribute("transResendPolicyVo", transResendPolicyVo);
		} catch (Exception e) {
			logger.error("Unable to init from policyResend3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyResend/policy-resend3";
	}

	@RequestLog
	@PostMapping("/policyResendSuccess")
	public String policyResendSuccess(TransResendPolicyVo transResendPolicyVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transResendPolicyVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.POLICY_RESEND_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transResendPolicyVo.getAuthenticationNum();
				String msg = checkAuthCode("policyResend", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:policyResend3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transResendPolicyVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.POLICY_RESEND_PARAMETER_CODE);
				apiReq.setTransResendPolicyVo(transResendPolicyVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransResendPolicy data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transResendPolicyVo.setTransNum(transNum);
					
					int result = transResendPolicyService.insertTransResendPolicy(transResendPolicyVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				String policyNo = transResendPolicyVo.getPolicyNoList().get(0);
				String payAcct = PAY_ACCT_PREFIX + policyNo.substring(2) + MyStringUtil.getCheckCode(policyNo);
				addAttribute("payAcct", payAcct);
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:policyResend3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from policyResendSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:policyResend3";
		}
		return "frontstage/onlineChange/policyResend/policy-resend-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransPolicyResendDetail")
	public String getTransResendPolicyDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			logger.info("Call internal service to get user[{}] getTransResendPolicyDetail data", userId);
			TransResendPolicyVo transResendPolicyVo = transResendPolicyService.getTransResendPolicyDetail(transNum);
			
			String policyNo = transResendPolicyVo.getPolicyNoList().get(0);
			String payAcct = PAY_ACCT_PREFIX + policyNo.substring(2) + MyStringUtil.getCheckCode(policyNo);
			addAttribute("payAcct", payAcct);
			
			addAttribute("transResendPolicyVo", transResendPolicyVo);
		} catch (Exception e) {
			logger.error("Unable to getTransResendPolicyDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyResend/policy-resend-detail";
	}
}