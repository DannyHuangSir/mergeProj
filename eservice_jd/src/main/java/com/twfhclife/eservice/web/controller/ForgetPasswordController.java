package com.twfhclife.eservice.web.controller;


import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.ValidateCodeUtil;
import com.twfhclife.eservice.util.ValidateUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
public class ForgetPasswordController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ForgetPasswordController.class);

    @Autowired
    private IRegisterUserService registerUserService;

    @Autowired
    private IParameterService parameterService;

    @GetMapping("/password1")
    public String password1() {
        logger.info("open frontstage/password1.html");

        if (getSession(ApConstants.SYSTEM_PARAMETER) == null) {
            List<ParameterVo> listParam = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYSTEM_MSG_PARAMETER);
            Map<String, ParameterVo> mapParam = listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, Function.identity()));
            Map<String, Map<String, ParameterVo>> mapParamSession = new HashMap<>();
            mapParamSession.put(ApConstants.SYSTEM_MSG_PARAMETER, mapParam);
            addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) mapParamSession);
            addSession(ApConstants.SYSTEM_MSG_PARAMETER, (Serializable) mapParam);
        }
        return "frontstage/forgetPassword/password1";
    }

    /**
     * 忘記密碼-查詢使用者帳號
     *
     * @param userVo
     * @param request
     * @return
     */
    @PostMapping("/register/getUserByAccount")
    public ResponseEntity<ResponseObj> getUserByAccount(
            @RequestBody UsersVo userVo,
            HttpServletRequest request) {
        logger.info("enter register getUserByAccount! account = " + userVo.getUserId() + ",validateCode=" + userVo.getValidateCode());
        HttpSession session = request.getSession();
        try {
            String message = "";
            UsersVo user = registerUserService.getUserByAccount(userVo.getUserId());
            if (user != null) {

                //安全機制：不允許ADMIN在此操作忘記密碼
                if ("admin".equalsIgnoreCase(user.getUserType())) {
                    /*message = "不允許的系統操作!";*/
                    message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0000");
                } else {
                    session.setAttribute("forget_account", user.getUserId());
                    if (user.getMailFlag() != null && user.getMailFlag().equals("1")) {
                        session.setAttribute("forget_email", user.getEmail());
                    } else {
                        session.setAttribute("forget_email", null);
                    }
                    if (user.getSmsFlag() != null && user.getSmsFlag().equals("1")) {
                        session.setAttribute("forget_mobile", user.getMobile());
                    } else {
                        session.setAttribute("forget_mobile", null);
                    }
                }

            } else {
                /*message = "所輸入的使用者不存在!";*/
                message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0112");
            }

            //this.setResponseObj(ResponseObj.SUCCESS, message, user);//modify for Penetration Test
            this.setResponseObj(ResponseObj.SUCCESS, message, null);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    /**
     * 進入驗證碼驗證畫面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/password2")
    public String password2(ModelMap modelMap) {
        logger.info("open frontstage/password2.html");
        try {
            String email = getSessionStr("forget_email");
            String mobile = getSessionStr("forget_mobile");
            String strMail = "";
            String strMobile = "";
            if (!email.equals("")) {
                String str = "";
                for (int i = 0; i < email.indexOf("@") - 2; i++) {
                    str = str + "*";
                }

                strMail = email.substring(0, 2) + str + email.substring(email.indexOf("@"));
            }

            if (!mobile.equals("")) {
                strMobile = mobile.substring(0, 3) + "***" + mobile.substring(6);
            }

            Date now = new Date();
            Date authenticationTime_s = getSessionDate("forgetAuthenticationTime");
            int diffDate = (int) ((now.getTime() - authenticationTime_s.getTime()) / 1000);
            int timeSet = 0;
            if (diffDate < 300) {
                timeSet = 300 - diffDate;
            }
            modelMap.addAttribute("timeSet", timeSet);

            modelMap.addAttribute("smsEmail", strMail);
            modelMap.addAttribute("smsMobile", strMobile);
            modelMap.addAttribute("authenticationType", "forget");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "frontstage/forgetPassword/password2";
    }

    @GetMapping("/password3")
    public String password3() {
        logger.info("open frontstage/password3.html");
        return "frontstage/forgetPassword/password3";
    }

    /**
     * 忘記密碼/ 密碼重置
     *
     * @param newPassword
     * @param request
     * @return
     */
    @PostMapping("/register/updatePassword")
    public ResponseEntity<ResponseObj> updatePassword(@RequestBody String newPassword, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String message = "";
            String account = session.getAttribute("forget_account").toString();
            if (StringUtils.equals(account, newPassword)) {
                message = "密碼與代號 帳號不應相同";
            } else {
                boolean isCheck = getSession("forget_isChack") != null ? (boolean) getSession("forget_isChack") : false;
                if (isCheck && getSession("forgetAuthentication") == null) {
                    if (ValidateUtil.isPwd(newPassword)) {
                        registerUserService.updatePassword(account, newPassword);
                        addSession("forget_isChack", null);
                    } else {
                        /*message = "請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";*/
                        message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0014");
                    }
                } else {
                    /*message = "請完成驗證碼驗證!";*/
                    message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0101");
                }
            }
            this.setResponseObj(ResponseObj.SUCCESS, message, null);
        } catch (InvalidParameterException e) {
            this.setResponseObj(ResponseObj.ERROR, e.getMessage(), null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @GetMapping("/passwordSuccess")
    public String passwordSuccess() {
        logger.info("open frontstage/password-success.html");
        return "frontstage/forgetPassword/password-success";
    }

}
