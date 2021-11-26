package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.util.DateUtil;
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
import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.onlineChange.service.ITransPaymodeService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

/**
 * 線上申請-變更繳別(保單為單選)
 */
@Controller
public class PayModeController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(PayModeController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransPaymodeService transPaymodeService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	@Autowired
	private ILoginService loginService;

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private MessageTemplateClient messageTemplateClient;

	@Autowired
	private ITransInvestmentService transInvestmentService;

	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/paymentMode1")
	public String paymentMode1(RedirectAttributes redirectAttributes) {
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
						userId, TransTypeUtil.PAYMODE_PARAMETER_CODE);
				transPaymodeService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.PAYMODE_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentMode1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "464");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/payMode/payment-mode1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMode2")
	public String paymentMode2(TransPaymodeVo transPaymodeVo, RedirectAttributes redirectAttributes) {
		try {
			Integer lipmTredTms = transPaymodeService
					.getPolicyPayMethodChange(transPaymodeVo.getPolicyNoList().get(0));
			String paymodeOld = transPaymodeVo.getPaymodeOld();
			boolean paymodeA = true;//年繳
			boolean paymodeS = true;//半年繳
			boolean paymodeQ = true;//季繳
			if(lipmTredTms != null) {
				switch(paymodeOld) {
				case "M":// 月繳可變更
					paymodeA = lipmTredTms %12 == 0;
					paymodeS = lipmTredTms %6 == 0;
					paymodeQ = lipmTredTms %3 == 0;
					break;
				case "Q": // 季繳可變更
					paymodeA = lipmTredTms %4 == 0;
					paymodeS = lipmTredTms %2 == 0;
					break;
				case "S": // 半年繳可變更
					paymodeA = lipmTredTms %2 == 0;
					break;
				}
			}
			Map<String, Boolean> mapPaymode = new HashMap<String, Boolean>();
			mapPaymode.put("A", paymodeA);
			mapPaymode.put("S", paymodeS);
			mapPaymode.put("Q", paymodeQ);
			mapPaymode.put("E", false);

			String policyNo = transPaymodeVo.getPolicyNoList().get(0);
			String type = policyNo.substring(0,2);
			String types = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "PAYMODE_INVESTMENT_TYPE");
			if (StringUtils.isNotBlank(types) && StringUtils.isNotBlank(type) &&
				types.contains(type)) {
				mapPaymode.put("A", false);
				mapPaymode.put("S", false);
				mapPaymode.put("Q", false);
				mapPaymode.put("M", true);
				mapPaymode.put("E", true);
			}
			addAttribute("showAmount", checkShowAmount(transPaymodeVo));
			addAttribute("paymodeCanChange", mapPaymode);
			addAttribute("transPaymodeVo", transPaymodeVo);
			addAttribute("minValue", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "PAYMODE_" + type + "_MIN"));
		} catch (Exception e) {
			logger.error("Unable to init from paymentMode2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/payMode/payment-mode2";
	}

	private boolean checkShowAmount(TransPaymodeVo transPaymodeVo) {
		//目前暫時關閉顯示 2021/11/25 xianzhi
		return false;
		/*String INVESTMENT_TYPES = parameterService.getParameterValueByCode("eservice", "INVESTMENT_TYPE");
		if (StringUtils.isNotBlank(INVESTMENT_TYPES) && INVESTMENT_TYPES.contains(transPaymodeVo.getPolicyNoList().get(0).substring(0,2))) {
			return true;
		} else {
			return false;
		}*/
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentMode3")
	public String paymentMode3(TransPaymodeVo transPaymodeVo) {
		try {
			// 發送驗證碼
			addAttribute("paymodeTimeSet", 300);
			sendAuthCode("payMode");
			addAttribute("transPaymodeVo", transPaymodeVo);
			addAttribute("showAmount", checkShowAmount(transPaymodeVo));
		} catch (Exception e) {
			logger.error("Unable to init from paymentMode3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/payMode/payment-mode3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/paymentModeSuccess")
	public String paymentModeSuccess(TransPaymodeVo transPaymodeVo) {

		String msg;
		if (StringUtils.equals(transPaymodeVo.getAuthType(), "password")) {
			msg = checkPassword(transPaymodeVo.getUserPassword());
		} else {
			msg = checkAuthCode("payMode", transPaymodeVo.getAuthenticationNum());
		}
		if (!StringUtils.isEmpty(msg)) {
			addSystemError(msg);
			return "forward:paymentMode3";
		}

		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transPaymodeVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.PAYMODE_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {

				// 設定使用者
				String userId = getUserId();
				transPaymodeVo.setUserId(userId);
				String transAddResult = "";
					// 設定交易序號
					String transNum = transService.getTransNum();
					transPaymodeVo.setTransNum(transNum);
					int result = transPaymodeService.insertTransPaymode(transPaymodeVo);
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
					}
				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:paymentMode3";
				}
				sendNotification(transPaymodeVo, getUserDetail());
			}
		} catch (Exception e) {
			logger.error("Unable to init from paymentModeSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:paymentMode3";
		}
		return "frontstage/onlineChange/payMode/payment-mode-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransPaymodeDetail")
	public String getTransPaymodeDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransPaymodeVo transPaymodeVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] transHistoryDetailResponse data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transPaymodeVo = transHistoryDetailList.get(0).getTransPaymodeVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransPaymodeDetail data", userId);
				transPaymodeVo = transPaymodeService.getTransPaymodeDetail(transNum);
			}
			
			addAttribute("transPaymodeVo", transPaymodeVo);
			addAttribute("showAmount", checkShowAmount(transPaymodeVo));
		} catch (Exception e) {
			logger.error("Unable to getTransPaymodeDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/payMode/payment-mode-detail";
	}

	private String checkPassword(String authenticationNum) {
		try {
			UsersVo userDetail = getUserDetail();
			if(StringUtils.isEmpty(userDetail.getUserId())){
				return "密碼驗證失敗！";
			}
			LoginRequestVo loginRequestVo = new LoginRequestVo();
			String decodePasswd = decodeBase64(authenticationNum);
			loginRequestVo.setUserId(userDetail.getUserId());
			loginRequestVo.setPassword(decodePasswd);
			LoginResultVo restLogin = loginService.doLogin(loginRequestVo);
			if(restLogin!=null && StringUtils.equals("SUCCESS", restLogin.getReturnCode())){
				return  null;
			}else{
				return "密碼驗證失敗！";
			}
		} catch (Exception ex) {
			logger.error("==========密碼驗證失敗=========== {}", ExceptionUtils.getStackTrace(ex));
			return "密碼驗證失敗！";
		}
	}

	private String decodeBase64(String mi) {
		String mingwen = "";
		if (StringUtils.isNotBlank(mi)) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				byte[] by = decoder.decodeBuffer(mi);
				mingwen = new String(by);
			} catch (Exception ex) {
				logger.error("==========密碼加密失敗=========== {}", ExceptionUtils.getStackTrace(ex));
			}
		}
		return mingwen;
	}


	public void sendNotification(TransPaymodeVo vo, UsersVo user) {
		try {
			Map<String, Object> mailInfo = transInvestmentService.getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransNum", vo.getTransNum());
			paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			logger.info("Trans Num : {}", vo.getTransNum());
			logger.info("Status Name : {}", mailInfo.get("statusName"));
			logger.info("Trans Remark : {}", mailInfo.get("transRemark"));
			logger.info("receivers={}", mailInfo.get("receivers"));
			logger.info("user phone : {}", user.getMobile());
			logger.info("user mail : {}", user.getEmail());
			//获取保单编号
			paramMap.put("POLICY_NO", vo.getPolicyNoList().get(0));
			logger.info("POLICY_NO : {}", vo.getPolicyNoList().get(0));

			List<String> receivers = new ArrayList<String>();

			String applyDate = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
			paramMap.put("DATA", applyDate);
			//申請狀態-申請中
			paramMap.put("TransStatus","處理中");
			//申請功能
			ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
					ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.PAYMODE_PARAMETER_CODE);
			paramMap.put("APPLICATION_FUNCTION", parameterValueByCode.getParameterName());


			//發送系統管理員
			receivers = (List) mailInfo.get("receivers");
			//推送管 理已接收 保單編號: [保單編號]  保戶[同意/不同意]轉送聯盟鏈
			messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_027, receivers, paramMap, "email");

			//發送保戶SMS
			//receivers = new ArrayList<String>();
			receivers.clear();//清空
			paramMap.clear();//清空模板參數
			//設置模板參數
			paramMap.put("TITLE", OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_TITLE);
			paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY2);
			receivers.add(user.getMobile());
			logger.info("user phone : {}", user.getMobile());
			messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_028, receivers, paramMap, "sms");
			//發送保戶MAIL
			//receivers = new ArrayList<String>();
			if (user.getEmail() != null) {
				receivers.clear();//清空
				receivers.add(user.getEmail());
				logger.info("user mail : {}", user.getEmail());
				messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_028, receivers, paramMap, "email");
			}
		} catch (Exception e) {
			logger.info("insertTransInvestment() success, but send notify mail/sms error.");
		}
		logger.info("End send mail");
	}
}