package com.twfhclife.eservice.onlineChange.controller;

import com.google.common.collect.Maps;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;
import com.twfhclife.eservice.onlineChange.model.OptionVo;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.service.IndividualChooseService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.IndividualVo;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.LicohilRequest;
import com.twfhclife.generic.api_model.LicohilResponse;
import com.twfhclife.generic.api_model.LicohilVo;
import com.twfhclife.generic.api_model.PolicyDetailRequest;
import com.twfhclife.generic.api_model.PolicyDetailVo;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 線上申請-風險屬性變更
 */
@Controller
public class AttributeController extends BaseUserDataController  {

    private static final Logger logger = LogManager.getLogger(AttributeController.class);

    @Autowired
    private IAttributeService attributeService;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private ITransRiskLevelService riskLevelService;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private ILoginService loginService;
    
	@Autowired
	private IndividualChooseService indivdualChooseService;
	
	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;
	
	@Autowired
	private IRegisterUserService registerUserService;
	
	@Autowired
	private ITransService transService;

    @RequestLog
    @RequestMapping("/attribute1")
    public String attribute1(RedirectAttributes redirectAttributes,
                             @RequestParam(name = "answers", required = false) List<String> answers,
                             @RequestParam(name = "read", required = false, defaultValue = "false") boolean read,
                             @RequestParam(name="ruleStatus" , required = false) String ruleStatus) {

        if (!checkCanUseOnlineChange()) {
            String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:apply1";
        }
        /**
         * 投資型保單申請中不可繼續申請
         * TRANS  status=-1,0,4
         */
//		  2023/09/28 USER 新增檢核機制  取消舊有檢核        
//        String msg = transInvestmentService.checkHasApplying(getUserId());
//        if (StringUtils.isNotBlank(msg)) {
//            redirectAttributes.addFlashAttribute("errorMessage", msg);
//            return "redirect:apply1";
//        }
        //2023-03-15修正 規則 新增 生日 + 前次評估報告
//        List<QuestionVo> questions = attributeService.getQuestions();
//        if (!CollectionUtils.isEmpty(questions) && !CollectionUtils.isEmpty(answers)) {
//            for (QuestionVo question : questions) {
//                for (OptionVo option : question.getOptions()) {
//                    option.setChecked(answers.contains(String.valueOf(option.getId())));
//                }
//            }
//        }
//        addAttribute("questions", questions);
//        addAttribute("read", read);
//        String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
//                ApConstants.SYSTEM_ID, OnlineChangeUtil.ATTRIBUTE_REMARK1);
//        if (parameterValueByCodeConsent != null) {
//            addAttribute("transformationRemark", parameterValueByCodeConsent);
//        }
        addAttribute("read", read);
        IndividualChooseVo individualChooseVo = new IndividualChooseVo();
        if(answers != null   && answers.size() > 0) {
        	individualChooseVo.setAnswers(answers);
        }
		if(StringUtils.isNotEmpty(ruleStatus)) {
			individualChooseVo.setRuleStatus(ruleStatus);
		}
        //既有客戶
		LicohilVo vo = new LicohilVo();
		//登入者
        UsersVo userVo = getUserDetail();
        //top 1 : 取esevice 是否有申請過風險評估
		IndividualChooseVo oldIndividualChooseVo = indivdualChooseService.getIndividualChoosData(userVo.getRocId());

		//top 2 : 取得 CTC資料
		PolicyDetailRequest request = new PolicyDetailRequest();
		request.setRocId(userVo.getRocId());			
		request.setPolicyInvestmentType(indivdualChooseService.getpolicyInvestmentType());
		LicohilResponse response = transCtcSelectUtilClient.getLicohiByPolicyNo(request);
		if (response.getLicohilVo().size() != 0) {
			individualChooseVo.setChooseType(true);//是公司客戶
			//查詢是否有投資型保單
			vo = indivdualChooseService.getpolicyHaveInvestmentType(response.getLicohilVo());
			individualChooseVo = indivdualChooseService.verificationDate(individualChooseVo , vo , oldIndividualChooseVo);
		} else {
			response = transCtcSelectUtilClient.getLilipmByPolicyNo(request);
			if(response.getLicohilVo().size() != 0) {
				individualChooseVo.setChooseType(true);//是公司客戶
				//查詢是否有投資型保單
				vo = indivdualChooseService.getpolicyHaveInvestmentType(response.getLicohilVo());
				individualChooseVo = indivdualChooseService.verificationDate(individualChooseVo , vo , oldIndividualChooseVo);
				}
			}
		individualChooseVo.setRocId(userVo.getRocId());
		individualChooseVo.setName(userVo.getUserName());
		individualChooseVo = mpaaingData(individualChooseVo , vo , oldIndividualChooseVo);
		//Top 3 : 取得風險評估問題
		int systemDate = LocalDate.now().getYear();
		if(vo.getBnagBirth() != null) {
			LocalDate birDate = vo.getBnagBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(systemDate - birDate.getYear() >= 65) {
				individualChooseVo.setShowPup(true);
			}
		}
		//測試用
//			individualChooseVo.setUsersType(true);//是公司客戶
//			individualChooseVo.setShowPup(true);
		if(!individualChooseVo.isRiskDateType()) {
			return moveToFinishIndivdualChoose(individualChooseVo , vo.getInveAttr());
		}else {
				return fillInInformation(individualChooseVo);
		}
    }
    
