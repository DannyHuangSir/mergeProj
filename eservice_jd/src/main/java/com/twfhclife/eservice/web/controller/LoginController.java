package com.twfhclife.eservice.web.controller;

import com.google.common.base.Splitter;
import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.IpUtil;
import com.twfhclife.eservice.util.ValidateCodeUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;


/**
 * 首頁登入.
 *
 * @author all
 */
@Controller
public class LoginController extends BaseController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private IRegisterUserService registerUserService;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private ILoginService loginService;

    /**
     * 登入.
     *
     * @param loginRequestVo LoginRequestVo
     * @return
     */
    @PostMapping("/doLogin")
    public String doLogin(LoginRequestVo loginRequestVo) {
        KeycloakUser keycloakUser = null;
        String loginSuccessPage = "redirect:dashboard";
        String userId = loginRequestVo.getUserId();
        try {
            // 取得session驗證碼
            Object validateCodeObj = getSession(ApConstants.LOGIN_VALIDATE_CODE);
            if (validateCodeObj != null) {
                loginRequestVo.setSessionValidateCode((String) validateCodeObj);
            }

            //decrypt pw,retset to loginRequestVo-start
            String encryptPw = loginRequestVo.getPassword();
            String decryptPw = decodeBase64(encryptPw);
            loginRequestVo.setPassword(decryptPw);
            System.out.println("decryptPw=" + decryptPw);
            logger.info("decryptPw=" + decryptPw);

            LoginResultVo loginResultVo = loginService.doLogin(loginRequestVo);
            String returnCode = loginResultVo.getReturnCode();
            String returnMsg = loginResultVo.getReturnMsg();
            String loginType = loginResultVo.getLoginType();
            keycloakUser = loginResultVo.getKeycloakUser();
            boolean verifyValidateCode = loginResultVo.isVerifyValidateCode();
            String clientIp = getClientIp();

            //IP address  validate
            if (StringUtils.isNotBlank(clientIp) && StringUtils.equals("localhost", clientIp)
                    && !StringUtils.equals("127.0.0.1", clientIp) && !StringUtils.equals(clientIp, "0:0:0:0:0:0:0:1")) {
                String whiteStr = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID_JD, "ESERVICE_JD_IP_WHITE_LIST");
                if (StringUtils.isNotBlank(whiteStr)) {
                    List<String> whiteList = Splitter.on(";").omitEmptyStrings().splitToList(whiteStr);
                    for (String ip : whiteList) {
                        if (IpUtil.isInRange(clientIp, ip)) {
                            break;
                        }
                        addAttribute("errorMessage", "不允許登錄");
                        resetVerifyCode();
                        return "login";
                    }
                }
            }

            if (!verifyValidateCode) {
                addAttribute("errorMessage", "驗證碼不正確");
                resetVerifyCode();
                return "login";
            }

            UsersVo userDetail = registerUserService.getUserByAccount(userId);
			if (userDetail == null) {
				addAttribute("errorMessage", "用戶不存在！");
				resetVerifyCode();
				return "login";
			}

			String expireDay = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID_JD, "LOGIN_EXPIRE_DAY");
			if (StringUtils.isNotBlank(expireDay) && userDetail.getLoginTime() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(userDetail.getLoginTime());
				calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(expireDay));
				if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
					addAttribute("errorMessage", "距離上次登入超過半年，請重設密碼！");
					addAuditLog(userId, "0", loginRequestVo.getEuNationality());
					return "login";
				}
			}

            String failCount = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID_JD, "LOGIN_FAIL_COUNT");
            if (StringUtils.isNotBlank(failCount) && userDetail.getLoginFailCount() != null) {
                if (Integer.parseInt(failCount) <= userDetail.getLoginFailCount()) {
                    addAttribute("errorMessage", "登錄失敗次數過多，請重設密碼！");
                    addAuditLog(userId, "0", loginRequestVo.getEuNationality());
                    return "login";
                }
            }

            if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
                if (userDetail.getStatus().equals("locked")) {
                    String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0099");
                    addAttribute("errorMessage", errorMessage);
                    addAuditLog(userId, "0", loginRequestVo.getEuNationality());
                    return "login";
                }

                addSession(UserDataInfo.USER_DETAIL, userDetail);
                addSession(ApConstants.KEYCLOAK_USER, keycloakUser);
                addSession(ApConstants.LOGIN_USER_ID, keycloakUser.getUsername());

                // 設定系統參數
                Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID);
                addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) sysParamMap);
                addAttribute(ApConstants.SYSTEM_CONSTANTS, (Serializable) sysParamMap.get("SYSTEM_CONSTANTS"));

                // 設定登出倒數秒數
                long logutTimeoutSeconds = 0;
                try {
                    String timeoutSeconds = sysParamMap.get("SYSTEM_CONSTANTS").get("TIMEOUT_SECONDS").getParameterValue();
                    logutTimeoutSeconds = Long.parseLong(timeoutSeconds);
                } catch (Exception e) {
                    logutTimeoutSeconds = ApConstants.LOGUT_TIME_DURATION_SECOND;
                }
                addSession(ApConstants.TIMEOUT_SECONDS, logutTimeoutSeconds);

                if (userDetail != null) {
                    Date lastLoginTime = loginService.getLastLoginTime(userId);
                    if (lastLoginTime != null) {
                        addSession(UserDataInfo.LAST_LOGIN_TIME, lastLoginTime);
                    }
                }
                addAttribute("errorMessage", "");
            } else {
                if ("password".equals(loginType) || (userDetail != null && userDetail.getStatus().equals("locked"))) {
                    String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0099");
                    addAttribute("errorMessage", errorMessage);
                } else {
                    if (returnCode.equals("API_ERROR")) {
                        addAttribute("errorMessage", returnMsg);
                    } else {
                        String errorMessage = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0100");
                        addAttribute("errorMessage", errorMessage);
                    }
                }
                resetVerifyCode();
				registerUserService.incLoginFailCount(userId);
                addAuditLog(userId, "0", loginRequestVo.getEuNationality());
                return "login";
            }
        } catch (Exception e) {
            logger.error("Unable to doLogin: {}", ExceptionUtils.getStackTrace(e));
            addAttribute("errorMessage", ApConstants.SYSTEM_ERROR);
            if (keycloakUser != null) {
                keycloakService.logout(keycloakUser.getId());
            }
            resetVerifyCode();
			registerUserService.incLoginFailCount(userId);
            return "login";
        }

        addAuditLog(userId, "1", loginRequestVo.getEuNationality());
		registerUserService.updateLoginSuccess(userId);
        return loginSuccessPage;
    }

    private void addAuditLog(String userId, String status, String euNationality) {
        // 記錄登入記錄
        try {
            AuditLogVo vo = new AuditLogVo();
            vo.setUserId(userId);
            vo.setLoginStatus(status);
            vo.setClientIp(getClientIp());

            //SR_GDPR_start
            vo.setEuNationality("GDPR=" + StringUtils.trimToEmpty(euNationality));
            //SR_GDPR_end

            loginService.addAuditLog(vo);
        } catch (Exception e) {
            logger.error("Unable to add login audit log: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 登出.
     *
     * @param request
     * @return
     */
    @GetMapping("/doLogout")
    public String doLogout(HttpServletRequest request) {
        try {
            loginService.updateLogoutDate(getUserId());

            Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
            if (KeycloakUserObj != null) {
                KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
                try {
                    loginService.doLogout(ApConstants.KEYCLOAK_REALM, keycloakUser.getId());
                } catch (Exception e) {
                    logger.error("Unable to logout by api: ", e);
                    keycloakService.logout(keycloakUser.getId());
                }
                logger.info(keycloakUser.getUsername() + " logout success!");
            }

            Enumeration<String> em = request.getSession().getAttributeNames();
            while (em.hasMoreElements()) {
                request.getSession().removeAttribute(em.nextElement().toString());
            }
        } catch (Exception e) {
            logger.error("Unable to doLogout: {}", ExceptionUtils.getStackTrace(e));
        }

        resetVerifyCode();
        return "redirect:login";
    }

    /**
     * 取得圖形驗證碼.
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/png");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);

            ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 20);
            addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());

            vcUtil.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("Unable to getVerify: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    private void resetVerifyCode() {
        // 設定驗證碼圖示
        ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
        addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
        addAttribute("validateImageBase64", vcUtil.imgToBase64String());
    }

    @RequestMapping("/login-timeout")
    public String logout() {
        logger.debug("force logout");
        return "force_logout";
    }

    @RequestMapping("/confirmLogout")
    public String confirmLogout() {
        return "confirmLogout";
    }

    /**
     * 登出其他重複登入的session
     *
     * @return
     */
    @PostMapping("/logoutOtherUser")
    public String logoutOtherUser() {
        String loginSuccessPage = "redirect:dashboard";
        try {
            KeycloakUser keycloakUser = getLoginUser();
            String userId = getUserId();
            String keycloakUserId = keycloakUser.getId();
            String nowSessionId = keycloakUser.getSessionState();
            List<String> userSessionList = loginService.getKeycloakUserSessionIdList(keycloakUserId);
            logger.debug("logoutOtherUser: Find user[{}] this login sessionId: {}", userId, nowSessionId);
            logger.debug("logoutOtherUser: Find user[{}] sessionList: {}", userId, userSessionList);

            List<String> removeSessionIdList = new ArrayList<>();
            if (userSessionList != null && userSessionList.size() > 0) {
                for (String sessionId : userSessionList) {
                    if (!sessionId.equals(nowSessionId)) {
                        if (!removeSessionIdList.contains(sessionId)) {
                            removeSessionIdList.add(sessionId);
                        }
                    }
                }
            }
            String returnCode = loginService.logoutOtherUser(removeSessionIdList);
            logger.debug("Remove user[{}] other sessionList[{}] result: {}", userId, removeSessionIdList, returnCode);

            // 判斷是否為內部人員及保代角色
            UsersVo userDetail = registerUserService.getUserByAccount(keycloakUser.getUsername());
            boolean isPartnerRole = loginService.isPartnerRole(userDetail.getUserType());
            if (isPartnerRole) {
                loginSuccessPage = "redirect:partner";
            }

        } catch (Exception e) {
            logger.error("Unable to logoutOtherUser: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return loginSuccessPage;
    }

    /**
     * 放棄本次登入session
     *
     * @param request
     * @return
     */
    @GetMapping("/abortLogin")
    public String abortLogin(HttpServletRequest request) {
        try {
            String userId = getUserId();
            loginService.updateLogoutDate(userId);

            Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
            if (KeycloakUserObj != null) {
                KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
                String nowSessionId = keycloakUser.getSessionState();

                List<String> removeSessionIdList = new ArrayList<>();
                removeSessionIdList.add(nowSessionId);

                String returnCode = loginService.logoutOtherUser(removeSessionIdList);
                logger.debug("Remove user[{}] sessionList[{}] result: {}", userId, removeSessionIdList, returnCode);
            }

            Enumeration<String> em = request.getSession().getAttributeNames();
            while (em.hasMoreElements()) {
                request.getSession().removeAttribute(em.nextElement().toString());
            }
        } catch (Exception e) {
            logger.error("Unable to abortLogin: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        resetVerifyCode();
        return "redirect:login";
    }

    // FB或自然人登入前先比對驗證碼
    @PostMapping("/preCheckkValidateCode")
    public ResponseEntity<ResponseObj> preCheckkValidateCode(@RequestBody String validateCode, HttpServletRequest request) {
        try {
            boolean isValidateCodeOk = false;
            // 取得session驗證碼
            Object validateCodeObj = getSession(ApConstants.LOGIN_VALIDATE_CODE);
            if (validateCodeObj != null) {
                //System.out.println("input validateCode="+validateCode);
                //System.out.println("session (validateCodeObj)="+(String)validateCodeObj);
                isValidateCodeOk = StringUtils.equals(validateCode, (String) validateCodeObj);
            } else {
                // 設定驗證碼圖示
                ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
                addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
                addAttribute("validateImageBase64", vcUtil.imgToBase64String());
            }

            String message = "";
            if (!isValidateCodeOk) {
                message = "驗證碼不正確";
            }

            this.setResponseObj(ResponseObj.SUCCESS, message, "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @PostMapping("/setSessioinCardSN")
    public ResponseEntity<ResponseObj> setSessioinCardSN(
            @RequestBody String cardSn,
            HttpServletRequest request) {

        //String afterEncryptCardSN = "";

        try {
            System.out.println("In setSessioinCardSN.");
            logger.info("In setSessioinCardSN,cardSn=" + cardSn);

            if (!StringUtils.isEmpty(cardSn)) {
                addSession("SESSION_CARDSN", cardSn.trim());
            }

            this.setResponseObj(ResponseObj.SUCCESS, "", "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());

    }


    /**
     * 加密自然人憑證卡號
     *
     * @param sCardSN
     * @return
     */
    @PostMapping("/encryptCardSN")
    public Object encryptCardSN(@RequestBody String sCardSN) {
        String afterEncryptCardSN = "";
        try {
            logger.info("Start to enctypt moica cardSN.");
            System.out.println("Start to enctypt moica cardSN.");

            //檢核session validate code是否仍有效


            //加密
            Map<String, Object> dataResult = new HashMap<>();
            if (!StringUtils.isEmpty(sCardSN)) {

                //AES
                afterEncryptCardSN = sCardSN + "ooxx";
                dataResult.put("cardSN", afterEncryptCardSN);

                addSession("CARDSN", sCardSN);

            }

            String message = "";
            this.setResponseObj(ResponseObj.SUCCESS, message, dataResult);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    /**
     * 加密解密演算法
     */
    private String decodeBase64(String mi) {

        String mingwen = "";
        if (mi == null || mi.equals("")) {
            //donothing.
        } else {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] by = decoder.decodeBuffer(mi);
                mingwen = new String(by);
            } catch (Exception e) {
                e.printStackTrace();
            }
//			System.out.println("解密後[" mingwen "]");
        }
        return mingwen;
    }

}