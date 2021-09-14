package com.twfhclife.eservice.onlineChange.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.onlineChange.service.ITransPaymodeService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
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
 * 線上申請-變更繳別(保單為單選)
 */
@Controller
public class PayModeController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(PayModeController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransPaymodeService transPaymodeService;
	
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
	@GetMapping("/paymentMode1")
	public String paymentMode1() {
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
						userId, TransTypeUtil.PAYMODE_PARAMETER_CODE);
				transPaymodeService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.PAYMODE_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentMode1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "464");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/payMode/payment-mode1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMode2")
	public String paymentMode2(TransPaymodeVo transPaymodeVo) {
		try {
			Integer lipmTredTms = transPaymodeService
					.getPolicyPayMethodChange(transPaymodeVo.getPolicyNoList().get(0));
			String paymodeOld = transPaymodeVo.getPaymodeOld();
			boolean paymodeA = true;//年繳
			boolean paymodeS = true;//半年繳
			boolean paymodeQ = true;//季繳
			if(lipmTredTms != null) {
				switch(paymodeOld) {
				case "M":// 月繳可變更
					paymodeA = lipmTredTms %12 == 0;
					paymodeS = lipmTredTms %6 == 0;
					paymodeQ = lipmTredTms %3 == 0;
					break;
				case "Q": // 季繳可變更
					paymodeA = lipmTredTms %4 == 0;
					paymodeS = lipmTredTms %2 == 0;
					break;
				case "S": // 半年繳可變更
					paymodeA = lipmTredTms %2 == 0;
					break;
				}
			}
			Map<String, Boolean> mapPaymode = new HashMap<String, Boolean>();
			mapPaymode.put("A", paymodeA);
			mapPaymode.put("S", paymodeS);
			mapPaymode.put("Q", paymodeQ);
			addAttribute("paymodeCanChange", mapPaymode);
			addAttribute("transPaymodeVo", transPaymodeVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentMode2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/payMode/payment-mode2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMode3")
	public String paymentMode3(TransPaymodeVo transPaymodeVo) {
		try {
			// 發送驗證碼
			sendAuthCode("payMode");
			addAttribute("transPaymodeVo", transPaymodeVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentMode3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/payMode/payment-mode3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentModeSuccess")
	public String paymentModeSuccess(TransPaymodeVo transPaymodeVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transPaymodeVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.PAYMODE_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transPaymodeVo.getAuthenticationNum();
				String msg = checkAuthCode("payMode", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:paymentMode3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transPaymodeVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.PAYMODE_PARAMETER_CODE);
				apiReq.setTransPaymodeVo(transPaymodeVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransPaymode data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transPaymodeVo.setTransNum(transNum);
					
					int result = transPaymodeService.insertTransPaymode(transPaymodeVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:paymentMode3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentModeSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:paymentMode3";
		}
		return "frontstage/onlineChange/payMode/payment-mode-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransPaymodeDetail")
	public String getTransPaymodeDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransPaymodeVo transPaymodeVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] transHistoryDetailResponse data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transPaymodeVo = transHistoryDetailList.get(0).getTransPaymodeVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransPaymodeDetail data", userId);
				transPaymodeVo = transPaymodeService.getTransPaymodeDetail(transNum);
			}
			
			addAttribute("transPaymodeVo", transPaymodeVo);
		} catch (Exception e) {
			logger.error("Unable to getTransPaymodeDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/payMode/payment-mode-detail";
	}
}