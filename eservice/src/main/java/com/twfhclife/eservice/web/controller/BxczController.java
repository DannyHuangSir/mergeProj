package com.twfhclife.eservice.web.controller;

import com.auth0.jwt.internal.org.apache.commons.codec.digest.HmacUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.util.AesUtil;
import com.twfhclife.eservice.util.SignStatusUtil;
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
                    String msg = SignStatusUtil.signStatusToStr(signRecord.getIdVerifyStatus(), signRecord.getSignStatus());
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



    @GetMapping("callBack418")
    public String callBack418(String idVerifyStatus, String signStatus) {
        try {
            addAttribute("msg", SignStatusUtil.signStatusToStr(idVerifyStatus, signStatus));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return "frontstage/onlineChange/call-back-418";
    }
}
