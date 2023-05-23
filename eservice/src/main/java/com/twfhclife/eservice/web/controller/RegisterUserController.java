package com.twfhclife.eservice.web.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.AuthenticationVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.RegisterQuestionVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.generic.util.ValidateUtil;

/**
 * 會員註冊
 * 
 */
@Controller
@EnableAutoConfiguration
public class RegisterUserController extends BaseController{
	
	private static final Logger logger = LogManager.getLogger(RegisterUserController.class);
	
	@Autowired
	private IRegisterUserService registerUserService;
	
	@Autowired
	private IParameterService parameterService;
	
	@GetMapping("/firstUseStart")
	public String firstUseStart() {
		String fbId = MyStringUtil.nullToString(getSessionStr("fbId"));
		String cardSn = MyStringUtil.nullToString(getSessionStr("cardSn"));
		String email = MyStringUtil.nullToString(getSessionStr("email"));
		addAttribute("fbId", fbId);
		addAttribute("cardSn", cardSn);
		addAttribute("email", email);

		addAttribute("register_rocId", getSession("register_rocId"));
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
		.getRequest().getSession().removeAttribute("register_rocId");
		
		if(getSession(ApConstants.SYSTEM_PARAMETER) == null) {
			List<ParameterVo> listParam = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYSTEM_MSG_PARAMETER);
			Map<String, ParameterVo> mapParam = listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, Function.identity()));
			Map<String, Map<String, ParameterVo>> mapParamSession = new HashMap<>();
			mapParamSession.put(ApConstants.SYSTEM_MSG_PARAMETER, mapParam);
			addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) mapParamSession);
			addSession(ApConstants.SYSTEM_MSG_PARAMETER, (Serializable) mapParam);
		}
		
		return "frontstage/registerUser/firstUseStart";
	}
	
	/**
	 * 檢查身分證號碼是否已註冊過會員
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping("/register/checkRegister")
	public ResponseEntity<ResponseObj> checkRegister(@RequestBody UsersVo user, HttpServletRequest request) {
		logger.info("enter register checkRegister! user=" + user.getRocId());
		try {
			//檢查身分證是否已被註冊
			String isRegister = registerUserService.checkRegister(user);
			String message = "";
			if(isRegister != null) {
				if(isRegister.equals("rocid")) {
					/*message = user.getRocId().length() == 8 ? "此統一編號已註冊" : "此身分證號已註冊";*/
					message = user.getRocId().length() == 8 ? 
							getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0103") 
							: getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0104");
				} else if(isRegister.equals("email")) {
					/*message = "此電子郵件已註冊";*/
					message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0105");
				}
				this.setResponseObj(ResponseObj.SUCCESS, message, user);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			//email,cellphone來源LIPMDA
			List<UsersVo> uVo = registerUserService.getMailPhoneByRocid(user.getRocId());
			//非臺銀保戶
			if (uVo.size() == 0) {
				/*message = "保戶專區會員申請僅開放本公司保戶申請，若有相關疑問請來電本公司客服專線：0800-011-966。";*/
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0106");
				this.setResponseObj(ResponseObj.SUCCESS, message, user);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			for (UsersVo vo : uVo) {
				if (StringUtils.isNotEmpty(vo.getMobile())) {
					user.setMobile(vo.getMobile());
				}
				if (StringUtils.isNotEmpty(vo.getEmail())) {
					user.setEmail(vo.getEmail());
				}
				if (StringUtils.isNotEmpty(user.getEmail()) || StringUtils.isNotEmpty(user.getMobile())) {
					break;
				}
			}
			
			if(StringUtils.isEmpty(user.getEmail()) && StringUtils.isEmpty(user.getMobile())) {
				message = "NODATA";
				this.setResponseObj(ResponseObj.SUCCESS, message, user);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("register_rocId", user.getRocId());
			session.setAttribute("register_mobile", user.getMobile());
			session.setAttribute("register_email", user.getEmail());
			session.setAttribute("register_fbId", user.getFbId());
			session.setAttribute("register_moicaId", user.getMoicaId());
			session.setAttribute("register_isMember", user.getIsMember());
			//this.setResponseObj(ResponseObj.SUCCESS, message, user);//modify for Penetration test
			this.setResponseObj(ResponseObj.SUCCESS, message, null);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	/**
	 * 進入條款頁
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/firstUse1")
	public String firstUse1(HttpServletRequest request, ModelMap modelMap) {
		logger.info("open frontstage/firstUse1.html");
		//字太多塞不下,hardcode在firstUse1.html
		//List<ParameterVo> terms = registerUserService.getTerms();
		//modelMap.put("terms", terms);
		return "frontstage/registerUser/firstUse1";
	}
	
	/**
	 * 進入身分認證頁
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/firstUse2")
	public String firstUse2(HttpServletRequest request, ModelMap modelMap) {
		logger.info("open frontstage/firstUse2.html");
		HttpSession session = request.getSession();
		session.setAttribute(ApConstants.USER_TERM_REGISTER, new Date());
		return "frontstage/registerUser/firstUse2";
	}
	
	/**
	 * 檢查保戶輸入的保單號碼是否存在
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping("/register/getPolicyByRocId")
	public ResponseEntity<ResponseObj> getPolicyByRocId(@RequestBody String policyNo, HttpServletRequest request) {
		logger.info("enter register getPolicyByRocId! policyNo = "+policyNo);
		//檢查保單 如果有取出問題
		HttpSession session = request.getSession();
		
		try {
			if (session.getAttribute("register_rocId") == null) {
				this.setResponseObj(ResponseObj.ERROR, "ROCIDEMPTY", null);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			String rocId = session.getAttribute("register_rocId").toString();
			
			//不允許三分鐘內連續查詢三次以上，減少暴力破解的可能_start
			if(!StringUtils.isEmpty(rocId) && !StringUtils.isEmpty(policyNo) ) {
				HashMap hmPolicyNo   = null;
				String hmPolicyNoKey = "HM_POLICY_NO";
				if(session.getAttribute(hmPolicyNoKey)==null) {
					hmPolicyNo = new HashMap();
					hmPolicyNo.put("CREATE_TIMEMILLIS", System.currentTimeMillis()+"");
					hmPolicyNo.put(policyNo, policyNo);
					session.setAttribute(hmPolicyNoKey, hmPolicyNo);
				}else {
					hmPolicyNo = (HashMap)session.getAttribute("HM_POLICY_NO");
					if(hmPolicyNo.size()<4) {//3times+CREATE_TIMEMILLIS
						hmPolicyNo.put(policyNo, policyNo);
						session.setAttribute(hmPolicyNoKey, hmPolicyNo);
					}else {
						long currentTM = System.currentTimeMillis();
						
						String creatTimemillis = (String)hmPolicyNo.get("CREATE_TIMEMILLIS");
						long createTM = Long.parseLong(creatTimemillis);
						
						long diffTM = currentTM-createTM;
						if(diffTM >= 3*60*1000 ) {//3mins
							hmPolicyNo.clear();
							hmPolicyNo.put("CREATE_TIMEMILLIS", currentTM+"");
							hmPolicyNo.put(policyNo, policyNo);
							session.setAttribute(hmPolicyNoKey, hmPolicyNo);
						}else {
							//not allow
							logger.error("Maybe Penetration test:"+"不允許的系統操作");
							String msg = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0000");
							this.setResponseObj(ResponseObj.ERROR, msg, null);
							return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
						}
					}
				}
			}
			//不允許三分鐘內連續查詢三次，減少暴力破解的可能_end
			
			boolean isRegister = registerUserService.getPolicyByRocId(policyNo, rocId);
			String message = "";
			List<RegisterQuestionVo> questions = null;
			if(!isRegister){
				/*message = "查無此保單資料，若您確實持有本公司保單，請聯絡客戶服務專線0800-011-966。";*/
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0020");
			}else{
				questions = registerUserService.getPolicyQues(rocId, policyNo);
				//將答案塞進去
				for(RegisterQuestionVo question : questions){
					session.setAttribute(question.getQuestionNo(), question.getAnswer());
					//不要傳到jsp
					question.setAnswer(null);
				}
				// 更新保單對應的電話號碼 & email
				UsersVo userResult = registerUserService.getMailPhoneByRocidPolicyNo(rocId, policyNo);
				if(userResult != null) {
					session.setAttribute("register_mobile", userResult.getMobile());
					session.setAttribute("register_email", userResult.getEmail());
				} else {
					// 使用 rocId + policyNo 沒有找到可用連絡電話 or email, 可能輸入有誤
				}
			}
			this.setResponseObj(ResponseObj.SUCCESS, message, questions);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	/**
	 * 檢查輸入的答案
	 * @param questionList
	 * @param request
	 * @return
	 */
	@PostMapping("/register/checkAnswer")
	public ResponseEntity<ResponseObj> checkAnswer(@RequestBody List<RegisterQuestionVo> questionList, HttpServletRequest request) {
		logger.info("enter register checkAnswer!");
		try {
			HttpSession session = request.getSession();
			if(questionList != null && !questionList.isEmpty()){
				for(RegisterQuestionVo question : questionList){
					if(question != null) {
						String questionNo = question.getQuestionNo();
						if(session.getAttribute(questionNo) != null) {
							String trueAnswer = session.getAttribute(questionNo).toString();
							logger.debug("trueAnswer=" + trueAnswer + ", question.getAnswer()=" + question.getAnswer());
							
							if(question.getAnswer().equals(trueAnswer)){
								question.setResult("OK");
							}else{
								if(questionNo.equals("QUESTION5")){
									/*question.setResult("身分證填寫錯誤！");*/
									question.setResult(getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0107"));
								}else if(questionNo.equals("QUESTION3") || questionNo.equals("QUESTION4")){
									/*question.setResult("出生日期填寫錯誤！");*/
									question.setResult(getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0108"));
								}else{
									/*question.setResult("答題錯誤！");*/
									question.setResult(getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0109"));
								}
							}
						}
						
					}
				}
				//有答題紀錄 記錄成保戶會員
				session.setAttribute("register_userType", ApConstants.USER_TYPE_MEMBER);
			}else{
				//無答題紀錄 記錄成一般會員
				session.setAttribute("register_userType", ApConstants.USER_TYPE_NORMAL);
			}
			this.setResponseObj(ResponseObj.SUCCESS, "", questionList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	/**
	 * 進入驗證碼驗證畫面
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@GetMapping("/firstUse3")
	public String firstUse3(ModelMap modelMap) {
		logger.info("open frontstage/firstUse3.html");
		try {
			String email = getSessionStr("register_email");
			String mobile = getSessionStr("register_mobile");
			String strMail = "";
			String strMobile = "";
			if(!email.equals("")){
				String str = "";
				for(int i=0 ; i<email.indexOf("@")-2 ; i++){
					str = str + "*";
				}
				
				strMail = email.substring(0, 2)+str+email.substring(email.indexOf("@"));
			}
				
			if(StringUtils.isNotEmpty(mobile) && mobile.length() == 10){
				strMobile = mobile.substring(0,3) + "***" + mobile.substring(6);
			}
			
			Date now = new Date();
			Date authenticationTime_s = getSessionDate("registerAuthenticationTime");
			int diffDate = (int) ((now.getTime()-authenticationTime_s.getTime())/1000);
			int timeSet = 0;
			if(diffDate < 300){
				timeSet = 300-diffDate;
			}
			modelMap.addAttribute("timeSet", timeSet);		
			modelMap.addAttribute("smsEmail", strMail);
			modelMap.addAttribute("smsMobile", strMobile);
			modelMap.addAttribute("authenticationType", "register");
			
			//非保戶會員會直接進到step3,所以需要在這放同意條款時間
			if (getSession(ApConstants.USER_TERM_REGISTER) == null) {
				addSession(ApConstants.USER_TERM_REGISTER, new Date());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return "frontstage/registerUser/firstUse3";
	}
	
	/**
	 * 認證碼驗證
	 * @return
	 */
	@PostMapping("/register/checkAuthentication")
	public ResponseEntity<ResponseObj> checkAuthentication(@RequestBody AuthenticationVo authenticationVo, HttpServletRequest request) {
		logger.info("enter register checkAuthentication! when enter authentication = "+authenticationVo.getAuthenticationNum());
		//紀錄驗證碼與時間
		String message;
		try {
			HttpSession session = request.getSession();
			message = "";
			
			if(session.getAttribute(authenticationVo.getAuthenticationType()+"Authentication") == null){
				//message = "驗證碼已失效，請重新寄送驗碼。";
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");
			}else{
				String authentication_s = session.getAttribute(authenticationVo.getAuthenticationType()+"Authentication").toString();
				Date now = new Date();
				Date authenticationTime_s = (Date) session.getAttribute(authenticationVo.getAuthenticationType()+"AuthenticationTime");
				
				int milli = 5*(60*1000);
				int diffDate = (int) (now.getTime()-authenticationTime_s.getTime());
				
				if(milli >= diffDate){
					//未超過五分鐘
					if(!authenticationVo.getAuthenticationNum().equals(authentication_s)){
						//輸入錯誤
						//message = "驗證碼輸入錯誤，請確認驗證碼或重新寄送驗證碼。";
						message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0022");
					}else{
						//驗證完成 清除驗證碼session
						addSession(authenticationVo.getAuthenticationType()+"Authentication",null);
						addSession(authenticationVo.getAuthenticationType()+"_isChack", true);
					}
				}else{
					//已超過五分鐘
					//message = "驗證碼已失效，請重新寄送驗碼。";
					message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0021");
				}
			}
			
			this.setResponseObj(ResponseObj.SUCCESS, message, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, "", null);
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@GetMapping("/firstUse4")
	public String firstUse4() {
		logger.info("open frontstage/firstUse4.html");
		return "frontstage/registerUser/firstUse4";
	}
	
	/**
	 * 新增會員資料
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping("/register/registerUserData")
	public ResponseEntity<ResponseObj> registerUserData(@RequestBody UsersVo user, HttpServletRequest request) {
		logger.info("enter register registerUserData!");
		HttpSession session = request.getSession();
		String message = "";
		try {
			boolean isCheck = getSession("register_isChack") != null ? (boolean) getSession("register_isChack") : false;
			
			if (user.getUserId().length() < 8) {
				/*message = "用戶名稱請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";*/
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0023");
				this.setResponseObj(ResponseObj.ERROR, message, null);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			if (!ValidateUtil.isPwd(user.getPassword())) {
				/*message = "密碼請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";*/
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0024");
				this.setResponseObj(ResponseObj.ERROR, message, null);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			// 檢查是否通過驗證碼驗證，避免用偷吃步進到此頁註冊帳號
			if(!isCheck || getSession("registerAuthentication") != null) {
				//message = "請完成驗證碼驗證!";
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0101");
				this.setResponseObj(ResponseObj.ACCESS_DENY, message, null);
				return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
			}
			
			String email = MyStringUtil.nullToString(session.getAttribute("register_email"));
			String mobile = MyStringUtil.nullToString(session.getAttribute("register_mobile"));
			String rocId = MyStringUtil.nullToString(session.getAttribute("register_rocId"));
			String userType = MyStringUtil.nullToString(session.getAttribute("register_userType"));
			String fbId = MyStringUtil.nullToString(session.getAttribute("register_fbId"));
			String cardSn = MyStringUtil.nullToString(session.getAttribute("register_moicaId"));
			if(email.equals("")){
				user.setMailFlag("0");
			}else{
				user.setMailFlag("1");
			}
			if(mobile.equals("")){
				user.setSmsFlag("0");
			}else{
				user.setSmsFlag("1");
			}
			user.setMobile(mobile);
			user.setEmail(email);
			user.setRocId(rocId);
			user.setUserType("".equals(userType) ? ApConstants.USER_TYPE_NORMAL : userType);
			user.setFbId(fbId);
			user.setMoicaId(cardSn);
			Map<String, String> retMap = registerUserService.registerUserData(user);
			String userId = retMap.get("userId");
			String error = retMap.get("error");
			String errorMsg = retMap.get("errorMsg");
			if(MyStringUtil.isNotNullOrEmpty(error)) {
				/*message = "建立新用戶時發生錯誤，請重試或聯絡系統管理員";*/
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0110");
				if(error.equals("409")) {
					// 用戶名稱重複
					logger.info("[Register error] User or Email existed in Keycloak!");
					/*message = "用戶名稱已被使用，請改用其他名稱";*/
					message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0111");
				} else if(error.equals("400")) {
					// 新增Keycloak User錯誤
					logger.error("[Register error] Keycloak Create user error!");
				}  else if(error.equals("SQL")) {
					// 新增至DB錯誤
					logger.error("[Register error] Insert User data into DB fail!");
				} else {
					if(MyStringUtil.isNotNullOrEmpty(errorMsg)){
						logger.error("[Register error] API return message: " + errorMsg);
						message = errorMsg;
					}
				}
				this.setResponseObj(ResponseObj.ERROR, message, null);
			} else {
				if(userId == null) {
					/*message = "建立新用戶時發生錯誤，請重試或聯絡系統管理員";*/
					message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0110");
					this.setResponseObj(ResponseObj.ERROR, message, null);
				} else {
					this.setResponseObj(ResponseObj.SUCCESS, message, null);
					addSession("register_isChack", null);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			/*message = "建立新用戶時發生錯誤，請重試或聯絡系統管理員";*/
			message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0110");
			this.setResponseObj(ResponseObj.ERROR, message, null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@GetMapping("/firstUseSuccess")
	public String firstUseSuccess() {
		logger.info("open frontstage/firstUse-success.html");
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest().getSession().removeAttribute("register_rocId");
		Boolean bxczLoginFlag = (Boolean) getSession("BXCZ_REGISTER_FLAG");
		if (bxczLoginFlag != null && bxczLoginFlag) {
			removeFromSession("rocId");
			removeFromSession("BXCZ_REGISTER_FLAG");
			return "redirect:autoBxczLogin";
		}
		return "frontstage/registerUser/firstUse-success";
	}

}
