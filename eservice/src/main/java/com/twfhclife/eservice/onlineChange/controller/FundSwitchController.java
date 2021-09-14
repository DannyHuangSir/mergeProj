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

import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransFundSwitchVo;
import com.twfhclife.eservice.onlineChange.service.ITransFundSwitchService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.FundSwitchVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.policy.service.IPortfolioService;
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
 * 線上申請-變更投資標的及配置比例
 */
@Controller
public class FundSwitchController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(FundSwitchController.class);
	
	@Autowired
	private IPolicyListService policyListService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransFundSwitchService transFundSwitchService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	@Autowired
	private IPortfolioService portfolioService;
	
	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/fundSwitch1")
	public String fundSwitch1() {
		try {
			if(!checkCanUseOnlineChange()) {
				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				addSystemError(message);
				return "redirect:apply1";
			}
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<PolicyListVo> policyList = policyListService.getInvtPolicyList(userRocId);
			
			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.FUND_SWITCH_PARAMETER_CODE);
				transFundSwitchService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.FUND_SWITCH_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from fundSwitch1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundSwitch/fund-switch1";
	}

	/**
	 * 步驟2(選擇轉出投資標的).
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/fundSwitch2")
	public String fundSwitch2(TransFundSwitchVo transFundSwitchVo) {
		try {
			List<String> policyNos = transFundSwitchVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				List<PortfolioVo> protfolioList = portfolioService.getPortfolioList(policyNo, null);
				transFundSwitchVo.setRiskLevel(protfolioList.get(0).getInvtRiskBeneLevel());
				transFundSwitchVo.setPolicyNoList(policyNos);
				addAttribute("protfolioList", protfolioList);
			}
			
		} catch (Exception e) {
			logger.error("Unable to init from fundSwitch2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundSwitch/fund-switch2";
	}

	/**
	 * 步驟3(選擇轉入投資標的).
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/fundSwitch3")
	public String fundSwitch3(TransFundSwitchVo transFundSwitchVo) {
		try {
			transFundSwitchService.convertFundSwitch(transFundSwitchVo);
			addAttribute("transFundSwitchVo", transFundSwitchVo);
			
			List<FundSwitchVo> fundValueList = transFundSwitchService.getFundValueOptionList(transFundSwitchVo.getPolicyNoList().get(0).substring(0,2));
			addAttribute("fundValueList", fundValueList);
		} catch (Exception e) {
			logger.error("Unable to init from fundSwitch3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundSwitch/fund-switch3";
	}

	/**
	 * 步驟4(確認資料及發送驗證碼).
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/fundSwitch4")
	public String fundSwitch4(TransFundSwitchVo transFundSwitchVo) {
		try {
			transFundSwitchService.convertFundSwitch(transFundSwitchVo);
			// 發送驗證碼
			sendAuthCode("fundSwitch");
			addAttribute("transFundSwitchVo", transFundSwitchVo);
		} catch (Exception e) {
			logger.error("Unable to init from fundSwitch4: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundSwitch/fund-switch4";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/fundSwitchSuccess")
	public String fundSwitchSuccess(TransFundSwitchVo transFundSwitchVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transFundSwitchVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.FUND_SWITCH_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transFundSwitchVo.getAuthenticationNum();
				String msg = checkAuthCode("fundSwitch", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:fund-switch4";
				}
				
				// 設定使用者
				String userId = getUserId();
				transFundSwitchVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				transFundSwitchService.convertFundSwitch(transFundSwitchVo);
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.FUND_SWITCH_PARAMETER_CODE);
				apiReq.setTransFundSwitchVo(transFundSwitchVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransFundSwitch data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transFundSwitchVo.setTransNum(transNum);
					
					int result = transFundSwitchService.insertTransFundSwitch(transFundSwitchVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:fund-switch4";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from fundSwitchSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:fund-switch4";
		}
		return "frontstage/onlineChange/fundSwitch/fund-switch-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransFundSwitchDetail")
	public String getTransFundSwitchDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransFundSwitchVo transFundSwitchVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] transHistoryDetailResponse data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transFundSwitchVo = transHistoryDetailList.get(0).getTransFundSwitchVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransFundSwitchDetail data", userId);
				transFundSwitchVo = transFundSwitchService.getTransFundSwitchDetail(transNum);
			}
			
			addAttribute("transFundSwitchVo", transFundSwitchVo);
		} catch (Exception e) {
			logger.error("Unable to getTransFundSwitchDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundSwitch/fund-switch-detail";
	}	
}