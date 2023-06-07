package com.twfhclife.eservice.onlineChange.controller;

import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseBlackListVo;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseIpVo;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;
import com.twfhclife.eservice.onlineChange.model.OptionVo;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransChooseLevelVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.IndividualChooseService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.IndividualVo;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.BackgroundStaffRequest;
import com.twfhclife.generic.api_model.BackgroundStaffResponse;
import com.twfhclife.generic.api_model.LicohilResponse;
import com.twfhclife.generic.api_model.LicohilVo;
import com.twfhclife.generic.api_model.PolicyDetailRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.service.ISendAuthenticationService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.ValidateCodeUtil;

/**
 * 官網投資風險屬性評估
 * 
 * @author chiawei
 */

@Controller
@EnableAutoConfiguration
public class IndividualChooseController extends BaseController {

	private static final Logger logger = LogManager.getLogger(IndividualChooseController.class);

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private IndividualChooseService indivdualChooseService;

	@Autowired
	private ISendAuthenticationService sendAuthenticationService;

	@Autowired
	private IAttributeService attributeService;
	
	@Autowired
	private ITransRiskLevelService riskLevelService;

	@Autowired
	private ITransInvestmentService transInvestmentService;

	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;	

	public final static String GRANTTYPE = "password";
	
	public final static String REALM = "twfhclife";
	
	public final static String CLIENTID ="eservice_adm";
	
