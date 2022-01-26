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

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.MaturityVo;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.IMaturityService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 線上申請-滿期(保單為多選)
 */
@Controller
public class MaturityController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(MaturityController.class);
	
	@Autowired
	private IPolicyListService policyListService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private IMaturityService maturityService;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	@RequestLog
	@GetMapping("/maturity1")
	public String maturity1() {
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
						userId, TransTypeUtil.MATURITY_PARAMETER_CODE);
				maturityService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.MATURITY_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from maturity1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "488");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/maturity/maturity1";
	}

	@RequestLog
	@PostMapping("/maturity2")
	public String maturity2(MaturityVo maturityVo) {
		try {
			String policyNo = maturityVo.getPolicyNo();
			
			// 要保人
			LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
			if (lilipmVo != null) {
				maturityVo.setCustomerName(lilipmVo.getLipmName1());
			}
			
			// 被保人
			LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyNo);
			if (lilipiVo != null) {
				maturityVo.setInsuredName(lilipiVo.getLipiName());
			}
			
			PolicyVo policyVo = policyListService.findById(policyNo);
			if (policyVo != null) {
				if (policyVo.getEffectiveDate() != null ) {
					maturityVo.setEffectiveDate(DateUtil.formatDateTime(policyVo.getEffectiveDate(), DateUtil.FORMAT_WEST_DATE));
				}
				if (policyVo.getExpireDate() != null ) {
					maturityVo.setExpireDate(DateUtil.formatDateTime(policyVo.getExpireDate(), DateUtil.FORMAT_WEST_DATE));
				}
				maturityVo.setMainAmount(policyVo.getMainAmount());
			}
			
			List<BeneficiaryVo> beneficiaryList = beneficiaryService.getBeneficiaryByPolicyNo(policyNo);
			if (beneficiaryList != null && beneficiaryList.size() > 0) {
				for (BeneficiaryVo beneficiaryVo : beneficiaryList) {
					if (beneficiaryVo.getBeneficiaryType() != null && beneficiaryVo.getBeneficiaryType().intValue() == 2) { // 2:滿期
						maturityVo.setBeneficiaryName(beneficiaryVo.getBeneficiaryName());
					}
				}
			}
			
			addAttribute("maturityVo", maturityVo);
		} catch (Exception e) {
			logger.error("Unable to init from maturity2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/maturity/maturity2";
	}

	@RequestLog
	@PostMapping("/maturity3")
	public String maturity3(MaturityVo maturityVo) {
		try {
			addAttribute("maturityVo", maturityVo);
		} catch (Exception e) {
			logger.error("Unable to init from maturity3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/maturity/maturity3";
	}
	
	@RequestLog
	@RequestMapping(value = "/downloadMaturityPDF")
	public @ResponseBody HttpEntity<byte[]> downloadMaturityPDF(MaturityVo maturityVo) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=滿期-%s.pdf", maturityVo.getPolicyNo());
			
			document = maturityService.getPDF(maturityVo);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);

			// 新增申請主檔記錄
			String policyNo = maturityVo.getPolicyNo();
			boolean isTransApplyed = transService.isTransApplyed(policyNo, TransTypeUtil.MATURITY_PARAMETER_CODE,
					OnlineChangeUtil.TRANS_STATUS_BEADDED);
			if (!isTransApplyed && "member".equals(getUserType())) {
				// 設定使用者
				String userId = getUserId();
				maturityVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.MATURITY_PARAMETER_CODE);
				apiReq.setMaturityVo(maturityVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransMaturity data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					maturityVo.setTransNum(transNum);
					
					int result = maturityService.insertTransMaturity(maturityVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (transAddResult.equals(ReturnHeader.FAIL_CODE)) {
					logger.warn("Unable to insert trans record for maturity");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to get data from downloadMaturityPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}
}