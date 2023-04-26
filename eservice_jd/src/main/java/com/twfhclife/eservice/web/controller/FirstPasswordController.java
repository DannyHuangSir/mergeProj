package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.ValidateUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class FirstPasswordController extends BaseController {

    private static final Logger logger = LogManager.getLogger(FirstPasswordController.class);

    @Autowired
    private IRegisterUserService registerUserService;

    @Autowired
    private IParameterService parameterService;

    @GetMapping("/firstPassword1")
    public String password1() throws Exception {
        if (getSession(ApConstants.SYSTEM_PARAMETER) == null) {
            List<ParameterVo> listParam = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYSTEM_MSG_PARAMETER);
            Map<String, ParameterVo> mapParam = listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, Function.identity()));
            Map<String, Map<String, ParameterVo>> mapParamSession = new HashMap<>();
            mapParamSession.put(ApConstants.SYSTEM_MSG_PARAMETER, mapParam);
            addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) mapParamSession);
            addSession(ApConstants.SYSTEM_MSG_PARAMETER, (Serializable) mapParam);
        }
        getUserByAccount(getSessionStr(ApConstants.LOGIN_USER_ID), getRequest());
        addAttribute("JD_CLAUSE1", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "JD_CLAUSE1"));
        addAttribute("JD_CLAUSE2", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "JD_CLAUSE2"));
        return "frontstage/firstPassword/password1";
    }

    private void getUserByAccount(String userId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        UsersVo user = registerUserService.getUserByAccount(userId);
        if (user != null) {
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
        } else {
            throw new Exception("用戶不存在！");
        }
    }

    /**
     * 進入驗證碼驗證畫面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/firstPassword2")
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
        return "frontstage/firstPassword/password2";
    }

    @GetMapping("/firstPassword3")
    public String password3() {
        logger.info("open frontstage/password3.html");
        return "frontstage/firstPassword/password3";
    }

    @GetMapping("/firstPasswordSuccess")
    public String passwordSuccess() {
        logger.info("open frontstage/password-success.html");
        return "frontstage/firstPassword/password-success";
    }
}
