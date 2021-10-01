package com.twfhclife.eservice.onlineChange.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twfhclife.eservice.odm.OnlineChangeModel;
import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.IMedicalTreatmentService;
import com.twfhclife.eservice.onlineChange.service.IOnlineChangeService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.OnlineChangeClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.StatuCode;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 線上申請-醫起通申請書套印(保單為單選)
 */
@Controller
public class MedicalTreatmentController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(MedicalTreatmentController.class);
	
	public static final String TRANS_TYPE = "MEDICAL_TREATMENT_CLAIM";


	@Autowired
	private FunctionUsageClient functionUsageClient;

	@Autowired
	private IMedicalTreatmentService iMedicalTreatmentService;
	@Autowired
	private ILoginService loginService;

	@Autowired
	private ILilipmService lilipmService;
	@Autowired
	private ITransService transService;

	@Autowired
	private IParameterService parameterSerivce;
	@Autowired
	private IInsuranceClaimService insuranceClaimService;

	@Autowired
	private ILilipiService lilipiService;


	@Autowired
	private MessageTemplateClient messageTemplateClient;

	@Autowired
	private IOnlineChangeService onlineChangeService;

	@RequestLog
	@GetMapping("/medicalTreatment1")
	public String medicalTreatment1(RedirectAttributes redirectAttributes) {
		try {
//			if(!checkCanUseOnlineChange()) {
//				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
//				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
//				addSystemError(message);
//				return "redirect:apply1";
//			}

			//TODO
			/**
			 * ODM
			 * 1.被保人才可申請
			 */

			/**
			 * ODM
			 * 2.不是黑名單才能申請
			 */
			BlackListVo blackListVo = new BlackListVo();
			blackListVo.setIdNo(getUserRocId());
			String detailInfo = iMedicalTreatmentService.checkBackList(blackListVo);
			if (detailInfo != null) {
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.BACK_LIST_MSG);
				return "redirect:apply1";
			}
			/**
			 * 進行判斷是否有保單理賠的申請
			 *  ps :保單理賠有申請,則醫療不可進行申請
			 */
			int resultInsurance = insuranceClaimService.getPolicyClaimCompleted(getUserRocId());
			if (resultInsurance > 0) {
//				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.INSURANCE_CLAIM_APPLYING);
				return "redirect:apply1";
			}


			/**
			 * 3.有申請中的保單,則不可再申請
			 * TRANS中transType=INSURANCE_TYPE,status=-1,0,4
			 */
			int result = iMedicalTreatmentService.getPolicyClaimCompleted(getUserRocId());
			if (result > 0) {
//				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.MEDICAL_TREATMENT_CLAIM_APPLYING);
				return "redirect:apply1";
			}

			String userRocId = getUserRocId();
			String userId = getUserId();
			UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);

			boolean isPartner = loginService.isPartnerRole(userDetail.getUserType());
			List<PolicyListVo> policyList = null;
			if(isPartner) {
				//login from admin/agent
				//need to get userRocId from /list1 session.
				userRocId = (String)getSession("ADMIN_QUERY_ROCID");
				logger.info("get session ADMIN_QUERY_ROCID={}",userRocId);
			}
			policyList = getUserOnlineChangePolicyMedicalListByRocId(userId, userRocId);

			List<String> insClaimsPlans = iMedicalTreatmentService.getInsClaimPlan();
			for (PolicyListVo policyListVo : policyList) {

				// 理賠聯盟鏈險種
				if (policyListVo.getPolicyNo() != null) {
					//modify 2021/08/06-不能單用policyNo來判斷該保單是否可操作,要找出其所有productCode
					//String pNo = policyListVo.getPolicyNo().substring(0,2);
					String policyNo = policyListVo.getPolicyNo();
					List<String> productCodes = iMedicalTreatmentService.getProductCodeByPolicyNo(policyNo);
					logger.info("Before setApplyLockedFlag():policyNo="+policyNo+",productCodes="+productCodes);

					boolean containProductCodes = false;
					for(String productCode : productCodes) {
						containProductCodes = insClaimsPlans.contains(productCode);
						if(containProductCodes) {
							break;
						}
					}
					if(containProductCodes) {
						policyListVo.setApplyLockedFlag("N");
					}else {
						policyListVo.setApplyLockedFlag("Y");
						appendMsg(policyListVo,OnlineChangMsgUtil.MEDICAL_TREATMENT_CLAIM_PLAN);
					}
				}
				//登入者和被保人不是同一人。
				if(!StringUtils.isEmpty(policyListVo.getLipiRocId()) && !StringUtils.isEmpty(userRocId) &&  !userRocId.equals(policyListVo.getLipiRocId()) ) {
					policyListVo.setApplyLockedFlag("Y");
					appendMsg(policyListVo,OnlineChangMsgUtil.INSURANCE_CLAIM_NOT_SAME);
				}

				LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyListVo.getPolicyNo());
				if (lilipmVo != null) {
					policyListVo.setCustomerName(lilipmVo.getLipmName1());
				}

				//檢核:要/被保人是否相同
				policyListVo.setIsLoginerFlag("N");
