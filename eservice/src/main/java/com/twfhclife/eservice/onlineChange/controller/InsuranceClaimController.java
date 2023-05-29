package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.odm.OnlineChangeModel;
import com.twfhclife.eservice.onlineChange.model.BlackListVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.IMedicalTreatmentService;
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
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.OnlineChangeClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;

/**
 * 線上申請-保單理賠申請書套印(保單為單選)
 */
@Controller
public class InsuranceClaimController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(InsuranceClaimController.class);
	
	public static final String TRANS_TYPE = "INSURANCE_CLAIM";
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private IInsuranceClaimService insuranceClaimService;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private IParameterService parameterSerivce;
	
	@Autowired
	private MessageTemplateClient messageTemplateClient;
	
	@Autowired
	private IRegisterUserService registerUserService;
	
	@Autowired
	private ILoginService loginService;


	@Autowired
	private IMedicalTreatmentService iMedicalTreatmentService;
	@RequestLog
	@GetMapping("/policyClaims1")
	public String policyClaims1(RedirectAttributes redirectAttributes) {
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
			String detailInfo = insuranceClaimService.checkBackList(blackListVo);
			if (detailInfo != null) {
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.BACK_LIST_MSG);
				return "redirect:apply1";
			}
			/**
			 * 進行判斷是否有醫療保單的申請
			 *  ps :醫療有申請,則保單理賠不可進行申請
			 */
			int resultMedical = iMedicalTreatmentService.getPolicyClaimCompleted(getUserRocId());
			if (resultMedical > 0) {
//				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.MEDICAL_TREATMENT_CLAIM_APPLYING);
				return "redirect:apply1";
			}

			/**
			 * 3.有申請中的保單理賠,則不可再申請
			 * TRANS中transType=INSURANCE_TYPE,status=-1,0,4
			 */
			int result = insuranceClaimService.getPolicyClaimCompleted(getUserRocId());
			if (result > 0) {
//				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.INSURANCE_CLAIM_APPLYING);
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
			policyList = getUserOnlineChangePolicyListByRocId(userId, userRocId);
			
			List<String> insClaimsPlans = insuranceClaimService.getInsClaimPlan();
			for (PolicyListVo policyListVo : policyList) {
				
				// 理賠聯盟鏈險種
				if (policyListVo.getPolicyNo() != null) {
					//modify 2021/08/06-不能單用policyNo來判斷該保單是否可操作理賠,要找出其所有productCode
					//String pNo = policyListVo.getPolicyNo().substring(0,2);
					String policyNo = policyListVo.getPolicyNo();
					List<String> productCodes = insuranceClaimService.getProductCodeByPolicyNo(policyNo);
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
						appendMsg(policyListVo,OnlineChangMsgUtil.INSURANCE_CLAIM_PLAN);
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
						userId, TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE);
				insuranceClaimService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from policyClaims1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "487");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/policyClaims/policyClaims1";
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
	@PostMapping("/policyClaimCompleted")
	public ResponseEntity<ResponseObj> getPolicyClaimCompleted() {
		try {
			int result = insuranceClaimService.getPolicyClaimCompleted(getUserRocId());
			if (result > 0) {
				processError(OnlineChangMsgUtil.INSURANCE_CLAIM_APPLYING);
			} else {
				processSuccess(result);
			}
		} catch (Exception e) {
			logger.error("Unable to getPolicyClaimCompleted: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/policyClaims2")
	public String policyClaims2(PolicyListVo policyList,RedirectAttributes redirectAttributes) {
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
			
			String str = insuranceClaimService.getAgeByPolicyNo(policyList.getPolicyNo());//使用被保人生日計算年齡
			String check_url = insuranceClaimService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.INS_CLAIM_POLICY_2);
			logger.error("check_url: {}", check_url);
			logger.error("age: {}", str);
			if(str != null && check_url != null) {
				//call ODM check service-start
				int age = Integer.parseInt(str);
				//PARAMETER.PARAMETER_CODE=ESERVICE_ONLINECHANGE_ODM_URL
				String odmCheckServcieUrl = check_url;//odm flow
				OnlineChangeModel ocModel = new OnlineChangeModel();
				ocModel.setTransType(InsuranceClaimController.TRANS_TYPE);
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
			
			addAttribute("claimVo", claimVo);
		} catch (Exception e) {
			logger.error("Unable to init from policyClaims2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyClaims/policyClaims2";
	}

	@RequestLog
	@PostMapping("/policyClaims3")
	public String policyClaims3(TransInsuranceClaimVo claimVo) {
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


			addAttribute("claimVo", claimVo);
			addAttribute("uploadMaxNumber", uploadMaxNumber);
		} catch (Exception e) {
			logger.error("Unable to init from policyClaims3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyClaims/policyClaims3";
	}
	
	@RequestLog
	@PostMapping("/policyClaims4")
	public String policyClaims4(TransInsuranceClaimVo claimVo, BindingResult bindingResult) {
		try {
			// 與主被保險人
			if (claimVo.getRelation() != null) {
				ParameterVo vo = parameterSerivce.getParameterByParameterValue(ApConstants.SYSTEM_ID, ApConstants.PAGE_WORDING, claimVo.getRelation());
				if (vo != null) {
					claimVo.setRelation_name(vo.getParameterName());
				}
			}
			// 發送驗證碼
			sendAuthCode("policyClaim");
			addAttribute("claimVo", claimVo);
			getRequest().removeAttribute("errorMessage");//能執行到這裡應不會有系統錯誤訊息
		} catch (Exception e) {
			logger.error("Unable to init from policyClaims4: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyClaims/policyClaims4";
	}
	
	@RequestLog
	@PostMapping("/policyClaims5")
	public String policyClaims5(TransInsuranceClaimVo claimVo, BindingResult bindingResult) {
		try {
			boolean isTransApplyed = false;
			logger.error("TransInsuranceClaimVo is: {}-----", claimVo.toString());
			ObjectMapper mapper = new ObjectMapper();
			List<TransInsuranceClaimFileDataVo> fileDatas = new ArrayList<TransInsuranceClaimFileDataVo>();
			if (claimVo.getFileDataList() != null) {
				List<TransInsuranceClaimFileDataVo> fileDataTemp = Arrays.asList(mapper.readValue(claimVo.getFileDataList(), TransInsuranceClaimFileDataVo[].class));
				fileDatas.addAll(fileDataTemp);
			}
			claimVo.setFileDatas(fileDatas);
			logger.error("TransInsuranceClaimVo is: {}+++++", claimVo.toString());
			String policyNo = claimVo.getPolicyNo();
			isTransApplyed = transService.isTransApplyed(policyNo,TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			
			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = claimVo.getAuthenticationNum();
				String msg = checkAuthCode("policyClaim", authNum);
				if (!StringUtils.isEmpty(msg)) {
					addSystemError(msg);
					return "forward:policyClaims4";
				}
				getRequest().removeAttribute("errorMessage");//do not alert system check auth.
				
				// 設定使用者
				String userId = getUserId();
				claimVo.setUserId(userId);
				
				claimVo.setFrom("L01");
				
				// Call api 送出線上申請資料
				logger.info("Send TransInsuranceClaimVo[{}] trans data to eservice_api[addTransRequest]", claimVo);
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE);
				apiReq.setTransInsuranceClaimVo(claimVo);
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


					Map<String,Object> rMap = insuranceClaimService.insertTransInsuranceClaim(claimVo, hisVo);
					int result = (int) rMap.get("result");
					if (result <= 0) {
						transAddResult = ReturnHeader.FAIL_CODE;
					} else {
						transAddResult = ReturnHeader.SUCCESS_CODE;
						logger.info("start send mail");
						Map<String, Object> mailInfo = insuranceClaimService.getSendMailInfo((String)rMap.get("status"));
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
			logger.error("Unable to init from policyClaims5: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyClaims/policyClaims-success";
	}
	
	@RequestLog
	@PostMapping(value = "/downloadPolicyClaimsPDF")
	public @ResponseBody HttpEntity<byte[]> downloadPolicyClaimPDF(TransInsuranceClaimVo claimVo, BindingResult bindingResult) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			String fileName = String.format("inline; filename=保單理賠申請書-%s.pdf", claimVo.getPolicyNo());
			
			document = insuranceClaimService.getPDF1(claimVo);
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);
		} catch (Exception e) {
			logger.error("Unable to get data from downloadPolicyClaimPDF: {}", ExceptionUtils.getStackTrace(e));
		}

		return new HttpEntity<byte[]>(document, header);
	}
	
	/**
	 * 取得申請保單理賠明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransInsuranceClaimDetail")
	public String getTransInsuranceClaimDetail(@RequestParam("transNum") String transNum) {
		try {
			TransInsuranceClaimVo claimVo = null;

			claimVo = insuranceClaimService.getTransInsuranceClaimDetail(transNum);
			logger.error("TransInsuranceClaimVo is: {}", claimVo.toString());

			addAttribute("claimVo", claimVo);
			Map<String, List<TransInsuranceClaimFileDataVo>> fileData = Maps.newHashMap();
			if (claimVo != null && !CollectionUtils.isEmpty(claimVo.getFileDatas())) {
				claimVo.getFileDatas().forEach(f -> {
					if (fileData.get(f.getType()) != null) {
						fileData.get(f.getType()).add(f);
					} else {
						fileData.put(f.getType(), Lists.newArrayList(f));
					}
				});
			}
			addAttribute("fileData", fileData);
		} catch (Exception e) {
			logger.error("Unable to getTransInsuranceClaimDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyClaims/policyClaims-detail";
	}
	
	/**
	 * 上傳申請應備文件
	 * 
	 * @param files
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/upLoadFiles")
	@ResponseBody
	public List<TransInsuranceClaimFileDataVo> upLoadFiles(@RequestParam("files") MultipartFile[] files) {
		List<TransInsuranceClaimFileDataVo> fileUpload = insuranceClaimService.upLoadFiles(files);
		return fileUpload;
	}
	
}