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
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransRenewReduceVo;
import com.twfhclife.eservice.onlineChange.service.ITransRenewReduceService;
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
 * 線上申請-變更展期定期保險/減額繳清保險(保單為單選)
 */
@Controller
public class RenewReduceController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(RenewReduceController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransRenewReduceService transRenewReduceService;
	
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
	@GetMapping("/renew1")
	public String renew1() {
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
				List<PolicyListVo> handledPolicyList = transService.handlePolicyStatusExpiredLocked(policyList);
				transRenewReduceService.handlePolicyStatusLocked(handledPolicyList, userId);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.RENEW_PARAMETER_CODE);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.REDUCE_PARAMETER_CODE);
				//展期定期保險/減額繳清保險合併處理,其中一個可用就不擋
				for (PolicyListVo vo : handledPolicyList) {
					logger.debug("@renew1@, policyNo:{}, canRenew:{}, canReduce:{}", vo.getPolicyNo(), vo.getCanRenew(), vo.getCanReduce());
					if (vo.getApplyLockedFlag().equals("N") && vo.getCanRenew().equals("Y") || vo.getCanReduce().equals("Y")) {
						vo.setApplyLockedFlag("N");
						vo.setApplyLockedMsg(null);
					}
				}
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from renew1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "471");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/renewReduce/renew1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transRenewReduceVo TransRenewReduceVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/renew2")
	public String renew2(TransRenewReduceVo transRenewReduceVo) {
		try {
			List<String> policyNos = transRenewReduceVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				String renewApplyed = "";
				String reduceApplyed = "";
				
				List<PolicyListVo> handledPolicyList = new ArrayList<PolicyListVo>();
				PolicyListVo vo = new PolicyListVo();
				vo.setPolicyNo(policyNo);
				handledPolicyList.add(vo);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.RENEW_PARAMETER_CODE);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.REDUCE_PARAMETER_CODE);
				
				for (PolicyListVo po : handledPolicyList) {
					logger.debug("@renew2@, policyNo:{}, canRenew:{}, canReduce:{}", po.getPolicyNo(), po.getCanRenew(), po.getCanReduce());
				}
				
				//先disable不可用的選項
				if (vo.getCanRenew().equals("N")) {
					renewApplyed = TransTypeUtil.RENEW_PARAMETER_CODE;
				}
				if (vo.getCanReduce().equals("N")) {
					reduceApplyed = TransTypeUtil.REDUCE_PARAMETER_CODE;
				}
				
				//未被disable才檢查是否已經申請過
				if ("".equals(renewApplyed)) {
					// 找展期是否已經申請過
					boolean isRenewApplyed = transService.isTransApplyed(policyNo, TransTypeUtil.RENEW_PARAMETER_CODE,
							OnlineChangeUtil.TRANS_STATUS_PROCESSING);
					if (isRenewApplyed) {
						renewApplyed = TransTypeUtil.RENEW_PARAMETER_CODE;
					}
				}
				
				if ("".equals(reduceApplyed)) {
					// 找減額是否已經申請過
					boolean isReduceApplyed = transService.isTransApplyed(policyNo, TransTypeUtil.REDUCE_PARAMETER_CODE,
							OnlineChangeUtil.TRANS_STATUS_PROCESSING);
					if (isReduceApplyed) {
						reduceApplyed = TransTypeUtil.REDUCE_PARAMETER_CODE;
					}
				}
				
				logger.debug("{} renewApplyed: {}", policyNo, renewApplyed);
				logger.debug("{} reduceApplyed: {}", policyNo, reduceApplyed);
				addAttribute("renewApplyed", renewApplyed);
				addAttribute("reduceApplyed", reduceApplyed);
			}
			
			addAttribute("transRenewReduceVo", transRenewReduceVo);
		} catch (Exception e) {
			logger.error("Unable to init from renew2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/renewReduce/renew2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transRenewReduceVo TransRenewReduceVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/renew3")
	public String renew3(TransRenewReduceVo transRenewReduceVo) {
		try {
			// 發送驗證碼
			sendAuthCode("renewReduce");
			addAttribute("transRenewReduceVo", transRenewReduceVo);
		} catch (Exception e) {
			logger.error("Unable to init from renew3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/renewReduce/renew3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transRenewReduceVo TransRenewReduceVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/renewSuccess")
	public String renewSuccess(TransRenewReduceVo transRenewReduceVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transRenewReduceVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						transRenewReduceVo.getTransType(), OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transRenewReduceVo.getAuthenticationNum();
				String msg = checkAuthCode("renewReduce", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:renew3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transRenewReduceVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(transRenewReduceVo.getTransType());
				apiReq.setTransRenewReduceVo(transRenewReduceVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransRenewReduce data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transRenewReduceVo.setTransNum(transNum);
					
					int result = transRenewReduceService.insertTransRenewReduce(transRenewReduceVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:renew3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from renewSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:renew3";
		}
		return "frontstage/onlineChange/renewReduce/renew-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransRenewReduceDetail")
	public String getTransRenewReduceDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransRenewReduceVo transRenewReduceVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transRenewReduceVo = transHistoryDetailList.get(0).getTransRenewReduceVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransRenewReduceDetail data", userId);
				transRenewReduceVo = transRenewReduceService.getTransRenewReduceDetail(transNum);
			}
			
			addAttribute("transRenewReduceVo", transRenewReduceVo);
		} catch (Exception e) {
			logger.error("Unable to getTransRenewReduceDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/renewReduce/renew-detail";
	}
}