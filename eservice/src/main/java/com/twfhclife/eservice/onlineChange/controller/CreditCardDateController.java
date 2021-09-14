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

import com.twfhclife.eservice.onlineChange.model.TransCreditCardDateVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.service.ITransCreditCardDateService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyPayerVo;
import com.twfhclife.eservice.policy.service.IPolicyPayerService;
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
 * 線上申請-變更信用卡效期(保單為單選)
 */
@Controller
public class CreditCardDateController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(CreditCardDateController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransCreditCardDateService transCreditCardDateService;
	
	@Autowired
	private IPolicyPayerService policyPayerService;
	
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
	@GetMapping("/paymentCardDate1")
	public String paymentCardDate1() {
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
						userId, TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE);
				transCreditCardDateService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentCardDate1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "483");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/creditCardDate/payment-card-date1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentCardDate2")
	public String paymentCardDate2(TransCreditCardDateVo transCreditCardDateVo) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int year = cal.get(Calendar.YEAR);
			
			List<String> yearList = new ArrayList<>();
			for (int i = 0; i < 11; i++) {//Modify.20201120.選項列出十年
				yearList.add(String.valueOf(year + i).substring(2));
			}
			
			List<String> monthList = new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				monthList.add(StringUtils.leftPad(i + "", 2, "0"));
			}
			
			// 找出舊的信用卡資料
			List<String> policyNos = transCreditCardDateVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				PolicyPayerVo payerVo = policyPayerService.getPolicyPayer(policyNo);
				if (payerVo != null) {
					String expireDate = payerVo.getExpireDate();
					if (!StringUtils.isEmpty(expireDate) && expireDate.length() > 2) {
						transCreditCardDateVo.setValidYyOld(expireDate.substring(2));
						transCreditCardDateVo.setValidMmOld(expireDate.substring(0, 2));
					}
					addAttribute("policyPayerVo", payerVo);
				}
			}
			
			addAttribute("yearList", yearList);
			addAttribute("monthList", monthList);
			addAttribute("transCreditCardDateVo", transCreditCardDateVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentCardDate2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/creditCardDate/payment-card-date2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentCardDate3")
	public String paymentCardDate3(TransCreditCardDateVo transCreditCardDateVo) {
		try {
			// 找出舊的信用卡資料
			List<String> policyNos = transCreditCardDateVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				PolicyPayerVo payerVo = policyPayerService.getPolicyPayer(policyNo);
				if (payerVo != null) {
					addAttribute("policyPayerVo", payerVo);
				}
			}
			
			// 發送驗證碼
			sendAuthCode("creditCardDate");
			addAttribute("transCreditCardDateVo", transCreditCardDateVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentCardDate3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/creditCardDate/payment-card-date3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentCardDateSuccess")
	public String paymentCardDateSuccess(TransCreditCardDateVo transCreditCardDateVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transCreditCardDateVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transCreditCardDateVo.getAuthenticationNum();
				String msg = checkAuthCode("creditCardDate", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:paymentCardDate3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transCreditCardDateVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE);
				apiReq.setTransCreditCardDateVo(transCreditCardDateVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransCreditCardDate data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transCreditCardDateVo.setTransNum(transNum);
					
					int result = transCreditCardDateService.insertTransCreditCardDate(transCreditCardDateVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:paymentCardDate3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentCardDateSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:paymentCardDate3";
		}
		return "frontstage/onlineChange/creditCardDate/payment-card-date-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransCreditCardDateDetail")
	public String getTransCreditCardDateDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransCreditCardDateVo transCreditCardDateVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transCreditCardDateVo = transHistoryDetailList.get(0).getTransCreditCardDateVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransCreditCardDateDetail data", userId);
				transCreditCardDateVo = transCreditCardDateService.getTransCreditCardDateDetail(transNum);
			}
			
			addAttribute("transCreditCardDateVo", transCreditCardDateVo);
		} catch (Exception e) {
			logger.error("Unable to getTransCreditCardDateDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/creditCardDate/payment-card-date-detail";
	}
}