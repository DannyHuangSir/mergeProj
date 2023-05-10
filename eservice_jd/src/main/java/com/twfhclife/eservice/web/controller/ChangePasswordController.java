package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.ValidateUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * 修改密碼
 * (無保單)
 */
@Controller
@EnableAutoConfiguration
public class ChangePasswordController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ChangePasswordController.class);

    @Autowired
    private IRegisterUserService registerUaerService;

    @Autowired
    private KeycloakService keycloakService;



    @GetMapping("/changePassword1")
    public String changePassword1(UsersVo usersVo) {
        try {
            //備註
            String changePassword = getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE, ApConstants.WORDING_NA201);
            addAttribute("changePasswordNote", changePassword);
            UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
            if (userDetail.getLastChangPasswordDate() == null) {
                addAttribute("errorMessage", "為確保您的帳號安全，請立即變更密碼");
            }
        } catch (Exception e) {
            logger.error("changeInfo1 error! {}", e);
        }
        return "frontstage/onlineChange/changePassword/change-password1";
    }


    @PostMapping("/changePassword1/updatePassword")
    public ResponseEntity<ResponseObj> updatePassword(@RequestBody UsersVo users) {
        String message = "";
        try {
            String userId = "";
            String loginUserType = getUserType();
            if (StringUtils.equals("member", loginUserType)) {
                userId = getUserId();
            } else {
                //admin or agent
                UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
                userId = userDetail.getUserId();
            }

            if (StringUtils.equals(userId, users.getNewPassword())) {
                message = "密碼與代號 帳號不應相同";
            }  else if (ValidateUtil.simpleLetterAndNumCheck(users.getNewPassword(), 2)) {
                message = "不應訂為相同的英數字、連續英文字或連號數字";
            } else {
                KeycloakUser keycloakUser = keycloakService.login(userId, users.getPassword());
                if (keycloakUser != null && keycloakUser.getAccessToken() != null) {
                    if (ValidateUtil.isPwd(users.getNewPassword())) {
                        message = registerUaerService.changePassword(userId, users.getNewPassword());
                    } else {
                        /*message = "請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";*/
                        message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0024");
                    }
                } else {
                    /*message = "密碼錯誤!";*/
                    message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0118");
                }
            }
            addBussinessEvent("JD-021", getUserId(), "經代變更密碼");
            this.setResponseObj(ResponseObj.SUCCESS, message, null);
        } catch (InvalidParameterException e) {
            this.setResponseObj(ResponseObj.ERROR, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("updatePassword error! {}", e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @GetMapping("/changePasswordSuccess")
    public String changePasswordSuccess() {
        try {
            //備註
            String changePasswordSuccess = getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE, ApConstants.WORDING_NA202);
            SuccessMsg("changePassword", changePasswordSuccess);

            String loginUserType = getUserType();
            if (StringUtils.equals("member", loginUserType)) {
                //userId = getUserId();
                UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
                userDetail.setLastChangPasswordDate(Calendar.getInstance().getTime());
            } else {
                //admin or agent
                UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
                userDetail.setLastChangPasswordDate(Calendar.getInstance().getTime());
            }
        } catch (Exception e) {
            logger.error("changePasswordSuccess error! {}", e);
        }
        return "frontstage/onlineChange/changePassword/change-password-success";
    }
}
