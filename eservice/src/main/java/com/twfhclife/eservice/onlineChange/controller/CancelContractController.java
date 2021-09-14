package com.twfhclife.eservice.onlineChange.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twfhclife.eservice.onlineChange.model.TransCancelContractVo;
import com.twfhclife.eservice.onlineChange.service.ITransCancelContractService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 線上申請-解約(保單為多選)
 */
@Controller
public class CancelContractController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(CancelContractController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransCancelContractService transCancelContractService;
	
	@Autowired
	private TransAddClient transAddClient;

	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/cancelContract1")
	public String cancelContract1() {
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
						userId, TransTypeUtil.CANCEL_CONTRACT_PARAMETER_CODE);
				transCancelContractService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CANCEL_CONTRACT_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from cancelContract1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cancelContract/cancel-contract1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transCancelContractVo transCancelContractVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/cancelContract2")
	public String cancelContract2(TransCancelContractVo transCancelContractVo) {
		try {
			addAttribute("proposer", getProposerName());
			addAttribute("transCancelContractVo", transCancelContractVo);
		} catch (Exception e) {
			logger.error("Unable to init from cancelContract2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cancelContract/cancel-contract2";
	}

	/**
	 * 步驟3(確認資料).
	 * 
	 * @param transCancelContractVo transCancelContractVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/cancelContract3")
	public String cancelContract3(TransCancelContractVo transCancelContractVo) {
		try {
			addAttribute("proposer", getProposerName());
			addAttribute("transCancelContractVo", transCancelContractVo);
		} catch (Exception e) {
			logger.error("Unable to init from cancelContract3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/cancelContract/cancel-contract3";
	}
	
	/**
	 * 下載解約申請書.
	 * 
	 * @param transCancelContractVo TransCancelContractVo
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/downloadCancelContractPDF")
	public @ResponseBody HttpEntity<byte[]> downloadCancelContractPDF(TransCancelContractVo transCancelContractVo) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			if ("member".equals(getUserType())) {
				transCancelContractVo.setMobile(getUserDetail().getMobile());
				
				boolean isTransApplyed = false;
				List<String> policyNos = transCancelContractVo.getPolicyNoList();
				for (String policyNo : policyNos) {
					boolean applyed = transService.isTransApplyed(policyNo,
							TransTypeUtil.CANCEL_CONTRACT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_COMPLETE);
					if (applyed) {
						isTransApplyed = true;
						break;
					}
				}
				
				// 沒有申請過才新增
				if (!isTransApplyed) {
					// 設定使用者
					String userId = getUserId();
					transCancelContractVo.setUserId(userId);
					
					// Call api 送出線上申請資料
					logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
					
					String transAddResult = "";
					TransAddRequest apiReq = new TransAddRequest();
					apiReq.setSysId(ApConstants.SYSTEM_ID);
					apiReq.setTransType(TransTypeUtil.CANCEL_CONTRACT_PARAMETER_CODE);
					apiReq.setTransCancelContractVo(transCancelContractVo);
					apiReq.setUserId(userId);
					
					TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
					if (transAddResponse != null) {
						logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
								MyJacksonUtil.object2Json(transAddResponse));
						transAddResult = transAddResponse.getTransResult();
					} else {
						// 若無資料，嘗試由內部服務取得資料
						logger.info("Call internal service to get user[{}] insertTransCancelContract data", userId);
						
						// 設定交易序號
						String transNum = transService.getTransNum();
						transCancelContractVo.setTransNum(transNum);
						
						int result = transCancelContractService.insertTransCancelContract(transCancelContractVo);
						if (result <= 0) {
							transAddResult = ReturnHeader.FAIL_CODE;
						} else {
							transAddResult = ReturnHeader.SUCCESS_CODE;
						}
					}
					
					if (transAddResult.equals(ReturnHeader.FAIL_CODE)) {
						logger.warn("Unable to insert trans record for cancelContract");
					}
				}
			}
			
			String fileName = "inline; filename=解約申請書.pdf";
			document = transCancelContractService.getPDF(transCancelContractVo);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);
		} catch (Exception e) {
			logger.error("Unable to get data from downloadCancelContractPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}
}