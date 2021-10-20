package com.twfhclife.generic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.generic.service.ISendAuthenticationService;
import com.twfhclife.generic.service.IUnicodeService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

public class BaseController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(BaseController.class);
	
	@Autowired
	private ISendAuthenticationService sendAuthenticationService;
	
	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	protected IUnicodeService unicodeService;
	
	@Value("${onlinechange.enable.entry}")
	protected String onlinechangeEnableEntry;
	
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
				if(status.equals("REFRESH")) {
					// 已換發Token，重新放入session
					keycloakUser.setAccessToken(apiResponse.getAccessToken());
					keycloakUser.setRefreshToken(apiResponse.getRefreshToken());
					addSession(ApConstants.KEYCLOAK_USER, keycloakUser);
				} else if(!status.equals("SUCCESS")) {
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
	 * @param attributeKey
	 */
	protected void removeFromSession(String attributeKey) {
		if(attributeKey!=null && !"".equals(attributeKey.trim())){
			Object objAttribute = getRequest().getSession().getAttribute(attributeKey);
			if(objAttribute!=null) {
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
		if(session != null){
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
	 * 取得登入者證號.
	 * 
	 * @return 回傳登入者證號
	 */
	protected String getUserRocId() {
		String rocId = "";
		String loginUserType = getUserType();
		if ("admin".equals(loginUserType)) {
			Object adminQryUsersObj = getSession("ADMIN_QUERY_USERS");
			if (adminQryUsersObj != null) {
				UsersVo usersVo = (UsersVo) adminQryUsersObj;
				rocId = usersVo.getRocId();
			}
		} else {
			UsersVo usersVo = getUserDetail();
			if (usersVo != null) {
				rocId = usersVo.getRocId();
			}
		}
		return rocId;
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
	 * 取得要保人名稱資訊.
	 * 
	 * @return 回傳要保人名稱
	 */
	protected String getProposerName() {
		String proposerName = "";
		String loginUserType = getUserType();
		if ("admin".equals(loginUserType)) {
			Object adminQryUsersObj = getSession("ADMIN_QUERY_USERS");
			if (adminQryUsersObj != null) {
				UsersVo usersVo = (UsersVo) adminQryUsersObj;
				proposerName = usersVo.getUserName();
			}
		} else {
			proposerName = getUserDetail().getUserName();
		}
		
		return proposerName;
	}

	/**
	 * 取得代理人代碼.
	 * 
	 * @return 回傳代理人代碼
	 */
	protected String getAgentCode() {
		String agentCode = "";
		String userId = getUserId();
		if (userId.startsWith("0")) {
			agentCode = userId.substring(1, 3);
		} else {
			agentCode = userId.substring(0, 3);
		}
		return agentCode;
	}
	
	/**
	 * 取得登入者保單號碼清單.
	 * 
	 * @return 回傳登入者保單號碼清單
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getUserPolicyNo() {
		Object userPolicyNoListObj = getRequest().getSession().getAttribute(UserDataInfo.USER_POLICY_NO_LIST);
		return (userPolicyNoListObj != null ? (List<String>) userPolicyNoListObj : null);
	}

	/**
	 * 取得保單號碼.
	 * 
	 * @return 回傳保單號碼
	 */
	protected String getRequestPolicyNo() {
		return getRequest().getParameter("policyNo");
	}
	
	/**
	 * 取得session中 parameter category Code 的 參數清單
	 * @param categoryCode
	 * @return Map<String, ParameterVo>
	 */
	protected Map<String, ParameterVo> getParameterMap(String categoryCode){
		Map<String, Map<String, ParameterVo>> parameterListMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
		Map<String, ParameterVo> parameterList = parameterListMap.get(categoryCode);
		return parameterList;
	}
	
	/**
	 * 取得session中 parameter category Code 的 參數清單
	 * @param categoryCode
	 * @return List<ParameterVo>
	 */
	protected List<ParameterVo> getParameterList(String categoryCode){
		Map<String, Map<String, ParameterVo>> parameterListMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
		Map<String, ParameterVo> parameterList = parameterListMap.get(categoryCode);
		List<ParameterVo> list = new ArrayList<ParameterVo>();
		for (Object key : parameterList.keySet()) {
			list.add(parameterList.get(key));
		}
		return list;
	}
	
	
	/**
	 * 取得session中 parameter value (需要category Code)
	 * @param categoryCode
	 * @param code
	 * @return
	 */
	protected String getParameterValue(String categoryCode, String code){
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
	 * 取得session中 parameter name (需要category Code)
	 * @param categoryCode
	 * @param code
	 * @return
	 */
	protected String getParameterName(String categoryCode, String code){
		String name = "";
		try {
			Map<String, ParameterVo> parameterList = getParameterMap(categoryCode);
			name = parameterList.get(code).getParameterName();
		} catch (Exception e) {
			e.getMessage();
		}
		
		return name;
	}
	
	/**
	 * 取得請求者IP.
	 * 
	 * @return 回傳請求者IP
	 */
	protected String getClientIp() {
		HttpServletRequest req = getRequest();
		String remoteAddr = req.getHeader("X-FORWARDED-FOR");
		if (StringUtils.isEmpty(remoteAddr)) {
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
	
	/**
	 * 取得 HttpServletResponse
	 * 
	 * @return response
	 */
	protected HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	/**
	 * 驗證碼寄送前User資訊
	 * @param categoryCode
	 * @param code
	 * @return
	 */
	protected void setUserForSendAuth(String type){
		String userId = getUserId();
		KeycloakUser userInfo = getLoginUser();
		addSession(type+"_userId", userId);
		addSession(type+"_mobile", userInfo.getMobile());
		addSession(type+"_email", userInfo.getEmail());
	}
	
	protected String hideEmail(String email){
		String strMail = "";
		if(email != null && !email.equals("")){
			String str = "";
			for(int i=0 ; i<email.indexOf("@")-2 ; i++){
				str = str + "*";
			}
			
			strMail = email.substring(0, 2)+str+email.substring(email.indexOf("@"));
		}
		return strMail;
	}
	
	protected String hideMobile(String mobile){
		String strMobile = "";
			
		if(StringUtils.isNotBlank(mobile) && mobile.trim().length()>=10){//大於等於十碼才執行隱碼動作
			strMobile = mobile.substring(0,3) + "***" + mobile.substring(6);
		}
		return strMobile;
	}
	
	/**
	 * 驗證碼寄送前User資訊
	 * @param
	 * @param
	 * @return
	 */
	protected void setAuthenticationShow(String type){
		String email = getSessionStr(type+"_email");
		String mobile = getSessionStr(type+"_mobile");
		String strMail = hideEmail(email);
		String strMobile = hideMobile(mobile);
		
		Date now = new Date();
		Date authenticationTime_s = getSessionDate(type+"AuthenticationTime");
		int diffDate = (int) ((now.getTime()-authenticationTime_s.getTime())/1000);
		int timeSet = 0;
		if(diffDate < 300){
			timeSet = 300-diffDate;
		}
		addAttribute("timeSet", timeSet);

		UsersVo usersVo = getUserDetail();
		if (usersVo != null) {
			addAttribute("smsEmail", "1".equals(usersVo.getMailFlag()) ? strMail : "");
			addAttribute("smsMobile", "1".equals(usersVo.getSmsFlag()) ? strMobile : "");
		} else {
			addAttribute("smsEmail", strMail);
			addAttribute("smsMobile", strMobile);
		}
		addAttribute("authenticationType", type);
	}
	
	protected void sendAuthCode(String authenticationType) {
		try {
			setUserForSendAuth(authenticationType);

			// 20210628 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
			if (getSession(authenticationType+"Authentication") == null) {
			String email = "";
			String mobile = "";
			UsersVo usersVo = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
			if("1".equals(usersVo.getMailFlag()) && getSession(authenticationType+"_email") != null){
				email = getSession(authenticationType+"_email").toString();
			}
			if("1".equals(usersVo.getSmsFlag()) && getSession(authenticationType+"_mobile") != null){
				mobile = getSession(authenticationType+"_mobile").toString();
			}
			
			//String authentication = "0000";//POC後還原
					//sendAuthenticationService.sendAuthentication(email, mobile);
			String authentication = sendAuthenticationService.sendAuthentication(email, mobile);
			logger.info("sendAuthentication authentication(otp code)="+authentication);
			
			// 紀錄驗證碼與時間
			addSession(authenticationType+"Authentication", authentication);
			addSession(authenticationType+"AuthenticationTime", new Date());
			addSession(authenticationType+"AuthenticationCheckCounts", getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));
			}
			//else {
			//	addSession(authenticationType+"AuthenticationTime", new Date());
			//}

			setAuthenticationShow(authenticationType);
		} catch (Exception e) {
			logger.error("sendAuthCode error: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	protected void sendAuthCode(String authenticationType,String newMail, String newMobile) {
		try {
			setUserForSendAuth(authenticationType);

			// 20210628 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
			if (getSession(authenticationType+"Authentication") == null) {
				String email = "";
				String mobile = "";
				UsersVo usersVo = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				if("1".equals(usersVo.getMailFlag()) && getSession(authenticationType+"_email") != null){
					email = getSession(authenticationType+"_email").toString();
				}
				if("1".equals(usersVo.getSmsFlag()) && getSession(authenticationType+"_mobile") != null){
					mobile = getSession(authenticationType+"_mobile").toString();
				}

				//String authentication = "0000";//POC後還原
				//sendAuthenticationService.sendAuthentication(email, mobile);
		/*		if (newMail != null) {
					email=email+";"+newMail;
				}
				if (newMobile != null) {
					mobile=mobile+";"+newMobile;
				}*/
				StringBuffer emails  = new StringBuffer();
				StringBuffer mobiles = new StringBuffer();
				if(email!=null && !"".equals(email)){
					emails.append(email);
				}
				if(newMail!=null && !"".equals(newMail) && !newMail.equals(email) ){
					emails.append(";"+newMail);
				}
				if(mobile!=null && !"".equals(mobile)){
					mobiles.append(mobile);
				}
				if(newMobile!=null && !"".equals(newMobile) && !newMobile.equals(mobile)){
					mobiles.append(";"+newMobile);
				}
				String authentication = sendAuthenticationService.sendAuthentication(emails.toString(), mobiles.toString());
				logger.info("sendAuthentication authentication(otp code)="+authentication);

				// 紀錄驗證碼與時間
				addSession(authenticationType+"Authentication", authentication);
				addSession(authenticationType+"AuthenticationTime", new Date());
				addSession(authenticationType+"AuthenticationCheckCounts", getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));
			}
			//else {
			//	addSession(authenticationType+"AuthenticationTime", new Date());
			//}

			setAuthenticationShow(authenticationType);
		} catch (Exception e) {
			logger.error("sendAuthCode error: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	protected String checkAuthCode(String authenticationType, String authenticationNum) {
		//紀錄驗證碼與時間
		String message = "";
		try {
			if(getSession(authenticationType+"Authentication") == null){
				//message = "驗證碼已失效，請重新寄送驗碼。";
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");
			}else{
				// 20210625 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
				int authentication_ccs = Integer.parseInt(getSessionStr(authenticationType+"AuthenticationCheckCounts"));

				String authentication_s = getSessionStr(authenticationType+"Authentication").toString();
				Date now = new Date();
				Date authenticationTime_s = getSessionDate(authenticationType+"AuthenticationTime");
				logger.info("驗證碼寄送時間: {} ; 正確驗證碼: {}", authenticationTime_s, authentication_s);
				int milli = 5*(60*1000);
				int diffDate = (int) (now.getTime()-authenticationTime_s.getTime());
				
				if(milli >= diffDate){
					//未超過五分鐘
					// 20210628 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS) AUTH_CHECK_COUNTS 值設為 5
					if (authentication_ccs <= 0) {
						message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");						
					}
					else {
					if(!authenticationNum.equals(authentication_s)){
						//輸入錯誤
						//message = "驗證碼輸入錯誤，請確認驗證碼或重新寄送驗證碼。";
						message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0022");
	
						authentication_ccs--;
						addSession(authenticationType+"AuthenticationCheckCounts", authentication_ccs);
							if (authentication_ccs <= 0) {
								message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");
								addSession(authenticationType+"AuthenticationTime", new Date( (int)(now.getTime()-milli) ));
							}
					}else{
						//驗證完成 清除驗證碼session
						addSession(authenticationType+"Authentication", null);
						addSession(authenticationType+"_isCheck", true);
					}
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
		return message;
	}
	
	protected void SuccessMsg(String type, String msg){
		String email = getSessionStr(type+"_email");
		String mobile = getSessionStr(type+"_mobile");
		String strMail = hideEmail(email);
		String strMobile = hideMobile(mobile);
		
		UsersVo user = getUserDetail();
		
		if(msg.indexOf("$mail$") != -1){
			if(user.getMailFlag().equals("1")){
				msg = msg.replace("$mail$", strMail);
			}else{
				msg = msg.replace("$mail$", "");
			}
		}
		
		if(msg.indexOf("$mobile$") != -1){
			if(user.getSmsFlag().equals("1")){
				msg = msg.replace("$mobile$", strMobile);
			}else{
				msg = msg.replace("$mobile$", "");
			}
		}
		addAttribute(type+"SuccessMsg", msg);
		
	}
	
	/**
	 * 檢查可以使用 Online Change 功能
	 * @return boolean 
	 */
	protected boolean checkCanUseOnlineChange() {
		try {
			String str = getRequest().getServletPath().replaceFirst("/", "");
			logger.debug("checkCanUseOnlineChange servletPath="+str+",onlinechangeEnableEntry="+onlinechangeEnableEntry+",onlineFlag="+getUserDetail().getOnlineFlag());
			if("Y".equals(getUserDetail().getOnlineFlag()) && onlinechangeEnableEntry.contains(str)) {
				return true;
			}
		} catch(Exception e) {
			// 避免 session 沒有 userVo 造成錯誤
		}
		return false;
	}

}