//				LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyListVo.getPolicyNo());
//				if (lilipiVo != null) {
//					if (lilipiVo.getLipiId().equals(userRocId)) {
//						policyListVo.setIsLoginerFlag("N");
//					} else {
//						policyListVo.setIsLoginerFlag("Y");
//						appendMsg(policyListVo,OnlineChangMsgUtil.INSURED_NOT_SAME_MSG);
//					}
//				}
				//end-if

			}

			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
				iMedicalTreatmentService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from MedicalTreatmentController- medicalTreatment1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
			functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "487");
		} catch(Exception e) {
			logger.debug("Call FunctionUsageAdd error: ", e);
		}

		return "frontstage/onlineChange/medicalTreatment/medicalTreatment1";
	}


	private void appendMsg(PolicyListVo policyListVo, String msg) {
		// TODO Auto-generated method stub
		if(policyListVo.getApplyLockedMsg() != null) {
			String eStr = policyListVo.getApplyLockedMsg();
			if(!eStr.contains(msg)) {
				policyListVo.setApplyLockedMsg(eStr + "|" + msg);
			}
		}else {
			policyListVo.setApplyLockedMsg(msg);
		}

	}

	@RequestLog
	@PostMapping("/medicalTreatmentClaimCompleted")
	public ResponseEntity<ResponseObj> getPolicyClaimCompleted() {
		try {
			int result = iMedicalTreatmentService.getPolicyClaimCompleted(getUserRocId());
			if (result > 0) {
				processError(OnlineChangMsgUtil.MEDICAL_TREATMENT_CLAIM_APPLYING);
			} else {
				processSuccess(result);
			}
		} catch (Exception e) {
			logger.error("Unable to  MedicalTreatmentController  medicalTreatmentClaimCompleted: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}


	@RequestLog
	@PostMapping("/medicalTreatment2")
	public String medicalTreatment2(PolicyListVo policyList,RedirectAttributes redirectAttributes) {
		try {
			if ("Y".equals(policyList.getApplyLockedFlag()) || "Y".equals(policyList.getExpiredFlag())) {
				//TODO
				//issue:this system error can't see in UI.
				addSystemError("Unable to select apply insurance of the policy");
				return "redirect:apply1";
			}


			//TODO
			/**
			 * ODM
			 * 1.成年人20歲才能申請
			 *
			 */
			String str = iMedicalTreatmentService.getAgeByPolicyNo(policyList.getPolicyNo());//使用被保人生日計算年齡
			String check_url = iMedicalTreatmentService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL);
			logger.error("check_url: {}", check_url);
			logger.error("age: {}", str);
			if(str != null && check_url != null) {
				//call ODM check service-start
				int age = Integer.parseInt(str);
				//PARAMETER.PARAMETER_CODE=ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL
				String odmCheckServcieUrl = check_url;//odm flow
				OnlineChangeModel ocModel = new OnlineChangeModel();
				ocModel.setTransType(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
				ocModel.setInsuredAge(age);
				OnlineChangeClient ocClient = new OnlineChangeClient();
				String resultStr = ocClient.postForEntity(odmCheckServcieUrl, ocModel);
				boolean resultPass = ocClient.checkLiaAPIResponseValue(resultStr, "/resultPass", "true");
				logger.info("resultPass="+resultPass);
				if(!resultPass) {
					String resultMsg = ocClient.readValue(resultStr, "/resultMsg");
					logger.info("resultMsg="+resultMsg);
					redirectAttributes.addFlashAttribute("errorMessage", resultMsg);
					return "redirect:apply1";
				}
				//call ODM check service-end
			}


			TransInsuranceClaimVo claimVo = new TransInsuranceClaimVo();
			claimVo.setPolicyNo(policyList.getPolicyNo());

			//獲取同意條款數據信息
			String medicalTreatmentConsent = parameterSerivce.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_TREATMENT_CONSENT);


			addAttribute("medicalTreatmentConsent", medicalTreatmentConsent);
			addAttribute("claimVo", claimVo);
		} catch (Exception e) {
			logger.error("Unable to init from MedicalTreatmentController - medicalTreatment2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/medicalTreatment/medicalTreatment2";
	}


	/**
	 * 進行驗證成年人20歲才能申請
	 * @param claimVo
	 * @param bindingResult
	 * @return
	 */
	@RequestLog
	@PostMapping(value = "/getMedicalVerifyAge")
	@ResponseBody
	public ResponseEntity<ResponseObj> getMedicalVerifyAge(@RequestBody TransMedicalTreatmentClaimVo claimVo) {
		String resultMsg=null;
		try {
			//獲取當前保單的出生日期
			String policyNo = claimVo.getPolicyNo();
			String birdate = claimVo.getBirdate();
			if (!StringUtils.isEmpty(policyNo) &&!StringUtils.isEmpty(birdate) ) {
				String birdateByPolicyNo = iMedicalTreatmentService.getBirdateByPolicyNo(policyNo);//獲取被保人生日
				birdate=birdate.replaceAll("/","-");
				if (birdateByPolicyNo.equals(birdate)) {
					String str = iMedicalTreatmentService.getAgeByPolicyNo(policyNo);//使用被保人生日計算年齡
					String check_url = iMedicalTreatmentService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL);
					logger.error("check_url: {}", check_url);
					logger.error("age: {}", str);
					if (str != null && check_url != null) {
						//call ODM check service-start
						int age = Integer.parseInt(str);
						//PARAMETER.PARAMETER_CODE=ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL
						String odmCheckServcieUrl = check_url;//odm flow
						OnlineChangeModel ocModel = new OnlineChangeModel();
						ocModel.setTransType(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
						ocModel.setInsuredAge(age);
						OnlineChangeClient ocClient = new OnlineChangeClient();
						String resultStr = ocClient.postForEntity(odmCheckServcieUrl, ocModel);
						boolean resultPass = ocClient.checkLiaAPIResponseValue(resultStr, "/resultPass", "true");
						logger.info("resultPass=" + resultPass);
						if (!resultPass) {
							resultMsg = ocClient.readValue(resultStr, "/resultMsg");
							logger.info("resultMsg=" + resultMsg);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to MedicalTreatmentController  -  getMedicalTreatmentWhetherFirst: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		processSuccess(resultMsg);
		return processResponseEntity();
	}





	@RequestLog
	@PostMapping("/medicalTreatment3")
	public String medicalTreatment3(TransMedicalTreatmentClaimVo claimVo,BindingResult bindingResult) {
		try {
			// 要保人
			LilipmVo lilipmVo = lilipmService.findByPolicyNo(claimVo.getPolicyNo());
			if (lilipmVo != null) {
				claimVo.setCustomerName(lilipmVo.getLipmName1());
			}
			// 被保人
			LilipiVo lilipiVo = lilipiService.findByPolicyNo(claimVo.getPolicyNo());
			if (lilipiVo != null) {
				claimVo.setName(lilipiVo.getLipiName());
				claimVo.setIdNo(lilipiVo.getNoHiddenLipiId());
				claimVo.setBirdate(DateUtil.formatDateTime(lilipiVo.getLipiBirth(), DateUtil.FORMAT_WEST_DATE));
			}
			//获取文件上传最大值，kb单位
			int  uploadMaxNumber=	lilipiService.findByUploadNumber(OnlineChangeUtil.UPLOAD_MAX_NUMBER,ApConstants.SYSTEM_ID);


			//獲取保險公司明顯
		  	List<Hospital>  hospitalList=	iMedicalTreatmentService.getHospitalList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE, StatuCode.AGREE_CODE.code);

			//獲取醫院明顯
			List<HospitalInsuranceCompany>  hospitalInsuranceCompanyList=	iMedicalTreatmentService.getHospitalInsuranceCompanyList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE,StatuCode.AGREE_CODE.code);
			addAttribute("claimVo", claimVo);
			addAttribute("uploadMaxNumber", uploadMaxNumber);
			addAttribute("hospitalInsuranceCompanyList", hospitalInsuranceCompanyList);
			addAttribute("hospitalList", hospitalList);
		} catch (Exception e) {
			logger.error("Unable to init from MedicalTreatmentController - medicalTreatment3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/medicalTreatment/medicalTreatment3";
	}

	@RequestLog
	@PostMapping("/medicalTreatment4")
	public String medicalTreatment4(TransMedicalTreatmentClaimVo claimVo, BindingResult bindingResult) {
		try {
			// 與主被保險人
			if (claimVo.getRelation() != null) {
				ParameterVo vo = parameterSerivce.getParameterByParameterValue(ApConstants.SYSTEM_ID, ApConstants.PAGE_WORDING, claimVo.getRelation());
				if (vo != null) {
					claimVo.setRelation_name(vo.getParameterName());
				}
			}
			//時間轉換
			claimVo.setBirdate(claimVo.getBirdate().replace("-",""));

			//獲取保險公司明顯
			List<Hospital>  hospitalList=	iMedicalTreatmentService.getHospitalList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE,null);

			//獲取醫院明顯
			List<HospitalInsuranceCompany>  hospitalInsuranceCompanyList=	iMedicalTreatmentService.getHospitalInsuranceCompanyList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE,null);


			// 發送驗證碼
			sendAuthCode("MedicalTreatment");
			addAttribute("claimVo", claimVo);
			addAttribute("hospitalInsuranceCompanyList", hospitalInsuranceCompanyList);
			addAttribute("hospitalList", hospitalList);
			getRequest().removeAttribute("errorMessage");//能執行到這裡應不會有系統錯誤訊息
		} catch (Exception e) {
			logger.error("Unable to init from  MedicalTreatmentController - medicalTreatment4: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/medicalTreatment/medicalTreatment4";
	}

	/**
	 * 上傳申請應備文件
	 *
	 * @param files
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/upLoadFilesMedicalTreatment")
	@ResponseBody
	public List<TransMedicalTreatmentClaimFileDataVo> upLoadFiles(@RequestParam("files") MultipartFile[] files) {
		List<TransMedicalTreatmentClaimFileDataVo> fileUpload = iMedicalTreatmentService.upLoadFiles(files);
		return fileUpload;
	}

	@RequestLog
	@PostMapping("/medicalTreatmentSuccess")
	public String medicalTreatmentSuccess(TransMedicalTreatmentClaimVo claimVo, BindingResult bindingResult) {
		logger.error("TransMedicalTreatmentClaimVo is: {}-----", claimVo);
		try {
			boolean isTransApplyed = false;
			logger.error("TransMedicalTreatmentClaimVo is: {}-----", claimVo.toString());
			ObjectMapper mapper = new ObjectMapper();
			List<TransMedicalTreatmentClaimFileDataVo> fileDatas = new ArrayList<TransMedicalTreatmentClaimFileDataVo>();
			if (claimVo.getFileDataList() != null) {
				List<TransMedicalTreatmentClaimFileDataVo> fileDataTemp = Arrays.asList(mapper.readValue(claimVo.getFileDataList(), TransMedicalTreatmentClaimFileDataVo[].class));
				fileDatas.addAll(fileDataTemp);
			}
			claimVo.setFileDatas(fileDatas);
			logger.error("TransMedicalTreatmentClaimVo is: {}+++++", claimVo.toString());
			String policyNo = claimVo.getPolicyNo();
			isTransApplyed = transService.isTransApplyed(policyNo,TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);

			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = claimVo.getAuthenticationNum();
				String msg = checkAuthCode("MedicalTreatment", authNum);
   				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					addAttribute("claimVo", claimVo);
					return "forward:medicalTreatment4";
				}
				getRequest().removeAttribute("errorMessage");//do not alert system check auth.

				// 設定使用者
				String userId = getUserId();
				claimVo.setUserId(userId);

				claimVo.setFrom("L01");

				// Call api 送出線上申請資料
				logger.info("Send TransMedicalTreatmentClaimVo[{}] trans data to eservice_api[addTransRequest]", claimVo);
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);

				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
				apiReq.setTransMedicalTreatmentClaimVo(claimVo);
				apiReq.setUserId(userId);

				//TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
//				if (transAddResponse != null) {
//					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
//							MyJacksonUtil.object2Json(transAddResponse));
//					transAddResult = transAddResponse.getTransResult();
//				} else {
				// 若無資料，嘗試由內部服務送出資料
				logger.info("Call internal service to insert user[{}] insertTransInsuranceClaim data", userId);

				// 設定交易序號
				String transNum = transService.getTransNum();
				claimVo.setTransNum(transNum);
				// 狀態歷程
				TransStatusHistoryVo hisVo = new TransStatusHistoryVo();
				//hisVo.setCustomerName(OnlineChangeUtil.CUSTOMER_NAME);

				//获取当前登录的用户信息
				UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				//logger.info("session---UsersVo---{}",session);
				hisVo.setCustomerName(userDetail.getUserName());
				hisVo.setIdentity(userDetail.getUserId());
				addAttribute("claimVo", claimVo);


				Map<String,Object> rMap = iMedicalTreatmentService.inserttransMedicalTreatmentClaimVo(claimVo, hisVo);
				int result = (int) rMap.get("result");
				if (result <= 0) {
					transAddResult = ReturnHeader.FAIL_CODE;
				} else {
					transAddResult = ReturnHeader.SUCCESS_CODE;
					logger.info("start send mail");
					Map<String, Object> mailInfo = iMedicalTreatmentService.getSendMailInfo((String)rMap.get("status"));
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("TransNum", claimVo.getTransNum());
					//paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
					//paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
					logger.info("Trans Num : {}", claimVo.getTransNum());
					logger.info("Status Name : {}", (String) mailInfo.get("statusName"));
					logger.info("Trans Remark : {}", (String) mailInfo.get("transRemark"));
					logger.info("receivers={}", (List)mailInfo.get("receivers"));
					logger.info("user phone : {}", claimVo.getPhone());
					logger.info("user mail : {}", claimVo.getMail());
					List<String> receivers = new ArrayList<String>();

					String loginTime = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
					paramMap.put("LoginTime", loginTime);

					//發送系統管理員
					receivers = (List)mailInfo.get("receivers");
					//messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_005, receivers, paramMap, "email");
					//使用新郵件範本
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_025, receivers, paramMap, "email");


					//發送保戶MAIL
					receivers = new ArrayList<String>();
					receivers.add(claimVo.getMail());
					paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
					logger.info("user mail : {}", claimVo.getMail());
					//messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_005, receivers, paramMap, "email");
					//使用新郵件範本
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_024, receivers, paramMap, "email");
					logger.info("End send mail");


					//發送保戶SMS
					receivers = new ArrayList<String>();
					receivers.add(claimVo.getPhone());
					paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
					logger.info("user phone : {}", claimVo.getPhone());
					messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_SMS_005, receivers, paramMap, "sms");

				}
//				}
			}

		} catch (Exception e) {
			logger.error("Unable to init from medicalTreatmentSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/medicalTreatment/medicalTreatment-success";
	}

	@RequestLog
	@PostMapping(value = "/downloadMedicalTreatmentPDF")
	public @ResponseBody
	HttpEntity<byte[]> downloadPolicyClaimPDF(TransMedicalTreatmentClaimVo claimVo, BindingResult bindingResult) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=保單理賠醫起通申請書-%s.pdf", claimVo.getPolicyNo());

			document = iMedicalTreatmentService.getPDF1(claimVo);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);
		} catch (Exception e) {
			logger.error("Unable to get data from MedicalTreatmentController  -downloadPolicyClaimPDF: {}", ExceptionUtils.getStackTrace(e));
		}

		return new HttpEntity<byte[]>(document, header);
	}

	/**
	 *
	 * 當前保單是否為地一次提交,是否有對於的文件數據信息
	 * */
	@RequestLog
	@PostMapping(value = "/getMedicalTreatmentWhetherFirst")
	@ResponseBody
	public ResponseEntity<ResponseObj> getMedicalTreatmentWhetherFirst(@RequestBody TransMedicalTreatmentClaimVo claimVo) {

		Integer   investments = null;
		try {
			investments = iMedicalTreatmentService.getMedicalTreatmentWhetherFirst(claimVo.getPolicyNo()
					 , TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
		} catch (Exception e) {
			logger.error("Unable to MedicalTreatmentController  -  getMedicalTreatmentWhetherFirst: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		processSuccess(investments);
		 return processResponseEntity();
	}


	/**
	 * 取得申請保單明細資料.
	 *
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransMedicalTreatmentDetail")
	public String getTransInsuranceClaimDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransMedicalTreatmentClaimVo claimVo = null;

			// Call api 取得資料
//			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
//					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
//			if (transHistoryDetailResponse != null) {
//				logger.info("Get user[{}] data from eservice_api[getTransInsuranceClaimDetail]", userId);
//				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
//				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
//					transInsuranceClaimVo = transHistoryDetailList.get(0).getTransInsuranceClaimVo();
//				}
//			} else {
//				logger.info("Call internal service to get user[{}] getTransInsuranceClaimDetail data", userId);
			claimVo = iMedicalTreatmentService.getTransInsuranceClaimDetail(transNum);
//			}
//			if (claimVo.getFileDatas() != null) {
//				for (TransInsuranceClaimFileDataVo fileData : claimVo.getFileDatas()) {
//					logger.error("TransInsuranceClaimFileDataVo is: {}", fileData.toString());
//				}
//			}

			logger.error(" MedicalTreatmentController  -- TransInsuranceClaimVo is: {}", claimVo.toString());
			//獲取保險公司明顯
			List<Hospital>  hospitalList=	iMedicalTreatmentService.getHospitalList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE,null);

			//獲取醫院明顯
			List<HospitalInsuranceCompany>  hospitalInsuranceCompanyList=	iMedicalTreatmentService.getHospitalInsuranceCompanyList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE,null);


			addAttribute("claimVo", claimVo);
			addAttribute("hospitalInsuranceCompanyList", hospitalInsuranceCompanyList);
			addAttribute("hospitalList", hospitalList);
		} catch (Exception e) {
			logger.error("Unable to MedicalTreatmentController  -  getTransInsuranceClaimDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/medicalTreatment/medicalTreatment-detail";
	}

	/**
	 * 狀態歷程.
	 *
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransStatusHistoryList")
	public ResponseEntity<ResponseObj> getTransStatusHistoryList(@RequestBody TransStatusHistoryVo vo) {
		try {
			List<TransStatusHistoryVo> result = onlineChangeService.getTransStatusHistoryList(vo);
			if (result != null && result.size() != 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getTransStatusHistoryList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-醫療申請狀態
	 *
	 * @return
	 */
	@RequestLog
	@PostMapping("/optionMedicalApplyForStatusList")
	public ResponseEntity<ResponseObj> optionMedicalApplyForStatusList() {
		try {
			//獲取申請狀態數據信息
			List<ParameterVo> applyForOptionStatusList = parameterSerivce.getParameterByCategoryCode(ApConstants.SYSTEM_ID,ApConstants.ONLINE_CHANGE_STATUS);
			/*0	處理中 1	已審核  5	已上傳 6	失敗*/
			String [] str = {"0","1","5","6"};
			List<ParameterVo> collect = applyForOptionStatusList.stream().filter((x) -> {
				for (String s : str) {
					if (s.equals(x.getParameterCode())) {
						return false;
					}
				}
				return true;
			}).collect(Collectors.toList());
			processSuccess(applyForOptionStatusList);
		} catch (Exception e) {
			logger.error("Unable to optionList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

}