package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.onlineChange.model.TransLoanVo;
import com.twfhclife.eservice.onlineChange.service.ITransLoanService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.service.IOptionService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 線上申請-申請保單貸款(保單為單選)
 */
@Controller
public class LoanController extends BaseUserDataController {
	
	private static final Logger logger = LogManager.getLogger(LoanController.class);
	
	@Autowired
	private IPolicyExtraService policyExtraService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransLoanService transLoanService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@Autowired
	private IOptionService optionService;

	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/loan1")
	public String loan1() {
		try {
//			if(!checkCanUseOnlineChange()) {
//				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
//				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
//				addSystemError(message);
//				return "redirect:apply1";
//			}
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<PolicyListVo> policyList = getUserOnlineChangePolicyList(userId, userRocId);
			//保留要、被保人不同人的保單  20190702暫不限制
//			policyList = policyList.stream().filter((p) -> p.getSameIdCount() == 0).collect(Collectors.toList());

			// 查詢貸款資訊
			for (PolicyListVo policyListVo : policyList) {
				String policyNo = policyListVo.getPolicyNo();
				policyListVo.setPolicyExtraVo(policyExtraService.findByPolicyNo(policyNo));
			}
			//保留無指定帳號者之保單  20190702暫不限制
//			policyList = policyList.stream()
//					.filter((p) -> p.getPolicyExtraVo() != null && StringUtils.isEmpty(p.getPolicyExtraVo().getInmsAccountNo()))
//					.collect(Collectors.toList());

			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.LOAN_PARAMETER_CODE);
				transLoanService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.LOAN_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from loan1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "486");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/loan/loan1";
	}

	/**
	 * 輸入欲借金額與給付方式.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/loan2")
	public String loan2(TransLoanVo transLoanVo) {
		try {
			String policyNo = transLoanVo.getPolicyNo();
			
			// 要保人
			LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
			if (lilipmVo != null) {
				transLoanVo.setCustomerName(lilipmVo.getLipmName1());
			}
			
			// 被保人
			LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyNo);
			if (lilipiVo != null) {
				transLoanVo.setInsuredName(lilipiVo.getLipiName());
			}
			
			addAttribute("transLoanVo", transLoanVo);
		} catch (Exception e) {
			logger.error("Unable to init from loan2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/loan/loan2";
	}

	/**
	 * 確認資料.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/loan3")
	public String loan3(TransLoanVo transLoanVo) {
		try {
			addAttribute("transLoanVo", transLoanVo);
			Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
			String limitSizeStr = sysParamMap.get("SYSTEM_CONSTANTS").get("UPLOAD_FILE_SIZE_LIMIT").getParameterValue();
			addAttribute("limitSizeStr", limitSizeStr);
		} catch (Exception e) {
			logger.error("Unable to init from loan3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/loan/loan3";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransLoanDetail")
	public String getTransLoanDetail(@RequestParam("transNum") String transNum) {
		try {
			addAttribute("transLoanVo", transLoanService.getTransLoanDetail(transNum));
		} catch (Exception e) {
			logger.error("Unable to getTransLoanDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/loan/loan-detail";
	}
	
	/**
	 * 下載保單借款約定書PDF.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/downloadLoanPDF")
	public @ResponseBody HttpEntity<byte[]> downloadLoanPDF(TransLoanVo transLoanVo) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=保單借款約定書-%s.pdf", transLoanVo.getPolicyNo());
			
			document = transLoanService.getPDF(transLoanVo);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);

			// 新增申請主檔記錄
			String policyNo = transLoanVo.getPolicyNo();
			boolean isTransApplyed = transService.isTransApplyed(policyNo, TransTypeUtil.LOAN_PARAMETER_CODE,
					OnlineChangeUtil.TRANS_STATUS_BEADDED);
			if (!isTransApplyed && "member".equals(getUserType())) {
				// 設定使用者
				String userId = getUserId();
				transLoanVo.setUserId(userId);
				
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.LOAN_PARAMETER_CODE);
				apiReq.setTransLoanVo(transLoanVo);
				apiReq.setUserId(userId);
				
				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
				if (transAddResponse != null) {
					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
							MyJacksonUtil.object2Json(transAddResponse));
					transAddResult = transAddResponse.getTransResult();
				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransLoan data", userId);
					
					// 設定交易序號
					String transNum = transService.getTransNum();
					transLoanVo.setTransNum(transNum);
					
					int result = transLoanService.insertTransLoan(transLoanVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				}
				
				if (transAddResult.equals(ReturnHeader.FAIL_CODE)) {
					logger.warn("Unable to insert trans record for loan");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to get data from downloadLoanPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}

	/**
	 * 保單貸款(已申請指定帳號)保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/loanNew1")
	public String loanNew1() {
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
			//保留要、被保人同一人的保單
			policyList = policyList.stream().filter((p) -> p.getSameIdCount() == 1).collect(Collectors.toList());
			
			// 查詢貸款資訊
			for (PolicyListVo policyListVo : policyList) {
				String policyNo = policyListVo.getPolicyNo();
				policyListVo.setPolicyExtraVo(policyExtraService.findByPolicyNo(policyNo));
			}
			//保留已有指定帳號的保單
			policyList = policyList.stream()
					.filter((p) -> p.getPolicyExtraVo() != null && StringUtils.isNotEmpty(p.getPolicyExtraVo().getInmsAccountNo()))
					.collect(Collectors.toList());
			
			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.LOAN_NEW_PARAMETER_CODE);
				handledPolicyList = transLoanService.filterPolicyLoanAcct(handledPolicyList);
				transLoanService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.LOAN_NEW_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from loanNew1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "486");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/loan/loan-new1";
	}

	/**
	 * 輸入欲借金額與給付方式.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/loanNew2")
	public String loanNew2(TransLoanVo transLoanVo) {
		try {
			String policyNo = transLoanVo.getPolicyNo();
			
			// 要保人
			LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
			if (lilipmVo != null) {
				transLoanVo.setCustomerName(lilipmVo.getLipmName1());
			}
			
			// 被保人
			LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyNo);
			if (lilipiVo != null) {
				transLoanVo.setInsuredName(lilipiVo.getLipiName());
			}
			
			// 保單
			PolicyExtraVo policyExtraVo = policyExtraService.findByPolicyNo(policyNo);
			if (policyExtraVo != null) {
				String bankId = policyExtraVo.getInmsBankCode();
				List<Map<String, Object>> bankMap = optionService.getBankList();
				for (Map<String, Object> m : bankMap) {
					if (m.containsKey("KEY") && m.get("KEY").toString().equals(bankId)) {
						transLoanVo.setBankName(m.get("VALUE").toString());
						break;
					}
				}
				transLoanVo.setBankId(bankId);

				String branchId = policyExtraVo.getInmsBankBranchCode();
				List<Map<String, Object>> branchMap = optionService.getBranchesList(bankId);
				for (Map<String, Object> m : branchMap) {
					if (m.containsKey("KEY") && m.get("KEY").toString().equals(branchId)) {
						transLoanVo.setBranchName(m.get("VALUE").toString());
						break;
					}
				}				
				transLoanVo.setBranchId(branchId);				
				transLoanVo.setAccountNumber(policyExtraVo.getInmsAccountNo());
			}
			
			addAttribute("transLoanVo", transLoanVo);
		} catch (Exception e) {
			logger.error("Unable to init from loanNew2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/loan/loan-new2";
	}

	/**
	 * 確認資料.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/loanNew3")
	public String loanNew3(TransLoanVo transLoanVo) {
		try {
			// 發送驗證碼
			sendAuthCode(TransTypeUtil.LOAN_NEW_PARAMETER_CODE);
			addAttribute("transLoanVo", transLoanVo);
		} catch (Exception e) {
			logger.error("Unable to init from loanNew3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/loan/loan-new3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/loanNewSuccess")
	public String loanNewSuccess(TransLoanVo transLoanVo) {
		try {			
			// 驗證驗證碼
			String authNum = transLoanVo.getAuthenticationNum();
			String msg = checkAuthCode(TransTypeUtil.LOAN_NEW_PARAMETER_CODE, authNum);
			if (!StringUtils.isEmpty(msg)) {
				addSystemError(msg);
				return "forward:loanNew3";
			}
			
			// 設定使用者
			String userId = getUserId();
			transLoanVo.setUserId(userId);
			
			// Call api 送出線上申請資料
			logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
			
			//temp
//			transLoanVo.setBankId("005");
//			transLoanVo.setBranchId("0061");
			
			String transAddResult = "";
			TransAddRequest apiReq = new TransAddRequest();
			apiReq.setSysId(ApConstants.SYSTEM_ID);
			apiReq.setTransType(TransTypeUtil.LOAN_NEW_PARAMETER_CODE);
			apiReq.setTransLoanVo(transLoanVo);
			apiReq.setUserId(userId);
			
			List<TransExtendAttrVo> transExtendAttrList = new ArrayList<TransExtendAttrVo>();
			transExtendAttrList.add(new TransExtendAttrVo(null, null, "noticeMethod", transLoanVo.getNoticeMethod()));
			transExtendAttrList.add(new TransExtendAttrVo(null, null, "noticeTarget", transLoanVo.getNoticeTarget()));
			apiReq.setTransExtendAttrList(transExtendAttrList);
			
			TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
			if (transAddResponse != null) {
				logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
						MyJacksonUtil.object2Json(transAddResponse));
				transAddResult = transAddResponse.getTransResult();
			} else {
				// 設定交易序號
				String transNum = transService.getTransNum();
				transLoanVo.setTransNum(transNum);
				// 若無資料，嘗試由內部服務執行
				logger.info("Call internal service to get insertTransLoanNew data:{}", MyJacksonUtil.object2Json(transLoanVo));
				
				int result = transLoanService.insertTransLoanNew(transLoanVo, transExtendAttrList);
				if (result <= 0) {
					transAddResult = ReturnHeader.FAIL_CODE;
				} else {
					transAddResult = ReturnHeader.SUCCESS_CODE;
				}
			}
			
			if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
				addDefaultSystemError();
				return "forward:loanNew3";
			}
		} catch (Exception e) {
			logger.error("Unable to init from loanNewSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:loanNew3";
		}
		return "frontstage/onlineChange/loan/loan-new-success";
	}
}