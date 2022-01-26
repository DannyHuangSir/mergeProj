package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
import com.twfhclife.eservice.onlineChange.model.TransAnnuityMethodVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.onlineChange.service.ITransAnnuityMethodService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
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
 * 線上申請-變更年金給付方式(保單為單選)
 */
@Controller
public class AnnuityMethodController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(AnnuityMethodController.class);
	
	@Autowired
	private IPolicyExtraService policyExtraService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransAnnuityMethodService transAnnuityMethodService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient	functionUsageClient;

	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/annuityPayMethod1")
	public String annuityPayMethod1() {
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
						userId, TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE);
				transAnnuityMethodService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from annuityPayMethod1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "465");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/annuityMethod/annuity-pay_method1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/annuityPayMethod2")
	public String annuityPayMethod2(TransAnnuityMethodVo transAnnuityMethodVo) {
		try {
			List<String> policyNos = transAnnuityMethodVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				
				VerifyPolicyResult verifyPolicyResult = transService.verifyPolicyRule(policyNo,
						TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE);
				//年金給付方式
				List<String> optionList = null;
				if (verifyPolicyResult != null && verifyPolicyResult.getOptiionList() != null) {
					optionList = new ArrayList<>();
					for(String option: verifyPolicyResult.getOptiionList()) {
						String[] splitOption = option.split("、");
						optionList.addAll(Arrays.asList(splitOption));
					}
				}
				addAttribute("optionList", optionList);

				// 找出舊年金給付方式
				PolicyExtraVo policyExtraVo = policyExtraService.findByPolicyNo(policyNo);
				String annuityMethodOld = "";
				if (policyExtraVo != null) {
					annuityMethodOld = policyExtraVo.getMethAnnuPay();
				}
				transAnnuityMethodVo.setAnnuityMethodOld(annuityMethodOld);
				
				
				
				//取得保證期間parameter
				String policyType = policyNo.substring(0,2);//險種
				String strGPeriod = getParameterValue("ANNUITY_METHOD_GUARANTEE_PERIOD", policyType);
				List<String> listPeriod = null;
				if (strGPeriod!=null && !"".equals(strGPeriod.trim())) {//有設定保證期間的險種才要顯示選項
					listPeriod = new ArrayList<>();
					String[] splitPeriod = strGPeriod.trim().split(",");
					listPeriod.addAll(Arrays.asList(splitPeriod));
					addAttribute("listPeriod", listPeriod);
				}
				
				//有設定保證期間的險種才去找舊年金保證期間
				if(listPeriod!=null && !listPeriod.isEmpty()) {//有設定保證期間的險種才去找舊年金保證期間
					//select LIPI_GUAR_TERM FROM LILIPI_ES@db51 where lipi_insu_type||lipi_insu_grp_no||lipi_insu_seq_no = '保單號碼';
					String oldGuarTerm = transAnnuityMethodService.getOldGuarTerm(policyNo);
					if(oldGuarTerm==null || "".equals(oldGuarTerm.trim())) {
						transAnnuityMethodVo.setGuaranteePeriodOld("");
					}else {
						transAnnuityMethodVo.setGuaranteePeriodOld(oldGuarTerm);
					}
				}
				
			}
			addAttribute("transAnnuityMethodVo", transAnnuityMethodVo);
		} catch (Exception e) {
			logger.error("Unable to init from annuityPayMethod2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/annuityMethod/annuity-pay_method2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/annuityPayMethod3")
	public String annuityPayMethod3(TransAnnuityMethodVo transAnnuityMethodVo) {
		try {
			// 發送驗證碼
			sendAuthCode("annuityMethod");
			addAttribute("transAnnuityMethodVo", transAnnuityMethodVo);
		} catch (Exception e) {
			logger.error("Unable to init from annuityPayMethod3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/annuityMethod/annuity-pay_method3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/annuityPayMethodSuccess")
	public String annuityPayMethodSuccess(TransAnnuityMethodVo transAnnuityMethodVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transAnnuityMethodVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transAnnuityMethodVo.getAuthenticationNum();
				String msg = checkAuthCode("annuityMethod", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:annuityPayMethod3";
				}
				
				// 設定使用者
				String userId = getUserId();
				transAnnuityMethodVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE);
				apiReq.setTransAnnuityMethodVo(transAnnuityMethodVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransAnnuityMethod data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transAnnuityMethodVo.setTransNum(transNum);
					
					//guaranteePeriod,guaranteePeriodOld格式化為"00"
					transAnnuityMethodVo.setGuaranteePeriod(formatTo00(transAnnuityMethodVo.getGuaranteePeriod()));
					transAnnuityMethodVo.setGuaranteePeriodOld(formatTo00(transAnnuityMethodVo.getGuaranteePeriodOld()));
					
					int result = transAnnuityMethodService.insertTransAnnuityMethod(transAnnuityMethodVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:annuityPayMethod3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from annuityPayMethodSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:annuityPayMethod3";
		}
		return "frontstage/onlineChange/annuityMethod/annuity-pay_method-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransAnnuityMethodDetail")
	public String getTransAnnuityMethodDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransAnnuityMethodVo transAnnuityMethodVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transAnnuityMethodVo = transHistoryDetailList.get(0).getTransAnnuityMethodVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransAnnuityMethodDetail data", userId);
				transAnnuityMethodVo = transAnnuityMethodService.getTransAnnuityMethodDetail(transNum);
			}
			
			addAttribute("transAnnuityMethodVo", transAnnuityMethodVo);
		} catch (Exception e) {
			logger.error("Unable to getTransAnnuityDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/annuityMethod/annuity-pay_method-detail";
	}
	
	private String formatTo00(String str) {
		String formatStr = "%02d";
		String rtn = "";
		
		if(str==null || "".equals(str.trim())) {
			str = "0";//避免空字串會error
		}
		if(str.length()<2) {
			rtn = rtn.format(formatStr, Integer.parseInt(str));
		}
		return rtn;
	}
	
	public static void main(String[] args) {
		AnnuityMethodController amc = new AnnuityMethodController();
		String str = amc.formatTo00("0");
		System.out.println("****"+str+"****");
	}

}