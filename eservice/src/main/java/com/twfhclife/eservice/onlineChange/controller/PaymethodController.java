package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.twfhclife.eservice.onlineChange.model.TransPaymethodVo;
import com.twfhclife.eservice.onlineChange.service.ITransPaymethodService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
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
 * 線上申請-變更收費管道(保單為多選)
 */
@Controller
public class PaymethodController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(PaymethodController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransPaymethodService transPaymethodService;
	
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
	@GetMapping("/paymentMethod1")
	public String paymentMethod1() {
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
						userId, TransTypeUtil.PAYMETHOD_PARAMETER_CODE);
				transPaymethodService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.PAYMETHOD_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentMethod1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "482");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/paymethod/payment-method1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMethod2")
	public String paymentMethod2(TransPaymethodVo transPaymethodVo) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int year = cal.get(Calendar.YEAR);
			
			List<String> yearList = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				yearList.add(String.valueOf(year + i).substring(2));
			}
			
			List<String> monthList = new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				monthList.add(StringUtils.leftPad(i + "", 2, "0"));
			}
			addAttribute("yearList", yearList);
			addAttribute("monthList", monthList);
			addAttribute("transPaymethodVo", transPaymethodVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentMethod2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/paymethod/payment-method2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMethod3")
	public String paymentMethod3(TransPaymethodVo transPaymethodVo) {
		try {
			// 發送驗證碼
			sendAuthCode("paymentMethod");
			addAttribute("transPaymethodVo", transPaymethodVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentMethod3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/paymethod/payment-method3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMethodSuccess")
	public String paymentMethodSuccess(TransPaymethodVo transPaymethodVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transPaymethodVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.PAYMETHOD_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transPaymethodVo.getAuthenticationNum();
				String msg = checkAuthCode("paymentMethod", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:paymentMethod3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transPaymethodVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.PAYMETHOD_PARAMETER_CODE);
				apiReq.setTransPaymethodVo(transPaymethodVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransPaymethod data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transPaymethodVo.setTransNum(transNum);
					
					int result = transPaymethodService.insertTransPaymethod(transPaymethodVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:paymentMethod3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentMethodSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:paymentMethod3";
		}
		return "frontstage/onlineChange/paymethod/payment-method-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransPaymethodDetail")
	public String getTransPaymethodDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransPaymethodVo transPaymethodVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transPaymethodVo = transHistoryDetailList.get(0).getTransPaymethodVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransPaymethodDetail data", userId);
				transPaymethodVo = transPaymethodService.getTransPaymethodDetail(transNum);
			}
			
			addAttribute("transPaymethodVo", transPaymethodVo);
		} catch (Exception e) {
			logger.error("Unable to getTransPaymethodDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/paymethod/payment-method-detail";
	}
}