package com.twfhclife.eservice.onlineChange.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationVo;
import com.twfhclife.eservice.onlineChange.model.TransNotificationVo;
import com.twfhclife.eservice.onlineChange.service.ITransDepositService;
import com.twfhclife.eservice.onlineChange.service.ITransFundNotificationService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import com.twfhclife.eservice.policy.model.NotificationPortfolioVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 線上申請-設定停損停利通知(保單為單選)
 */
@Controller
public class FundNotificationController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(FundNotificationController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransFundNotificationService transFundNotificationService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	@Autowired
	private ILoginService loginService;

	@Autowired
	private IPolicyListService policyListService;

	@Autowired
	private ITransInvestmentService transInvestmentService;

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private ITransRiskLevelService riskLevelService;

	@Autowired
	private ITransDepositService transDepositService;

	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/notification1")
	public String notification1(RedirectAttributes redirectAttributes) {
		try {
			if(!checkCanUseOnlineChange()) {
				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				redirectAttributes.addFlashAttribute("errorMessage", message);
				return "redirect:apply1";
			}

			boolean expire = riskLevelService.checkRiskLevelExpire(getUserId());
			if (expire) {
				redirectAttributes.addFlashAttribute("errorMessage", "距上一次線上風險屬性變更已超過一年，再請先重新執行線上風險屬性測試及變更！");
				return "redirect:apply1";
			}

			String userRocId = getUserRocId();
			String riskLevel = riskLevelService.getUserRiskAttr(userRocId);
			if(StringUtils.isBlank(riskLevel)) {
				redirectAttributes.addFlashAttribute("errorMessage", "請先變更風險屬性！");
				return "redirect:apply1";
			}

			/**
			 * 投資型保單申請中不可繼續申請
			 * TRANS  status=-1,0,4
			 */
			String msg = transInvestmentService.checkHasApplying(getUserId());
			if (StringUtils.isNotBlank(msg)) {
				redirectAttributes.addFlashAttribute("errorMessage", msg);
				return "redirect:apply1";
			}

			String userId = getUserId();
			List<PolicyListVo> policyList = policyListService.getInvestmentPolicyList(userRocId);
			
			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE);
				transInvestmentService.handlePolicyStatusLocked(userRocId, handledPolicyList, TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList, TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from notification1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "474");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/fundNotification/notification1";
	}

	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/notification2")
	public String notification2(TransFundNotificationVo transFundNotificationVo) {
		try {
			String policyNo = transFundNotificationVo.getPolicyNoList().get(0);
			addAttribute("policyNo", policyNo);
			addAttribute("transFundNotificationVo", transFundNotificationVo);
			addAttribute("policyType", policyNo.substring(0, 2));
			addAttribute("configs", transDepositService.getDepositConfigs());
			String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
					ApConstants.SYSTEM_ID, OnlineChangeUtil.NOTIFICATION_REMARKS);
			if (parameterValueByCodeConsent != null) {
				addAttribute("transformationRemark", parameterValueByCodeConsent);
			}
		} catch (Exception e) {
			logger.error("Unable to init from notification2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundNotification/notification2";
	}

	/**
	 * 步驟3(確認資料及發送驗證碼).
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/notification3")
	public String notification3(TransFundNotificationVo transFundNotificationVo) {
		try {
			logger.info("trans notification3 params:{}", transFundNotificationVo);
			List<TransFundNotificationDtlVo> transFundNotificationDtlList = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			transFundNotificationDtlList = mapper.readValue(
					transFundNotificationVo.getFundNotificationDtlJsonData(),
					new TypeReference<List<TransFundNotificationDtlVo>>() {});

			if (!CollectionUtils.isEmpty(transFundNotificationDtlList)) {
				addAttribute("holdList", transFundNotificationDtlList.stream().filter(x -> StringUtils.equals("1", x.getType())).collect(Collectors.toList()));
				addAttribute("observeList", transFundNotificationDtlList.stream().filter(x -> StringUtils.equals("2", x.getType())).collect(Collectors.toList()));
			} else {
				addAttribute("holdList", Lists.newArrayList());
				addAttribute("observeList", Lists.newArrayList());
			}

			transFundNotificationVo.setTransFundNotificationDtlList(transFundNotificationDtlList);

			addAttribute("notificationTimeSet", 300);
			// 發送驗證碼
			sendAuthCode("fundNotification");
			addAttribute("transFundNotificationVo", transFundNotificationVo);
		} catch (Exception e) {
			logger.error("Unable to init from notification3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundNotification/notification3";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/notificationSuccess")
	public String notificationSuccess(TransFundNotificationVo transFundNotificationVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transFundNotificationVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼或密码
				String msg;
				if (StringUtils.equals(transFundNotificationVo.getAuthType(), "password")) {
					msg = checkPassword(transFundNotificationVo.getUserPassword());
				} else {
					msg = checkAuthCode("fundNotification", transFundNotificationVo.getAuthenticationNum());
				}

				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:notification3";
				}
				
				List<TransFundNotificationDtlVo> transFundNotificationDtlList = new ArrayList<>();
				ObjectMapper mapper = new ObjectMapper();
				transFundNotificationDtlList = mapper.readValue(
						transFundNotificationVo.getFundNotificationDtlJsonData(),
						new TypeReference<List<TransFundNotificationDtlVo>>() {});
				transFundNotificationVo.setTransFundNotificationDtlList(transFundNotificationDtlList);
				
				// 設定使用者
				String userId = getUserId();
				transFundNotificationVo.setUserId(userId);
					String transNum = transService.getTransNum();
					transFundNotificationVo.setTransNum(transNum);

					int result = transFundNotificationService.insertTransFundNotification(transFundNotificationVo);
					if (result <= 0) {
					return "forward:notification3";
				}
			}
			
			addAttribute("smsEmail",  hideEmail(getLoginUser().getEmail()));
			
		} catch (Exception e) {
			logger.error("Unable to init from notificationSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:notification3";
		}
		return "frontstage/onlineChange/fundNotification/notification-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransFundNotificationDetail")
	public String getTransFundNotificationDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			List<TransFundNotificationDtlVo> transFundNotificationDtlList = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transFundNotificationDtlList = transHistoryDetailList.get(0).getTransFundNotificationDtlList();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransFundNotificationDetail data", userId);
				BigDecimal transFundNotificationId = transFundNotificationService.getTransFundNotificationId(transNum);
				transFundNotificationDtlList = transFundNotificationService.getTransFundNotificationDtlDetail(transNum,
						transFundNotificationId);
			}
			
			addAttribute("transFundNotificationDtlList", transFundNotificationDtlList);
		} catch (Exception e) {
			logger.error("Unable to getTransFundNotificationDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/fundNotification/notification-detail";
	}

	@RequestLog
	@PostMapping("/getSearchPortfolio")
	@ResponseBody
	public ResponseEntity<ResponseObj> getSearchPortfolio(@RequestBody TransNotificationVo vo) {
		processSuccess(transFundNotificationService.getSearchPortfolio(vo.getPolicyNo(), vo.getInvtNos(), getUserRocId()));
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getObservePortfolio")
	@ResponseBody
	public ResponseEntity<ResponseObj> getObservePortfolio(@RequestBody TransNotificationVo vo) {
		processSuccess(transFundNotificationService.getObservePortfolio(vo.getPolicyNo()));
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getNotificationPortfolioList")
	public ResponseEntity<ResponseObj> getPortfolioList(@RequestParam("policyNo") String policyNo) {
		try {
			List<NotificationPortfolioVo> portfolioList = transFundNotificationService.getOwnNotifications(policyNo);
			processSuccess(portfolioList);
		} catch (Exception e) {
			logger.error("Unable to getNotificationPortfolioList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	//keycloak驗證密碼
	private String checkPassword(String authenticationNum) {
		try {
			UsersVo loginUser = getUserDetail();
			if (loginUser == null) {
				return "密碼驗證失敗！";
			}
			LoginRequestVo loginRequestVo = new LoginRequestVo();
			String decodePasswd = decodeBase64(authenticationNum);
			loginRequestVo.setUserId(loginUser.getUserId());
			loginRequestVo.setPassword(decodePasswd);
			LoginResultVo res = loginService.doLogin(loginRequestVo);
			return res != null && StringUtils.equals("SUCCESS", res.getReturnCode()) ?  null : "密碼驗證失敗！";
		} catch (Exception e) {
			logger.error(e);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mingwen;
	}
}