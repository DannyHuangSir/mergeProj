package com.twfhclife.eservice.onlineChange.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.onlineChange.service.IHospitalServcie;
import com.twfhclife.eservice.onlineChange.service.IOnlineChangeService;
import com.twfhclife.eservice.onlineChange.service.IHospitalServcie;
import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransHistoryListClient;
import com.twfhclife.generic.api_model.TransHistoryListResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.StatuCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@EnableAutoConfiguration
public class OnlineChangeController extends BaseController {

	private static final Logger logger = LogManager.getLogger(OnlineChangeController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;

	@Autowired
	private IRegisterUserService registerUserService;

	@Autowired
	private TransHistoryListClient transHistoryListClient;

	@Autowired
	private FunctionUsageClient functionUsageClient;

	@Autowired
	private ITransContactInfoService transContactInfoService;

	@Autowired
	private IHospitalServcie iHospitalServcie;

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private ITransInvestmentService transInvestmentService;

	@Autowired
	private MessageTemplateClient messageTemplateClient;

	@Autowired
	private IMailService mailService;
	
	/**
	 * 線上申請清單
     *
	 * @return
	 */
	@GetMapping("/apply1")
	public String apply1(ModelMap modelMap) {
		try {
			if (!loginCheck()) {
				return "redirect:/login-timeout";
			}
		} catch (Exception e) {
			logger.error("Unable to loginCheck: {}", ExceptionUtils.getStackTrace(e));
		}

		Map<String, ParameterVo> parameterList = getParameterMap("PAGE_WORDING");
		Map<String, String> map = new HashMap<String, String>();
        for (String key : parameterList.keySet()) {
            if (key.substring(0, 9).equals(OnlineChangeUtil.ONLINE_CHANGE_HOME_PARAMETER_CATEGORY_CODE)) {
				map.put(key, parameterList.get(key).getParameterValue());
			}
			if (key.startsWith(OnlineChangeUtil.ONLINE_CHANGE_HOME_PARAMETER_INVESTMENT_CATEGORY_CODE)) {
				map.put(key, parameterList.get(key).getParameterValue());
			}
		}

        String disableShowMedicalForMembers = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "DISABLE_SHOW_MEDICAL_FOR_MEMBER");
        if(StringUtils.isBlank(disableShowMedicalForMembers)) {
        	disableShowMedicalForMembers = "";
        }
        
        String disableShowForMembers = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "DISABLE_SHOW_ONLINECHANGE_FOR_MEMBER");
        if(StringUtils.isBlank(disableShowForMembers)) {
        	disableShowForMembers = "";
        }
        
		modelMap.addAttribute("disableShowMedicalForMembers", disableShowMedicalForMembers);
		modelMap.addAttribute("disableShowForMembers", disableShowForMembers);
		modelMap.addAttribute("onlineChangeHome", map);
		modelMap.addAttribute("canUseFlag", getUserDetail().getOnlineFlag());
		modelMap.addAttribute("onlinechangeEnableEntry", onlinechangeEnableEntry);

