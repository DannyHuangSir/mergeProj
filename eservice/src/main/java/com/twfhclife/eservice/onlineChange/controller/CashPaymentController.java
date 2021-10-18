package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.service.ITransCashPaymentService;
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
import com.twfhclife.generic.api_client.FunctionUsageClient;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.util.*;

@Controller
public class CashPaymentController extends BaseUserDataController {

    private static final Logger logger = LogManager.getLogger(CashPaymentController.class);

    @Autowired
    private ITransService transService;

    @Autowired
    private FunctionUsageClient functionUsageClient;

    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private ITransCashPaymentService transCashPaymentService;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private IParameterService parameterService;

    @RequestLog
    @GetMapping("/cashPayment1")
    public String cashPayment1(RedirectAttributes redirectAttributes) {
        try {
            if (!checkCanUseOnlineChange()) {
                /*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
                String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
                redirectAttributes.addFlashAttribute("errorMessage", message);
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
            String userId = getUserId();
            List<PolicyListVo> policyList = policyListService.getInvestmentPolicyList(userRocId);

            // 處理保單狀態是否鎖定
            if (policyList != null) {
                List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
                        userId, TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE);
                transInvestmentService.handlePolicyStatusLocked(userRocId, handledPolicyList, TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE);
                transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList, TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE);
                addAttribute("policyList", handledPolicyList);
            }
        } catch (Exception e) {
            logger.error("Unable to init from cashPayment1: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/cashPayment/cashPayment1";
    }

    @RequestLog
    @PostMapping("/cashPayment2")
    public String cashPayment2(TransCashPaymentVo vo) {
        addAttribute("proposer", getProposerName());
        addAttribute("transCashPaymentVo", vo);
        addAttribute("preAllocation", transCashPaymentService.getPreAllocation(vo.getPolicyNo()));
        return "frontstage/onlineChange/cashPayment/cashPayment2";
    }

    @RequestLog
    @PostMapping("/cashPayment3")
    public String cashPayment3(TransCashPaymentVo vo) {
        addAttribute("transCashPaymentVo", vo);
        addAttribute("cashPaymentTimeSet", 300);
        // 發送驗證碼
        sendAuthCode("cashPayment");
        return "frontstage/onlineChange/cashPayment/cashPayment3";
    }

    @RequestLog
    @PostMapping("/cashPaymentSuccess")
    public String cashPaymentSuccess(TransCashPaymentVo vo) {
        String msg;
        if (StringUtils.equals(vo.getAuthType(), "password")) {
            msg = checkPassword(vo.getUserPassword());
        } else {
            msg = checkAuthCode("cashPayment", vo.getAuthenticationNum());
        }
        if (!StringUtils.isEmpty(msg)) {
            addSystemError(msg);
            return "forward:cashPayment3";
        }
        try {
            boolean applyed = transService.isTransApplyed(vo.getPolicyNo(),
                    TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_AUDITED);
            // 沒有申請過才新增
            if (!applyed) {
                if (!StringUtils.isEmpty(msg)) {
                    addSystemError(msg);
                    return "forward:cashPayment3";
                }

                // 設定交易序號
                String transNum = transService.getTransNum();
                vo.setTransNum(transNum);

                int result = transCashPaymentService.insertCashPayment(vo, getUserId());
                if (result <= 0) {
                    addDefaultSystemError();
                    return "forward:cashPayment3";
                }
                sendNotification(vo, getUserDetail());
            }
        } catch (Exception e) {
            logger.error(e);
            return "forward:cashPayment3";
        }
        return "frontstage/onlineChange/cashPayment/cashPayment-success";
    }

    @RequestLog
    @PostMapping("/getCashPaymentDetail")
    public String getCashPaymentDetail(@RequestParam("transNum") String transNum) {
        try {
            addAttribute("cashPaymentDetail", transCashPaymentService.getCurrentTransCashPayment(transNum));
        } catch (Exception e) {
            logger.error("Unable to getTransDepositDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/cashPayment/cashPayment-detail";
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

    public void sendNotification(TransCashPaymentVo vo, UsersVo user) {
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
                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE);
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
            paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY3);
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
