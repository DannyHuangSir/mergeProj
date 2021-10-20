package com.twfhclife.eservice.onlineChange.controller;

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
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
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

import java.util.ArrayList;
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

    @RequestLog
    @RequestMapping("/attribute1")
    public String attribute1(RedirectAttributes redirectAttributes, @RequestParam(name = "answers", required = false) List<String> answers) {

        if(!checkCanUseOnlineChange()) {
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
        return "frontstage/onlineChange/attribute/attribute1";
    }

    @RequestLog
    @PostMapping("/attribute2")
    public String attribute2(RedirectAttributes redirectAttributes, TransAnswerVo vo) {

        List<QuestionVo> questions = attributeService.getQuestions();
        boolean flag = checkChoose(questions, vo.getAnswers());
        if (!flag) {
            redirectAttributes.addFlashAttribute("errorMessage", "请回答完整问卷！");
            return "redirect:attribute1";
        }

        String riskLevel = riskLevelService.computeRiskLevel(vo.getScore());
        vo.setLevel(riskLevel);
        vo.setDesc(transInvestmentService.transRiskLevelToName(riskLevel));
        addAttribute("transAnswerVo", vo);
        addAttribute("analysisStatement", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_ATTRIBUTE_ANALYSIS_STATEMENT"));
        return "frontstage/onlineChange/attribute/attribute2";
    }

    @RequestLog
    @PostMapping("/attributeSuccess")
    public String attributeSuccess(TransAnswerVo vo) {
        UsersVo user = getUserDetail();
        try {
            int result = attributeService.addNewApply(vo, user);
            sendNotification(vo, user);
            if (result <= 0) {
                addDefaultSystemError();
                return "forward:attribute2";
            }
        } catch (Exception e) {
            logger.error(e);
            return "forward:attribute2";
        }
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

    private boolean checkChoose(List<QuestionVo> questions, List<String> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            return false;
        }
        if (!CollectionUtils.isEmpty(questions)) {
            a: for (QuestionVo question : questions) {
                for (OptionVo option : question.getOptions()) {
                    if (answers.contains(String.valueOf(option.getId()))) {
                        continue a;
                    }
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
            paramMap.put("TransStatus","已審核");
            //申請功能
            ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
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
            paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY6);
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
