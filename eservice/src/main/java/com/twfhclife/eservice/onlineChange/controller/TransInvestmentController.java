package com.twfhclife.eservice.onlineChange.controller;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransDepositService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.CompareInvestmentVo;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.StatuCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.util.*;

/***
 * 線上申請-未來保費投資標的與分配比例
 */
@Controller
public class TransInvestmentController extends BaseUserDataController  {

    private static final Logger logger = LogManager.getLogger(TransInvestmentController.class);

    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private ITransRiskLevelService riskLevelService;

    @Autowired
    private IInsuranceClaimService insuranceClaimService;

    @Autowired
    private ITransService transService;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private ITransDepositService transDepositService;

    @RequestLog
    @GetMapping("/investment1")
    public String investment1(RedirectAttributes redirectAttributes) {
        try {
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

            addAttribute("riskLevel", transInvestmentService.transRiskLevelToName(riskLevel));
            String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_REMARKS);
            if (parameterValueByCodeConsent != null) {
                addAttribute("transformationRemark", parameterValueByCodeConsent);
            }

            List<PolicyListVo> policyList = policyListService.getInvestmentPolicyList(userRocId);
            transInvestmentService.handlePolicyStatusLocked(userRocId, policyList, TransTypeUtil.INVESTMENT_PARAMETER_CODE);
            transService.handleVerifyPolicyRuleStatusLocked(policyList, TransTypeUtil.INVESTMENT_PARAMETER_CODE);
            addAttribute("policyList", policyList);
        } catch (Exception e) {
            logger.error("Unable to init from cancelContract1: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/investment/investment1";
    }

    @RequestLog
    @PostMapping("/investment2")
    public String investment2(TransInvestmentVo transInvestmentVo) {
        //進行查詢當前同意條款
        String policyType = transInvestmentVo.getPolicyNo().substring(0, 2);
        String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
                ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_CONSENT + "_"  + policyType);
        if (parameterValueByCodeConsent != null) {
            addAttribute("next", true);
            addAttribute("transformationConsent", parameterValueByCodeConsent);
        } else {
            String error = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_CONSENT + "_ERROR");
            addSystemError(error);
            addAttribute("next", false);
        }
        addAttribute("transInvestmentVo", transInvestmentVo);
        return "frontstage/onlineChange/investment/investment2";
    }

    @RequestLog
    @PostMapping("/investment3")
    public String investment3(TransInvestmentVo transInvestmentVo) {
        String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
                ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_REMARK1);
        if (parameterValueByCodeConsent != null) {
            addAttribute("transformationRemark", parameterValueByCodeConsent);
        }

        final List<String> showAccountInvts = Lists.newArrayList();
        parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SHOW_ACCOUNT_INVT_NOS")
                .forEach(e -> showAccountInvts.add(e.getParameterValue()));

        addAttribute("showAccountInvts", showAccountInvts);
        addAttribute("proposer", getProposerName());
        addAttribute("uri", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_URI));
        String limit = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_LIMIT);
        addAttribute("INVESTMENT_LIMIT", StringUtils.isEmpty(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit));
        UsersVo user = getUserDetail();
        String riskLevel = riskLevelService.getUserRiskAttr(user.getRocId());
        addAttribute("riskLevel", transInvestmentService.transRiskLevelToName(riskLevel));
        //查询已有投资标
        String policyType = transInvestmentVo.getPolicyNo().substring(0, 2);
        addAttribute("policyType", policyType);
        List<InvestmentPortfolioVo> investments = transInvestmentService.getOwnInvestment(transInvestmentVo.getPolicyNo());
        addAttribute("configs", transDepositService.getDepositConfigs());
        addAttribute("investments", investments);
        return "frontstage/onlineChange/investment/investment3";
    }

    @RequestLog
    @PostMapping("/investment4")
    public String investment4(TransInvestmentVo vo, @RequestParam(name = "showAccount", defaultValue = "false") Boolean showAccount) {
        List<InvestmentPortfolioVo> newInvts = new Gson().fromJson(vo.getNewInvestments(), new TypeToken<List<InvestmentPortfolioVo>>(){}.getType());
        List<InvestmentPortfolioVo> ownInvts = new Gson().fromJson(vo.getInvestments(), new TypeToken<List<InvestmentPortfolioVo>>(){}.getType());
        List<CompareInvestmentVo> finalInvestments =  transInvestmentService.compareNew(ownInvts, newInvts);
        addAttribute("finalInvestments", finalInvestments);
        sendAuthCode("investment");
        addAttribute("investmentTimeSet", 300);
        addAttribute("transInvestmentVo", vo);
        addAttribute("showAccount", showAccount);
        return "frontstage/onlineChange/investment/investment4";
    }

    @RequestLog
    @PostMapping("/investmentSuccess")
    public String investmentSuccess(TransInvestmentVo vo) {
        // 驗證驗證碼或者密码
        String msg;
        addAttribute("transInvestmentVo", vo);
        if (StringUtils.equals(vo.getAuthType(), "password")) {
            msg = checkPassword(vo.getUserPassword());
        } else {
            msg = checkAuthCode("investment", vo.getAuthenticationNum());
        }
        if (!StringUtils.isEmpty(msg)) {
            addSystemError(msg);
            return "forward:investment4";
        } else {
            UsersVo user = getUserDetail();
            try {
                transInvestmentService.addNewInvestmentApply(vo, user);
                sendNotification(vo, user);
            } catch (Exception e) {
                logger.error(e);
                return "forward:investment4";
            }
            return "frontstage/onlineChange/investment/investment-success";
        }
    }

    @RequestLog
    @PostMapping("/getNewInvestments")
    @ResponseBody
    public ResponseEntity<ResponseObj> getNewInvestments(@RequestBody TransInvestmentVo vo) {
        List<InvestmentPortfolioVo> investments = transInvestmentService.getNewInvestments(vo.getPolicyNo(), vo.getOwnInvestments(), getUserRocId());
        processSuccess(investments);
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/getInvestmentSearchItem")
    @ResponseBody
    public ResponseEntity<ResponseObj> getInvestmentSearchItem(@RequestBody TransInvestmentVo vo) {
        Map<String, List<Map<String, String>>> map = transInvestmentService.getCompanyAndCurrencyList(vo.getPolicyNo());
        processSuccess(map);
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/getTransInvestmentDetail")
    public String getTransDnsDetail(@RequestParam("transNum") String transNum) {
        try {
            final List<String> showAccountInvts = Lists.newArrayList();
            parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SHOW_ACCOUNT_INVT_NOS")
                    .forEach(e -> showAccountInvts.add(e.getParameterValue()));
            addAttribute("showAccountInvts", showAccountInvts);
            addAttribute("finalInvestments", transInvestmentService.getAppliedInvestments(transNum));
        } catch (Exception e) {
            logger.error("Unable to getTransInvestmentDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/investment/investment-detail";
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

    public void sendNotification(TransInvestmentVo vo, UsersVo user) {
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
            paramMap.put("POLICY_NO", vo.getPolicyNo());
            logger.info("POLICY_NO : {}", vo.getPolicyNo());

            List<String> receivers = new ArrayList<String>();

            String applyDate = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
            paramMap.put("DATA", applyDate);
            //申請狀態-申請中
            paramMap.put("TransStatus","已審核");
            //申請功能
            ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.INVESTMENT_PARAMETER_CODE);
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
            paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY1);
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
