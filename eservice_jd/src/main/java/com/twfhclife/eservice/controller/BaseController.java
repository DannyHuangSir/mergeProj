package com.twfhclife.eservice.controller;

import com.twfhclife.eservice.keycloak.model.KeycloakLoginResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.MyStringUtil;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

public class BaseController extends BaseMvcController {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    @Autowired
    private KeycloakService keycloakService;

    protected boolean loginCheck() throws Exception {
        logger.debug("Check login session alive.");
        KeycloakUser keycloakUser = getLoginUser();
        if (keycloakUser == null) {
            // session不存在，須將user登出
            logger.info("Session not exists, User not logged in!");
            return false;
        }

        boolean isLoginTokenVaild = true;
        if (getClientIp().equals("0:0:0:0:0:0:0:1")) {
            return isLoginTokenVaild;
        }
        try {
            KeycloakLoginResponse apiResponse = null;
            apiResponse = keycloakService.validateToken(keycloakUser.getAccessToken(),
                    keycloakUser.getRefreshToken());
            if (apiResponse != null) {
                String status = MyStringUtil.objToStr(apiResponse.getStatus());
                if (status.equals("REFRESH")) {
                    // 已換發Token，重新放入session
                    keycloakUser.setAccessToken(apiResponse.getAccessToken());
                    keycloakUser.setRefreshToken(apiResponse.getRefreshToken());
                    addSession(ApConstants.KEYCLOAK_USER, keycloakUser);
                } else if (!status.equals("SUCCESS")) {
                    // 驗證不通過，登出
                    logger.info("1.Session timeout, force logout! status=" + status);
                    HttpServletRequest request = getRequest();
                    getRequest().getSession().removeAttribute(ApConstants.KEYCLOAK_USER);
                    Enumeration<String> em = request.getSession().getAttributeNames();
                    while (em.hasMoreElements()) {
                        request.getSession().removeAttribute(em.nextElement().toString());
                    }
                    isLoginTokenVaild = false;
                }
            } else {
                // 驗證失敗，登出
                logger.info("2.Session timeout, force logout!");
                HttpServletRequest request = getRequest();
                request.getSession().removeAttribute(ApConstants.KEYCLOAK_USER);
                Enumeration<String> em = request.getSession().getAttributeNames();
                while (em.hasMoreElements()) {
                    request.getSession().removeAttribute(em.nextElement().toString());
                }

                isLoginTokenVaild = false;
            }
        } catch (Exception e) {
            logger.error("Unable to validateToken: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }

        return isLoginTokenVaild;
    }

    /**
     * 添加變量至request
     *
     * @param key
     * @param value
     */
    protected void addAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    /**
     * 添加系統錯誤至request.
     *
     * @param errorMessage 錯誤訊息
     */
    protected void addSystemError(String errorMessage) {
        addAttribute("errorMessage", errorMessage);
    }

    /**
     * 添加系統錯誤至request.
     */
    protected void addDefaultSystemError() {
        addAttribute("errorMessage", ApConstants.SYSTEM_ERROR);
    }

    /**
     * 添加變量至Session.
     *
     * @return UsersVo
     */
    protected void addSession(String key, Serializable value) {
        getRequest().getSession().setAttribute(key, value);
    }

    /**
     * 移除session中指定變量
     *
     * @param attributeKey
     */
    protected void removeFromSession(String attributeKey) {
        if (attributeKey != null && !"".equals(attributeKey.trim())) {
            Object objAttribute = getRequest().getSession().getAttribute(attributeKey);
            if (objAttribute != null) {
                getRequest().getSession().removeAttribute(attributeKey);
            }
        }
    }

    /**
     * 從Session取出物件.
     *
     * @return Object
     */
    protected Object getSession(String key) {
        return getRequest().getSession().getAttribute(key);
    }

    /**
     * 從Session取出物件回傳字串
     * 如果是空的回傳空字串.
     *
     * @return Object
     */
    protected String getSessionStr(String key) {
        Object session = getRequest().getSession().getAttribute(key);
        String toStr = session != null ? session.toString() : "";
        return toStr;
    }

    /**
     * 從Session取出Date物件
     * 如果是空的回傳今天.
     *
     * @return Object
     */
    protected Date getSessionDate(String key) {
        Object session = getRequest().getSession().getAttribute(key);
        Date sesDate = new Date();
        if (session != null) {
            sesDate = (Date) session;
        }
        return sesDate;
    }

    /**
     * 取得登入者資訊.
     *
     * @return KeycloakUser UsersVo
     */
    protected KeycloakUser getLoginUser() {
        Object userObj = getRequest().getSession().getAttribute(ApConstants.KEYCLOAK_USER);
        return (userObj != null ? (KeycloakUser) userObj : null);
    }

    /**
     * 取得登入者資訊.
     *
     * @return 回傳登入者資訊
     */
    protected UsersVo getUserDetail() {
        Object userObj = getRequest().getSession().getAttribute(UserDataInfo.USER_DETAIL);
        return (userObj != null ? (UsersVo) userObj : null);
    }

    /**
     * 取得登入者的類型-.
     *
     * @return 回傳登入者的類型
     */
    protected String getUserType() {
        UsersVo usersVo = getUserDetail();
        return (usersVo != null ? usersVo.getUserType() : null);
    }

    /**
     * 取得登入者資訊.
     *
     * @return UsersVo
     */
    protected String getUserId() {
        String userId = "";
        String loginUserType = getUserType();
        if ("member".equals(loginUserType)) {
            KeycloakUser keycloakUser = getLoginUser();
            userId = (keycloakUser != null ? keycloakUser.getUsername() : null);
        } else {
            //admin or agent
            Object adminQryUsersObj = getSession("ADMIN_QUERY_USERS");
            if (adminQryUsersObj != null) {
                UsersVo usersVo = (UsersVo) adminQryUsersObj;
                userId = usersVo.getUserId();
            }
        }

        return userId;
    }

    /**
     * 取得session中 parameter category Code 的 參數清單
     *
     * @param categoryCode
     * @return Map<String, ParameterVo>
     */
    protected Map<String, ParameterVo> getParameterMap(String categoryCode) {
        Map<String, Map<String, ParameterVo>> parameterListMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
        Map<String, ParameterVo> parameterList = parameterListMap.get(categoryCode);
        return parameterList;
    }

    /**
     * 取得session中 parameter value (需要category Code)
     *
     * @param categoryCode
     * @param code
     * @return
     */
    protected String getParameterValue(String categoryCode, String code) {
        String value = "";
        try {
            Map<String, ParameterVo> parameterList = getParameterMap(categoryCode);
            value = parameterList.get(code).getParameterValue();
        } catch (Exception e) {
            e.getMessage();
        }
        return value;
    }

    /**
     * 取得請求者IP.
     *
     * @return 回傳請求者IP
     */
    protected String getClientIp() {
        HttpServletRequest req = getRequest();
        String remoteAddr = req.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = req.getRemoteAddr();
        }
        return remoteAddr;
    }

    /**
     * 取得 HttpServletRequest
     *
     * @return request
     */
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected void SuccessMsg(String type, String msg) {
        String email = getSessionStr(type + "_email");
        String mobile = getSessionStr(type + "_mobile");
        String strMail = hideEmail(email);
        String strMobile = hideMobile(mobile);

        UsersVo user = getUserDetail();

        if (msg.indexOf("$mail$") != -1) {
            if (user.getMailFlag().equals("1")) {
                msg = msg.replace("$mail$", strMail);
            } else {
                msg = msg.replace("$mail$", "");
            }
        }

        if (msg.indexOf("$mobile$") != -1) {
            if (user.getSmsFlag().equals("1")) {
                msg = msg.replace("$mobile$", strMobile);
            } else {
                msg = msg.replace("$mobile$", "");
            }
        }
        addAttribute(type + "SuccessMsg", msg);

    }

    protected String hideEmail(String email) {
        String strMail = "";
        if (email != null && !email.equals("")) {
            String str = "";
            for (int i = 0; i < email.indexOf("@") - 2; i++) {
                str = str + "*";
            }

            strMail = email.substring(0, 2) + str + email.substring(email.indexOf("@"));
        }
        return strMail;
    }

    protected String hideMobile(String mobile) {
        String strMobile = "";

        if (StringUtils.isNotBlank(mobile) && mobile.trim().length() >= 10) {//大於等於十碼才執行隱碼動作
            strMobile = mobile.substring(0, 3) + "***" + mobile.substring(6);
        }
        return strMobile;
    }
}
