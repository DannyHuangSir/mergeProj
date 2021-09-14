package com.twfhclife.eservice.onlineChange.controller;

import java.math.BigDecimal;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyVo;
import com.twfhclife.eservice.onlineChange.service.ITransReducePolicyService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.ICoverageService;
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
 * 線上申請-減少保險金額(保單為單選)
 */
@Controller
public class ReducePolicyController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(ReducePolicyController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransReducePolicyService transReducePolicyService;
	
	@Autowired
	private ICoverageService coverageService;
	
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
	@GetMapping("/paymentReduce1")
	public String paymentReduce1() {
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
						userId, TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE);
				transReducePolicyService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentReduce1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "472");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/reducePolicy/payment-reduce1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentReduce2")
	public String paymentReduce2(TransReducePolicyVo transReducePolicyVo) {
		try {
			List<String> policyNos = transReducePolicyVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				
				CoverageVo coverageVo = new CoverageVo();
				coverageVo.setPolicyNo(policyNo);
				coverageVo.setLtStatus("12");
				
				List<CoverageVo> coverageVoList = coverageService.getCoverageList(coverageVo);
				addAttribute("coverageVoList", coverageVoList);
			}
			addAttribute("transReducePolicyVo", transReducePolicyVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentReduce2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/reducePolicy/payment-reduce2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentReduce3")
	public String paymentReduce3(TransReducePolicyVo transReducePolicyVo) {
		try {
			List<TransReducePolicyDtlVo> transReducePolicyDtlList = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			transReducePolicyDtlList = mapper.readValue(
					transReducePolicyVo.getTransReducePolicyDtlJsonData(),
					new TypeReference<List<TransReducePolicyDtlVo>>() {});
			transReducePolicyVo.setTransReducePolicyDtlList(transReducePolicyDtlList);
			
			// 發送驗證碼
			sendAuthCode("reducePolicy");
			addAttribute("transReducePolicyVo", transReducePolicyVo);
		} catch (Exception e) {
			logger.error("Unable to init from paymentReduce3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/reducePolicy/payment-reduce3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentReduceSuccess")
	public String paymentReduceSuccess(TransReducePolicyVo transReducePolicyVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transReducePolicyVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transReducePolicyVo.getAuthenticationNum();
				String msg = checkAuthCode("reducePolicy", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:paymentReduce3";
				}
				
				List<TransReducePolicyDtlVo> transReducePolicyDtlList = new ArrayList<>();
				ObjectMapper mapper = new ObjectMapper();
				transReducePolicyDtlList = mapper.readValue(
						transReducePolicyVo.getTransReducePolicyDtlJsonData(),
						new TypeReference<List<TransReducePolicyDtlVo>>() {});
				transReducePolicyVo.setTransReducePolicyDtlList(transReducePolicyDtlList);
				
				// 設定使用者
				String userId = getUserId();
				transReducePolicyVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE);
				apiReq.setTransReducePolicyVo(transReducePolicyVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransReducePolicy data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transReducePolicyVo.setTransNum(transNum);
					
					int result = transReducePolicyService.insertTransReducePolicy(transReducePolicyVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:paymentReduce3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentReduceSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:paymentReduce3";
		}
		return "frontstage/onlineChange/reducePolicy/payment-reduce-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransReducePolicyDetail")
	public String getTransReducePolicyDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			List<TransReducePolicyDtlVo> transReducePolicyDtlList = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transReducePolicyDtlList = transHistoryDetailList.get(0).getTransReducePolicyDtlList();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransReducePolicyDetail data", userId);
				BigDecimal transReducePolicyId = transReducePolicyService.getTransReducePolicyId(transNum);
				transReducePolicyDtlList = transReducePolicyService.getTransReducePolicyDtlDetail(transReducePolicyId);
			}
			
			addAttribute("transReducePolicyDtlList", transReducePolicyDtlList);
		} catch (Exception e) {
			logger.error("Unable to getTransReducePolicyDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/reducePolicy/payment-reduce-detail";
	}
}