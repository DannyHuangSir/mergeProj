package com.twfhclife.eservice.onlineChange.controller;

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
import com.twfhclife.eservice.onlineChange.model.TransCushionVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.service.ITransCushionService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
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
 * 線上申請-自動墊繳選擇權(保單為單選)
 */
@Controller
public class CushionController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(CushionController.class);
	
	@Autowired
	private IPolicyExtraService policyExtraService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransCushionService transCushionService;
	
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
	@GetMapping("/policyAutopay1")
	public String policyAutopay1() {
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
						userId, TransTypeUtil.CUSHION_PARAMETER_CODE);
				transCushionService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CUSHION_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from policyAutopay1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "469");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/cushion/policy-autopay1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/policyAutopay2")
	public String policyAutopay2(TransCushionVo transCushionVo) {
		try {
			List<String> policyNos = transCushionVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);

				// 找出舊自動墊繳值
				PolicyExtraVo policyExtraVo = policyExtraService.findByPolicyNo(policyNo);
				String cushionModeOld = "N";
				if (policyExtraVo != null) {
					cushionModeOld = "".equals(StringUtils.trimToEmpty(policyExtraVo.getAutoRcpMk())) ? "N" : policyExtraVo.getAutoRcpMk();
				}
				transCushionVo.setCushionModeOld(cushionModeOld);
			}
			
			addAttribute("transCushionVo", transCushionVo);
		} catch (Exception e) {
			logger.error("Unable to init from policyAutopay2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cushion/policy-autopay2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/policyAutopay3")
	public String policyAutopay3(TransCushionVo transCushionVo) {
		try {
			// 發送驗證碼
			sendAuthCode("cushionMode");
			addAttribute("transCushionVo", transCushionVo);
		} catch (Exception e) {
			logger.error("Unable to init from policyAutopay3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cushion/policy-autopay3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/policyAutopaySuccess")
	public String policyAutopaySuccess(TransCushionVo transCushionVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transCushionVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.CUSHION_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transCushionVo.getAuthenticationNum();
				String msg = checkAuthCode("cushionMode", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:policyAutopay3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transCushionVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.CUSHION_PARAMETER_CODE);
				apiReq.setTransCushionVo(transCushionVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransCushion data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transCushionVo.setTransNum(transNum);
					
					int result = transCushionService.insertTransCushion(transCushionVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:policyAutopay3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from policyAutopaySuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:policyAutopay3";
		}
		return "frontstage/onlineChange/cushion/policy-autopay-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransCushionDetail")
	public String getTransCushionDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransCushionVo transCushionVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transCushionVo = transHistoryDetailList.get(0).getTransCushionVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransCushionDetail data", userId);
				transCushionVo = transCushionService.getTransCushionDetail(transNum);
			}
			
			addAttribute("transCushionVo", transCushionVo);
		} catch (Exception e) {
			logger.error("Unable to getTransCushionDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cushion/policy-autopay-detail";
	}
}