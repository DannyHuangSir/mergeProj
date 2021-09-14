package com.twfhclife.eservice.onlineChange.controller;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.onlineChange.service.ITransDepositService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.math.BigDecimal;
import java.util.*;

/***
 * 線上申請 - 申請保單提領(贖回)
 */
@Controller
public class TransDepositController extends BaseUserDataController {

    private static final Logger logger = LogManager.getLogger(TransDepositController.class);

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private ITransDepositService transDepositService;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private ITransService transService;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private IParameterService parameterService;

    @RequestLog
    @GetMapping("/deposit1")
    public String deposit1(RedirectAttributes redirectAttributes) {
        if (!checkCanUseOnlineChange()) {
            String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
            addSystemError(message);
            return "redirect:apply1";
        }

        /**
         * 有申請中的保單理賠,則不可再申請
         * TRANS  status=-1,0,4
         */
        String msg = transInvestmentService.checkHasApplying(getUserId());
        if (StringUtils.isNotBlank(msg)) {
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:apply1";
        }

        String userRocId = getUserRocId();
        List<PolicyListVo> policyList = policyListService.getDepositList(userRocId, null);
        computeMaxAcctValue(policyList);
        if (policyList != null) {
            List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
                    getUserId(), TransTypeUtil.DEPOSIT_PARAMETER_CODE);
            transInvestmentService.handlePolicyStatusLocked(userRocId, handledPolicyList);
            transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
                    TransTypeUtil.DEPOSIT_PARAMETER_CODE);
            addAttribute("policyList", handledPolicyList);
        }
        return "frontstage/onlineChange/deposit/deposit1";
    }

    @RequestLog
    @PostMapping("/deposit2")
    public String deposit2(TransDepositVo vo) {
        String userRocId = getUserRocId();
        addAttribute("depositPercent", transDepositService.getDepositConfigs(vo.getPolicyNo()));
        PolicyListVo depositPolicy = transDepositService.getDepositPolicy(userRocId, vo.getPolicyNo());
        List<ParameterVo> parameterVos = transDepositService.getDepositConfigs(vo.getPolicyNo());
        BigDecimal max = depositPolicy.getPolicyAcctValue();
        BigDecimal min = BigDecimal.valueOf(0);
        if (!CollectionUtils.isEmpty(parameterVos)) {
            for (ParameterVo parameterVo : parameterVos) {
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_MAX_VALUE")) {
                    max = new BigDecimal(parameterVo.getParameterValue());
                    if (depositPolicy.getPolicyAcctValue().compareTo(max) < 0) {
                        max = depositPolicy.getPolicyAcctValue();
                    }
                }
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_MIN_VALUE")) {
                    min = new BigDecimal(parameterVo.getParameterValue());
                }
            }
        }
        addAttribute("maxValue", max);
        addAttribute("minValue", min);
        addAttribute("depositVo", depositPolicy);
        return "frontstage/onlineChange/deposit/deposit2";
    }

    @RequestLog
    @PostMapping("/deposit3")
    public String deposit3(TransDepositVo vo) {
        addAttribute("depositVo", vo);
        addAttribute("userName", getUserDetail().getUserName());
        addAttribute("showPost", checkPostShow(vo.getPolicyNo()));
        return "frontstage/onlineChange/deposit/deposit3";
    }

    @RequestLog
    @PostMapping("/deposit4")
    public String deposit4(TransDepositVo vo) {
        addAttribute("depositVo", vo);
        if (StringUtils.isNotBlank(vo.getInvtDeposits())) {
            addAttribute("invtDeposits", new Gson().fromJson(vo.getInvtDeposits(), new TypeToken<List<Map<String, Object>>>() {
            }.getType()));
        }
        sendAuthCode("deposit");
        addAttribute("depositTimeSet", 300);
        return "frontstage/onlineChange/deposit/deposit4";
    }

    @RequestLog
    @PostMapping("/depositSuccess")
    public String depositSuccess(TransDepositVo vo) {
        String msg;
        if (StringUtils.equals(vo.getAuthType(), "password")) {
            msg = checkPassword(vo.getUserPassword());
        } else {
            msg = checkAuthCode("deposit", vo.getAuthenticationNum());
        }
        if (!StringUtils.isEmpty(msg)) {
            addSystemError(msg);
            return "forward:deposit4";
        } else {
            UsersVo user = getUserDetail();
            try {
                transDepositService.addNewDepositApply(vo, user);
                sendNotification(vo, user);
            } catch (Exception e) {
                logger.error(e);
                return "forward:deposit4";
            }
            return "frontstage/onlineChange/investment/investment-success";
        }
    }

    @RequestLog
    @PostMapping("/getTransDepositDetail")
    public String getTransDepositDetail(@RequestParam("transNum") String transNum) {
        try {
            addAttribute("depositDetail", transDepositService.getAppliedTransDeposits(transNum));
        } catch (Exception e) {
            logger.error("Unable to getTransDepositDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/deposit/deposit-detail";
    }

    private Boolean checkPostShow(String policyNo) {
        if (!StringUtils.isEmpty(policyNo)) {
            if (policyNo.length() >= 2) {
                String substring = policyNo.substring(0, 2);
                return "UC,UH".contains(substring);
            }
        }
        return true;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mingwen;
    }

    public void sendNotification(TransDepositVo vo, UsersVo user) {
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
            paramMap.put("TransStatus","申請中");
            //申請功能
            ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.DEPOSIT_PARAMETER_CODE);
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
            paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY);
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

    private void computeMaxAcctValue(List<PolicyListVo> policyList) {
        if (!CollectionUtils.isEmpty(policyList)) {
            for (PolicyListVo depositPolicyListVo : policyList) {
                BigDecimal max = depositPolicyListVo.getPolicyAcctValue();
                List<ParameterVo> parameterVos = transDepositService.getDepositConfigs(depositPolicyListVo.getPolicyNo());
                if (!CollectionUtils.isEmpty(parameterVos)) {
                    for (ParameterVo parameterVo : parameterVos) {
                        if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicyListVo.getPolicyType() + "_" + depositPolicyListVo.getCurrency() + "_MAX_VALUE")) {
                            max = new BigDecimal(parameterVo.getParameterValue());
                            if (depositPolicyListVo.getPolicyAcctValue().compareTo(max) < 0) {
                                max = depositPolicyListVo.getPolicyAcctValue();
                            }
                        }
                    }
                }
                depositPolicyListVo.setPolicyAcctValue(max);
            }
        }
    }
}
