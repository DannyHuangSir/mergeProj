package com.twfhclife.jd.web.controller;

import com.twfhclife.jd.controller.BaseController;
import com.twfhclife.jd.service.ISendAuthenticationService;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.web.domain.ResponseObj;
import com.twfhclife.jd.web.model.AuthenticationVo;
import com.twfhclife.jd.web.model.UsersVo;
import com.twfhclife.jd.web.service.IParameterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@EnableAutoConfiguration
public class SendAuthenticationController extends BaseController {

    private static final Logger logger = LogManager.getLogger(SendAuthenticationController.class);

    @Autowired
    private ISendAuthenticationService sendAuthenticationService;

    /**
     * 寄送認證碼  / 輸入的使用者
     *
     * @return
     */
    @PostMapping("/sendAuthentication")
    public ResponseEntity<ResponseObj> sendAuthentication(@RequestParam(value = "authenticationType", required = false) String authenticationType,
                                                          @RequestParam(value = "newMobile", required = false) String newMobile,
                                                          @RequestParam(value = "newEmail", required = false) String newEmail
    ) {
        logger.info("call sendAuthentication authenticationType=" + authenticationType);
        logger.info("call sendAuthentication mobile=" + newMobile);
        logger.info("call sendAuthentication email=" + newEmail);
        String email = "";
        String mobile = "";
        UsersVo user = new UsersVo();
        try {
            //註冊跳過抓取UserVo
            if (!authenticationType.equals("register") && !authenticationType.equals("forget")) {
                user = getUserDetail();
                if (getSession(authenticationType + "_email") != null && user.getMailFlag().equals("1")) {
                    email = getSession(authenticationType + "_email").toString();
                }
                if (getSession(authenticationType + "_mobile") != null && user.getSmsFlag().equals("1")) {
                    mobile = getSession(authenticationType + "_mobile").toString();
                }
            } else {
                if (getSession(authenticationType + "_email") != null) {
                    email = getSession(authenticationType + "_email").toString();
                }
                if (getSession(authenticationType + "_mobile") != null) {
                    mobile = getSession(authenticationType + "_mobile").toString();
                }
            }


            logger.info("sendAuthentication email=" + email + ", mobile=" + mobile);
            logger.info("sendAuthentication email=" + newEmail + ", mobile=" + newMobile);

            //mails
            StringBuffer emails = new StringBuffer();
            if (email != null && !"".equals(email.trim())) {
                emails.append(email.trim());
            }
            if (newEmail != null && !"".equals(newEmail.trim()) && !email.equals(newEmail)) {
                if (!StringUtils.isBlank(email)) {
                    emails.append(";");
                    emails.append(newEmail.trim());
                } else {
                    emails.append(newEmail);
                }
            }

            //mobiles
            StringBuffer mobiles = new StringBuffer();
            if (mobile != null && !"".equals(mobile.trim())) {
                mobiles.append(mobile.trim());
            }
            if (newMobile != null && !"".equals(newMobile.trim()) && !StringUtils.equals(mobile, newMobile)) {
                if (!StringUtils.isBlank(mobile)) {
                    mobiles.append(";");
                    mobiles.append(newMobile);
                } else {
                    mobiles.append(newMobile);
                }
            }

            String authentication = sendAuthenticationService.sendAuthentication(emails.toString(), mobiles.toString());
            logger.info("sendAuthentication authentication(otp code)=" + authentication);

            //紀錄驗證碼與時間
            addSession(authenticationType + "Authentication", authentication);
            addSession(authenticationType + "AuthenticationTime", new Date());

            // 20210421 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
            addSession(authenticationType + "AuthenticationCheckCounts", getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));

            this.setResponseObj(ResponseObj.SUCCESS, "", null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            /*this.setResponseObj(ResponseObj.ERROR, "寄送驗證碼失敗!", null);*/
            this.setResponseObj(ResponseObj.ERROR, getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0117"), null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @Autowired
    private IParameterService parameterService;
    /**
     * 認證碼驗證
     *
     * @return
     */
    @PostMapping("register/checkAuthentication")
    public ResponseEntity<ResponseObj> checkAuthentication(@RequestBody AuthenticationVo authenticationVo) {
        logger.info("enter register checkAuthentication! when enter authentication = " + authenticationVo.getAuthenticationNum());
        //紀錄驗證碼與時間
        String message;
        try {
            HttpSession session = getRequest().getSession();
            message = "";

            if (session.getAttribute(authenticationVo.getAuthenticationType() + "Authentication") == null) {
                //message = "驗證碼已失效，請重新寄送驗碼。";
                message = getParameterValue("ESERVICE_JD_CATEGORY", "E0021");
            } else {
                String authentication_s = session.getAttribute(authenticationVo.getAuthenticationType() + "Authentication").toString();
                Date now = new Date();
                Date authenticationTime_s = (Date) session.getAttribute(authenticationVo.getAuthenticationType() + "AuthenticationTime");

                int milli = 5 * (60 * 1000);
                int diffDate = (int) (now.getTime() - authenticationTime_s.getTime());

                if (milli >= diffDate) {
                    //未超過五分鐘
                    if (!authenticationVo.getAuthenticationNum().equals(authentication_s)) {
                        //輸入錯誤
                        //message = "驗證碼輸入錯誤，請確認驗證碼或重新寄送驗證碼。";
                        message = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0022");
                    } else {
                        //驗證完成 清除驗證碼session
                        addSession(authenticationVo.getAuthenticationType() + "Authentication", null);
                        addSession(authenticationVo.getAuthenticationType() + "_isChack", true);
                    }
                } else {
                    //已超過五分鐘
                    //message = "驗證碼已失效，請重新寄送驗碼。";
                    message = getParameterValue(ApConstants.SYSTEM_ID, "E0021");
                }
            }

            this.setResponseObj(ResponseObj.SUCCESS, message, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, "", null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

}
