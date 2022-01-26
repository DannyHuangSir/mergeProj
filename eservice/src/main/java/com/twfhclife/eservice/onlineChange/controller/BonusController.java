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

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransBounsVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.onlineChange.service.ITransBounsService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;
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
 * 線上申請-變更紅利選擇權(保單為單選)
 */
@Controller
public class BonusController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(BonusController.class);
	
	@Autowired
	private IPolicyBonusService policyBonusService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransBounsService transBounsService;
	
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
	@GetMapping("/chooseBonus1")
	public String chooseBonus1() {
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
						userId, TransTypeUtil.BONUS_PARAMETER_CODE);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.BONUS_PARAMETER_CODE);
				transBounsService.handlePolicyStatusLocked(handledPolicyList);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from chooseBonus1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "467");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/bonus/choose-bonus1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/chooseBonus2")
	public String chooseBonus2(TransBounsVo transBounsVo) {
		try {
			List<String> policyNos = transBounsVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);

				// 取得可以變更的紅利給付方式
				VerifyPolicyResult verifyPolicyResult = transService.verifyPolicyRule(policyNo,
						TransTypeUtil.BONUS_PARAMETER_CODE);
				List<String> optionList = null;
				if (verifyPolicyResult != null && verifyPolicyResult.getOptiionList() != null) {
					optionList = new ArrayList<>();
					for(String option: verifyPolicyResult.getOptiionList()) {
						String[] splitOption = option.split("、");
						optionList.addAll(Arrays.asList(splitOption));
					}
				}
				addAttribute("bonusOptionList", optionList);

				// 找出舊的紅利給付方式
				String bounsModeOld = policyBonusService.getBounsTake(policyNo);
				if (!StringUtils.isEmpty(bounsModeOld)) {
					transBounsVo.setBounsModeOld(bounsModeOld);
				}
			}
			
			addAttribute("transBounsVo", transBounsVo);
		} catch (Exception e) {
			logger.error("Unable to init from chooseBonus2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/bonus/choose-bonus2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/chooseBonus3")
	public String chooseBonus3(TransBounsVo transBounsVo) {
		try {
			// 發送驗證碼
			sendAuthCode("bounsMode");
			addAttribute("transBounsVo", transBounsVo);
		} catch (Exception e) {
			logger.error("Unable to init from chooseBonus3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/bonus/choose-bonus3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/chooseBonusSuccess")
	public String chooseBonusSuccess(TransBounsVo transBounsVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transBounsVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.BONUS_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transBounsVo.getAuthenticationNum();
				String msg = checkAuthCode("bounsMode", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:chooseBonus3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transBounsVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.BONUS_PARAMETER_CODE);
				apiReq.setTransBounsVo(transBounsVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransBouns data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transBounsVo.setTransNum(transNum);
					
					int result = transBounsService.insertTransBouns(transBounsVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:chooseBonus3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from chooseBonusSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:chooseBonus3";
		}
		return "frontstage/onlineChange/bonus/choose-bonus-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransBounsDetail")
	public String getTransBounsDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransBounsVo transBounsVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transBounsVo = transHistoryDetailList.get(0).getTransBounsVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransBounsDetail data", userId);
				transBounsVo = transBounsService.getTransBounsDetail(transNum);
			}
			
			addAttribute("transBounsVo", transBounsVo);
		} catch (Exception e) {
			logger.error("Unable to getTransBounsDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/bonus/choose-bonus-detail";
	}
}