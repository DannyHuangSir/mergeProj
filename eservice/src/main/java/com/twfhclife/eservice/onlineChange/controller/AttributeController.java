package com.twfhclife.eservice.onlineChange.controller;

import com.google.common.collect.Maps;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.OptionVo;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.IndividualVo;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.PortfolioClient;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestLog
    @RequestMapping("/attribute1")
    public String attribute1(RedirectAttributes redirectAttributes,
                             @RequestParam(name = "answers", required = false) List<String> answers,
                             @RequestParam(name = "read", required = false, defaultValue = "false") boolean read) {

        if (!checkCanUseOnlineChange()) {
            String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
            redirectAttributes.addFlashAttribute("errorMessage", message);
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
        List<QuestionVo> questions = attributeService.getQuestions();
        if (!CollectionUtils.isEmpty(questions) && !CollectionUtils.isEmpty(answers)) {
            for (QuestionVo question : questions) {
                for (OptionVo option : question.getOptions()) {
                    option.setChecked(answers.contains(String.valueOf(option.getId())));
                }
            }
        }
        addAttribute("questions", questions);
        addAttribute("read", read);
        String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
                ApConstants.SYSTEM_ID, OnlineChangeUtil.ATTRIBUTE_REMARK1);
        if (parameterValueByCodeConsent != null) {
            addAttribute("transformationRemark", parameterValueByCodeConsent);
        }
        return "frontstage/onlineChange/attribute/attribute1";
    }

    @RequestLog
    @PostMapping("/attribute2")
    public String attribute2(RedirectAttributes redirectAttributes, TransAnswerVo vo) {

        try {
	        List<QuestionVo> questions = attributeService.getQuestions();
	        boolean flag = checkChoose(questions, vo.getAnswers());
	        if (!flag) {
	            addAttribute("transAnswerVo", vo);
	            addAttribute("errorMessage", "請回答完整問卷！");
	            return "forward:attribute1";
	        }
            IndividualVo individualVo = attributeService.getIndividualVoByRocId(getUserRocId());
            boolean checkAge = checkAge(vo, questions, individualVo);
            if (!checkAge) {
                addAttribute("transAnswerVo", vo);
                addAttribute("errorMessage", "您第一題的作答選項與系統中記錄的年齡不符，請重新選擇！");
                return "forward:attribute1";
            }
            calculateScore(questions, vo);
        } catch (Exception e) {
            addAttribute("transAnswerVo", vo);
            addAttribute("errorMessage", e.getMessage());
            logger.error("attribute2 error: ", e);
            return "forward:attribute1";
        }

        String riskLevel = riskLevelService.computeRiskLevel(vo.getScore());
        vo.setLevel(riskLevel);
        vo.setDesc(transInvestmentService.transRiskLevelToName(riskLevel));
        addAttribute("transAnswerVo", vo);
        addAttribute("attributeTimeSet", 300);
        sendAuthCode("attribute");
        addAttribute("analysisStatement", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_ATTRIBUTE_ANALYSIS_STATEMENT"));
        return "frontstage/onlineChange/attribute/attribute2";
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

    private void calculateScore(List<QuestionVo> questions, TransAnswerVo vo) throws Exception {
    	Map<Long, Integer> scoreMap = Maps.newHashMap();
        for (QuestionVo qVo : questions) {
            for (OptionVo oVo : qVo.getOptions()) {
                if (vo.getAnswers().contains(String.valueOf(oVo.getId()))) {
                    Integer oScore = scoreMap.get(qVo.getId());
                    Integer tmpScore = oVo.getScore().intValue();
                    if (oScore != null && oScore > tmpScore) {
                        tmpScore = oScore;
                    }
                    scoreMap.put(qVo.getId(), tmpScore);
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
        vo.setScore(finalScore);
    }

    @RequestLog
    @PostMapping("/attributeSuccess")
    public String attributeSuccess(TransAnswerVo vo) {
        UsersVo user = getUserDetail();
        try {
            String msg;
            if (StringUtils.equals(vo.getAuthType(), "password")) {
                msg = checkPassword(vo.getUserPassword());
            } else {
                msg = checkAuthCode("attribute", vo.getAuthenticationNum());
            }
            if (!StringUtils.isEmpty(msg)) {
                addSystemError(msg);
                return "forward:attribute2";
            } else {
	            int result = attributeService.addNewApply(vo, user);
	            sendNotification(vo, user);
	            if (result <= 0) {
	                addDefaultSystemError();
	                return "forward:attribute2";
	            }
                String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
                        ApConstants.SYSTEM_ID, OnlineChangeUtil.ATTRIBUTE_SUCCESS1);
                if (parameterValueByCodeConsent != null) {
                    addAttribute("transformationRemark", parameterValueByCodeConsent);
                }
                return "frontstage/onlineChange/attribute/attribute-success";
            }
        } catch (Exception e) {
            logger.error(e);
            return "forward:attribute2";
        }
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

    private boolean checkChoose(List<QuestionVo> questions, List<String> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            return false;
        }
        if (!CollectionUtils.isEmpty(questions)) {
            a:for (QuestionVo question : questions) {
                for (OptionVo option : question.getOptions()) {
                    if (answers.contains(String.valueOf(option.getId()))) {
                        continue a;
                    }
                }
                if ((question.getId() == 3 || question.getId() == 4 || question.getId() == 5)
                    && answers.contains("5")) {
                	continue;
                }
                return false;
            }
        }
        return true;
    }

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
}
