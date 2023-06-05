package com.twfhclife.eservice.web.controller;

import com.auth0.jwt.internal.org.apache.commons.codec.digest.HmacUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.util.AesUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.BxczState;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

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
    private IInsuranceClaimService insuranceClaimService;
    @Autowired
    private ITransService transService;

    @PostMapping("/generateLipeiSignUrl")
    public ResponseEntity<ResponseObj> generateLipeiSignUrl(@RequestBody TransVo transVo, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(transVo.getTransNum())) {
                this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
            } else {
                String actionId = UUID.randomUUID().toString().replaceAll("-", "");
                addSession("lipeiActionId", actionId);
                String idToken = getSessionStr("BXCZ_ID_TOKEN");
                String encId = StringUtils.isBlank(idToken) ? "" : AesUtil.encrypt(idToken, actionId);
                String code = HmacUtils.hmacSha256Hex(secret, "companyId=" + companyId + "&actionId=" + actionId +"&idVerifyType=F");
                String url = bxcz413url + "?" + "companyId=" + companyId + "&actionId=" + actionId +"&idVerifyType=F" + "&state=" + Base64.getEncoder().encodeToString(new Gson().toJson(new BxczState(actionId, transVo.getTransNum(), ApConstants.INSURANCE_CLAIM, encId)).getBytes())
                        + "&code=" + code;
                insuranceClaimService.addSignBxczRecord(actionId, transVo.getTransNum(), new Date());
                transService.updateTransStatus(transVo.getTransNum(), OnlineChangeUtil.TRANS_STATUS_PROCESS_SIGN);
                this.setResponseObj(ResponseObj.SUCCESS, "", url);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @PostMapping("/getNewSignStatus")
    public String getNewSignStatus(TransVo transVo) {
        try {
            addAttribute("signTransNum", transVo.getTransNum());
            if (StringUtils.isBlank(transVo.getTransNum())) {
                this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
            } else {
                SignRecord signRecord = insuranceClaimService.getNewSignStatus(transVo.getTransNum());
                if (signRecord != null) {
                    String msg = signStatusToStr(signRecord.getIdVerifyStatus(), signRecord.getSignStatus());
                    addAttribute("msg", msg);
                    if (StringUtils.equals(signRecord.getSignStatus(), "SIGN_U_S")) {
                        return "frontstage/onlineChange/policyClaims/policyClaims-success";
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "frontstage/onlineChange/policyClaims/policyClaims-wait-sign";
    }

    private String signStatusToStr(String idVerifyStatus, String signStatus) {
        if (StringUtils.isNotBlank(idVerifyStatus) && Lists.newArrayList("MID_S", "PBS_S", "EZ_OTP_S", "IDS_S").contains(idVerifyStatus)) {
            switch (idVerifyStatus) {
                case "CASE_I" : return "尚未身分驗證";
                case "MID_S" : return "MID身分驗證完成";
                case "PBS_S" : return "保險存摺身分驗證完成";
                case "EZ_OTP_S" : return "便利入口驗證完成";
                case "EZ_OTP_F" : return "便利入口驗證失敗";
                case "EZ_OTP_E" : return "便利入口有效期限逾期";
                case "IDS_S" : return "身分驗證中心驗證完成";
                case "IDS_F" : return "身分驗證中心驗證失敗";
                case "IDS_E" : return "身分驗證中心有效期限逾期";
                case "ACT_E" : return "活動編碼有效期限逾期";
                case "MID_F" : return "MID驗證失敗";
                case "PBS_F" : return "保險存摺登入/註冊失敗";
                case "PBS_N" : return "保險存摺分非白金會員";
                case "MID_S_ID_F" : return "MID身分驗證完成身分證字號不符";
                case "PBS_S_ID_F" : return "保險存摺身分驗證身分證字號不符";
                case "EZ_OTP_S_ID_F " : return "便利入口驗證完成身分證字號不符";
                case "IDS_S_ID_F" : return "身分驗證中心驗證完成身分證字號不符";
            }
        } else if (StringUtils.isNotBlank(signStatus)) {
            switch (signStatus) {
                case "SIGN_I" : return "尚未簽署";
                case "SIGN_U_S" : return "保戶簽署完成";
                case "SIGN_L_S" : return "公會簽署完成";
                case "SIGN_U_F" : return "保戶簽署失敗";
                case "SIGN_L_F" : return "公會簽署失敗";
                case "SIGN_E" : return "文件簽署逾期";
            }
        }
        return "";
    }

    @GetMapping("callBack418")
    public String callBack418(String idVerifyStatus, String signStatus) {
        try {
            addAttribute("msg", signStatusToStr(idVerifyStatus, signStatus));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return "frontstage/onlineChange/call-back-418";
    }
}