    @RequestLog
	@PostMapping("/attribute2")
	public String attribute2(RedirectAttributes redirectAttributes, IndividualChooseVo individualChooseVo,
			BindingResult bindingResult) {

		try {
			List<QuestionVo> questions = attributeService.getQuestions();
			String flag = checkChoose(questions, individualChooseVo.getAnswers());
			if (StringUtils.isNotEmpty(flag)) {
				flag = flag.substring(0, flag.length() - 1);
				addSystemError("第" + flag + "題目尚未填寫資料，請確實填寫，謝謝");
				addAttribute("individualChooseVo", individualChooseVo);
				return "forward:attribute1";
			}
			return checkQuestion(individualChooseVo);
		} catch (Exception e) {
			logger.error("attribute2 error: {}", ExceptionUtils.getStackTrace(e));
			return "forward:apply1";
		}
	}

    @RequestLog
    @PostMapping("/attribute3")
    public String attribute3(RedirectAttributes redirectAttributes, IndividualChooseVo vo ,BindingResult bindingResult) {

        try {
	        List<QuestionVo> questions = attributeService.getQuestions();
			String flag = checkChoose(questions, vo.getAnswers());
			if (StringUtils.isNotEmpty(flag)) {
	            addAttribute("transAnswerVo", vo);
	            addAttribute("errorMessage", "請回答完整問卷！");
	            return "forward:attribute1";
	        }
           calculateScore(questions, vo);
        } catch (Exception e) {
            addAttribute("transAnswerVo", vo);
            addAttribute("errorMessage", e.getMessage());
            logger.error("attribute2 error: ", e);
            return "forward:attribute1";
        }

        String riskLevel = riskLevelService.computeRiskLevel(Integer.valueOf(vo.getScore()));
        if (!vo.isOptionsType()) {
        	vo.setRiskAttr("D"); // 若 /8、9選項是A 則紀錄為D
        	vo.setOptionsMsg("【經評估結果，投資型商品不適合您，如您需要其他商品，請洽詢本公司客服電話0800-011-966】");
		} else {
			if(vo.getRuleStatus().equals("1")) {
				vo.setRiskAttr("A");
			}else {
				vo.setRiskAttr(riskLevel);
			}
		}
        vo.setDesc(transInvestmentService.transRiskLevelToName(riskLevel));
        addAttribute("individualChooseVo", vo);
        addAttribute("analysisStatement", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_ATTRIBUTE_ANALYSIS_STATEMENT"));
        UsersVo userVo = getUserDetail();
        //20230411新增 預防使用者按F5 或者 上一頁重複新增
		IndividualChooseVo	checkVo = indivdualChooseService.getIndividualChoosData(userVo.getRocId());
		boolean ckeckVoStatus = true;
		if(checkVo != null) {
			if(indivdualChooseService.checkRatingDate(checkVo.getRatingDate())) {
				TransAnswerVo transAnswerVo = attributeService.addNewApply(vo, userVo);
		        addAttribute("transAnswerVo" ,transAnswerVo ); 
			}else {
				ckeckVoStatus = false;
			}
		}else {
	        TransAnswerVo transAnswerVo = attributeService.addNewApply(vo, userVo);
	        addAttribute("transAnswerVo" ,transAnswerVo ); 
		}
        StringBuilder sb = new StringBuilder();
		sb.append("【完成時間(");
		if(!ckeckVoStatus) {
			sb.append(new SimpleDateFormat("yyyy-MM-dd").format(checkVo.getRatingDate()));
		}else {
			sb.append(LocalDate.now());
		}
		sb.append(")，及本評估問卷結果有效期限為一年】");
        addAttribute("finishDateTime" , sb.toString());   
        return "frontstage/onlineChange/attribute/attribute3";
    }

    private boolean checkAge(TransAnswerVo vo, List<QuestionVo> questions, IndividualVo individualVo) {
        if (individualVo == null || individualVo.getBirthDate() == null) {
            return true;
        }
        int age = getAge(individualVo.getBirthDate());
        for (OptionVo oVo : questions.get(0).getOptions()) {
            if (vo.getAnswers().contains(String.valueOf(oVo.getId()))) {
                String extraValue = oVo.getExtraValue();
                if (StringUtils.isBlank(extraValue)) {
                    return true;
                }
                if (extraValue.contains(">")) {
                    if (age > Integer.parseInt(extraValue.substring(1))) {
                        return true;
                    }
                }
                if (extraValue.contains("<")) {
                    if (age < Integer.parseInt(extraValue.substring(1))) {
                        return true;
                    }
                }
                if (extraValue.contains("-")) {
                    String[] extraValueArr = extraValue.split("-");
                    if (age > Integer.parseInt(extraValueArr[0]) && age < Integer.parseInt(extraValueArr[1])) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    @RequestLog
    @PostMapping("/attributeSuccess")
    public String attributeSuccess(TransAnswerVo vo) {
        UsersVo user = getUserDetail();
//        try {
//            String msg;
//            if (StringUtils.equals(vo.getAuthType(), "password")) {
//                msg = checkPassword(vo.getUserPassword());
//            } else {
//                msg = checkAuthCode("attribute", vo.getAuthenticationNum());
//            }
//            if (!StringUtils.isEmpty(msg)) {
//                addSystemError(msg);
//                return "forward:attribute2";
//            } else {
//	            int result = attributeService.addNewApply(vo, user);
	            sendNotification(vo, user);
//	            if (result <= 0) {
//	                addDefaultSystemError();
//	                return "forward:attribute2";
//	            }
//                String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
//                        ApConstants.SYSTEM_ID, OnlineChangeUtil.ATTRIBUTE_SUCCESS1);
//                if (parameterValueByCodeConsent != null) {
//                    addAttribute("transformationRemark", parameterValueByCodeConsent);
//                }
//                return "frontstage/onlineChange/attribute/attribute-success";
//            }
//        } catch (Exception e) {
//            logger.error(e);
//            return "forward:attribute2";
//        }
    	return "frontstage/onlineChange/attribute/attribute-success";
    }

    @RequestLog
    @PostMapping("/getNewTransRiskLevelDetail")
    public String getNewTransRiskLevelDetail(@RequestParam("transNum") String transNum) {
        try {
            TransRiskLevelVo transRiskLevelVo = attributeService.getTransRiskLevelDetail(transNum);
            addAttribute("before", transInvestmentService.transRiskLevelToName(transRiskLevelVo.getRiskLevelOld()));
            addAttribute("after", transInvestmentService.transRiskLevelToName(transRiskLevelVo.getRiskLevelNew()));
        } catch (Exception e) {
            logger.error("Unable to getNewTransRiskLevelDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/attribute/attribute-detail";
    }

//    private boolean checkChoose(List<QuestionVo> questions, List<String> answers) {
//        if (CollectionUtils.isEmpty(answers)) {
//            return false;
//        }
//        if (!CollectionUtils.isEmpty(questions)) {
//            a:for (QuestionVo question : questions) {
//                for (OptionVo option : question.getOptions()) {
//                    if (answers.contains(String.valueOf(option.getId()))) {
//                        continue a;
//                    }
//                }
//                if ((question.getId() == 3 || question.getId() == 4 || question.getId() == 5)
//                    && answers.contains("5")) {
//                	continue;
//                }
//                return false;
//            }
//        }
//        return true;
//    }
    
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendNotification(TransAnswerVo vo, UsersVo user) {
        try {
            Map<String, Object> mailInfo = transInvestmentService.getSendMailInfo();
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("TransNum", vo.getTransNum());
            paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
            paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
            paramMap.put("POLICY_NO", " ");
            logger.info("Trans Num : {}", vo.getTransNum());
            logger.info("Status Name : {}", mailInfo.get("statusName"));
            logger.info("Trans Remark : {}", mailInfo.get("transRemark"));
            logger.info("receivers={}", mailInfo.get("receivers"));
            logger.info("user phone : {}", user.getMobile());
            logger.info("user mail : {}", user.getEmail());
            //获取保单编号

            List<String> receivers = new ArrayList<String>();
            String applyDate = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
            paramMap.put("DATA", applyDate);
            //申請狀態-申請中
            paramMap.put("TransStatus", "已審核");
            //申請功能
            ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
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
            paramMap.put("MESSAGE", OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY6);
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
            return res != null && StringUtils.equals("SUCCESS", res.getReturnCode()) ? null : "密碼驗證失敗！";
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
            } catch (Exception ex) {
                logger.error("==========密碼加密失敗=========== {}", ExceptionUtils.getStackTrace(ex));
            }
        }
        return mingwen;
    }

    private static int getAge(Date dateOfBirth) {

        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
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
	 * 非台銀保戶驗證資料
	 * 
	 * @param individualChooseVo
	 */
	private String checkDetailForIndividualChoose(IndividualChooseVo individualChooseVo) {
		IndividualChooseVo selectVo = indivdualChooseService.getIndividualChoosData(individualChooseVo.getRocId());
		if (selectVo == null) {
			individualChooseVo.setUsersType(false); // 不適本行客戶 等同不是會員
			return fillInInformation(individualChooseVo);
		} else {
			boolean ratingType = indivdualChooseService.checkRatingDate2(selectVo.getRatingDate());
			if (!ratingType) {
				return moveToFinishIndivdualChoose(individualChooseVo, selectVo.getRiskAttr());
			} else {
				individualChooseVo.setUsersType(false); // 不適本行客戶 等同不是會員
				return fillInInformation(individualChooseVo);
			}
		}
	}
	
	/**
	 * 台銀保戶驗證資料
	 * @param individualChooseVo
	 * @param licohilVo
	 */
	private String checkDetail(IndividualChooseVo individualChooseVo, LicohilVo licohilVo) {
		boolean ratingType = indivdualChooseService.checkRatingDate(licohilVo.getRatingDate());
		if (!ratingType) {
			return moveToFinishIndivdualChoose(individualChooseVo, licohilVo.getInveAttr());
		} else {
			individualChooseVo.setUsersType(true); // 網服會員
			return fillInInformation(individualChooseVo);
		}
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
		} catch (Exception e) {
			logger.error("fillInInformation error: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/attribute/attribute1";
	}
	
	/**
	 * 不須重新評估風險等級
	 * @param individualChooseVo
	 * @param attr 風險等級
	 * @return
	 */
	private String moveToFinishIndivdualChoose(IndividualChooseVo individualChooseVo, String attr) {
		String desc = transInvestmentService.transRiskLevelToName(attr);
		if ("".equals(desc) && attr.equals("D")) {
			individualChooseVo.setDesc("您不適合投資型");
		} else {
			individualChooseVo.setDesc(desc);
		}
//		UsersVo user = getUserDetail();
//		String rocId = user.getRocId();
//		TransRiskLevelVo transRiskLevelVo = riskLevelService.getTransRiskLevel(rocId);
		StringBuilder sb = new StringBuilder();
		sb.append("【完成時間(");
//		if(transRiskLevelVo != null) {
//			TransVo transVo = attributeService.findByTransNum(transRiskLevelVo.getTransNum());			
//			sb.append(transVo.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//	    }else {
			sb.append(individualChooseVo.getRatingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//		}
		sb.append(")，及本評估問卷結果有效期限為一年】");
		addAttribute("finishDateTime" ,sb.toString());  
		addAttribute("individualChooseVo", individualChooseVo);
		addAttribute("analysisStatement",
				parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_ATTRIBUTE_ANALYSIS_STATEMENT"));
		addAttribute("notReassess", "您前次評估未滿一年，毋須重新評估");
		return "frontstage/onlineChange/attribute/attribute3";
	}
	/**
	 * 塞選資料
	 */
	private IndividualChooseVo mpaaingData(IndividualChooseVo individualChooseVo , LicohilVo vo , IndividualChooseVo oldIndividualChooseVo) {
		
		if(vo.getBnagBirth() != null) {
			individualChooseVo.setBirthDate(new SimpleDateFormat("yyyy/MM/dd").format(vo.getBnagBirth()));
		}
		individualChooseVo.setRiskAttr(vo.getInveAttr());
		if(oldIndividualChooseVo != null){
			if(indivdualChooseService.compareDate(oldIndividualChooseVo.getRatingDate() , vo.getRatingDate())) {
				individualChooseVo.setRatingDate(oldIndividualChooseVo.getRatingDate());	
			}else {
				individualChooseVo.setRatingDate(vo.getRatingDate());	
			}
		}else {
			individualChooseVo.setRatingDate(vo.getRatingDate());	
		}				
		return individualChooseVo; 
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
		return "frontstage/onlineChange/attribute/attribute2";
	}

}
