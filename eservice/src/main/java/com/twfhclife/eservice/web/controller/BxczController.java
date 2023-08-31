package com.twfhclife.eservice.web.controller;

import com.auth0.jwt.internal.org.apache.commons.codec.digest.HmacUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.onlineChange.model.BxczSignApiLog;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import com.twfhclife.eservice.onlineChange.service.IBxczSignService;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.IMedicalTreatmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.util.AesUtil;
import com.twfhclife.eservice.util.SignStatusUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.BxczState;
import com.twfhclife.eservice.web.model.SignTrans;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@EnableAutoConfiguration
public class BxczController extends BaseController {

    private static final Logger logger = LogManager.getLogger(BxczController.class);

    @Value("${eservice.bxcz.company_id}")
    private String companyId;

    @Value("${eservice.bxcz.413.url}")
    private String bxcz413url;

    @Value("${eservice.bxcz.client_secret}")
    private String secret;

    @Autowired
    private IBxczSignService bxczSignService;

    @Autowired
    private ITransService transService;

    @PostMapping("/generateBxczSignUrl")
    public ResponseEntity<ResponseObj> generateBxczSignUrl(@RequestBody BxczState bxczState) {
        try {
            Date startTime = new Date();
            if (StringUtils.isBlank(bxczState.getTransNum())) {
                this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
            } else {
                String type = StringUtils.equals(bxczState.getType(), ApConstants.INSURANCE_CLAIM) ? "F" : "H";
                String actionId = UUID.randomUUID().toString().replaceAll("-", "");
                String idToken = getSessionStr("BXCZ_ID_TOKEN");
                String encId = StringUtils.isBlank(idToken) ? "" : AesUtil.encrypt(idToken, actionId);
                String params = "companyId=" + companyId + "&actionId=" + actionId +"&idVerifyType=" + type + "&state=" + Base64.getEncoder().encodeToString(new Gson().toJson(new BxczState(actionId, bxczState.getTransNum(), bxczState.getType(), encId)).getBytes());
                String code = Base64.getEncoder().encodeToString(HmacUtils.hmacSha256(secret, params));
                if (StringUtils.isNotBlank(code)) {
                    code = code.replaceAll("\\+", "-").replaceAll("/", "_").replaceAll("=", "");
                }
                String url = bxcz413url + "?" + params + "&code=" + code;
                SignRecord signRecord = new SignRecord();
                signRecord.setTransNum(bxczState.getTransNum());
                signRecord.setActionId(actionId);
                Calendar calendar = Calendar.getInstance();
                signRecord.setSignStart(calendar.getTime());
                calendar.add(Calendar.MILLISECOND, 300 * 1000);
                signRecord.setSignEnd(calendar.getTime());
                bxczSignService.addSignBxczRecord(signRecord);
                transService.updateTransStatus(bxczState.getTransNum(), OnlineChangeUtil.TRANS_STATUS_PROCESS_SIGN);
                this.setResponseObj(ResponseObj.SUCCESS, "", url);

                BxczSignApiLog bxczSignApiLog = new BxczSignApiLog("CALL", "URL413-數位身分驗證頁面轉導", "0", "", actionId, bxczState.getTransNum(), startTime, new Date());
                bxczSignService.addSignBxczApiRecord(bxczSignApiLog);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @PostMapping("/getNewSignStatus")
    public String getNewSignStatus(BxczState bxczState, @RequestParam("opened") String opened) {
        try {
            addAttribute("signTransNum", bxczState.getTransNum());
            addAttribute("opened", opened);
            if (StringUtils.isBlank(bxczState.getTransNum())) {
                this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
            } else {
                SignRecord signRecord = bxczSignService.getNewSignStatus(bxczState.getTransNum());
                if (signRecord != null) {
                    String statusSuccess = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "BXCZ_SIGN_SUCCESS_CODE");
                    if (StringUtils.contains(statusSuccess, signRecord.getSignStatus())) {
                        if (StringUtils.equals(bxczState.getType(), ApConstants.INSURANCE_CLAIM)) {
                            return "frontstage/onlineChange/policyClaims/policyClaims-success";
                        } else {
                            return "frontstage/onlineChange/medicalTreatment/medicalTreatment-success";
                        }
                    } else {
                        String msg = SignStatusUtil.signStatusToStr(signRecord.getIdVerifyStatus(), signRecord.getSignStatus());
                        addAttribute("msg", msg);
                        addAttribute("signFail", true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (StringUtils.equals(bxczState.getType(), ApConstants.INSURANCE_CLAIM)) {
            return "frontstage/onlineChange/policyClaims/policyClaims-wait-sign";
        } else {
            return "frontstage/onlineChange/medicalTreatment/medicalTreatment-wait-sign";
        }
    }

    @Autowired
    private IParameterService parameterService;

    @GetMapping("callBack418")
    public String callBack418(@RequestParam("actionId") String actionId, @RequestParam("idVerifyStatus") String idVerifyStatus, @RequestParam("signStatus") String signStatus) {
        try {

            logger.info("callBack418 actionId is: {}, idVerifyStatus is: {}, signStatus is: {}", actionId, idVerifyStatus, signStatus);
            Date startTime = new Date();
            addAttribute("msg", SignStatusUtil.signStatusToStr(idVerifyStatus, signStatus));

            SignTrans signTrans = bxczSignService.getSignTrans(actionId);
            if (signTrans != null ) {
                List<String> statusSuccessList = Lists.newArrayList();
                List<String> statusVerifyFailList = Lists.newArrayList();
                List<String> statusSignFailList = Lists.newArrayList();
                String statusSuccess = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "BXCZ_SIGN_SUCCESS_CODE");
                String statusVerifyFail = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "BXCZ_VERIFY_FAIL_CODE");
                String statusSignFail = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "BXCZ_SIGN_FAIL_CODE");
                if (StringUtils.isNotBlank(statusSuccess)) {
                    statusSuccessList = Splitter.on(",").omitEmptyStrings().splitToList(statusSuccess);
                }
                if (StringUtils.isNotBlank(statusVerifyFail)) {
                    statusVerifyFailList = Splitter.on(",").omitEmptyStrings().splitToList(statusVerifyFail);
                }
                if (StringUtils.isNotBlank(statusSignFail)) {
                    statusSignFailList = Splitter.on(",").omitEmptyStrings().splitToList(statusSignFail);
                }
                if (StringUtils.equals(TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE, signTrans.getTransType())) {
                    TransInsuranceClaimVo claimVo = insuranceClaimService.getTransInsuranceClaimDetail(signTrans.getTransNum());
                    if (claimVo != null) {
                        if (statusVerifyFailList.contains(idVerifyStatus)) {
                            transService.updateTransStatus(signTrans.getTransNum(), OnlineChangeUtil.TRANS_STATUS_FAIL_VERIFY);
                            sendPolicyClaimNotify(claimVo, OnlineChangeUtil.TRANS_STATUS_FAIL_VERIFY, OnlineChangeUtil.ELIFE_MAIL_071, OnlineChangeUtil.ELIFE_SMS_071);
                        } else if (statusSuccessList.contains(signStatus)) {
                            sendPolicyClaimNotify(claimVo, OnlineChangeUtil.TRANS_STATUS_APPLYING, OnlineChangeUtil.ELIFE_MAIL_024, OnlineChangeUtil.ELIFE_SMS_005);
                            transService.updateTransStatus(signTrans.getTransNum(), OnlineChangeUtil.TRANS_STATUS_APPLYING);
                            insuranceClaimService.updateTransApplyDate(claimVo.getClaimSeqId(), new Date());
                        } else if (statusSignFailList.contains(signStatus)) {
                            transService.updateTransStatus(signTrans.getTransNum(), OnlineChangeUtil.TRANS_STATUS_FAIL_SIGN);
                            sendPolicyClaimNotify(claimVo, OnlineChangeUtil.TRANS_STATUS_FAIL_SIGN, OnlineChangeUtil.ELIFE_MAIL_071, OnlineChangeUtil.ELIFE_SMS_071);
                        }
                    }
                } else if (StringUtils.equals(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE, signTrans.getTransType())) {
                    TransMedicalTreatmentClaimVo claimVo = medicalTreatmentService.getTransInsuranceClaimDetail(signTrans.getTransNum());
                    if (claimVo != null) {
                        if (statusVerifyFailList.contains(idVerifyStatus)) {
                            transService.updateTransStatus(signTrans.getTransNum(), OnlineChangeUtil.TRANS_STATUS_FAIL_VERIFY);
                            sendMedicalNotify(claimVo, OnlineChangeUtil.TRANS_STATUS_FAIL_VERIFY, OnlineChangeUtil.MEDICAL_MAIL_039, OnlineChangeUtil.MEDICAL_SMS_040);
                        } else if (statusSuccessList.contains(signStatus)) {
                            sendMedicalNotify(claimVo, OnlineChangeUtil.TRANS_STATUS_APPLYING, OnlineChangeUtil.MEDICAL_MAIL_036, OnlineChangeUtil.MEDICAL_SMS_037);
                            transService.updateTransStatus(signTrans.getTransNum(), OnlineChangeUtil.TRANS_STATUS_APPLYING);
                            medicalTreatmentService.updateTransApplyDate(claimVo.getClaimSeqId(), new Date());
                        } else if (statusSignFailList.contains(signStatus)) {
                            transService.updateTransStatus(signTrans.getTransNum(), OnlineChangeUtil.TRANS_STATUS_FAIL_SIGN);
                            sendMedicalNotify(claimVo, OnlineChangeUtil.TRANS_STATUS_FAIL_SIGN, OnlineChangeUtil.MEDICAL_MAIL_039, OnlineChangeUtil.MEDICAL_SMS_040);
                        }
                    }
                }
                bxczSignService.updateSignStatus418(actionId, idVerifyStatus, signStatus, new Date());
                BxczSignApiLog bxczSignApiLog = new BxczSignApiLog("CALL_BACK", "URL418-數位身分驗證/數位簽署結束頁面轉導", "0", "", actionId, signTrans.getTransNum(), startTime, new Date());
                bxczSignService.addSignBxczApiRecord(bxczSignApiLog);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return "frontstage/onlineChange/call-back-418";
    }

    @Autowired
    private MessageTemplateClient messageTemplateClient;
    @Autowired
    private IInsuranceClaimService insuranceClaimService;
    @Autowired
    private IMedicalTreatmentService medicalTreatmentService;

    private void sendPolicyClaimNotify(TransInsuranceClaimVo claimVo, String status, String mailTemplate, String smsTemplate) {
        logger.info("start send mail");
        Map<String, Object> mailInfo = insuranceClaimService.getSendMailInfo(status);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("TransNum", claimVo.getTransNum());
        //paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
        //paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
        logger.info("Trans Num : {}", claimVo.getTransNum());
        logger.info("Status Name : {}", (String) mailInfo.get("statusName"));
        logger.info("Trans Remark : {}", (String) mailInfo.get("transRemark"));
        logger.info("receivers={}", (List)mailInfo.get("receivers"));
        logger.info("user phone : {}", claimVo.getPhone());
        logger.info("user mail : {}", claimVo.getMail());
        List<String> receivers = new ArrayList<String>();

        String loginTime = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
        paramMap.put("LoginTime", loginTime);

        if (StringUtils.equals(OnlineChangeUtil.TRANS_STATUS_APPLYING, status)) {
            //發送系統管理員
            receivers = (List)mailInfo.get("receivers");
            //messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_005, receivers, paramMap, "email");
            //使用新郵件範本
            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_025, receivers, paramMap, "email");
        }

        //發送保戶MAIL
        receivers = new ArrayList<String>();
        receivers.add(claimVo.getMail());
        paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
        logger.info("user mail : {}", claimVo.getMail());
        //messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_005, receivers, paramMap, "email");
        //使用新郵件範本
        messageTemplateClient.sendNoticeViaMsgTemplate(mailTemplate, receivers, paramMap, "email");
        logger.info("End send mail");


        //發送保戶SMS
        receivers = new ArrayList<String>();
        receivers.add(claimVo.getPhone());
        paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
        logger.info("user phone : {}", claimVo.getPhone());
        messageTemplateClient.sendNoticeViaMsgTemplate(smsTemplate, receivers, paramMap, "sms");
    }

    private void sendMedicalNotify(TransMedicalTreatmentClaimVo claimVo, String status, String mailTemplate, String smsTemplate) {
        logger.info("start send mail");
        Map<String, Object> mailInfo = medicalTreatmentService.getSendMailInfo(status);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("TransNum", claimVo.getTransNum());
        //paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
        //paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
        logger.info("Trans Num : {}", claimVo.getTransNum());
        logger.info("Status Name : {}", (String) mailInfo.get("statusName"));
        logger.info("Trans Remark : {}", (String) mailInfo.get("transRemark"));
        logger.info("receivers={}", (List)mailInfo.get("receivers"));
        logger.info("user phone : {}", claimVo.getPhone());
        logger.info("user mail : {}", claimVo.getMail());
        List<String> receivers = new ArrayList<String>();

        String loginTime = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
        paramMap.put("LoginTime", loginTime);

        if (StringUtils.equals(OnlineChangeUtil.TRANS_STATUS_APPLYING, status)) {
            //發送系統管理員
            receivers = (List)mailInfo.get("receivers");
            //messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_005, receivers, paramMap, "email");
            //使用新郵件範本
            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.MEDICAL_MAIL_035, receivers, paramMap, "email");
        }

        //發送保戶MAIL
        receivers = new ArrayList<String>();
        receivers.add(claimVo.getMail());
        paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
        logger.info("user mail : {}", claimVo.getMail());
        //messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_005, receivers, paramMap, "email");
        //使用新郵件範本
        messageTemplateClient.sendNoticeViaMsgTemplate(mailTemplate, receivers, paramMap, "email");
        logger.info("End send mail");


        //發送保戶SMS
        receivers = new ArrayList<String>();
        receivers.add(claimVo.getPhone());
        paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
        logger.info("user phone : {}", claimVo.getPhone());
        messageTemplateClient.sendNoticeViaMsgTemplate(smsTemplate, receivers, paramMap, "sms");
    }
}