		if (getSession("POPUP_FLAG") == null || !(boolean) getSession("POPUP_FLAG")) {
			try {
				String onlineChangeMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "ONLINECHANGE_POPUP_MSG");
				if (StringUtils.isNotBlank(onlineChangeMsg)) {
					String onlineChangeTime = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "ONLINECHANGE_POPUP_TIME_FORMAT");
					if (StringUtils.isNotBlank(onlineChangeTime) && StringUtils.contains(onlineChangeTime, "-")) {
						String[] timeStr = onlineChangeTime.split("-");
						SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
						long startTime = timeFormat.parse(timeStr[0]).getTime();
						long endTime = timeFormat.parse(timeStr[1]).getTime();
						long currentTime = System.currentTimeMillis();
						if (currentTime >= startTime && currentTime <= endTime) {
							addAttribute("POPUP_MSG", onlineChangeMsg);
							addSession("POPUP_FLAG", true);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Show POPUP MSG error: ", e);
			}
		}

		logger.info("open {}", "frontstage/apply1.html");
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "162");
        } catch (Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/apply1";
	}

	/**
	 * 我的申請紀錄
     *
	 * @return
	 */
	@GetMapping("/apply2")
	public String apply2() {
		logger.info("open {}", "frontstage/apply2.html");
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "162");
			Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
			String limitSizeStr = sysParamMap.get("SYSTEM_CONSTANTS").get("UPLOAD_FILE_SIZE_LIMIT").getParameterValue();
			addAttribute("limitSizeStr", limitSizeStr);
        } catch (Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/apply2";
	}

	/**
	 * 取得交易歷史記錄.
     *
	 * @param policyNo 保單號碼
	 * @param systemId 系統別
	 * @param currencyCategoryCode 幣別參數類型代碼
	 * @param trCode 交易類別
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @return 回傳保單的保費費用記錄
	 */
	@PostMapping("/getTransList")
	public ResponseEntity<ResponseObj> getTransList(
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "transType", required = false) String transType,
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum
			) {
        try {
			String userId = getUserId();
			String errorMessage = "";
			
			int defaultPageSize = Integer.valueOf(parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "APPLY2_COUNT"));

			status = (StringUtils.isEmpty(status) ? null : status);
			transType = (StringUtils.isEmpty(transType) ? null : transType);
			policyNo = (StringUtils.isEmpty(policyNo) ? null : policyNo);
			startDate = (StringUtils.isEmpty(startDate) ? null : startDate);
			endDate = (StringUtils.isEmpty(endDate) ? null : endDate);

			if (startDate != null && endDate != null) {
				if (startDate.compareTo(endDate) > 0) {
					/*errorMessage = "結束日期不能小於開始日期";*/
					errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0003");
				}
			}
			if (StringUtils.isEmpty(errorMessage)) {
				List<TransVo> transHistoryList = null;

				// Call api 取得資料
				TransHistoryListResponse transHistoryListResponse = transHistoryListClient.getTransHistoryList(userId,
						status, transType, policyNo, startDate, endDate, pageNum, defaultPageSize);
				// 若無資料，嘗試由內部服務取得資料
				if (transHistoryListResponse != null) {
					logger.info("Get user[{}] data from eservice_api[getTransHistoryList]", userId);
					transHistoryList = transHistoryListResponse.getTransHistoryList();
				} else {
					logger.info("Call internal service to get user[{}] getTransHistoryList data", userId);
					transHistoryList = onlineChangeService.getTransByUserId(userId, status, transType, policyNo,
							startDate, endDate, pageNum, defaultPageSize);
				}

				 // TODO 进行处理数据
				if (transHistoryList != null) {
					transHistoryList = transHistoryList.stream().map(x -> {
						logger.info("getTransList ! {}", x);
						if (x.getFromCompanyId() != null && !"".equals(x.getFromCompanyId())) {
							if (!"L01".equals(x.getFromCompanyId())) {
								String transTypeStr = x.getTransTypeStr();
								transTypeStr = transTypeStr != null ? transTypeStr + "(聯盟件)" :"";
								x.setTransTypeStr(transTypeStr);
								logger.info("===================================================================");
								logger.info("getTransList ! x.getTransTypeStr() {}", x.getTransTypeStr());
								logger.info("===================================================================");
							}
						}
						//進行對醫起通進行單獨的處理
						String type = x.getTransType();
						if(type!=null  &&  !"".equals(type)){
							if (TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE.equals(type)){
								//進行查詢當前醫起通 保單是否推送至聯盟
							String  sendAlliance=	onlineChangeService.getTransMedicalTreatmentClaimBySendAlliance(x.getTransNum());
								if (!StringUtils.isBlank(sendAlliance) && sendAlliance.equals(StatuCode.ALLIANCE_Y.code)) {
									x.setSendAlliance(StatuCode.ALLIANCE_Y.code);
								}
							}
						}
						logger.info("getTransList ! {}", x);
						return x;
					}).collect(Collectors.toList());
				}

				this.setResponseObj(ResponseObj.SUCCESS, null, transHistoryList);
			} else {
				this.setResponseObj(ResponseObj.ERROR, errorMessage, null);
			}
		} catch (Exception e) {
			logger.error("getTransList error! {}", e);
			this.setResponseObj(ResponseObj.ERROR, e.getMessage(), null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	/**
	 * 轉移到詳細畫面
     *
	 * @param transType
	 * @return
	 */
	@GetMapping("/tableget")
    public String tableget(@RequestParam("transType") String transType,
			@RequestParam("transNum") String transNum, ModelMap modelMap) {
		logger.info("open tableget transType = {}", transType);
		String userId = getUserId();
        try {
			TableGetVo tableGet = onlineChangeService.getTransDetail(transType, transNum, userId);
			modelMap.addAttribute("tableGet", tableGet);
			logger.info("open :{}", "frontstage/tableget/tableget1.html");
		} catch (Exception e) {
			logger.error("tableget error! {}", e);
			return "frontstage/tableget/tableget0";
		}
		return "frontstage/tableget/tableget1";
	}


	private static final List<String> INVSETMENT_TYPES = Lists.newArrayList(
			TransTypeUtil.INVESTMENT_CONVERSION_CODE,
			TransTypeUtil.INVESTMENT_PARAMETER_CODE,
			TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE,
			TransTypeUtil.RISK_LEVEL_PARAMETER_CODE,
			TransTypeUtil.CHANGE_PREMIUM_CODE,
			TransTypeUtil.DEPOSIT_PARAMETER_CODE,
			TransTypeUtil.PAYMODE_PARAMETER_CODE,
			TransTypeUtil.ELECTRONIC_FORM_A_CODE,
			TransTypeUtil.ELECTRONIC_FORM_C_CODE,
			TransTypeUtil.ROLLOVER_PERIODICALLY,
			TransTypeUtil.DERATE_PAID_OFF
	);

	private static final Map<String, String> MSG_MAP = ImmutableMap.<String, String>builder()
			.put(TransTypeUtil.INVESTMENT_CONVERSION_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY)
			.put(TransTypeUtil.INVESTMENT_PARAMETER_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY1)
			.put(TransTypeUtil.PAYMODE_PARAMETER_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY2)
			.put(TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY3)
			.put(TransTypeUtil.CHANGE_PREMIUM_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY4)
			.put(TransTypeUtil.DEPOSIT_PARAMETER_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY5)
			.put(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE, OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY6)
			.build();
	/**
	 * 取消申請
     *
	 * @param transNum
	 * @return
	 */
	@PostMapping("/cancelApplyTrans")
	public String cancelApplyTrans(@RequestParam("transNum") String transNum,@RequestParam("transType") String transType) {
		TransStatusHistoryVo hisVo = new TransStatusHistoryVo();
		hisVo.setTransNum(transNum);
		hisVo.setStatus(OnlineChangeUtil.TRANS_STATUS_CANCEL);
		UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
		hisVo.setCustomerName(userDetail.getUserName());
		hisVo.setIdentity(userDetail.getUserId());
		UsersVo user = getUserDetail();
		//查询当前保单同批次的保单，序号
		List<TransContactInfoVo> transContactInfoTransNum
				   = transContactInfoService.getTransContactInfoTransNum(transNum);
		try {

			if (transContactInfoTransNum != null && transContactInfoTransNum.size() > 0) {
				for (TransContactInfoVo transContactInfoVo : transContactInfoTransNum) {
					String transNum1 = transContactInfoVo.getTransNum();
					if (transNum1 != null && !"".equals(transNum1)) {
						onlineChangeService.cancelApplyTrans(transNum1, hisVo);
					}
				}
			} else if (transType != null && INVSETMENT_TYPES.contains(transType)) {
				if (StringUtils.equals(transType, TransTypeUtil.RISK_LEVEL_PARAMETER_CODE)) {
					//進行取消 已持有投資標的轉換保單
					onlineChangeService.cancelApplyTransInvestment(transNum, hisVo);
					//發送 郵件
					TransInvestmentVo transInvestmentVo = new TransInvestmentVo();
					transInvestmentVo.setTransNum(transNum);
					transInvestmentVo.setPolicyNo("");
					//申請功能名稱
					ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
							ApConstants.SYSTEM_ID, OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, transType);
					transInvestmentVo.setAuthType(parameterValueByCode.getParameterName());
					transInvestmentVo.setTitle(OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_TITLE);
					transInvestmentVo.setMessage(MSG_MAP.get(transType));
					transInvestmentVo.setApplyDate(new Date());
					sendConversionSMSAndEmail(transInvestmentVo, user, transType);
				} else {
					//進行查詢數據當前批次的保單號
					String policyNo = transContactInfoService.getHistoryPolicyNo(transNum);
					String type = policyNo.substring(0, 2);
					String types = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "PAYMODE_INVESTMENT_TYPE");
					if (StringUtils.equals(transType, TransTypeUtil.PAYMODE_PARAMETER_CODE) &&
							!(StringUtils.isNotBlank(types) && StringUtils.isNotBlank(type) && types.contains(type))) {
						//繳別非投資型保單，取消不需要發送投資型通知
						onlineChangeService.cancelApplyTrans(transNum, hisVo);
					} else {
						//進行取消 已持有投資標的轉換保單
						onlineChangeService.cancelApplyTransInvestment(transNum, hisVo);
						//發送 郵件
						TransInvestmentVo transInvestmentVo = new TransInvestmentVo();
						transInvestmentVo.setPolicyNo(policyNo);
						transInvestmentVo.setTransNum(transNum);
						//申請功能名稱
						ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
								ApConstants.SYSTEM_ID, OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, transType);
						transInvestmentVo.setAuthType(parameterValueByCode.getParameterName());
						transInvestmentVo.setTitle(OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_TITLE);
						transInvestmentVo.setMessage(MSG_MAP.get(transType));
						transInvestmentVo.setApplyDate(new Date());
						if(transType.equals(TransTypeUtil.ELECTRONIC_FORM_A_CODE) || transType.equals(TransTypeUtil.ELECTRONIC_FORM_C_CODE)||
								transType.equals(TransTypeUtil.DERATE_PAID_OFF) || transType.equals(TransTypeUtil.ROLLOVER_PERIODICALLY)	) {
							sendSMSAndEmailToManage(transInvestmentVo, user, transType);
						}
						else {
							sendConversionSMSAndEmail(transInvestmentVo, user, transType);
						}						
					}
				}
			} else if (transType != null && (StringUtils.equals(transType, TransTypeUtil.LOAN_PARAMETER_CODE) || StringUtils.equals(transType, TransTypeUtil.LOAN_NEW_PARAMETER_CODE))) {
				// 20220714 by 203990 保單貸款 新增寄送撤消通知
				/// 參考 /eservice_mergeProj/src/main/java/com/twfhclife/eservice/onlineChange/service/impl/OnlineChangeServiceImpl.java 296~306行
				onlineChangeService.cancelApplyTrans(transNum, hisVo);
				//發送 郵件
				String content = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "LOAN_MAIL_CONTENT")
						.replace("TRANS_NUM", transNum);
				String subject = "!!!"+OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CANCEL_TITLE+" > "+parameterService.getParameterValueByCode("eservice", "LOAN_MAIL_SUBJECT")
						.replace("TRANS_NUM", transNum);
				String email = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "LOAN_MAIL_ADDR");
				mailService.sendMail(content, subject, email, "", null);
			}
			else {
				onlineChangeService.cancelApplyTrans(transNum, hisVo);
			}
		} catch (Exception e) {
			logger.error("cancelApplyTrans error! {}", e);
		}

		return "frontstage/onlineChange/apply2";
	}

	/**
	 * 验证,取消申請是否可以进行取消此時STATUS=1
	 * @param transNum
	 * @return
	 */
	@RequestLog
	@PostMapping("/cancelApplyTransCheck")
	@ResponseBody
	public  List<String>  cancelApplyTransCheck(@RequestParam("transNum") String transNum){
		ArrayList<String> checkList = new ArrayList<>();
		//查询当前保单同批次的状态
	   String  check=	 transContactInfoService.getTransContactInfoTransNumCheck(transNum);
		checkList.add(check);
		return  checkList;
	}

	/**
	 * 補件
     *
	 * @param transNum
	 * @return
	 */
	@PostMapping("/beAddedTrans")
    public String beAddedTrans(@RequestParam("transNum") String transNum,
                               @RequestParam("file1") MultipartFile file1,
                               @RequestParam("file2") MultipartFile file2,
                               @RequestParam("file3") MultipartFile file3,
                               @RequestParam("file4") MultipartFile file4,
			@RequestParam("file5") MultipartFile file5) {
		String userId = getUserId();
        try {
			List<MultipartFile> uploadFiles = new ArrayList<>();
			if (file1 != null && !StringUtils.isEmpty(file1.getOriginalFilename())) {
				uploadFiles.add(file1);
			}
			if (file2 != null && !StringUtils.isEmpty(file2.getOriginalFilename())) {
				uploadFiles.add(file2);
			}
			if (file3 != null && !StringUtils.isEmpty(file3.getOriginalFilename())) {
				uploadFiles.add(file3);
			}
			if (file4 != null && !StringUtils.isEmpty(file4.getOriginalFilename())) {
				uploadFiles.add(file4);
			}
			if (file5 != null && !StringUtils.isEmpty(file5.getOriginalFilename())) {
				uploadFiles.add(file5);
			}

			if (!uploadFiles.isEmpty()) {
				Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
				String limitSizeStr = sysParamMap.get("SYSTEM_CONSTANTS").get("UPLOAD_FILE_SIZE_LIMIT").getParameterValue();
				boolean isAllow = onlineChangeService.checkFileSize(transNum, uploadFiles, limitSizeStr);
                if (isAllow) {
					onlineChangeService.addTransFile(transNum, uploadFiles, userId);
					onlineChangeService.beAddedTrans(transNum);
					addAttribute("isAllow", "success");
				} else {
					addAttribute("isAllow", limitSizeStr);
				}
			}
		} catch (Exception e) {
			logger.error("beAddedTrans error! {}", e);
		}

		return "frontstage/onlineChange/apply2";
	}

	/**
	 * 下載批註單PDF.
     *
	 * @param String transNum
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/getEndorsementPDF")
	public @ResponseBody HttpEntity<byte[]> getEndorsementPDF(String transNum, String transType) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=批註單-%s.pdf", transNum);
			String userId = getUserId();
            String str = onlineChangeService.getUserIdByTransNum(transNum, transType);
			if (str == null || !str.equals(userId)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			UsersVo userDetail = registerUserService.getUserByAccount(userId);
			String rocId = "";
			if (userDetail != null) {
				rocId = userDetail.getRocId();
			}

			document = onlineChangeService.getEndorsementPDF(transNum, rocId);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);

		} catch (Exception e) {
			logger.error("Unable to get data from downloadLoanPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}


	/** 20211118 by 203990
	 * 下載電子保單PDF.
     *
	 * @param String transNum
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/getEINPDF")
	public @ResponseBody HttpEntity<byte[]> getEINPDF(String policyNo) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=電子保單-%s.pdf", policyNo);
			String userId = getUserId();
            String str = onlineChangeService.getUserIdByPolicyNo(policyNo);
			if (str == null || !str.equals(userId)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			UsersVo userDetail = registerUserService.getUserByAccount(userId);
			String rocId = "";
			if (userDetail != null) {
				rocId = userDetail.getRocId();
			}

			document = onlineChangeService.getEINPDF(policyNo, rocId);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);

		} catch (Exception e) {
			logger.error("Unable to get data from downloadLoanPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}
	
	/**
	 * 檢查是否進入進入黑名單
	 * @param blackListVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/checkBackList")
	public ResponseEntity<ResponseObj> checkBackList(@RequestBody BlackListVo blackListVo) {
		try{
			Map<String, String> rMap = onlineChangeService.checkBackList(blackListVo);
			String detailInfo = rMap.get("detailInfo");
			String errMsg = rMap.get("errMsg");
			if (detailInfo != null) {
				String[] infos = detailInfo.split("\\|");
				errMsg = errMsg.replace("${NAME}", infos[0]);
				errMsg = errMsg.replace("${TRANS_CREATEDATE}", infos[2]);
				errMsg = errMsg.replace("${TRANS_NUM}", infos[1]);
				processError(errMsg);
			}else {
				processSuccess(1);
			}
		} catch (Exception e) {
			logger.error("checkBackList error! {}", e);
			processSystemError();
		}
		
		return processResponseEntity();
	}
	/**
	 * 檢查醫療是否進入進入黑名單
	 * @param blackListVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/checkMedicalBackList")
	public ResponseEntity<ResponseObj> checkMedicalBackList(@RequestBody BlackListVo blackListVo) {
		try{
			Map<String, String> rMap = onlineChangeService.checkMedicalBackList(blackListVo);
			String detailInfo = rMap.get("detailInfo");
			String errMsg = rMap.get("errMsg");
			if (detailInfo != null) {
				String[] infos = detailInfo.split("\\|");
				String transNum = infos[1];
				errMsg = errMsg.replace("${NAME}", infos[0]);
				errMsg = errMsg.replace("${TRANS_CREATEDATE}", infos[2]);
				errMsg = errMsg.replace("${TRANS_NUM}", transNum);
				/**
				 * 獲取TRANS_NUM查詢異常原因
				 */
				if (transNum!=null &&!"".equals(transNum)) {
					//查詢
					String reject_reason= iHospitalServcie.getTransStatusHistoryByRejectReason(transNum,OnlineChangeUtil.TRANS_STATUS_ABNORMAL);
					/**
					 * 查詢異常的名稱
					 */
					String parameterName= parameterService.getParameterByCategoryCodeParameterValue(ApConstants.SYSTEM_ID_ADM,TransTypeUtil.MEDICAL_ABNORMAL_REASON_MSG,reject_reason);
					if (org.springframework.util.StringUtils.isEmpty(parameterName)) {
						parameterName= OnlineChangMsgUtil.BACK_LIST_MSG;
					}
					errMsg = errMsg.replace("${MEDICAL_ABNORMAL_MESSAGE}", parameterName);
				}else {
					errMsg = errMsg.replace("${MEDICAL_ABNORMAL_MESSAGE}", OnlineChangMsgUtil.BACK_LIST_MSG);
				}
				processError(errMsg);
			}else {
				processSuccess(1);
			}
		} catch (Exception e) {
			logger.error("checkBackList error! {}", e);
			processSystemError();
		}

		return processResponseEntity();
	}

	/**
	 * 補件單歷程.
	 *
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransRFEList")
	public ResponseEntity<ResponseObj> getTransRFEList(@RequestBody TransRFEVo vo) {
		try {
			List<TransRFEVo> result = onlineChangeService.getTransRFEList(vo);
			if (result != null && result.size() != 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getTransRFEList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 验证,取消申請
	 *    如,已經推送至聯盟并且已經審核通過,則不能進行取消
	 *		SEND_ALLIANCE!=Y
	 * @param transNum
	 * @return
	 */
	@RequestLog
	@PostMapping("/transMedicalTreatmentClaimByCheck")
	@ResponseBody
	public  List<String>  transMedicalTreatmentClaimByCheck(@RequestParam("transNum") String transNum){
		ArrayList<String> checkList = new ArrayList<>();
		//查询当前保单同批次的状态
		String  check=	 transContactInfoService.transMedicalTreatmentClaimByCheck(transNum);
		checkList.add(check);
		return  checkList;
	}

		//發送郵件
	public void sendConversionSMSAndEmail(TransInvestmentVo vo, UsersVo user, String transType) {
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
			paramMap.put("POLICY_NO", StringUtils.isEmpty(vo.getPolicyNo()) ? " " : vo.getPolicyNo());
			logger.info("POLICY_NO : {}", vo.getPolicyNo());

			List<String> receivers = new ArrayList<String>();

			String applyDate = DateUtil.formatDateTime(vo.getApplyDate(), "yyyy年MM月dd日 HH時mm分ss秒");
			paramMap.put("DATA", applyDate);
			//申請狀態-申請中
			paramMap.put("TransStatus","已撤銷");

			paramMap.put("APPLICATION_FUNCTION", vo.getAuthType());


			//發送系統管理員
			receivers = (List) mailInfo.get("receivers");
			//推送管 理已接收 保單編號: [保單編號]  保戶[同意/不同意]轉送聯盟鏈
			messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_027, receivers, paramMap, "email");

			if (StringUtils.equals(transType, "DEPOSIT")) {
				receivers.clear();//清空
				Map<String, Object> depositMailInfo = transInvestmentService.getDepositMailInfo();
				//發送系統管理員
				receivers = (List) depositMailInfo.get("receivers");
				//推送管 理已接收 保單編號: [保單編號]  保戶[同意/不同意]轉送聯盟鏈
				messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_027, receivers, paramMap, "email");
			}

			//發送保戶SMS
			//receivers = new ArrayList<String>();
			receivers.clear();//清空
			paramMap.clear();//清空模板參數
			//設置模板參數
			paramMap.put("TITLE",vo.getTitle());
			paramMap.put("MESSAGE",vo.getMessage());
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
			logger.error("cancel apply send email error : {}", ExceptionUtils.getStackTrace(e));
		}
		logger.info("End send mail");
	}
	
	//發送郵件
	public void sendSMSAndEmailToManage(TransInvestmentVo vo, UsersVo user, String transType) {
		try {
			Map<String, Object> mailInfo = transInvestmentService.getElectronicFormSendMailInfo();
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
			// 获取保单编号
			paramMap.put("POLICY_NO", StringUtils.isEmpty(vo.getPolicyNo()) ? " " : vo.getPolicyNo());
			logger.info("POLICY_NO : {}", vo.getPolicyNo());

			List<String> receivers = new ArrayList<String>();

			String applyDate = DateUtil.formatDateTime(vo.getApplyDate(), "yyyy年MM月dd日 HH時mm分ss秒");
			paramMap.put("DATA", applyDate);
			// 申請狀態-申請中
			paramMap.put("TransStatus", "已撤銷");

			paramMap.put("APPLICATION_FUNCTION", vo.getAuthType());

			// 發送系統管理員
			receivers = (List) mailInfo.get("receivers");
			// 推送管 理已接收 保單編號: [保單編號] 保戶[同意/不同意]轉送聯盟鏈
			switch(transType) {
				case TransTypeUtil.ROLLOVER_PERIODICALLY :
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_034, receivers, paramMap,"email");
					break;
				case TransTypeUtil.DERATE_PAID_OFF :
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_032, receivers, paramMap,"email");
					break;
				case TransTypeUtil.ELECTRONIC_FORM_A_CODE :
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_030, receivers, paramMap,"email");
					break;
				case TransTypeUtil.ELECTRONIC_FORM_C_CODE :
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_030, receivers, paramMap,"email");
					break;
				default :
					break;
			}


