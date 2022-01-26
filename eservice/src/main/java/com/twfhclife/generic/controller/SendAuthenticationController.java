package com.twfhclife.generic.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.AuthenticationVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.service.ISendAuthenticationService;
import com.twfhclife.generic.util.ApConstants;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableAutoConfiguration
public class SendAuthenticationController extends BaseController {

	private static final Logger logger = LogManager.getLogger(SendAuthenticationController.class);
	
	@Autowired
	private ISendAuthenticationService sendAuthenticationService;
	
	/**
	 * 寄送認證碼  / 輸入的使用者
	 * @return
	 */
	@RequestLog
	@PostMapping("/sendAuthentication")
	public ResponseEntity<ResponseObj> sendAuthentication(@RequestParam(value = "authenticationType",required = false)   String authenticationType,
														  @RequestParam(value = "newMobile",required = false)   String newMobile,
														  @RequestParam(value = "newEmail",required = false)   String newEmail
														  ) {
		logger.info("call sendAuthentication authenticationType="+authenticationType);
		logger.info("call sendAuthentication mobile="+newMobile);
		logger.info("call sendAuthentication email="+newEmail);
		String email = "";
		String mobile = "";
		UsersVo user = new UsersVo();
		try {
			//註冊跳過抓取UserVo
			if(!authenticationType.equals("register") && !authenticationType.equals("forget")){
				user = getUserDetail();
				if(getSession(authenticationType+"_email") != null && user.getMailFlag().equals("1")){
					email = getSession(authenticationType+"_email").toString();
				}
				if(getSession(authenticationType+"_mobile") != null && user.getSmsFlag().equals("1")){
					mobile = getSession(authenticationType+"_mobile").toString();
				}
			}else{
				if(getSession(authenticationType+"_email") != null){
					email = getSession(authenticationType+"_email").toString();
				}
				if(getSession(authenticationType+"_mobile") != null){
					mobile = getSession(authenticationType+"_mobile").toString();
				}
			}

			
			logger.info("sendAuthentication email="+email+", mobile="+mobile);
			logger.info("sendAuthentication email="+newEmail+", mobile="+newMobile);
			
			//mails
			StringBuffer emails = new StringBuffer();
			if(email!=null && !"".equals(email.trim())){
				emails.append(email.trim());
			}
			if(newEmail!=null && !"".equals(newEmail.trim()) && !email.equals(newEmail)){
				if(!StringUtils.isBlank(email)) {
				emails.append(";");
				emails.append(newEmail.trim());
				}else {
					emails.append(newEmail);
				}
			}
			
			//mobiles
			StringBuffer mobiles = new StringBuffer();
			if(mobile!=null && !"".equals(mobile.trim())){
				mobiles.append(mobile.trim());
			}
			if(newMobile!=null && !"".equals(newMobile.trim())  && !mobile.equals(newMobile)){
				if(!StringUtils.isBlank(mobile)){
				mobiles.append(";");
				mobiles.append(newMobile);
				}else{
					mobiles.append(newMobile);
				}
			}
			
			String authentication = sendAuthenticationService.sendAuthentication(emails.toString(), mobiles.toString());
			logger.info("sendAuthentication authentication(otp code)="+authentication);
			
			//紀錄驗證碼與時間
			addSession(authenticationType+"Authentication", authentication);
			addSession(authenticationType+"AuthenticationTime", new Date());

			// 20210421 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
			addSession(authenticationType+"AuthenticationCheckCounts", getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));
			
			this.setResponseObj(ResponseObj.SUCCESS, "", null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			/*this.setResponseObj(ResponseObj.ERROR, "寄送驗證碼失敗!", null);*/
			this.setResponseObj(ResponseObj.ERROR, getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0117"), null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	/**
	 * 認證碼驗證
	 * @return
	 */
	@RequestLog
	@PostMapping("/checkAuthentication")
	public ResponseEntity<ResponseObj> checkAuthentication(@RequestBody AuthenticationVo authentication){
		//紀錄驗證碼與時間
		String message = "";
		String authenticationType = authentication.getAuthenticationType();
		String authenticationNum = authentication.getAuthenticationNum();
		try {
			if(getSession(authenticationType+"Authentication") == null){
				//message = "驗證碼已失效，請重新寄送驗碼。";
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");
			}else{
				String authentication_s = getSessionStr(authenticationType+"Authentication").toString();
				Date now = new Date();
				Date authenticationTime_s = getSessionDate(authenticationType+"AuthenticationTime");
				logger.info("驗證碼寄送時間: {} ; 正確驗證碼: {}", authenticationTime_s, authentication_s);
				int milli = 5*(60*1000);
				int diffDate = (int) (now.getTime()-authenticationTime_s.getTime());
				
				if(milli >= diffDate){
					//未超過五分鐘
					if(!authenticationNum.equals(authentication_s)){
						//輸入錯誤
						//message = "驗證碼輸入錯誤，請確認驗證碼或重新寄送驗證碼。";
						message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0022");
					}else{
						//驗證完成 清除驗證碼session
						addSession(authenticationType+"Authentication", null);
						addSession(authenticationType+"_isCheck", true);
					}
				}else{
					//已超過五分鐘
					//message = "驗證碼已失效，請重新寄送驗碼。";
					message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");
				}
			}
		} catch (Exception e) {
			message = ApConstants.SYSTEM_ERROR;
			logger.error("checkAuthentication: {}", ExceptionUtils.getStackTrace(e));
		}	
		this.setResponseObj(ResponseObj.SUCCESS, message, null);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
}
