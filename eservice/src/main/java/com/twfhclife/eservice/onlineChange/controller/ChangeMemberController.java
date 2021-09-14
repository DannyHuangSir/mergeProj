package com.twfhclife.eservice.onlineChange.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.eservice.onlineChange.service.IChangeInfoService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeNoteUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.RegisterQuestionVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.keycloak.model.KeycloakUser;

/**
 * 線上申請-升級保戶會員.
 */
@Controller
public class ChangeMemberController extends BaseController {
	
	private static final Logger logger = LogManager.getLogger(ChangeMemberController.class);
	
	@Autowired
	private IChangeInfoService changeInfoService;
	
	@Autowired
	private IRegisterUserService registerUserService;
	
	@RequestLog
	@GetMapping("/changeMember")
	public String changeMember() {
		try {
			//有功能的話加入此
		} catch (Exception e) {
			logger.error("changeMember error! {}", e);
		}
		return "frontstage/onlineChange/changeInfo/change-member";
	}
	
	/**
	 * 檢查保戶輸入的保單號碼是否存在
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeMember/getPolicyByRocId")
	public ResponseEntity<ResponseObj> getPolicyByRocId(@RequestBody String policyNo, HttpServletRequest request) {
		List<RegisterQuestionVo> questions = null;
		String message = "";
		try {
			HttpSession session = request.getSession();
			KeycloakUser keycloakUser = getLoginUser();
			//檢查保單 如果有取出問題
			String rocId = keycloakUser.getRocId();
			boolean isRegister = registerUserService.getPolicyByRocId(policyNo, rocId);
			if(!isRegister){
				//message = "查無此保單資料，若您確實持有本公司保單，請聯絡客戶服務專線0800-011-966。";
				message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0020");
			}else{
				questions = registerUserService.getPolicyQues(rocId, policyNo);
				//將答案塞進去
				for(RegisterQuestionVo question : questions){
					session.setAttribute(question.getQuestionNo(), question.getAnswer());
					//不要傳到jsp
					question.setAnswer(null);
				}
			}
			this.setResponseObj(ResponseObj.SUCCESS, message, questions);
		} catch (Exception e) {
			logger.error("changeMember/getPolicyByRocId error! {}", e);
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
	@RequestLog
	@PostMapping("/changeMember/checkAnswer")
	public ResponseEntity<ResponseObj> checkAnswer(@RequestBody List<RegisterQuestionVo> questionList, HttpServletRequest request) {
		try {
			String message = "success";
			HttpSession session = request.getSession();
			for(RegisterQuestionVo question : questionList){
				String questionNo = question.getQuestionNo();
				String trueAnswer = session.getAttribute(questionNo).toString();
				String inputAnswer = question.getAnswer();
				if (questionNo.equals("QUESTION3") || questionNo.equals("QUESTION4")) {
					if (inputAnswer != null) {
						Calendar c = Calendar.getInstance();
						c.setTime(new Date(inputAnswer));
						inputAnswer = new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
					}
				}
				
				if(inputAnswer != null && inputAnswer.equals(trueAnswer)){
					question.setResult("OK");
				}else{
					if(questionNo.equals("QUESTION5")){
						/*question.setResult("身分證填寫錯誤！");*/
						question.setResult(getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0107"));
						message = "faile";
					}else if(questionNo.equals("QUESTION3") || questionNo.equals("QUESTION4")){
						/*question.setResult("出生日期填寫錯誤！");*/
						question.setResult(getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0108"));
						message = "faile";
					}else{
						/*question.setResult("答題錯誤！");*/
						question.setResult(getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0109"));
						message = "faile";
					}
				}
			}
			//有答題紀錄 記錄成保戶會員
			if(message.equals("success")){
				//更新會員類型
				String userId = getUserId();
				changeInfoService.updateUserType(userId, ApConstants.USER_TYPE_MEMBER);
				UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				userDetail.setUserType(ApConstants.USER_TYPE_MEMBER);
			}
			this.setResponseObj(ResponseObj.SUCCESS, "", questionList);
		} catch (Exception e) {
			logger.error("changeMember/checkAnswer error! {}", e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
		}
		// keycloakService
		
		
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@GetMapping("/changeMemberSuccess")
	public String changeMemberSuccess() {
		try {
			String changeInfoSuccess = getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE, OnlineChangeNoteUtil.CUST_INFO_SUCCESS_CODE);
			SuccessMsg("changeInfo", changeInfoSuccess);
		} catch (Exception e) {
			logger.error("changeMemberSuccess error! {}", e);
		}
		return "frontstage/onlineChange/changeInfo/change-member-success";
	}
}