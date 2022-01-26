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

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.service.IOnlineChangeService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.ParameterVo;
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
 * 線上申請-變更風險屬性
 */
@Controller
public class RiskLevelController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(RiskLevelController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private ITransRiskLevelService transRiskLevelService;
	
	@Autowired
	private IPortfolioService portfolioService;
	
	private void prepaidPage() {
		Map<String, ParameterVo> parameterList = getParameterMap("PAGE_WORDING");
		Map<String, String> map = new HashMap<String, String>();
		for(String key : parameterList.keySet()){ 
			if(key.substring(0, 9).equals(OnlineChangeUtil.ONLINE_CHANGE_HOME_PARAMETER_CATEGORY_CODE)){
				map.put(key, parameterList.get(key).getParameterValue());
			}
		}
		addAttribute("onlineChangeHome", map);
		addAttribute("canUseFlag", getUserDetail().getOnlineFlag());
		addAttribute("onlinechangeEnableEntry", onlinechangeEnableEntry);
	}
	
	/**
	 * 投資型保險商品保戶風險適性問卷頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/riskLevel1")
	public String riskLevel1() {
		try {
			String userId = getUserId();
			List<TransVo> transHistoryList = onlineChangeService.getTransByUserId(userId, null, TransTypeUtil.RISK_LEVEL_PARAMETER_CODE, null, null, null, 1, 1);
			for (TransVo vo : transHistoryList) {
				if (vo.getStatus().equals(OnlineChangeUtil.TRANS_STATUS_PROCESSING)
						|| vo.getStatus().equals(OnlineChangeUtil.TRANS_STATUS_AUDITED)
						|| vo.getStatus().equals(OnlineChangeUtil.TRANS_STATUS_UPLOADED)) {
					addAttribute("errorMessage", OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
					prepaidPage();
					return "frontstage/onlineChange/apply1";
				}
			}
			
			boolean hasVul = false;
			String userRocId = getUserRocId();
			List<PolicyListVo> policyList = getUserOnlineChangePolicyList(userId, userRocId);
			for (PolicyListVo pol : policyList) {
				if ("UC,UH,UR,UQ,UP,US".contains(pol.getPolicyType())) {
					hasVul = true;
					break;
				}
			}
			if (!hasVul) {
				addAttribute("errorMessage", OnlineChangMsgUtil.NO_VUL_POLICY_MSG);
				prepaidPage();
				return "frontstage/onlineChange/apply1";
			}
			
		} catch (Exception e) {
			logger.error("Unable to init from riskLevel1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/riskLevel/risk-level1";
	}

	/**
	 * 步驟2(確認資料及發送驗證碼).
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/riskLevel2")
	public String riskLevel2(TransRiskLevelVo transRiskLevelVo) {
		try {
			if(transRiskLevelVo.getRiskScore() <= 20) {
				transRiskLevelVo.setRiskLevelDesc("保守型");
				transRiskLevelVo.setRiskLevelNew("RR1");
	        } else if (transRiskLevelVo.getRiskScore() >= 50) {
	        	transRiskLevelVo.setRiskLevelDesc("積極型");
				transRiskLevelVo.setRiskLevelNew("RR5");
	        } else {
	        	transRiskLevelVo.setRiskLevelDesc("穩健型");
				transRiskLevelVo.setRiskLevelNew("RR3");
	        }
			addAttribute("transRiskLevelVo", transRiskLevelVo);
			// 發送驗證碼
			sendAuthCode("riskLevel");
		} catch (Exception e) {
			logger.error("Unable to init from riskLevel2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/riskLevel/risk-level2";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param TransRiskLevelVo transRiskLevelVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/riskLevelSuccess")
	public String riskLevelSuccess(TransRiskLevelVo transRiskLevelVo) {
		try {
			// 驗證驗證碼
			String authNum = transRiskLevelVo.getAuthenticationNum();
			String msg = checkAuthCode("riskLevel", authNum);
			if (!StringUtils.isEmpty(msg)) {
				addSystemError(msg);
				return "forward:riskLevel2";
			}
			
			// 設定使用者
			String userId = getUserId();
			transRiskLevelVo.setUserId(userId);
			String userRocId = getUserRocId();
			
//			// 設定舊的風險屬性
			List<PolicyListVo> policyList = getUserOnlineChangePolicyList(userId, userRocId);
			for (PolicyListVo pol : policyList) {
				if ("UC,UH,UR,UQ,UP,US".contains(pol.getPolicyType())) {
					String riskLevelOld = transRiskLevelService.getRiskLevelCode(pol.getPolicyNo());
					logger.info("policyNo:{}, riskLevelOld:{}", pol.getPolicyNo(), riskLevelOld);
					if (!"".equals(StringUtils.trimToEmpty(riskLevelOld))) {
						transRiskLevelVo.setRiskLevelOld(riskLevelOld);
						break;
					}
				}
			}
			
			// Call api 送出線上申請資料
			logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
			
			String transAddResult = "";
			TransAddRequest apiReq = new TransAddRequest();
			apiReq.setSysId(ApConstants.SYSTEM_ID);
			apiReq.setTransType(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
			apiReq.setTransRiskLevelVo(transRiskLevelVo);
			apiReq.setUserId(userId);
			
			TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
			if (transAddResponse != null) {
				logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
						MyJacksonUtil.object2Json(transAddResponse));
				transAddResult = transAddResponse.getTransResult();
			} else {
				// 若無資料，嘗試由內部服務取得資料
				logger.info("Call internal service to get user[{}] insertTransRiskLevel data", userId);
				
				// 設定交易序號
				String transNum = transService.getTransNum();
				transRiskLevelVo.setTransNum(transNum);
				
				int result = transRiskLevelService.insertTransRiskLevel(transRiskLevelVo);
				if (result <= 0) {
					transAddResult = ReturnHeader.FAIL_CODE;
				} else {
					transAddResult = ReturnHeader.SUCCESS_CODE;
				}
			}
			
			if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
				addDefaultSystemError();
				return "forward:riskLevel2";
			}
		} catch (Exception e) {
			logger.error("Unable to init from riskLevelSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:riskLevel2";
		}
		return "frontstage/onlineChange/riskLevel/risk-level-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransRiskLevelDetail")
	public String getTransRiskLevelDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransRiskLevelVo transRiskLevelVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transRiskLevelVo = transHistoryDetailList.get(0).getTransRiskLevelVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransRiskLevelDetail data", userId);
				transRiskLevelVo = transRiskLevelService.getTransRiskLevelDetail(transNum);
			}
			
			addAttribute("transRiskLevelVo", transRiskLevelVo);
		} catch (Exception e) {
			logger.error("Unable to getRiskLevelDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/riskLevel/risk-level-detail";
	}
}