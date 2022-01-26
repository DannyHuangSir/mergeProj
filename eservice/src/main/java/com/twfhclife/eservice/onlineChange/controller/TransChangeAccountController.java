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
import com.twfhclife.eservice.onlineChange.model.TransChangeAccountVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ILitracEsService;
import com.twfhclife.eservice.onlineChange.service.ITransChangeAccountService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyBonusVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
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
 * 線上申請-匯款帳號變更(保單為單選).
 * 
 * @author all
 */
@Controller
public class TransChangeAccountController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(TransChangeAccountController.class);
	
	@Autowired
	private ILitracEsService litracEsService;
	
	@Autowired
	private ITransService transService;

	@Autowired
	private ITransChangeAccountService transChangeAccountService;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;
	
	@Autowired
	private IPolicyBonusService policyBonusService;
	
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
	@GetMapping("/changePayAcct1")
	public String changePayAcct1() {
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
						userId, TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE);
				transChangeAccountService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from changePayAcct1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "489");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/changePayAcct/change-pay-acct1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changePayAcct2")
	public String changePayAcct2(TransChangeAccountVo transChangeAccountVo) {
		try {
			String canChangeType1 = "N";
			String canChangeType2 = "N";
			String canChangeType3 = "N";
			String[] payAccountName = new String[3];
			
			List<String> policyNoList = transChangeAccountVo.getPolicyNoList();
			if (policyNoList != null && policyNoList.size() == 1) {
				String policyNo = policyNoList.get(0);
				List<BeneficiaryVo> beneficiaryVoList = beneficiaryService.getBeneficiaryByPolicyNo(policyNo);
				for(BeneficiaryVo vo: beneficiaryVoList) {
					// 戶名、證號有一空白則不能申請
					boolean userDataComplete = true;
//					if(StringUtils.isBlank(vo.getBeneficiaryName())
//							|| StringUtils.isBlank(vo.getBeneficiaryRocid())) {
//						userDataComplete = false;
//					}
					if(userDataComplete && "01".equals(String.format("%02d", vo.getBeneficiaryType()))) {
						canChangeType1 = "Y";
						payAccountName[0] = vo.getBeneficiaryName();
						continue;
					}
					if(userDataComplete && "05".equals(String.format("%02d", vo.getBeneficiaryType()))) {
						canChangeType2 = "Y";
						payAccountName[1] = vo.getBeneficiaryName();
						continue;
					}
				}
				List<PolicyBonusVo> policyBonusVoList = policyBonusService.findByPolicyNo(policyNo);
				for(PolicyBonusVo vo: policyBonusVoList) {
					// 戶名、證號有一空白則不能申請
					boolean userDataComplete = true;
//					if(StringUtils.isEmpty(vo.getIndividualVo().getName())
//							|| StringUtils.isEmpty(vo.getIndividualVo().getRocId())) {
//						userDataComplete = false;
//					}
					if(userDataComplete && "1".equals(vo.getTakeCode())) {
						canChangeType3 = "Y";
						payAccountName[2] = vo.getIndividualVo().getName();
						continue;
					}
				}
			}
			
			addAttribute("canChangeType1", canChangeType1);
			addAttribute("canChangeType2", canChangeType2);
			addAttribute("canChangeType3", canChangeType3);
			addAttribute("payAccountName", payAccountName);
			addAttribute(ApConstants.TRANS_CHANGE_ACCT_VO, transChangeAccountVo);
		} catch (Exception e) {
			logger.error("Unable to init from changePayAcct2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changePayAcct/change-pay-acct2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changePayAcct3")
	public String changePayAcct3(TransChangeAccountVo transChangeAccountVo) {
		try {
			// 發送驗證碼
			sendAuthCode("changePayAcct");
			addAttribute(ApConstants.TRANS_CHANGE_ACCT_VO, transChangeAccountVo);
		} catch (Exception e) {
			logger.error("Unable to init from changePayAcct3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changePayAcct/change-pay-acct3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changePayAcctSuccess")
	public String changePayAcctSuccess(TransChangeAccountVo transChangeAccountVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transChangeAccountVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transChangeAccountVo.getAuthenticationNum();
				String msg = checkAuthCode("changePayAcct", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:changePayAcct3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transChangeAccountVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE);
				apiReq.setTransChangeAccountVo(transChangeAccountVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransChangeAccount data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transChangeAccountVo.setTransNum(transNum);
					
					int result = transChangeAccountService.insertTransChangeAccount(transChangeAccountVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:changePayAcct3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from changePayAcctSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:changePayAcct3";
		}
		return "frontstage/onlineChange/changePayAcct/change-pay-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransChangeAccountDetail")
	public String getTransChangeAccountDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransChangeAccountVo transChangeAccountVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transChangeAccountVo = transHistoryDetailList.get(0).getTransChangeAccountVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransChangeAccountDetail data", userId);
				transChangeAccountVo = transChangeAccountService.getTransChangeAccountDetail(transNum);
			}
			
			addAttribute("transChangeAccountVo", transChangeAccountVo);
		} catch (Exception e) {
			logger.error("Unable to getTransChangeAccountDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changePayAcct/change-pay-detail";
	}
}