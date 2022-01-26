package com.twfhclife.eservice.onlineChange.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ITransBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.model.ParameterVo;
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
 * 線上申請-變更受益人(保單為單選)
 */
@Controller
public class BeneficiaryController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(BeneficiaryController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransBeneficiaryService transBeneficiaryService;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;
	
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
	@GetMapping("/beneficiary1")
	public String beneficiary1() {
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
						getUserId(), TransTypeUtil.BENEFICIARY_PARAMETER_CODE);
				transBeneficiaryService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.BENEFICIARY_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from beneficiary1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "470");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/beneficiary/beneficiary1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/beneficiary2")
	public String beneficiary2(TransBeneficiaryVo transBeneficiaryVo) {
		try {
			List<String> policyNos = transBeneficiaryVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				
				//受益人類型下拉
				List<ParameterVo> beneficiaryTypeList = new LinkedList<>();
				List<BeneficiaryVo> userBeneficiaryList = beneficiaryService.getBeneficiaryByPolicyNo(policyNo);
				List<ParameterVo> beniCodeParameterList = getParameterList(OnlineChangeUtil.BENEFICIARY_TYPE_PARAMETER_CATEGORY_CODE);
				List<String> userBeniCodeList = new LinkedList<>();
				if (!CollectionUtils.isEmpty(userBeneficiaryList)) {
					for (BeneficiaryVo userBeniVo : userBeneficiaryList) {
						String beniCode = "";
						if (userBeniVo.getBeneficiaryType() != null ) {
							beniCode = userBeniVo.getBeneficiaryType().toString();
						}
						if (!StringUtils.isEmpty(beniCode) && !userBeniCodeList.contains(beniCode)) {
							userBeniCodeList.add(beniCode);
						}
					}
				}
				
				VerifyPolicyResult verifyPolicyResult = transService.verifyPolicyRule(policyNo,
						TransTypeUtil.BENEFICIARY_PARAMETER_CODE);
				List<String> optionList = new ArrayList<String>();
				if(verifyPolicyResult != null && verifyPolicyResult.getOptiionList() != null) {
					/* 20180904 修改成依照 Procedure 回覆設定受益人類型 */
					for(String option: verifyPolicyResult.getOptiionList()) {
						String[] splitOption = option.split("、");
						optionList.addAll(Arrays.asList(splitOption));
					}
				}
				
				for(ParameterVo parameterVo: beniCodeParameterList) {
					if(optionList.contains((parameterVo.getParameterValue()))) {
						beneficiaryTypeList.add(parameterVo);
					}
				}
				addAttribute("beneficiaryTypeList", beneficiaryTypeList);
			}
			
			// 與被保人關係下拉
			List<ParameterVo> beneficiaryRelationList = getParameterList(
					OnlineChangeUtil.BENEFICIARY_RELATION_PARAMETER_CATEGORY_CODE);
			addAttribute("beneficiaryRelationList", beneficiaryRelationList);
			addAttribute("transBeneficiaryVo", transBeneficiaryVo);
		} catch (Exception e) {
			logger.error("Unable to init from beneficiary2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/beneficiary/beneficiary2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/beneficiary3")
	public String beneficiary3(TransBeneficiaryVo transBeneficiaryVo) {
		try {
			List<TransBeneficiaryDtlVo> transBeneficiaryDtlList = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			transBeneficiaryDtlList = mapper.readValue(
					transBeneficiaryVo.getTransBeneficiaryDtlJsonData(),
					new TypeReference<List<TransBeneficiaryDtlVo>>() {});
			transBeneficiaryVo.setTransBeneficiaryDtlList(transBeneficiaryDtlList);
			
			// 發送驗證碼
			sendAuthCode("beneficiary");
			addAttribute("transBeneficiaryVo", transBeneficiaryVo);
		} catch (Exception e) {
			logger.error("Unable to init from beneficiary3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/beneficiary/beneficiary3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/beneficiarySuccess")
	public String beneficiarySuccess(TransBeneficiaryVo transBeneficiaryVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transBeneficiaryVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.BENEFICIARY_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transBeneficiaryVo.getAuthenticationNum();
				String msg = checkAuthCode("beneficiary", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:beneficiary3";
				}
				
				List<TransBeneficiaryDtlVo> transBeneficiaryDtlList = new ArrayList<>();
				ObjectMapper mapper = new ObjectMapper();
				transBeneficiaryDtlList = mapper.readValue(
						transBeneficiaryVo.getTransBeneficiaryDtlJsonData(),
						new TypeReference<List<TransBeneficiaryDtlVo>>() {});
				transBeneficiaryVo.setTransBeneficiaryDtlList(transBeneficiaryDtlList);
				
				// 設定使用者
				String userId = getUserId();
				transBeneficiaryVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.BENEFICIARY_PARAMETER_CODE);
				apiReq.setTransBeneficiaryVo(transBeneficiaryVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransBeneficiary data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transBeneficiaryVo.setTransNum(transNum);
					
					int result = transBeneficiaryService.insertTransBeneficiary(transBeneficiaryVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:beneficiary3";
				}
			}
			addAttribute("smsEmail",  hideEmail(getLoginUser().getEmail()));
		} catch (Exception e) {
			logger.error("Unable to init from beneficiarySuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:beneficiary3";
		}
		return "frontstage/onlineChange/beneficiary/beneficiary-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransBeneficiaryDetail")
	public String getTransBeneficiaryDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			List<TransBeneficiaryDtlVo> transBeneficiaryDtlList = null;
			List<TransBeneficiaryOldVo> transBeneficiaryOldList = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transBeneficiaryDtlList = transHistoryDetailList.get(0).getTransBeneficiaryDtlList();
					transBeneficiaryOldList = transHistoryDetailList.get(0).getTransBeneficiaryOldList();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransBeneficiaryDetail data", userId);
				BigDecimal beneficiaryId = transBeneficiaryService.getTransBeneficiaryId(transNum);
				transBeneficiaryDtlList = transBeneficiaryService.getTransBeneficiaryDtlDetail(beneficiaryId);
				transBeneficiaryOldList = transBeneficiaryService.getTransBeneficiaryOldDetail(beneficiaryId);
			}
			
			addAttribute("transBeneficiaryDtlList", transBeneficiaryDtlList);
			addAttribute("transBeneficiaryOldList", transBeneficiaryOldList);
		} catch (Exception e) {
			logger.error("Unable to getTransBeneficiaryDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/beneficiary/beneficiary-detail";
	}
}