//			if (StringUtils.equals(transType, "DEPOSIT")) {
//				receivers.clear();// 清空
//				Map<String, Object> depositMailInfo = transInvestmentService.getDepositMailInfo();
//				// 發送系統管理員
//				receivers = (List) depositMailInfo.get("receivers");
//				// 推送管 理已接收 保單編號: [保單編號] 保戶[同意/不同意]轉送聯盟鏈
//				messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_030, receivers, paramMap,
//						"email");
//			}

			// 發送保戶SMS
			// receivers = new ArrayList<String>();
//			receivers.clear();// 清空
//			paramMap.clear();// 清空模板參數
			// 設置模板參數 -取消申請暫時不發信通知申請人
//	    if(transType.equals(TransTypeUtil.ELECTRONIC_FORM_A_CODE)) {
//       	 //設置模板參數
//           receivers.add(user.getMobile());
//           logger.info("user phone : {}", user.getMobile());
//           messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_029, receivers, paramMap, "sms");
//           //發送保戶MAIL
//           if (user.getEmail() != null) {
//               receivers.clear();//清空
//               receivers.add(user.getEmail());
//               logger.info("user mail : {}", user.getEmail());
//               messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_029, receivers, paramMap, "email");	                
//           }
//       }else {
//       	 //設置模板參數
//           receivers.add(user.getMobile());
//           logger.info("user phone : {}", user.getMobile());
//           messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_036, receivers, paramMap, "sms");
//           //發送保戶MAIL
//           if (user.getEmail() != null) {
//               receivers.clear();//清空
//               receivers.add(user.getEmail());
//               logger.info("user mail : {}", user.getEmail());
//               messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_036, receivers, paramMap, "email");	                
//           }
//       }
		} catch (Exception e) {
			logger.error("cancel apply send email error : {}", ExceptionUtils.getStackTrace(e));
		}
		logger.info("End send mail");
	}

}
