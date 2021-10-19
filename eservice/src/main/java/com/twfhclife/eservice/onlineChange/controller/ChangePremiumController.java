package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.eservice.onlineChange.model.TransChangePremiumVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.onlineChange.service.ITransChangePremiumService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransPaymodeService;
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
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ChangePremiumController extends BaseUserDataController  {

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
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private ITransChangePremiumService transChangePremiumService;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private IPolicyListService policyListService;

    @RequestLog
    @GetMapping("/changePremium1")
    public String changePremium1(RedirectAttributes redirectAttributes) {
        try {
            if(!checkCanUseOnlineChange()) {
                /*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
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

            String userId = getUserId();
            List<PolicyListVo> policyList = policyListService.getInvestmentPolicyList(getUserRocId());

            // 處理保單狀態是否鎖定
            if (policyList != null) {
                List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
                        userId, TransTypeUtil.CHANGE_PREMIUM_CODE);
                transInvestmentService.handlePolicyStatusLocked(getUserRocId(), handledPolicyList, TransTypeUtil.CHANGE_PREMIUM_CODE);
                transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList, TransTypeUtil.CHANGE_PREMIUM_CODE);
                addAttribute("policyList", handledPolicyList);
            }
        } catch (Exception e) {
            logger.error("Unable to init from changePremium1: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/changePremium/changePremium1";
    }

    @RequestLog
    @PostMapping("/changePremium2")
    public String changePremium2(TransChangePremiumVo vo) {
        try {
            Integer lipmTredTms = transPaymodeService
                    .getPolicyPayMethodChange(vo.getPolicyNoList().get(0));
            String paymodeOld = vo.getPaymodeOld();
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
            addAttribute("paymodeCanChange", mapPaymode);
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            Date activeDate = transChangePremiumService.getActiveDate(vo.getPolicyNoList().get(0));
            if (activeDate != null) {
                vo.setNextPayDate(formater.format(activeDate));
            }
            addAttribute("transPaymodeVo", vo);
            String type = vo.getPolicyNoList().get(0).substring(0,2);
            addAttribute("minValue", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "CHANGE_PREMIUM_" + type + "_MIN"));
        } catch (Exception e) {
            logger.error("Unable to init from changePremium2: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/changePremium/changePremium2";
    }

    @RequestLog
    @PostMapping("/changePremium3")
    public String changePremium3(TransChangePremiumVo vo) {
        addAttribute("transPaymodeVo", vo);
        sendAuthCode("changePremium");
        addAttribute("changePremiumTimeSet", 300);
        return "frontstage/onlineChange/changePremium/changePremium3";
    }

    @RequestLog
    @PostMapping("/changePremiumSuccess")
    public String changePremiumSuccess(TransChangePremiumVo vo) {
        String msg;
        if (StringUtils.equals(vo.getAuthType(), "password")) {
            msg = checkPassword(vo.getUserPassword());
        } else {
            msg = checkAuthCode("changePremium", vo.getAuthenticationNum());
        }
        if (!StringUtils.isEmpty(msg)) {
            addSystemError(msg);
            return "forward:changePremium3";
        }
        try {
            boolean applyed = transService.isTransApplyed(vo.getPolicyNoList().get(0),
                    TransTypeUtil.CHANGE_PREMIUM_CODE, OnlineChangeUtil.TRANS_STATUS_AUDITED);
            // 沒有申請過才新增
            if (!applyed) {
                if (!StringUtils.isEmpty(msg)) {
                    addSystemError(msg);
                    return "forward:changePremium3";
                }

                // 設定交易序號
                String transNum = transService.getTransNum();
                vo.setTransNum(transNum);

                int result = transChangePremiumService.insertChangePremium(vo, getUserId());
                if (result <= 0) {
                    addDefaultSystemError();
                    return "forward:changePremium3";
                }
                sendNotification(vo, getUserDetail());
            }
        } catch (Exception e) {
            logger.error(e);
            return "forward:changePremium3";
        }
        return "frontstage/onlineChange/changePremium/changePremium-success";
    }


    /**
     * 取得申請明細資料.
     *
     * @param transNum 申請序號
     * @return
     */
    @RequestLog
    @PostMapping("/getTransChangePremiumDetail")
    public String getTransChangePremiumDetail(@RequestParam("transNum") String transNum) {
        try {

            TransChangePremiumVo transChangePremiumVo = transChangePremiumService.getTransChangePremiumDetail(transNum);
            addAttribute("transChangePremiumDetail", transChangePremiumVo);
        } catch (Exception e) {
            logger.error("Unable to getTransChangePremiumDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/changePremium/changePremium-detail";
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

    public void sendNotification(TransChangePremiumVo vo, UsersVo user) {
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
            logger.info("POLICY_NO : {}", vo.getPolicyNoList());

            List<String> receivers = new ArrayList<String>();

            String applyDate = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
            paramMap.put("DATA", applyDate);
            //申請狀態-申請中
            paramMap.put("TransStatus","已審核");
            //申請功能
            ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.CHANGE_PREMIUM_CODE);
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
            paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY4);
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
