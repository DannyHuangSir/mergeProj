package com.twfhclife.eservice.onlineChange.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
import com.twfhclife.generic.api_model.CommLogRequest;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.service.IOptionService;

/**
 * 線上申請-變更保單聯絡資料(保單為多選)
 */
@Controller
public class TransContactInfoController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(TransContactInfoController.class);
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransContactInfoService transContactInfoService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;

	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private MessageTemplateClient messageTemplateClient;
	
	@Autowired
	private IOptionService optionService;


	/**
	 * 保單清單頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/changeContact1")
	public String changeContact1(RedirectAttributes redirectAttributes) {
		try {
			if(!checkCanUseOnlineChange()) {
				/*addSystemError("目前無法使用此功能，請臨櫃申請線上服務。");*/
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				addSystemError(message);
				redirectAttributes.addFlashAttribute("errorMessage", message);
				return "redirect:apply1";
			}
			/**
			 * 3.有申請中的聯絡資料變更,則不可再申請
			 */
			int result = transContactInfoService.getContactInfoCompleted(getUserRocId());
			if (result > 0) {
				redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.CONTACT_INFO_APPLYING);
			  return "redirect:apply1";
			}
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<PolicyListVo> policyList = getUserOnlineChangePolicyList(userId, userRocId);
			
			// 處理保單狀態是否鎖定
			if (policyList != null) {
				List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
						userId, TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
				transContactInfoService.handlePolicyStatusLocked(handledPolicyList);
				transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
						TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
				addAttribute("policyList", handledPolicyList);
			}
		} catch (Exception e) {
			logger.error("Unable to init from changeContact1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "473");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/changeContact/change-contact1";
	}
	
	@RequestLog
	@GetMapping("/getUserCurrentNetworkData")
	public String getUserCurrentNetworkData(@RequestParam("policyNo") String policyNo) {
		addAttribute("userCND", transContactInfoService.getUserCurrentNetworkData(policyNo));
		return "frontstage/onlineChange/changeContact/change-contact1-detail";
	}
	
	/**
	 * 步驟2(填寫變更資料).
	 * 
	 * @param transContactInfoDtlVo TransCertPrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeContact2")
	public String changeContact2(TransContactInfoDtlVo transContactInfoDtlVo) {
		try {
			// 根據最新保單帶出變更前的要保人聯絡資料
			List<String> policyNos = transContactInfoDtlVo.getPolicyNoList();
			if (!CollectionUtils.isEmpty(policyNos)) {
				LilipmVo lilipmVo = lilipmService.findContactInfoByPolicyNoList(policyNos);
				if (lilipmVo != null) {
					transContactInfoDtlVo.setTelHome(lilipmVo.getNoHiddenLipmTelH());
					transContactInfoDtlVo.setTelOffice(lilipmVo.getNoHiddenLipmTelO());
					transContactInfoDtlVo.setMobile("");
					transContactInfoDtlVo.setEmail("");
					transContactInfoDtlVo.setAddress(lilipmVo.getLipmAddr());
					transContactInfoDtlVo.setAddressCharge(lilipmVo.getLipmCharAddr());
				}
			}
			
			addAttribute("transContactInfoDtlVo", transContactInfoDtlVo);
		} catch (Exception e) {
			logger.error("Unable to init from changeContact2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changeContact/change-contact2";
	}

	/**
	 * 步驟3(填寫變更資料).
	 * 
	 * @param transContactInfoDtlVo TransCertPrintVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeContact3")
	public String changeContact3(TransContactInfoDtlVo transContactInfoDtlVo) {
		try {
			
			Map<String,Object> rMap = null;
			HashMap userCurrentNetworkData = null;
			LilipmVo lilipmVo = null;
			
			// 根據最新保單帶出變更前的要保人聯絡資料
			com.twfhclife.eservice.web.model.UsersVo userVo = this.getUserDetail();
			List<String> policyNos = transContactInfoDtlVo.getPolicyNoList();
			//202201:保單'單筆'勾選時,只顯示該被勾選保單的現行保單資料作為預設顯示供修改
			if (CollectionUtils.isNotEmpty(policyNos)) {
				if( policyNos.size()==1) {//單選保單
					//20210120-保單單筆勾選時：只顯示該被勾選保單的現行保單資料作為預設顯示供修改
					rMap = new HashMap<String,Object>();
					userCurrentNetworkData = transContactInfoService.getUserCurrentNetworkData(policyNos.get(0));
					lilipmVo = lilipmService.findContactInfoByPolicyNoList(policyNos);
					
					if (lilipmVo != null) {
						transContactInfoDtlVo.setLipmName1(lilipmVo.getLipmName1());
						transContactInfoDtlVo.setTelHome(lilipmVo.getNoHiddenLipmTelH());
						transContactInfoDtlVo.setTelOffice(lilipmVo.getNoHiddenLipmTelO());
						transContactInfoDtlVo.setAddress(lilipmVo.getLipmAddrNoHidden());
						transContactInfoDtlVo.setAddressCharge(lilipmVo.getLipmCharAddrNoHidden());
						if(userCurrentNetworkData!=null && !userCurrentNetworkData.isEmpty()) {
							transContactInfoDtlVo.setMobile((String)userCurrentNetworkData.get("MOBILE"));
							transContactInfoDtlVo.setEmail((String)userCurrentNetworkData.get("EMAIL"));
						}
					}
				}else {
					rMap = transContactInfoService.getCIOUserDetailInfoNew(getUserRocId());
					if(rMap == null) {
					   rMap = transContactInfoService.getCIOUserDetailInfoOld(getUserRocId());
					}
				}
			}
			
		    List<Map<String, Object>> cityList = optionService.getCityList();
		    //List<Map<String, Object>> regionList = optionService.getRegionList("");
		    //List<Map<String, Object>> roadList = optionService.getRoadList("");

			if(rMap != null) {
				
				//for error control-start
				if(!rMap.containsKey("EMAIL")) {
					rMap.put("EMAIL", null);
				}
				if(!rMap.containsKey("MOBILE")) {
					rMap.put("MOBILE", null);
				}
				if(!rMap.containsKey("CITY_NAME_CHAR")) {
					rMap.put("CITY_NAME_CHAR", null);
				}
				if(!rMap.containsKey("REGION_NAME_CHAR")) {
					rMap.put("REGION_NAME_CHAR", null);
				}
				if(!rMap.containsKey("ADDRESS_CHAR")) {
					rMap.put("ADDRESS_CHAR", null);
				}
				if(!rMap.containsKey("CITY_CHAR")) {
					rMap.put("CITY_CHAR", null);
				}
				if(!rMap.containsKey("REGION")) {
					rMap.put("REGION", null);
				}
				if(!rMap.containsKey("CITY")) {
					rMap.put("CITY", null);
				}
				if(!rMap.containsKey("REGION_NAME")) {
					rMap.put("REGION_NAME", null);
				}
				if(!rMap.containsKey("CITY_NAME")) {
					rMap.put("CITY_NAME", null);
				}
				if(!rMap.containsKey("ADDRESS")) {
					rMap.put("ADDRESS", null);
				}
				if(!rMap.containsKey("REGION_CHAR")) {
					rMap.put("REGION_CHAR", null);
				}
				if(!rMap.containsKey("TEL_OFFICE")) {
					rMap.put("TEL_OFFICE", null);
				}
				if(!rMap.containsKey("TEL_HOME")) {
					rMap.put("TEL_HOME", null);
				}
				if(!rMap.containsKey("ADDRESS_FULL")) {//getCIOUserDetailInfoOld()不會回傳
					rMap.put("ADDRESS_FULL", null);
				}
				if(!rMap.containsKey("ADDRESS_FULL_CHARGE")) {//getCIOUserDetailInfoOld()不會回傳
					rMap.put("ADDRESS_FULL_CHARGE", null);
				}
				//for error control-end
				
				if(lilipmVo!=null) {//單選保單時才進入此流程
					rMap.put("TYPE", "new");//value='new' or 'old'
					//UI資料
					if(lilipmVo.getLipmNameBase64()!=null) {
						rMap.put("NAME", lilipmVo.getLipmNameBase64());
					}else {
						rMap.put("NAME", lilipmVo.getLipmName1());
					}

					rMap.put("TEL_HOME", lilipmVo.getNoHiddenLipmTelH());
					rMap.put("TEL_OFFICE", lilipmVo.getNoHiddenLipmTelO());
					
					if(userCurrentNetworkData!=null && !userCurrentNetworkData.isEmpty()) {
						rMap.put("MOBILE", (String)userCurrentNetworkData.get("MOBILE"));
						rMap.put("EMAIL",  (String)userCurrentNetworkData.get("EMAIL"));
					}
					
					rMap.put("ADDRESS_FULL", lilipmVo.getLipmAddrNoHidden());
					rMap.put("ADDRESS_FULL_CHARGE", lilipmVo.getLipmCharAddrNoHidden());
					
					//地址相關
					rMap.put("CITY_CHAR", null);
					rMap.put("REGION", null);
					rMap.put("CITY_NAME_CHAR", null);
					rMap.put("REGION_NAME_CHAR", null);
					
				}
				
				String telOffice = (String)rMap.get("TEL_OFFICE");
				if(telOffice != null && telOffice.contains("#")) {
					rMap.put("TEL_OFFICE",telOffice.split("#")[0]);
					rMap.put("TEL_OFFICE_1",telOffice.split("#")[1]);
				}else {
					rMap.put("TEL_OFFICE_1","");
				}
				
				String rMapType = (String)rMap.get("TYPE");
				if(rMap.containsKey("TYPE")) {
					rMapType = (String)rMap.get("TYPE");
				}
				logger.info("===步驟3(填寫變更資料)==="+"rMapType="+rMapType);
				String addressFull     = null;
				String addressCharFull = null;
				if("new".equals(rMapType)) {
					addressFull     = StringUtils.join("",rMap.get("ADDRESS_FULL"));
					addressCharFull = StringUtils.join("",rMap.get("ADDRESS_FULL_CHARGE"));
				}else if("old".equals(rMapType)){
					addressFull     = StringUtils.join("",rMap.get("ADDRESS"));
					addressCharFull = StringUtils.join("",rMap.get("ADDRESS_CHAR"));
				}else {
					//do nothing.
				}
				if(addressFull==null) {
					addressFull = "";
				}
				if(addressCharFull==null) {
					addressCharFull = "";
				}
				rMap.put("ADDRESS_FULL", addressFull);
				rMap.put("ADDRESS_CHAR_FULL", addressCharFull);
				
				/**
				if("new".equals(rMap.get("TYPE"))) {
					/*BigDecimal REGION = (BigDecimal)rMap.get("REGION");
					String ADDRESS = (String)rMap.get("ADDRESS");

					if(ADDRESS != null && REGION != null) {
						List<Map<String, Object>> roadList = optionService.getRoadList(REGION.toString());
						for (Map<String, Object> map : roadList) {
							String str = (String)map.get("VALUE");
							if(str != null && ADDRESS.contains(str)) {
								rMap.put("ROAD",map.get("KEY"));
								rMap.put("ADDRESS",ADDRESS.replace(str, ""));
							}
						}
					}

					BigDecimal REGION_CHAR = (BigDecimal)rMap.get("REGION_CHAR");
					String ADDRESS_CHAR = (String)rMap.get("ADDRESS_CHAR");

					if(ADDRESS_CHAR != null && REGION_CHAR != null) {
						List<Map<String, Object>> roadList = optionService.getRoadList(REGION_CHAR.toString());
						for (Map<String, Object> map : roadList) {
							String str = (String)map.get("VALUE");
							if(str != null && ADDRESS_CHAR.contains(str)) {
								rMap.put("ROAD_CHAR",map.get("KEY"));
								rMap.put("ADDRESS_CHAR",ADDRESS_CHAR.replace(str, ""));
							}
						}
					}
				}
				if("old".equals(rMap.get("TYPE"))) {
					//地址
					BigDecimal CITY = (BigDecimal)rMap.get("CITY");
					String CITY_NAME = (String)rMap.get("CITY_NAME");
					String ADDRESS = (String)rMap.get("ADDRESS");

					if(ADDRESS != null && CITY != null && CITY_NAME != null) {
						ADDRESS = ADDRESS.replace(CITY_NAME, "");
						List<Map<String, Object>> regionList = optionService.getRegionList(CITY.toString());
						for (Map<String, Object> map : regionList) {
							String str = (String)map.get("VALUE");
							if(str != null && ADDRESS.contains(str)) {
								rMap.put("REGION",map.get("KEY"));
								rMap.put("REGION_NAME",str);
								ADDRESS = ADDRESS.replace(str, "");
							}
						}
					}
					if(rMap.get("REGION") != null) {
						String REGION = rMap.get("REGION").toString();

						if(ADDRESS != null && !StringUtils.isEmpty(REGION)) {
							List<Map<String, Object>> roadList = optionService.getRoadList(REGION);
							for (Map<String, Object> map : roadList) {
								String str = (String)map.get("VALUE");
								if(str != null && ADDRESS.contains(str)) {
									rMap.put("ROAD",map.get("KEY"));
									rMap.put("ADDRESS",ADDRESS.replace(str, ""));
								}
							}
						}
					}
					rMap.put("ADDRESS",ADDRESS);
					//收費地址
					BigDecimal CITY_CHAR = (BigDecimal)rMap.get("CITY_CHAR");
					String CITY_NAME_CHAR = (String)rMap.get("CITY_NAME_CHAR");
					String ADDRESS_CHAR = (String)rMap.get("ADDRESS_CHAR");
					if(ADDRESS_CHAR != null && CITY_CHAR != null && CITY_NAME_CHAR != null) {
						ADDRESS_CHAR = ADDRESS_CHAR.replace(CITY_NAME_CHAR, "");

						List<Map<String, Object>> regionList = optionService.getRegionList(CITY_CHAR.toString());
						for (Map<String, Object> map : regionList) {
							String str = (String)map.get("VALUE");
							if(str != null && ADDRESS_CHAR.contains(str)) {
								rMap.put("REGION_CHAR",map.get("KEY"));
								rMap.put("REGION_NAME_CHAR",str);
								ADDRESS_CHAR = ADDRESS_CHAR.replace(str, "");
							}
						}
					}
					if(rMap.get("REGION_CHAR") != null) {
						String REGION_CHAR = rMap.get("REGION_CHAR").toString();
						if(ADDRESS_CHAR != null && !StringUtils.isEmpty(REGION_CHAR)) {

							List<Map<String, Object>> roadList = optionService.getRoadList(REGION_CHAR);
							for (Map<String, Object> map : roadList) {
								String str = (String)map.get("VALUE");
								if(str != null && ADDRESS_CHAR.contains(str)) {
									rMap.put("ROAD_CHAR",map.get("KEY"));
									rMap.put("ROAD_NAME_CHAR",str);
									rMap.put("ADDRESS_CHAR",ADDRESS_CHAR.replace(str, ""));
								}
							}
						}
					}
					rMap.put("ADDRESS_CHAR",ADDRESS_CHAR);
					
				}
				**/
			}
			logger.info("user-detail-info(end):{}",rMap);

			addAttribute("dataDetail", rMap);
			addAttribute("transContactInfoDtlVo", transContactInfoDtlVo);
		} catch (Exception e) {
			logger.error("Unable to init from changeContact3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changeContact/change-contact3";
	}

	/**
	 * 步驟4(確認資料及發送驗證碼).
	 * 
	 * @param transContactInfoDtlVo TransContactInfoDtlVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeContact4")
	public String changeContact4(TransContactInfoDtlVo transContactInfoDtlVo) {
		try {
			// 發送驗證碼
			sendAuthCode("contactInfo",transContactInfoDtlVo.getEmail(),transContactInfoDtlVo.getMobile());
			addAttribute("transContactInfoDtlVo", transContactInfoDtlVo);
			addAttribute("authenticationType", "contactInfo");
		} catch (Exception e) {
			logger.error("Unable to init from changeContact4: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changeContact/change-contact4";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param transContactInfoDtlVo TransContactInfoDtlVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeContactSuccess")
	public String changeContactSuccess(TransContactInfoDtlVo transContactInfoDtlVo) {
		try {
			boolean isTransApplyed = false;
			List<String> policyNos = transContactInfoDtlVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				boolean applyed = transService.isTransApplyed(policyNo,
						TransTypeUtil.CONTACT_INFO_PARAMETER_CODE, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}

			// 沒有申請過才新增
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transContactInfoDtlVo.getAuthenticationNum();
				String msg = checkAuthCode("contactInfo", authNum);
				if (!StringUtils.isEmpty(msg)) {
					transContactInfoDtlVo.setAuthenticationNum(null);
					addAttribute("transContactInfoDtlVo", transContactInfoDtlVo);
					addSystemError(msg);
					return "forward:changeContact4";
				}

				// 設定使用者
				String userId = getUserId();
				transContactInfoDtlVo.setUserId(userId);
				transContactInfoDtlVo.setFormCompany("L01");
				// Call api 送出線上申請資料
				logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
				
				String transAddResult = "";
				TransAddRequest apiReq = new TransAddRequest();
				apiReq.setSysId(ApConstants.SYSTEM_ID);
				apiReq.setTransType(TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
				apiReq.setTransContactInfoDtlVo(transContactInfoDtlVo);
				apiReq.setUserId(userId);
				
				// 設定交易序號
				logger.info("========================================================");
				Float batchId = transService.getBatchIdSequence();
				logger.info("==========================batchId:{}==============================",batchId);
				TransContactInfoVo transContactInfoVo = new TransContactInfoVo();
				transContactInfoVo.setBatchId(batchId);
				
//				apiReq.setTransContactInfoVo(transContactInfoVo);
//				TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
//				if (transAddResponse != null) {
//					logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
//							MyJacksonUtil.object2Json(transAddResponse));
//					transAddResult = transAddResponse.getTransResult();
//				} else {
					// 若無資料，嘗試由內部服務取得資料
					logger.info("Call internal service to get user[{}] insertTransContactInfo data", userId);
				/**
				 * 進行模仿文件上傳，測試，上榜需要進行mark
				 */
			/*	ArrayList<ContactInfoFileDataVo> objects = new ArrayList<>();
				ContactInfoFileDataVo cont = new ContactInfoFileDataVo();
				cont.setMsg("test");
				cont.setPath("C:\\Users\\chenhui\\Desktop\\ABC.pdf");
				cont.setFileName("ABC.pdf");
				cont.setType("1");
				cont.setSize("400");
				cont.setFileStatus("1");
				cont.setFileId("empty");
				cont.setCreateDate(new Date());
				cont.setNotifySeqId(1234.0000F);
				byte[] fileContent = FileUtils.readFileToByteArray(new File("C:\\Users\\chenhui\\Desktop\\ABC.pdf"));
				String base64= Base64.getEncoder().encodeToString(fileContent);
				cont.setFileBase64(base64);
				objects.add(cont);
				transContactInfoVo.setFileDatas(objects);*/
				// 狀態歷程
				TransStatusHistoryVo chisVo = new TransStatusHistoryVo();

				chisVo.setCustomerName("系統日程");
				UsersVo userDetail  = (UsersVo)getSession(UserDataInfo.USER_DETAIL);
				chisVo.setIdentity(userDetail.getUserId());
				logger.info("UsersVo",userDetail);
				chisVo.setUsersVo(userDetail);
				
				//UI件,不可能修改姓名和身份证
				transContactInfoDtlVo.setLipmName1(null);
				transContactInfoDtlVo.setIdno(null);
				Map<String,Object> rMap = transContactInfoService.insertTransContactInfo(transContactInfoVo, transContactInfoDtlVo, chisVo);
			    logger.info("Map<String,Object> rMap:{}",rMap);
				String transNums = (String)rMap.get("transNums");
				//Send Mail 通知系統管理員
				int result = (int) rMap.get("result");
				if (result <= 0) {
					transAddResult = ReturnHeader.FAIL_CODE;
				} else {
					transAddResult = ReturnHeader.SUCCESS_CODE;
					
					logger.info("start send mail");
					try {
						Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo((String)rMap.get("status"));
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("TransNum", transNums);
						paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
						paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
						logger.info("Trans Num : {}", transNums);
						logger.info("Status Name : {}", (String) mailInfo.get("statusName"));
						logger.info("Trans Remark : {}", (String) mailInfo.get("transRemark"));
						logger.info("receivers={}", (List)mailInfo.get("receivers"));
						logger.info("user phone : {}", transContactInfoDtlVo.getMobile());
						logger.info("user mail : {}", transContactInfoDtlVo.getEmail());
						
						//获取保单编号
						String strPolicyNoList = transContactInfoDtlVo.getPolicyNoList().toString();
						paramMap.put("POLICY_NO", strPolicyNoList);
						
						//获取状态，保戶[同意/不同意]轉送聯盟鏈
						boolean boo = StatuCode.AGREE_CODE.code.equals(transContactInfoDtlVo.getSendAlliance());
						String strSandAlliance = boo?StatuCode.AGREE_CODE.message:StatuCode.DISAGREE_CODE.message;
						paramMap.put("SENDALLIANCE", strSandAlliance);
						
						logger.info("POLICY_NO : {}",  strPolicyNoList);
						logger.info("SENDALLIANCE : {}", strSandAlliance);
						
						String loginTime = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
						paramMap.put("LoginTime", loginTime);
						
						List<String> receivers = new ArrayList<String>();
						
						/// 20211228 by 203990
						/// 已接收的管理員通知信不再發送
						/*
						//發送系統管理員
						receivers = (List)mailInfo.get("receivers");
						//推送管 理已接收 保單編號: [保單編號]  保戶[同意/不同意]轉送聯盟鏈
						messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_009, receivers, paramMap, "email");
						*/

						//發送保戶SMS
						//receivers = new ArrayList<String>();
						receivers.clear();//清空
						receivers.add(transContactInfoDtlVo.getMobile());
						logger.info("user phone : {}", transContactInfoDtlVo.getMobile());
						messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_SMS_007, receivers, paramMap, "sms");
						
						//發送保戶MAIL
						//receivers = new ArrayList<String>();
						if(transContactInfoDtlVo.getEmail()	!= null){
							receivers.clear();//清空
							receivers.add(transContactInfoDtlVo.getEmail());
							logger.info("user mail : {}", transContactInfoDtlVo.getEmail());
							if(boo){
								messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_010, receivers, paramMap, "email");
							}else{
								messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_011, receivers, paramMap, "email");
							}
						}

						/**
						 *进行对旧邮件发送信息
						 * */
						if (userDetail.getEmail() != null) {
							receivers.clear();//清空
							receivers.add(userDetail.getEmail());
							logger.info("userDetail  odMail : {}", userDetail.getEmail());
							if(boo){
								messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_010, receivers, paramMap, "email");
							}else{
								messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_011, receivers, paramMap, "email");
							}
						}

					}catch(Exception e) {
						logger.info("insertTransContactInfo() success, but send notify mail/sms error.");
					}
					logger.info("End send mail");

				}

				if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
					addDefaultSystemError();
					return "forward:changeContact4";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from changeContactSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:changeContact4";
		}
		return "frontstage/onlineChange/changeContact/change-contract-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransContactInfoDetail")
	public String getTransContactInfoDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransContactInfoDtlVo transContactInfoDtlVo = null;
			TransContactInfoOldVo transContactInfoOldVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transContactInfoDtlVo = transHistoryDetailList.get(0).getTransContactInfoDtlVo();
					transContactInfoOldVo = transHistoryDetailList.get(0).getTransContactInfoOldVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransContactInfoDetail data", userId);
				BigDecimal contactInfoId = transContactInfoService.getTransContactInfoId(transNum);
				transContactInfoDtlVo = transContactInfoService.getTransContactInfoDtlDetail(contactInfoId);
				transContactInfoOldVo = transContactInfoService.getTransContactInfoOldDetail(contactInfoId);
			}
			
			addAttribute("transContactInfoDtlVo", transContactInfoDtlVo);
			addAttribute("transContactInfoOldVo", transContactInfoOldVo);
		} catch (Exception e) {
			logger.error("Unable to getTransContactInfoDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changeContact/change-contract-detail";
	}
}