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
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransValuePrintVo;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.service.ITransValuePrintService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
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
 * 線上申請-保單價值列印(保單為多選)
 */
@Controller
public class TransValuePrintController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(TransValuePrintController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransValuePrintService transValuePrintService;
	
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
	@GetMapping("/valuePrint1")
	public String valuePrint1() {
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
						userId, TransTypeUtil.VALUE_PRINT_PARAMETER_CODE);
				transValuePrintService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.VALUE_PRINT_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from valuePrint1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "475");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/valuePrint/value-print1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/valuePrint2")
	public String valuePrint2(TransValuePrintVo transValuePrintVo) {
		try {
			addAttribute("transValuePrintVo", transValuePrintVo);
		} catch (Exception e) {
			logger.error("Unable to init from valuePrint2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/valuePrint/value-print2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/valuePrint3")
	public String valuePrint3(TransValuePrintVo transValuePrintVo) {
		try {
			// 發送驗證碼
			sendAuthCode("valuePrint");
			addAttribute("transValuePrintVo", transValuePrintVo);
		} catch (Exception e) {
			logger.error("Unable to init from valuePrint3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/valuePrint/value-print3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/valuePrintSuccess")
	public String valuePrintSuccess(TransValuePrintVo transValuePrintVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transValuePrintVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.VALUE_PRINT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transValuePrintVo.getAuthenticationNum();
				String msg = checkAuthCode("valuePrint", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:valuePrint3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transValuePrintVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.VALUE_PRINT_PARAMETER_CODE);
				apiReq.setTransValuePrintVo(transValuePrintVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransValuePrint data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transValuePrintVo.setTransNum(transNum);
					
					int result = transValuePrintService.insertTransValuePrint(transValuePrintVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:valuePrint3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from valuePrintSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:valuePrint3";
		}
		return "frontstage/onlineChange/valuePrint/value-print-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransValuePrintDetail")
	public String getTransValuePrintDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransValuePrintVo transValuePrintVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transValuePrintVo = transHistoryDetailList.get(0).getTransValuePrintVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransValuePrintDetail data", userId);
				transValuePrintVo = transValuePrintService.getTransValuePrintDetail(transNum);
			}
			
			addAttribute("transValuePrintVo", transValuePrintVo);
		} catch (Exception e) {
			logger.error("Unable to getTransValuePrintDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/valuePrint/value-print-detail";
	}
}