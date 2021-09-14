package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransRewardVo;
import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.onlineChange.service.ITransRewardService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 線上申請-變更增值回饋分享金領取方式(保單為單選)
 */
@Controller
public class RewardController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(RewardController.class);
	
	@Autowired
	private IPolicyBonusService policyBonusService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransRewardService transRewardService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/returnBonus1")
	public String returnBonus1() {
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
						userId, TransTypeUtil.REWARD_PARAMETER_CODE);
				transRewardService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.REWARD_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from returnBonus1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "468");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/reward/return-bonus1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/returnBonus2")
	public String returnBonus2(TransRewardVo transRewardVo) {
		try {
			List<String> policyNos = transRewardVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				
				// 取得可以變更的'增值回饋分享金領取方式'
				VerifyPolicyResult verifyPolicyResult = transService.verifyPolicyRule(policyNo,
						TransTypeUtil.REWARD_PARAMETER_CODE);
				List<String> optionList = null;
				if (verifyPolicyResult != null && verifyPolicyResult.getOptiionList() != null) {
					optionList = new ArrayList<>();
					for(String option: verifyPolicyResult.getOptiionList()) {
						String[] splitOption = option.split("、");
						optionList.addAll(Arrays.asList(splitOption));
					}
				}
				addAttribute("rewardOptionList", optionList);

				// 找出舊的紅利給付方式
				String rewardModeOld = policyBonusService.getBounsTake(policyNo);
				if (!StringUtils.isEmpty(rewardModeOld)) {
					transRewardVo.setRewardModeOld(rewardModeOld);
				}
			}
			
			addAttribute("transRewardVo", transRewardVo);
		} catch (Exception e) {
			logger.error("Unable to init from returnBonus2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/reward/return-bonus2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/returnBonus3")
	public String returnBonus3(TransRewardVo transRewardVo) {
		try {
			// 發送驗證碼
			sendAuthCode("rewardMode");
			addAttribute("transRewardVo", transRewardVo);
		} catch (Exception e) {
			logger.error("Unable to init from returnBonus3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/reward/return-bonus3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/returnBonusSuccess")
	public String returnBonusSuccess(TransRewardVo transRewardVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transRewardVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.REWARD_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transRewardVo.getAuthenticationNum();
				String msg = checkAuthCode("rewardMode", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:returnBonus3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transRewardVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.REWARD_PARAMETER_CODE);
				apiReq.setTransRewardVo(transRewardVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransReward data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transRewardVo.setTransNum(transNum);
					
					int result = transRewardService.insertTransReward(transRewardVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:returnBonus3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from returnBonusSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:returnBonus3";
		}
		return "frontstage/onlineChange/reward/return-bonus-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransRewardDetail")
	public String getTransRewardDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransRewardVo transRewardVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transRewardVo = transHistoryDetailList.get(0).getTransRewardVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransRewardDetail data", userId);
				transRewardVo = transRewardService.getTransRewardDetail(transNum);
			}
			
			addAttribute("transRewardVo", transRewardVo);
		} catch (Exception e) {
			logger.error("Unable to getTransRewardDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/reward/return-bonus-detail";
	}
}