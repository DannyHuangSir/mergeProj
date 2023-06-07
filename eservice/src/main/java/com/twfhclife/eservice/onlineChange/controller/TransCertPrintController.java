package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.onlineChange.service.ITransCertPrintService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;
import com.twfhclife.generic.util.PdfUtil;

/**
 * 線上申請-投保證明列印(保單為多選)
 */
@Controller
public class TransCertPrintController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(TransCertPrintController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransCertPrintService transCertPrintService;
	
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
	@GetMapping("/certificatePrint1")
	public String certificatePrint1() {
		try {
			if(!checkCanUseOnlineChange()) {
				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				addSystemError(message);
				return "redirect:apply1";
			}
			getRequest().getSession().removeAttribute("transCertPrintVo");
			
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<PolicyListVo> policyList = getUserOnlineChangePolicyList(userId, userRocId);
			
			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.CERT_PRINT_PARAMETER_CODE);
				transCertPrintService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CERT_PRINT_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
			
		} catch (Exception e) {
			logger.error("Unable to init from certificatePrint1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "476");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/certificatePrint/certificate-print1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/certificatePrint2")
	public String certificatePrint2(TransCertPrintVo transCertPrintVo) {
		try {
			addSession("transCertPrintVo", transCertPrintVo); // 20230524, session記錄選擇的保單
			addAttribute("transCertPrintVo", transCertPrintVo);
		} catch (Exception e) {
			logger.error("Unable to init from certificatePrint2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/certificatePrint/certificate-print2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/certificatePrint3")
	public String certificatePrint3(TransCertPrintVo transCertPrintVo) {
		try {
			// 發送驗證碼
			sendAuthCode("certPrint");
			addAttribute("transCertPrintVo", transCertPrintVo);
		} catch (Exception e) {
			logger.error("Unable to init from certificatePrint3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/certificatePrint/certificate-print3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/certificatePrintSuccess")
	public String certificatePrintSuccess(TransCertPrintVo transCertPrintVo) {
		try {
//			boolean isTransApplyed = false;
//			List<String> policyNos = transCertPrintVo.getPolicyNoList();
//			for (String policyNo : policyNos) {
//				boolean applyed = transService.isTransApplyed(policyNo,
//						TransTypeUtil.CERT_PRINT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
//				if (applyed) {
//					isTransApplyed = true;
//					break;
//				}
//			}
//			
//			// 沒有申請過才新增
//			if (!isTransApplyed) {
//				// 驗證驗證碼
//				String authNum = transCertPrintVo.getAuthenticationNum();
//				String msg = checkAuthCode("certPrint", authNum);
//				if (!StringUtils.isEmpty(msg)) {
//					addSystemError(msg);
//					return "forward:certificatePrint3";
//				}
//				
//				// 設定使用者
//				String userId = getUserId();
//				transCertPrintVo.setUserId(userId);
//				
//				// Call api 送出線上申請資料
//				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
//				
//				String transAddResult = "";
//				TransAddRequest apiReq = new TransAddRequest();
//				apiReq.setSysId(ApConstants.SYSTEM_ID);
//				apiReq.setTransType(TransTypeUtil.CERT_PRINT_PARAMETER_CODE);
//				apiReq.setTransCertPrintVo(transCertPrintVo);
//				apiReq.setUserId(userId);
//				
//				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
//				if (transAddResponse != null) {
//					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
//							MyJacksonUtil.object2Json(transAddResponse));
//					transAddResult = transAddResponse.getTransResult();
//				} else {
//					// 若無資料，嘗試由內部服務取得資料
//					logger.info("Call internal service to get user[{}] insertTransCertPrint data", userId);
//					
//					// 設定交易序號
//					String transNum = transService.getTransNum();
//					transCertPrintVo.setTransNum(transNum);
//					
//					int result = transCertPrintService.insertTransCertPrint(transCertPrintVo);
//					if (result <= 0) {
//						transAddResult = ReturnHeader.FAIL_CODE;
//					} else {
//						transAddResult = ReturnHeader.SUCCESS_CODE;
//					}
//				}
//				
//				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
//					addDefaultSystemError();
//					return "forward:certificatePrint3";
//				}
//			}
//			
//			addAttribute("transCertPrintVo", transCertPrintVo);
		} catch (Exception e) {
			logger.error("Unable to init from certificatePrintSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:certificatePrint3";
		}
		return "frontstage/onlineChange/certificatePrint/certificate-print-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransCertPrintDetail")
	public String getTransCertPrintDetail(@RequestParam("transNum") String transNum) {
		try {
			TransCertPrintVo transCertPrintVo = transCertPrintService.getTransCertPrintDetail(transNum);
			addAttribute("transCertPrintVo", transCertPrintVo);
		} catch (Exception e) {
			logger.error("Unable to getTransCertPrintDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/certificatePrint/certificate-print-detail";
	}
	
	@RequestLog
	@PostMapping("/certPrintPrepare")
	public ResponseEntity<ResponseObj> certPrintPrepare(@RequestBody(required = false) TransCertPrintVo transCertPrintVo){
		try {
			TransCertPrintVo transCertPrintChooseVo = (TransCertPrintVo)getSession("transCertPrintVo");
			transCertPrintVo.setPolicyNoList(transCertPrintChooseVo.getPolicyNoList()); // 20230524, 使用步驟1 session記錄選擇的保單
			addSession("transCertPrintVo", transCertPrintVo);
			processSuccessMsg(null);
		} catch(Exception e) {
			logger.error("Unable to prepareCerPrintPdf: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@RequestMapping("/certPrintDownload")
	public @ResponseBody HttpEntity<byte[]> downloadCertPrintPdf(TransCertPrintVo transCertPrintVo){
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			TransCertPrintVo transCertPrintDownloadVo = (TransCertPrintVo)getSession("transCertPrintVo");
			java.util.Optional<byte[]> optional = transCertPrintService.prepareCertPrint(transCertPrintDownloadVo);
			if(optional.isPresent()) {
				/* 開始設定回傳內容 */
				document = new PdfUtil().doEncryption(optional.get(), getUserRocId());
				String fileName = "inline; filename=投保證明書.pdf";
				header.setContentType(new MediaType("application", "pdf"));
				header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
				header.setContentLength(document.length);
			}
			
			// 設定使用者
			String userType = getUserType();
			logger.info("insertTransCertPrint userType:{}", userType);
			if ("member".equals(userType)) {
				String userId = getUserId();
				transCertPrintDownloadVo.setUserId(userId);
				// 送出線上申請資料
				String transNum = transService.getTransNum();
				transCertPrintDownloadVo.setTransNum(transNum);
				logger.info("insertTransCertPrint data:{}", MyJacksonUtil.object2Json(transCertPrintDownloadVo));
				
				List<TransExtendAttrVo> transExtendAttrList = new ArrayList<TransExtendAttrVo>();
				transExtendAttrList.add(new TransExtendAttrVo(null, null, "lang", transCertPrintDownloadVo.getLang()));
				if ("E".equals(transCertPrintDownloadVo.getLang())) {
					transExtendAttrList.add(new TransExtendAttrVo(null, null, "passportName", transCertPrintDownloadVo.getPassportName()));
					transExtendAttrList.add(new TransExtendAttrVo(null, null, "passportNo", transCertPrintDownloadVo.getPassportNo()));
					transExtendAttrList.add(new TransExtendAttrVo(null, null, "lipmPassportName", transCertPrintDownloadVo.getLipmPassportName()));
				}
				
				transCertPrintService.insertTransCertPrint(transCertPrintDownloadVo, transExtendAttrList);
			}
		} catch(Exception e) {
			logger.error("Unable to downloadCertPrintPdf: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}
}