	private final static DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	/**
	 * 官網入口 - 基本資料填寫
	 */
	@RequestLog
	@GetMapping("/indivdualChoose")
	public String initIndivdualChoose( Model model) {		
		
		try {
			String ip = getClientIp();
			IndividualChooseBlackListVo vo= indivdualChooseService.getIndividualChooseBlackList(ip);
			if(vo == null) {
				getPdf(model);
			}else {
				if(!LocalDateTime.now().isAfter(vo.getBlackEnd())) {
					StringBuilder sb = new StringBuilder();
					sb.append("因短時間內多次發送簡訊，請於");
					sb.append(vo.getBlackEnd().format(formater));
					sb.append("  後再次使用，如造成您的不便敬請見諒。");
					addAttribute("ipStatus",  sb.toString());
				}else {
					getPdf(model);
				}
			}
				return "frontstage/onlineChange/indivdualChoose/indivdualChoose";
		} catch (Exception e) {
			logger.error("indivdualChoose error: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/indivdualChoose/indivdualChoose";
	}
	
	/**
	 * 官網入口 - 基本資料填寫
	 */
	@RequestLog
	@PostMapping("/indivdualChoose0")
	public String IndivdualChoose0(@ModelAttribute IndividualChooseVo individualChooseVo, BindingResult bindingResult , Model model) {
		try {
			if(individualChooseVo.isReadPdfType()) {
				getScreenDate();
				// 設定驗證碼圖示
				ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
				addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
				model.addAttribute("validateImageBase64", vcUtil.imgToBase64String());
				model.addAttribute("individualChooseVo", individualChooseVo);
				return "frontstage/onlineChange/indivdualChoose/indivdualChoose0";
			}else {
				addSystemError("系統錯誤，請洽詢本公司客服電話 0800-011-966");
				getPdf(model);
				return "frontstage/onlineChange/indivdualChoose/indivdualChoose";
			}
		} catch (Exception e) {
			logger.error("indivdualChoose1 error: {}", ExceptionUtils.getStackTrace(e));
			addSystemError("系統錯誤，請洽詢本公司客服電話 0800-011-966");
			getPdf(model);
			return "frontstage/onlineChange/indivdualChoose/indivdualChoose";
		}
	}
	/**
	 * 驗證身分-查看是否為本公司客戶
	 * 20230607 by 203990 獨立填寫頁, 不做其它資料驗證
	 */
	@RequestLog
	@PostMapping("/indivdualChoose1")
	public String indivdualChoose1(IndividualChooseVo individualChooseVo, BindingResult bindingResult) {
		try {
			String validateSessionCode = getSession(ApConstants.LOGIN_VALIDATE_CODE).toString();
			if(!validateSessionCode.equals(individualChooseVo.getValidateCode())) {
				addSystemError("圖型驗證碼錯誤。");
				individualChooseVo.setValidateCode(null);
				addAttribute("individualChooseVo", individualChooseVo);
				getScreenDate();
				return "frontstage/onlineChange/indivdualChoose/indivdualChoose0";
			}
			
			//top 1.查詢是否有申請過風險變更屬性
			IndividualChooseVo oldIndividualChooseVo = indivdualChooseService.getIndividualChoosData(individualChooseVo.getRocId());
			
			PolicyDetailRequest request = new PolicyDetailRequest();
			request.setRocId(individualChooseVo.getRocId());			
			request.setPolicyInvestmentType(indivdualChooseService.getpolicyInvestmentType());
			LicohilResponse response = transCtcSelectUtilClient.getLicohiByPolicyNo(request);
			if (response.getLicohilVo().size() != 0) {
				individualChooseVo.setChooseType(true);//是公司客戶
				//既有客戶
				LicohilVo vo = new LicohilVo();
				//查詢是否有投資型保單
				vo = indivdualChooseService.getpolicyHaveInvestmentType(response.getLicohilVo());
				if(vo == null) {
					//沒有投資型保單
					if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
							StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
						vo = indivdualChooseService.getpolicyNotHaveInvestmentType(response.getLicohilVo());
						String msg = checkDetail(individualChooseVo , vo);
						if(StringUtils.isNotEmpty(msg)) {
							return msg;
						}
					}
					if(oldIndividualChooseVo != null) {
						//評估一年內是否有申請過風險屬性變更
						if(!indivdualChooseService.checkRatingDate2(oldIndividualChooseVo.getRatingDate())) {
							individualChooseVo.setRiskAttr(oldIndividualChooseVo.getRiskAttr());
							individualChooseVo.setRiskDateType(false);
						}
					}
				} else {
					// 有投資型保單 需額外驗證資料
					if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
							StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
						String msg = checkDetail(individualChooseVo, vo);
						if (StringUtils.isNotEmpty(msg)) {
							return msg;
						}
					}
					//判斷之前是否已經做過風險評估
					individualChooseVo = indivdualChooseService.verificationDate(individualChooseVo , vo , oldIndividualChooseVo);
				}
			} else {
				response = transCtcSelectUtilClient.getLilipmByPolicyNo(request);
				if(response.getLicohilVo().size() != 0) {
					individualChooseVo.setChooseType(true);//是公司客戶
					//既有客戶
					LicohilVo vo = new LicohilVo();
					//查詢是否有投資型保單
					vo = indivdualChooseService.getpolicyHaveInvestmentType(response.getLicohilVo());
					if(vo == null) {
						//沒有投資型保單
						if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
								StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
							vo = indivdualChooseService.getpolicyNotHaveInvestmentType(response.getLicohilVo());
							String msg = checkDetail(individualChooseVo , vo);
							if(StringUtils.isNotEmpty(msg)) {
								return msg;
							}
						}
						if(oldIndividualChooseVo != null) {
							//評估一年內是否有申請過風險屬性變更
							if(!indivdualChooseService.checkRatingDate2(oldIndividualChooseVo.getRatingDate())) {
								individualChooseVo.setRiskAttr(oldIndividualChooseVo.getRiskAttr());
								individualChooseVo.setRiskDateType(false);
							}
						}
					}else {
						//有投資型保單 需驗證資料
						if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
								StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
							String msg = checkDetail(individualChooseVo , vo);
							if(StringUtils.isNotEmpty(msg)) {
								return msg;
							}
						}
						//驗證日期:評估上次申請風險評估 是否已滿一年
						individualChooseVo = indivdualChooseService.verificationDate(individualChooseVo , vo , oldIndividualChooseVo);
					}
				}else {
					//非既有客戶
					//top 1.判斷風險屬性是否有紀錄 有: 需驗證畫面資料 、 無直接手機OTP
					if(oldIndividualChooseVo != null) {
						//top 2.當有申請過風險評估 需驗證資料是否正常
						if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
								StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
							String msg = indivdualChooseService.checkDetail2(oldIndividualChooseVo, individualChooseVo);
							if (StringUtils.isNotEmpty(msg)) {
								addSystemError(msg);
								addAttribute("individualChooseVo", individualChooseVo);
								getScreenDate();
								return "frontstage/onlineChange/indivdualChoose/indivdualChoose0";
							}
						}
						//top 3.評估一年內是否有申請過風險屬性變更
						if(!indivdualChooseService.checkRatingDate2(oldIndividualChooseVo.getRatingDate())) {
							individualChooseVo.setRiskAttr(oldIndividualChooseVo.getRiskAttr());
							individualChooseVo.setRiskDateType(false);
						}
					}
				}						
			}
			if(StringUtils.isNotEmpty(individualChooseVo.getEmployeeId()) &&
					StringUtils.isNotEmpty(individualChooseVo.getEmployeePassword())) {
				return	indivdualChoose2(individualChooseVo , bindingResult);
			}else {
				return getMobileCode(individualChooseVo);				
			}
		} catch (Exception e) {
			logger.error("indivdualChoose1 error: {}", ExceptionUtils.getStackTrace(e));
			addSystemError("系統錯誤，請洽詢本公司客服電話 0800-011-966");
			addAttribute("individualChooseVo", individualChooseVo);
			getScreenDate();
			return "frontstage/onlineChange/indivdualChoose/indivdualChoose0";
		}		
	}
	/**
	 * 寄送認證碼  / 輸入的使用者
	 * @return
	 */
	@RequestLog
	@PostMapping("/indivdualChooseSendAuthentication")
	public ResponseEntity<ResponseObj> chooseSendAuthentication(@RequestParam(value = "authenticationType",required = false)   String authenticationType,
														  @RequestParam(value = "newMobile",required = false)   String newMobile ,
														  @RequestParam(value = "rocId",required = false)   String rocId
														  ) {
		logger.info("call sendAuthentication authenticationType="+authenticationType);
		logger.info("call sendAuthentication mobile="+newMobile);
		String mobile = "";
		try {
			//mobiles
			StringBuffer mobiles = new StringBuffer();
			if(mobile!=null && !"".equals(mobile.trim())){
				mobiles.append(mobile.trim());
			}
			if(newMobile!=null && !"".equals(newMobile.trim())  && !mobile.equals(newMobile)){
				if(!StringUtils.isBlank(mobile)){
				mobiles.append(";");
				mobiles.append(newMobile);
				}else{
					mobiles.append(newMobile);
				}
			}
			List<ParameterVo> parameterList = parameterService.getParameterByCategoryCode("eservice", OnlineChangeUtil.BLACK_LIST_CODE);
			String count ="30";// 預設
			int time =- 5 ; //預設
			for(ParameterVo vo : parameterList) {
				if(vo.getParameterCode().equals(OnlineChangeUtil.LIST_COUNT)) {
					count = vo.getParameterValue();
				}else if(vo.getParameterCode().equals(OnlineChangeUtil.LIST_TIME)) {
					time = Integer.valueOf(vo.getParameterValue());
				}
			}
			String ip = getClientIp();
			List<IndividualChooseIpVo> ipList= indivdualChooseService.getIndividualChooseIp(ip ,time);
			if(ipList.size() > Integer.valueOf(count)) {
				indivdualChooseService.insertOrUpdateIndividualChooseBlackList(ip);
				StringBuilder sb = new StringBuilder();
				sb.append("因短時間內多次發送簡訊，請於");
				sb.append(LocalDateTime.now().plusHours(6).format(formater));
				sb.append("後再次使用，如造成您的不便敬請見諒。");
				addAttribute("ipStatus",  sb.toString());
				this.setResponseObj(ResponseObj.ERROR, sb.toString(), null);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			String authentication = sendAuthenticationService.sendAuthentication(null, mobiles.toString());
			logger.info("sendAuthentication authentication(otp code)="+authentication);

			//紀錄驗證碼與時間
			addSession(authenticationType+"Authentication", authentication);
			addSession(authenticationType+"AuthenticationTime", new Date());

			// 20210421 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
			addSession(authenticationType+"AuthenticationCheckCounts", getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));
			if(StringUtils.isEmpty(authentication)) {
				this.setResponseObj(ResponseObj.ERROR, "簡訊發送失敗", null);
			}else {
				this.setResponseObj(ResponseObj.SUCCESS, "", null);		
				insertIndividualChooseIp(rocId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			/*this.setResponseObj(ResponseObj.ERROR, "寄送驗證碼失敗!", null);*/
			this.setResponseObj(ResponseObj.ERROR, getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0117"), null);
		}		
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	/**
	 * 手機OTP + 資料確認
	 */
	public String getMobileCode(IndividualChooseVo individualChooseVo) {
		try {
			String ip = getClientIp();
			List<ParameterVo> parameterList = parameterService.getParameterByCategoryCode("eservice", OnlineChangeUtil.BLACK_LIST_CODE);
			String count ="30";// 預設
			int time = -5 ; //預設
			for(ParameterVo vo : parameterList) {
				if(vo.getParameterCode().equals(OnlineChangeUtil.LIST_COUNT)) {
					count = vo.getParameterValue();
				}else if(vo.getParameterCode().equals(OnlineChangeUtil.LIST_TIME)) {
					time = Integer.valueOf(vo.getParameterValue());
				}
			}
			List<IndividualChooseIpVo> ipList= indivdualChooseService.getIndividualChooseIp(ip ,time);
			if(ipList.size() > Integer.valueOf(count)) {
				indivdualChooseService.insertOrUpdateIndividualChooseBlackList(ip);
				StringBuilder sb = new StringBuilder();
				sb.append("因短時間內多次發送簡訊，請於");
				sb.append(LocalDateTime.now().plusHours(6).format(formater));
				sb.append("後再次使用，如造成您的不便敬請見諒。");
				addAttribute("ipStatus",  sb.toString());
				return "frontstage/onlineChange/indivdualChoose/indivdualChoose1";
			}
			addAttribute("individualChooseTimeSet", 300);
			addAttribute("individualChooseVo", individualChooseVo);						  
			addAttribute("authenticationType", "indivdualChoose");
			sendMobileCode("indivdualChoose", individualChooseVo.getMobile());
			insertIndividualChooseIp(individualChooseVo.getRocId());
		} catch (Exception e) {
			logger.error("indivdualChoose1 error: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/indivdualChoose/indivdualChoose1";
	}

	/**
	 * 手機OTP + 資料確認
	 */
	@RequestLog
	@PostMapping("/indivdualChoose2")
	public String indivdualChoose2(IndividualChooseVo individualChooseVo, BindingResult bindingResult) {
		try {
			if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
					StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
				String authentication = getSession("indivdualChooseAuthentication").toString();
				if (!authentication.equals(individualChooseVo.getAuthenticationNum())) {
					addSystemError("驗證碼錯誤");
					addAttribute("individualChooseTimeSet", 300);
					addAttribute("individualChooseVo", individualChooseVo);
					return "frontstage/onlineChange/indivdualChoose/indivdualChoose1";
				}
			}
			//新增判斷 其他評估項目應該是檢核以下三個條件之一時才需勾選
			//top 1.針對年齡為65歲以上
			int systemDate = LocalDate.now().getYear();
			if(systemDate - Integer.valueOf(individualChooseVo.getBirthDate().substring(0, 4)) >= 65) {
				individualChooseVo.setShowPup(true);
			}
			//top 2.教育程度國中畢業(含)以下
			if(individualChooseVo.getEducationLevel().equals(parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, TransTypeUtil.EDUCATION_LEVEL_CODE))) {
				individualChooseVo.setShowPup(true);
			}
			//top 3.近期有重大醫療支出、高額定期醫療或長期照顧支出
			if(individualChooseVo.getDisability().equals(OnlineChangeUtil.YES)) {
				individualChooseVo.setShowPup(true);
			}
			//測試用
//				individualChooseVo.setUsersType(true);//是公司客戶
//				individualChooseVo.setShowPup(true);
			if(!individualChooseVo.isRiskDateType()) {
				return moveToFinishIndivdualChoose(individualChooseVo , individualChooseVo.getRiskAttr());
			}else {
					return fillInInformation(individualChooseVo);
			}
		} catch (Exception e) {
			logger.error("indivdualChoose2 error: {}", ExceptionUtils.getStackTrace(e));
			addSystemError("系統錯誤，請洽詢本公司客服電話 0800-011-966");
			addAttribute("individualChooseVo", individualChooseVo);
			getScreenDate();
			return "frontstage/onlineChange/indivdualChoose/indivdualChoose0";
		}		
	}

	@RequestLog
	@PostMapping("/indivdualChoose3")
	public String indivdualChoose3(RedirectAttributes redirectAttributes, IndividualChooseVo individualChooseVo,
			BindingResult bindingResult) {

		try {
			List<QuestionVo> questions = attributeService.getQuestions();
			String flag = checkChoose(questions, individualChooseVo.getAnswers());
			if (StringUtils.isNotEmpty(flag)) {
				flag = flag.substring(0, flag.length() - 1);
				addSystemError("第" + flag + "題目尚未填寫資料，請確實填寫，謝謝");
				addAttribute("individualChooseVo", individualChooseVo);
				return "forward:indivdualChoose2";
			}
			return checkQuestion(individualChooseVo);
		} catch (Exception e) {
			logger.error("indivdualChoose3 error: {}", ExceptionUtils.getStackTrace(e));
			return "forward:indivdualChoose";
		}
	}
	
	@RequestLog
	@PostMapping("/indivdualChoose4")
	public String indivdualChoose4(RedirectAttributes redirectAttributes, IndividualChooseVo individualChooseVo,
			BindingResult bindingResult) {

		try {
			List<QuestionVo> questions = attributeService.getQuestions();
			calculateScore(questions, individualChooseVo);
		} catch (Exception e) {
			logger.error("indivdualChoose3 error: {}", ExceptionUtils.getStackTrace(e));
			return "forward:indivdualChoose";
		}

		String riskLevel = riskLevelService.computeRiskLevel(Integer.valueOf(individualChooseVo.getScore()));
		if (!individualChooseVo.isOptionsType()) {
			individualChooseVo.setRiskAttr("D"); // 若 /8、9選項是A 則紀錄為D
			individualChooseVo.setOptionsMsg("【經評估結果，投資型商品不適合您，如您需要其他商品，請洽詢本公司客服電話0800-011-966】");
		} else {
			if(individualChooseVo.getRuleStatus().equals("1")) {
				individualChooseVo.setRiskAttr("A"); //檢核規則若不允許 評級 = A
			}else {
				individualChooseVo.setRiskAttr(riskLevel);
			}
		}

		String desc = transInvestmentService.transRiskLevelToName(individualChooseVo.getRiskAttr());
		individualChooseVo.setDesc(desc);
		 //20230411新增 預防使用者按F5 或者 上一頁重複新增
		boolean ckeckVoStatus = true;
		IndividualChooseVo	checkVo = indivdualChooseService.getIndividualChoosData(individualChooseVo.getRocId());
		if(checkVo != null) {
			if(indivdualChooseService.checkRatingDate(checkVo.getRatingDate())) {
				indivdualChooseService.insertIndividualChoose(individualChooseVo);
			}else {
				ckeckVoStatus = false;
			}
		}else {
			indivdualChooseService.insertIndividualChoose(individualChooseVo);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("【完成時間(");
		if(!ckeckVoStatus) {
			sb.append(new SimpleDateFormat("yyyy-MM-dd").format(checkVo.getRatingDate()));
			individualChooseVo.setName(checkVo.getName()); // 20230602 by 203990, 查詢風險評估結果顯示的姓名以資料庫記錄的姓名來呈現
		}else {
			sb.append(LocalDate.now());
		}
		sb.append(")，及本評估問卷結果有效期限為一年】");
        addAttribute("finishDateTime" ,sb.toString());   
		addAttribute("individualChooseVo", individualChooseVo);
		addAttribute("analysisStatement",
				parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_ATTRIBUTE_ANALYSIS_STATEMENT"));
		return "frontstage/onlineChange/indivdualChoose/indivdualChoose4";
	}

	/**
	 * 取得手機 OTP
	 */
	protected void sendMobileCode(String authenticationType, String mobile) {
		try {
			String authentication = sendAuthenticationService.sendAuthentication(null, mobile);
			logger.info("sendAuthentication authentication(otp code)=" + authentication);
			// 紀錄驗證碼與時間
			addSession(authenticationType + "Authentication", authentication);
			addSession(authenticationType + "AuthenticationTime", new Date());
			addSession(authenticationType + "AuthenticationCheckCounts",
					getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));
		} catch (Exception e) {
			logger.error("sendAuthCode error: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	private String checkChoose(List<QuestionVo> questions, List<String> answers) {
		String flag = "";
		if (!CollectionUtils.isEmpty(questions)) {
			a: for (QuestionVo question : questions) {
				for (OptionVo option : question.getOptions()) {
					if (answers.contains(String.valueOf(option.getId()))) {
						continue a;
					}
				}
				flag += question.getId() + "、";
			}
			return flag;
		}
		return flag;
	}

	private void calculateScore(List<QuestionVo> questions, IndividualChooseVo vo) throws Exception {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		Map<Long, Integer> scoreMap = Maps.newHashMap();
//    	scoreMap.put((long) 2, 0);//預設
		for (QuestionVo qVo : questions) {
			for (OptionVo oVo : qVo.getOptions()) {
				if (vo.getAnswers().contains(String.valueOf(oVo.getId()))) {
					Integer oScore = scoreMap.get(qVo.getId());
					Integer tmpScore = oVo.getScore().intValue();
					if (oScore != null && oScore > tmpScore) {
						tmpScore = oScore;
					}
					if (String.valueOf(qVo.getId()).equals("1")) {
						sb.append(qVo.getId());
						sb.append(".");
						sb.append(qVo.getQuestion());
						sb.append(System.lineSeparator());
						sb.append(" Ans:");
						sb.append(oVo.getItem());
						sb.append("。 ");
						sb.append(System.lineSeparator());
						sb.append(oVo.getScore());
						sb.append("分");
						sb.append(System.lineSeparator());
					} else {
						sb1.append(qVo.getId());
						sb1.append(".");
						sb1.append(qVo.getQuestion());
						sb1.append(System.lineSeparator());
						sb1.append(" Ans:");
						sb1.append(oVo.getItem());
						sb1.append("。 ");
						sb1.append(System.lineSeparator());
						sb1.append(oVo.getScore());
						sb1.append("分");
						sb1.append(System.lineSeparator());
					}
                    if(qVo.getId() == 3 ||  qVo.getId() == 4) {
                    	int score = scoreMap.get((long) 2);
                    	//若第二題的得分為 0 則 第三 與第四題的評分 一率為0
                    	if(score != 0) {
                    		scoreMap.put(qVo.getId(), tmpScore);  
                    	}else {
                    		scoreMap.put(qVo.getId(), 0);  
                    	}
                    }else {
                    	scoreMap.put(qVo.getId(), tmpScore);
                    }
					if (StringUtils.isNotEmpty(oVo.getCancelFlag())) {
						vo.setOptionsType(false);
					}
					if (StringUtils.isNotEmpty(oVo.getRiskFlag())) {
						vo.setRiskType(false);
					}
				}
			}
		}
		int finalScore = 0;
		for (Map.Entry<Long, Integer> entry : scoreMap.entrySet()) {
			int tmpScore = entry.getValue();
			if (StringUtils.equals(String.valueOf(entry.getKey()), "6") && entry.getValue().equals(0)) {
				throw new Exception("不適合購買投資型保單");
			}
			finalScore += tmpScore;
		}
		vo.setScore(String.valueOf(finalScore));
		if (!vo.isRiskType()) {
			if (finalScore >= 42) {
				vo.setScore("42");
			}
		}
		sb.append(sb1.toString());
		vo.setChoose(sb.toString());
	}

	/**
	 * 不須重新評估風險等級
	 * @param individualChooseVo
	 * @param attr 風險等級
	 * @return
	 */
	private String moveToFinishIndivdualChoose(IndividualChooseVo individualChooseVo, String attr) {
		StringBuilder sb = new StringBuilder();
		IndividualChooseVo selectVo = indivdualChooseService.getIndividualChoosData(individualChooseVo.getRocId());
		if(selectVo != null) {
			String desc = transInvestmentService.transRiskLevelToName(attr);
			if ("".equals(desc) && attr.equals("D")) {
				individualChooseVo.setDesc("您不適合投資型");
			} else {
				individualChooseVo.setDesc(desc);
			}
			individualChooseVo.setRiskAttr(attr);
			individualChooseVo.setName(selectVo.getName());
			sb.append("【完成時間(");
			sb.append(selectVo.getRatingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			sb.append(")，及本評估問卷結果有效期限為一年】");
		}else {
			LicohilVo vo = new LicohilVo();
			PolicyDetailRequest request = new PolicyDetailRequest();
			request.setRocId(individualChooseVo.getRocId());			
			request.setPolicyInvestmentType(indivdualChooseService.getpolicyInvestmentType());
			LicohilResponse response = transCtcSelectUtilClient.getLicohiByPolicyNo(request);
			if(response.getLicohilVo().size() != 0) {
				vo =response.getLicohilVo().get(0);
			}else {
				response = transCtcSelectUtilClient.getLilipmByPolicyNo(request);
				vo =response.getLicohilVo().get(0);
			}
			String desc = transInvestmentService.transRiskLevelToName(vo.getInveAttr());
			if ("".equals(desc) && attr.equals("D")) {
				individualChooseVo.setDesc("您不適合投資型");
			} else {
				individualChooseVo.setDesc(desc);
			}
			individualChooseVo.setRiskAttr(vo.getInveAttr());
			individualChooseVo.setName(vo.getLipmName1());
			sb.append("【完成時間(");
			sb.append(vo.getRatingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			sb.append(")，及本評估問卷結果有效期限為一年】");	
		}
        addAttribute("finishDateTime" ,sb.toString());  
		addAttribute("individualChooseVo", individualChooseVo);
		addAttribute("analysisStatement",
				parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_ATTRIBUTE_ANALYSIS_STATEMENT"));
		addAttribute("notReassess", "您前次評估未滿一年，毋須重新評估");
		return "frontstage/onlineChange/indivdualChoose/indivdualChoose4";
	}

	/**
	 * 需要重新評估風險等級
	 * @param individualChooseVo
	 * @return
	 */
	private String fillInInformation(IndividualChooseVo individualChooseVo) {
		try {
			List<QuestionVo> questions = attributeService.getQuestions();
			List<String> questionIdList = new ArrayList<>();
			List<String> answers = new ArrayList<>();
			List<String> oldAnswers = individualChooseVo.getAnswers();
			questions = questions.stream().filter(x -> {
				questionIdList.add(String.valueOf(x.getId()));
				String questionstr = x.getQuestion();
				int str = questionstr.indexOf("<br>");
				if (str > 0) {
					x.setQuestion(questionstr.substring(0, str));
					String question2 = questionstr.substring(str + 4 );					
					
					int str2 = question2.indexOf("<br>");
					if(str2 > 0 ) {
						x.setQuestion2(question2.substring(0 , str2).replace("<br>", ""));
						x.setQuestion3(question2.substring(str2).replace("<br>", ""));
					}else {
						x.setQuestion2(question2);
					}
				}
				// 預設生日選項 不給使用者調整
				if (x.getId() == 1) {
					int optionsIndex = getBirDateRule(individualChooseVo.getBirthDate());
					if (optionsIndex < 7) {
						x.getOptions().get(optionsIndex - 1).setChecked(true);
						answers.add(String.valueOf(x.getOptions().get(optionsIndex - 1).getId()));
					}
				}else {
					if(oldAnswers.size() > 1) {
						List<OptionVo> optionsList = x.getOptions();
						for(int i = 0 ; i < optionsList.size() ; i ++) {
							if(oldAnswers.contains(optionsList.get(i).getId().toString())) {
								x.getOptions().get(i).setChecked(true);
							}
						}	
					}					
				}
				return true;
			}).collect(Collectors.toList());
			individualChooseVo.setAnswers(answers);
			addAttribute("questionIdList", questionIdList);
			addAttribute("individualChooseVo", individualChooseVo);
			addAttribute("questions", questions);
			if (individualChooseVo.isUsersType()) {
				addAttribute("userType", true);
			} else {
				addAttribute("userType", false);
			}
		} catch (Exception e) {
			logger.error("fillInInformation error: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/indivdualChoose/indivdualChoose2";
	}
	
	/**
	 * 資料確認畫面
	 * @param individualChooseVo
	 * @return
	 */
	private String checkQuestion(IndividualChooseVo individualChooseVo) {
		try {
			List<QuestionVo> questions = attributeService.getQuestions();
			List<String> questionIdList = new ArrayList<>();
			List<String> answers = new ArrayList<>();
			List<String> oldAnswers = individualChooseVo.getAnswers();
			questions = questions.stream().filter(x -> {
				questionIdList.add(String.valueOf(x.getId()));
				String questionstr = x.getQuestion();
				int str = questionstr.indexOf("<br>");
				if (str > 0) {
					x.setQuestion(questionstr.substring(0, str));
					String question2 = questionstr.substring(str + 4 );					
					
					int str2 = question2.indexOf("<br>");
					if(str2 > 0 ) {
						x.setQuestion2(question2.substring(0 , str2).replace("<br>", ""));
						x.setQuestion3(question2.substring(str2).replace("<br>", ""));
					}else {
						x.setQuestion2(question2);
					}
				}
				// 預設生日選項 不給使用者調整
				if (x.getId() == 1) {
					int optionsIndex = getBirDateRule(individualChooseVo.getBirthDate());
					if (optionsIndex < 7) {
						x.getOptions().get(optionsIndex - 1).setChecked(true);
						answers.add(String.valueOf(optionsIndex));
					}
				}else {
					List<OptionVo> optionsList = x.getOptions();
					for(int i = 0 ; i < optionsList.size() ; i ++) {
						if(oldAnswers.contains(optionsList.get(i).getId().toString())) {
							x.getOptions().get(i).setChecked(true);
						}
					}
				}
				return true;
			}).collect(Collectors.toList());
			individualChooseVo.setAnswers(oldAnswers);
			addAttribute("questionIdList", questionIdList);
			addAttribute("individualChooseVo", individualChooseVo);
			addAttribute("questions", questions);
			
		} catch (Exception e) {
			logger.error("checkQuestion error: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/indivdualChoose/indivdualChoose3";
	}

	/**
	 * 台銀-保戶驗證資料
	 * @param individualChooseVo
	 * @param licohilVo
	 */
	private String checkDetail(IndividualChooseVo individualChooseVo, LicohilVo licohilVo) {
		String msg = indivdualChooseService.checkDetail(licohilVo, individualChooseVo);
		if (StringUtils.isNotEmpty(msg)) {
			addSystemError(msg);
			addAttribute("individualChooseVo", individualChooseVo);
			getScreenDate();
			if(StringUtils.isEmpty(individualChooseVo.getEmployeeId()) &&
					StringUtils.isEmpty(individualChooseVo.getEmployeePassword())) {
				return "frontstage/onlineChange/indivdualChoose/indivdualChoose0";
			}else {
				return "frontstage/onlineChange/indivdualChoose/indivdualChooseForStaff";
			}
		}
		return "";
	}

	private int getBirDateRule(String bir) {
		LocalDate sysDate = LocalDate.now();
		int birDate = sysDate.getYear() - Integer.valueOf(bir.substring(0, 4));
		if (birDate > 64) {
			return 1;
		} else if (birDate > 55 && birDate < 65) {
			return 2;
		} else if (birDate > 45 && birDate < 56) {
			return 3;
		} else if (birDate > 35 && birDate < 46) {
			return 4;
		} else if (birDate > 18 && birDate < 36) {
			return 5;
		} else if (birDate < 19) {
			return 6;
		} else {
			return 7;
		}
	}
	
	/**
	 * 基本資料填寫
	 * 人壽職員填寫
	 */
	@RequestLog
	@GetMapping("/indivdualChooseForStaff")
	public String indivdualChooseForStaff() {
		try {
			List<ParameterVo> educationList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.EDUCATION_LEVEL);

			List<ParameterVo> incomeList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.INCOME);

			List<ParameterVo> disabilityList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.CUSHION_TYPE);
			
			// 設定驗證碼圖示
			ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
			addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
			addAttribute("validateImageBase64", vcUtil.imgToBase64String());
			
			addAttribute("educationList", educationList);
			addAttribute("incomeList", incomeList);
			addAttribute("disabilityList", disabilityList);
			addAttribute("individualChooseVo", new IndividualChooseVo());
		} catch (Exception e) {
			logger.error("indivdualChoose error: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/indivdualChoose/indivdualChooseForStaff";
	}
	
	/**
	 * 手機OTP + 資料確認
	 */
	@RequestLog
	@PostMapping("/indivdualChooseForStaff1")
	public String indivdualChooseForStaff1(IndividualChooseVo individualChooseVo, BindingResult bindingResult) {
		try {
			//TOP 驗證驗證碼是否正確
			String validateSessionCode = getSession(ApConstants.LOGIN_VALIDATE_CODE).toString();
			if(!validateSessionCode.equals(individualChooseVo.getValidateCode())) {
				addSystemError("圖型驗證碼錯誤。");
				individualChooseVo.setValidateCode(null);
				addAttribute("individualChooseVo", individualChooseVo);
				getScreenDate();
				return "frontstage/onlineChange/indivdualChoose/indivdualChooseForStaff";
			}
			
			//TOP 2 驗證 員工編號 是否 符合
			boolean status = getStaffStatus(individualChooseVo);
			if(!status) {
				addSystemError("登入帳號密碼有誤。");
				addAttribute("individualChooseVo", individualChooseVo);
				getScreenDate();
				return "frontstage/onlineChange/indivdualChoose/indivdualChooseForStaff";
			}
			//TOP 3 驗證輸入要保人資訊
			return	indivdualChoose1(individualChooseVo , bindingResult);
		} catch (Exception e) {
			logger.error("indivdualChooseForStaff1 error: {}", ExceptionUtils.getStackTrace(e));
			addSystemError("系統異常，請洽詢本公司客服電話 0800-011-966");
			addAttribute("individualChooseVo", individualChooseVo);
			getScreenDate();
			return "frontstage/onlineChange/indivdualChoose/indivdualChooseForStaff";
		}
	}
	
	private void getScreenDate() {
		List<ParameterVo> educationList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.EDUCATION_LEVEL);

		List<ParameterVo> incomeList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.INCOME);

		List<ParameterVo> disabilityList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.CUSHION_TYPE);

		addAttribute("educationList", educationList);
		addAttribute("incomeList", incomeList);
		addAttribute("disabilityList", disabilityList);
	}
	
	private void getPdf(Model model) {
		List<ParameterVo> parameterList = parameterService.getParameterByCategoryCode("eservice", OnlineChangeUtil.BACKGROUND_STAFF_CODE);
		String pdfName = "";
		String pdfPath = "";
		for(ParameterVo vo : parameterList) {
			if(vo.getParameterCode().equals(OnlineChangeUtil.STAFF_PDFPATH)) {
				pdfPath = vo.getParameterValue();
			}else if(vo.getParameterCode().equals(OnlineChangeUtil.STAFF_PDFNAME)) {
				pdfName = vo.getParameterValue();
			}
		}
		if(StringUtils.isNotEmpty(pdfPath) && StringUtils.isNotEmpty(pdfName)) {
			String file = pdfPath + pdfName;
			model.addAttribute("indivdualChooseConsent", file);
			model.addAttribute("individualChooseVo", new IndividualChooseVo());
		}else {
			addSystemError("系統錯誤，請洽詢本公司客服電話 0800-011-966");
		}
	}
	
	private boolean getStaffStatus(IndividualChooseVo individualChooseVo) {
		try {
		String url = "";
		String token ="";
		String account = "";
		List<ParameterVo> parameterList = parameterService.getParameterByCategoryCode("eservice", OnlineChangeUtil.BACKGROUND_STAFF_CODE);
		for(ParameterVo vo : parameterList) {
			if(vo.getParameterCode().equals(OnlineChangeUtil.STAFF_URL)) {
				url = vo.getParameterValue();
			}else if(vo.getParameterCode().equals(OnlineChangeUtil.STAFF_TOKEN)) {
				token = vo.getParameterValue();
			}else if(vo.getParameterCode().equals(OnlineChangeUtil.STAFF_ACCOUNT)) {
				account = vo.getParameterValue();
			}
		}
		if(StringUtils.isNotEmpty(account)) {
			String [] accountList= account.split(",");
			List<String> accountLists = Arrays.asList(accountList);
			if(!accountLists.contains(individualChooseVo.getEmployeeId())) {
				return false;
			}
		}else {
			return false;
		}
		BackgroundStaffRequest request = new BackgroundStaffRequest();

		request.setUsername(individualChooseVo.getEmployeeId());
		request.setPassword(individualChooseVo.getEmployeePassword());
		request.setGrantType(GRANTTYPE);
		request.setRealm(REALM);
		request.setClientId(CLIENTID);
		Gson gson = new Gson();
		String json = gson.toJson(request);

		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
				NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(scsf).build();
		
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json, "UTF-8");
		httpPost.setEntity(entity);
		httpPost.setHeader("Authorization", "Bearer " + token);
		httpPost.setHeader("Content-type", "application/json");

		CloseableHttpResponse response = client.execute(httpPost);
		if (response != null) {
			String body = EntityUtils.toString(response.getEntity(), "UTF-8");
			if(StringUtils.isNotEmpty(body)) {
				ObjectMapper mapper = new ObjectMapper();
				BackgroundStaffResponse backgroundStaffResponse = mapper.readValue(body, BackgroundStaffResponse.class);
				if(backgroundStaffResponse.getReturnHeader() !=null) {
					if(backgroundStaffResponse.getReturnHeader().getReturnCode().equals(OnlineChangeUtil.RETURNCODE)) {
						return true;
					}
				}
			}
		}
		response.close();
		client.close();	
	}catch (Exception e) {
		logger.error("indivdualChooseForStaff1 error: {}", ExceptionUtils.getStackTrace(e));
		return false;
	}
		return false;	
	}
	
	private void insertIndividualChooseIp(String rocId){
		try {
			String ip = getClientIp();
			IndividualChooseIpVo individualChooseIpVo = new IndividualChooseIpVo();
			individualChooseIpVo.setIp(ip);
			individualChooseIpVo.setOtpTime(LocalDateTime.now());
			individualChooseIpVo.setRocId(rocId);
			indivdualChooseService.insertIndividualChooseIp(individualChooseIpVo);
		} catch (Exception e) {
			logger.error("insertIndividualChooseIp error: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	
	/**
	 * 寄送認證碼  / 輸入的使用者
	 * @return
	 */
	@RequestLog
	@PostMapping("/indivdualChooseSendPdfMail")
	public ResponseEntity<ResponseObj> indivdualChooseSendPdfMail(@RequestParam(value = "email1",required = false)   String email1,
														  @RequestParam(value = "email2",required = false)   String email2 ,
														  @RequestParam(value = "validateCode",required = false)   String validateCode,
														  @RequestParam(value = "rocId",required = false) String rocId
														  ) {
		logger.info("call sendindivdualChooseSendPdfMail email1="+ email1);
		logger.info("call sendindivdualChooseSendPdfMail email2="+ email2);
		logger.info("call sendindivdualChooseSendPdfMail validateCode="+ validateCode);
		try {
			String validateSessionCode = getSession(ApConstants.LOGIN_VALIDATE_CODE).toString();
			if(!validateSessionCode.equals(validateCode)) {
				this.setResponseObj(ResponseObj.ERROR,"驗證碼錯誤", null);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			IndividualChooseVo individualChooseVo= new IndividualChooseVo();
			individualChooseVo = indivdualChooseService.getIndividualChoosData(rocId);
			boolean status = indivdualChooseService.sendPdfMail(individualChooseVo ,email1 , email2);
			if(status) {
				this.setResponseObj(ResponseObj.SUCCESS, "", null);						
			}else {
				this.setResponseObj(ResponseObj.ERROR, "寄送通知信件失敗", null);		
			}
			//2023/05/17 新增更新圖形驗證 防止意外攻擊
			ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 20);
			addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			/*this.setResponseObj(ResponseObj.ERROR, "寄送驗證碼失敗!", null);*/
			this.setResponseObj(ResponseObj.ERROR, getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0117"), null);
		}		
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
}
