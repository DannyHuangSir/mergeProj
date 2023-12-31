package com.twfhclife.eservice.onlineChange.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.BlackListVo;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;
import com.twfhclife.eservice.onlineChange.model.TransFundConversionVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.service.IndividualChooseService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
//import com.twfhclife.eservice.policy.service.IFundPrdtService;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
//import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.LicohilResponse;
import com.twfhclife.generic.api_model.LicohilVo;
import com.twfhclife.generic.api_model.PolicyDetailRequest;
import com.twfhclife.generic.api_model.PolicyDetailResponse;
import com.twfhclife.generic.api_model.PolicyDetailVo;
import com.twfhclife.generic.api_client.PortfolioClient;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.ValidateUtil;

import org.apache.commons.collections.CollectionUtils;
//import com.twfhclife.generic.util.StatuCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
//import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;
//import sun.rmi.log.LogInputStream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import javax.print.DocFlavor;
//import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
/***
 * 已持有投資標的轉換
 */
@Controller
public class FundConversionController extends BaseUserDataController  {
    private static final Logger logger = LogManager.getLogger(FundConversionController.class);

    @Autowired
    private IInsuranceClaimService insuranceClaimService;

    @Autowired
    private ILoginService loginService;

	@Autowired
	private IndividualChooseService indivdualChooseService;
	
    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private ITransInvestmentService transInvestmentService;
    @Autowired
    private IParameterService parameterService;

    @Autowired
    private PortfolioClient portfolioClient;
    @Autowired
    private IPortfolioService portfolioService;

    @Autowired
    private ITransRiskLevelService riskLevelService;

    @Autowired
    private ITransService transService;
    @Autowired
    private MessageTemplateClient messageTemplateClient;
    
	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;
	
	@Autowired
	private ILilipmService  lilipmService;

    @RequestLog
    @GetMapping("/fund1")
    public String fund1(RedirectAttributes redirectAttributes) {
        try {
            //1.验证功能
             if(!checkCanUseOnlineChange()) {
                 String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
                 redirectAttributes.addFlashAttribute("errorMessage", message);
                 return "redirect:apply1";
             }
            /**
             * ODM
             * 不是黑名單才能申請
             */
            BlackListVo blackListVo = new BlackListVo();
            blackListVo.setIdNo(getUserRocId());
            String detailInfo = insuranceClaimService.checkBackList(blackListVo);
            if (detailInfo != null) {
                redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.BACK_LIST_MSG);
                return "redirect:apply1";
            }

//            String limitDay = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "CONVERSION_APPLY_LIMIT_DAY");

//            //申請間隔管控
//            List<TransVo> transVos = transService.getLastCompleteTime(TransTypeUtil.INVESTMENT_CONVERSION_CODE, getUserId());
//            if (completeTime != null && StringUtils.isNotBlank(limitDay) && StringUtils.isNumeric(limitDay)) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(completeTime);
//                calendar.set(Calendar.MILLISECOND, 0 );
//                calendar.set(Calendar.SECOND, 0 );
//                calendar.set(Calendar.MINUTE, 0 );
//                calendar.set(Calendar.HOUR, 0 );
//                calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(limitDay));
//                if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
//                    String limitMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "CONVERSION_APPLY_LIMIT_MSG");
//                    if (StringUtils.isNotBlank(limitMsg)) {
//                        limitMsg = limitMsg.replace("${time}", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//                        redirectAttributes.addFlashAttribute("errorMessage", limitMsg);
//                        return "redirect:apply1";
//                    }
//                    redirectAttributes.addFlashAttribute("errorMessage", "您最近有完成此功能的申請處理！");
//                    return "redirect:apply1";
//                }
//            }
//            /**
//             * 投資型保單申請中不可繼續申請
//             * TRANS  status=-1,0,4
//             */
//			  2023/09/28 USER 新增檢核機制  取消舊有檢核        
//            String msg = transInvestmentService.checkHasApplying(getUserId());
//            if (StringUtils.isNotBlank(msg)) {
//                redirectAttributes.addFlashAttribute("errorMessage", msg);
//                return "redirect:apply1";
//            }
            
            //取得登入者證號.
            String userRocId = getUserRocId();
            UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);

//            boolean expire = riskLevelService.checkRiskLevelExpire(userDetail.getUserId());
//            if (expire) {
//                redirectAttributes.addFlashAttribute("errorMessage", "距上一次線上風險屬性變更已超過一年，再請先重新執行線上風險屬性測試及變更！");
//                return "redirect:apply1";
//            }
//
//            
//            String riskLevel = riskLevelService.getUserRiskAttr(userRocId);
//            if(StringUtils.isBlank(riskLevel)) {
//                String message = "請先變更風險屬性！";
//                redirectAttributes.addFlashAttribute("errorMessage", message);
//                return "redirect:apply1";
//            }


            boolean isPartner = loginService.isPartnerRole(userDetail.getUserType());
            if (isPartner) {
                userRocId = (String) getSession("ADMIN_QUERY_ROCID");
                logger.info("get session ADMIN_QUERY_ROCID={}", userRocId);
            }

            List<PolicyListVo> policyList = policyListService.getInvestmentPolicyList(userRocId);
            transInvestmentService.handlePolicyStatusLocked(userRocId, policyList, TransTypeUtil.INVESTMENT_CONVERSION_CODE );
            transService.handleVerifyPolicyRuleStatusLocked(policyList,TransTypeUtil.INVESTMENT_CONVERSION_CODE);
            transInvestmentService.newCheckHasApplying(getUserId() , policyList);
            //申請間隔管控
            List<TransVo> transVos = transService.getLastCompleteTime(TransTypeUtil.INVESTMENT_CONVERSION_CODE, getUserId());
            transInvestmentService.checkLastCompleteTime(policyList, transVos);
            //進行查詢當前保單是否已有投資標
            transInvestmentService.handleNotExistingInvestmentLock(userRocId, policyList);

//            List<PolicyListVo> policyList =  new ArrayList<PolicyListVo>();
//           	policyList =  (List<PolicyListVo>) getSession(UserDataInfo.USER_ALL_POLICY_LIST);
//            if(policyList == null) {
//				PolicyDetailRequest req = new PolicyDetailRequest();
//				req.setRocId(userRocId);
//				PolicyDetailResponse response = transCtcSelectUtilClient.getPolicyDataByRocId(req);
//				if(response != null) {
//					List<PolicyDetailVo> policyDetailList  = new ArrayList<PolicyDetailVo>();
//					policyDetailList = response.getPolicyDetailList();
//					policyList = loginService.mappingPolicyList(policyDetailList);
//				}				
//            }
//            
//            transInvestmentService.handlePolicyStatusLocked(userRocId, policyList, TransTypeUtil.INVESTMENT_CONVERSION_CODE );
//            transService.handleVerifyPolicyRuleStatusLocked(policyList,TransTypeUtil.INVESTMENT_CONVERSION_CODE);
//            //進行查詢當前保單是否已有投資標
//            transInvestmentService.handleNotExistingInvestmentLock(userRocId, policyList);
         
//            List<String> policyNoList = new ArrayList<>();
//            policyList.stream().filter(x->{
//            	if(x.getPolicyListType().equals("1")) {
//            		policyNoList.add(x.getPolicyNo());
//            		return true;
//            	}
//				return false;            	
//            }).collect(Collectors.toList());
//            if(policyNoList.size() > 0) {
    			PolicyDetailRequest request = new PolicyDetailRequest();
    			request.setRocId(userRocId);			
    			request.setPolicyInvestmentType(indivdualChooseService.getpolicyInvestmentType());
    			LicohilResponse response = transCtcSelectUtilClient.getLicohiByPolicyNo(request);
    			if(response.getLicohilVo().size() == 0) {
    				response = transCtcSelectUtilClient.getLilipmByPolicyNo(request);
    				LicohilVo licohilVo = indivdualChooseService.getpolicyHaveInvestmentType(response.getLicohilVo());
    				if(licohilVo != null) {
    					if(!insertOrUpdateForIndividual(licohilVo)) {
    						redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.INDIVDUAL_CHOOSE_ONEYEAR_MSG_1 +"<br/>" + OnlineChangMsgUtil.INDIVDUAL_CHOOSE_ONEYEAR_MSG_2 );
    			             return "redirect:apply1";
    					}
    				}else {
    	                redirectAttributes.addFlashAttribute("errorMessage", "請先變更風險屬性！");
    	                return "redirect:apply1";
    				}	           
    			}else {
    				LicohilVo licohilVo = indivdualChooseService.getpolicyHaveInvestmentType(response.getLicohilVo());
    	            transInvestmentService.insertOrUpdateForIndividual(licohilVo);
					if(!insertOrUpdateForIndividual(licohilVo)) {
						redirectAttributes.addFlashAttribute("errorMessage", OnlineChangMsgUtil.INDIVDUAL_CHOOSE_ONEYEAR_MSG_1 +"<br/>" + OnlineChangMsgUtil.INDIVDUAL_CHOOSE_ONEYEAR_MSG_2);
			             return "redirect:apply1";
					}    
    			}
//               }
//        		else {
//            	   redirectAttributes.addFlashAttribute("errorMessage", "請先變更風險屬性！");
//                   return "redirect:apply1"; 
//               }
 
          //進行查詢當前備注條款
            String parameterValueByCode = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_TRANSFORMATION_REMARKS);
            if (parameterValueByCode != null) {
                addAttribute("transformationRemarks", parameterValueByCode);
            }
            addAttribute("policyList", policyList);
        } catch (Exception ex) {
            logger.error("--------Unable to init from FundConversionController - fund1--------: {}", ExceptionUtils.getStackTrace(ex));
            addDefaultSystemError();
        }

        return "frontstage/onlineChange/fundConversion/fund1";
    }

    @RequestLog
    @PostMapping("/fund2")
    public String fund2(PolicyListVo formData) {
        try {
            //進行查詢當前同意條款
            String policyType = formData.getPolicyNo().substring(0, 2);
            //進行查詢當前同意條款
            String parameterValueByCodeConsent = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_CONSENT+ "_"  + policyType);
            if (parameterValueByCodeConsent != null) {
                addAttribute("next", true);
                addAttribute("transformationConsent", parameterValueByCodeConsent);
            }else {
                String error = parameterService.getParameterValueByCode(
                        ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_CONSENT + "_ERROR");
                addSystemError(error);
                addAttribute("next", false);
            }
            logger.error("--============================================================-policyList {}",formData.getPolicyNo());

            addAttribute("policyListPolicyNo", formData);
        } catch (Exception ex) {
            logger.error("--------Unable to init from FundConversionController - fund2--------: {}", ExceptionUtils.getStackTrace(ex));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/fundConversion/fund2";
    }

    @RequestLog
    @PostMapping("/fund3")
    public String fund3(PolicyListVo policyList) {
        try {
            //進行查詢當前同意條款
            String parameterValueByProportion = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_TRANSFORMATION_PROPORTION);
            if (parameterValueByProportion != null) {
                addAttribute("parameterValueByProportion", parameterValueByProportion);
            }
            //欲轉出基金與比例,限制單位數小值USD_UNIT_MIN
          Map<String,String>  unit_min = transInvestmentService.getNtdAndUsdUnitMin();
            //欲轉出基金與比例,限制比例小值
         Map<String,String>  proportion_min=  transInvestmentService.getNtdAndUsdProportionMin();

            addAttribute("unitMin", unit_min);
            addAttribute("proportionMin", proportion_min);
            addAttribute("policyListPolicyNo", policyList);
        } catch (Exception ex) {
            logger.error("--------Unable to init from FundConversionController - fund3--------: {}", ExceptionUtils.getStackTrace(ex));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/fundConversion/fund3";
    }
    /**
     * 取得取得投資損益及投報率清單資料.
     *
     * @param policyNo 保單號碼
     * @return
     */
    @RequestLog
    @PostMapping("/fund3PortfolioList")
    public ResponseEntity<ResponseObj> getPortfolioList(@RequestParam("policyNo") String policyNo) {
        try {
            //查询已有投资标
            List<InvestmentPortfolioVo> investments = transInvestmentService.getHeldInvestmentTarget(policyNo);
            //獲取到停泊賬號信息
            String uuStopAccount = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.DEPOSIT_UU_STOP_ACCOUNT);
            String usStopAccount = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.DEPOSIT_US_STOP_ACCOUNT);
            List<InvestmentPortfolioVo> collect = investments.stream().map(x -> {
                String invtNo = x.getInvtNo();
                if (!StringUtils.isEmpty(invtNo)) {
                    if (invtNo.equals(uuStopAccount) || invtNo.equals(usStopAccount)) {
                        x.setDepositAccount("Y");
                    } else {
                        x.setDepositAccount("N");
                    }
                }
                return x;
            }).collect(Collectors.toList());

            logger.error("U=====================取得取得投資損益及投報率清單資料.====={}", investments);
            processSuccess(collect);
        } catch (Exception e) {
            logger.error("Unable to getPortfolioList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }
    @RequestLog
    @PostMapping("/fund4")
    public String fund4(String  investmentsList, PolicyListVo policyList) {
        try {
            Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy/MM/dd HH:mm:ss").create();
            List<InvestmentPortfolioVo> investmentPortfolioVo = builderTime.fromJson(investmentsList, new TypeToken<List<InvestmentPortfolioVo>>(){}.getType());
            logger.error("--------Unable to init from FundConversionController - fund4--------: {}", investmentPortfolioVo);

            String riskLevel = riskLevelService.getUserRiskAttr(getUserRocId());
            String s = transInvestmentService.transRiskLevelToName(riskLevel);
             investmentPortfolioVo = investmentPortfolioVo.stream().map((x) -> {
                    x.setInvtRiskBeneLevel(s);
                    return x;
                }).collect(Collectors.toList());
            //進行查詢當前同意條款
            String parameterValueByProportion = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_TRANSFORMATION_PROPORTION);
            if (parameterValueByProportion != null) {
                addAttribute("parameterValueByProportion", parameterValueByProportion);
            }
            List<String> chckSwiftCode = transInvestmentService.getChckSwiftCode();
            //進行查詢
            addAttribute("uri", parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENT_DISTRIBUTION_URI));
            addAttribute("investmentPortfolioVo", investmentPortfolioVo);
            addAttribute("proposer", getProposerName());
            addAttribute("chckSwiftCode", chckSwiftCode);
            addAttribute("policyListPolicyNo", policyList);
        } catch (Exception ex) {
            logger.error("--------Unable to init from FundConversionController - fund4--------: {}", ExceptionUtils.getStackTrace(ex));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/fundConversion/fund4";
    }

    @RequestLog
    @PostMapping("/getInvestmentConversionSearchItem")
    @ResponseBody
    public ResponseEntity<ResponseObj> getInvestmentSearchItem(@RequestBody TransInvestmentVo vo) {
    	if(ValidateUtil.TransInvestmentIsValid(vo)) {
            Map<String, List<Map<String, String>>> map = transInvestmentService.getCompanyAndCurrencyList(vo.getPolicyNo());
            processSuccess(map);    		
    	}else {
            processSuccess(null);
    	}

        return processResponseEntity();
    }

    //獲取當前保單的新投資標的數據
    @RequestLog
    @PostMapping("/fund4NewInvestments")
    @ResponseBody
    public ResponseEntity<ResponseObj> getNewInvestments(@RequestBody TransInvestmentVo vo) {
        List<InvestmentPortfolioVo> investments = transInvestmentService.getNewInvestments(vo.getPolicyNo(), vo.getOwnInvestments(), getUserRocId());
	    	List<InvestmentPortfolioVo> ruleInvestments = new ArrayList<InvestmentPortfolioVo>();
	    	String investmentsDate = parameterService.getParameterValueByCode(
                     ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENTS_DATE_CODE);
            String investmentsFundsCode = parameterService.getParameterValueByCode(
                     ApConstants.SYSTEM_ID, OnlineChangeUtil.INVESTMENTS_FUND_CODE);
            List<String> investmentsList = new ArrayList<String>(); 
            investmentsList = Arrays.asList(investmentsFundsCode.split(","));
	    	LocalDate begDate = null;
	    	//ESERVICE
	    	LilipmVo pmVo = new LilipmVo();
	        pmVo = lilipmService.findByPolicyNo(vo.getPolicyNo());
	        if(pmVo != null) {
	        	begDate = pmVo.getLipmInsuBegDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        }
	        //檢核 保單生效日(LILIPM.LIPM_INSU_BEG_DATE) >= 20230701
	        LocalDate sysDate = LocalDate.parse(investmentsDate);	        
	        if(sysDate.isEqual(begDate) || sysDate.isBefore (begDate)) {
	        	for(InvestmentPortfolioVo portfolioVo : investments) {
	        		if(!investmentsList.contains(portfolioVo.getInvtNo())) {
	        			ruleInvestments.add(portfolioVo);
	        		}
	        	}	
		        processSuccess(ruleInvestments);
	        } else {
	        	processSuccess(investments);
	        }   
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/fund5")
        public String fund5(InvestmentPortfolioVo  formData) {
        try {
            Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy/MM/dd HH:mm:ss").create();
            String investmentsList = formData.getInvestmentsList();
            String numInvestment = formData.getNumInvestment();

            //轉出基金
            List<InvestmentPortfolioVo> outInvestmentPortfolioVo =
                    builderTime.fromJson(investmentsList, new TypeToken<List<InvestmentPortfolioVo>>() {}.getType());
            //轉入基金
            List<InvestmentPortfolioVo> inInvestmentPortfolioVo =
                    builderTime.fromJson( numInvestment,new TypeToken<List<InvestmentPortfolioVo>>() {}.getType());
            //基金匯總數據
            List<TransFundConversionVo> outInvestmentService = transInvestmentService.outransFundConversionDate(outInvestmentPortfolioVo);
            List<TransFundConversionVo> inInvestmentService = transInvestmentService.inransFundConversionDate(inInvestmentPortfolioVo);
            addAttribute("outInvestmentService", outInvestmentService);
            addAttribute("inInvestmentService", inInvestmentService);
            addAttribute("investmentsList",investmentsList);
            addAttribute("numInvestment", numInvestment);
            addAttribute("formData", formData);
            //發驗證碼
            sendAuthCode("conversion");
            addAttribute("timeSet", 300);
        } catch (Exception ex) {
            logger.error("--------Unable to init from FundConversionController - fund5--------: {}", ExceptionUtils.getStackTrace(ex));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/fundConversion/fund5";
    }

    @RequestLog
    @PostMapping("/fundSuccess")
    public String fundSuccess(TransInvestmentVo transInvestmentVo) {
         try {
             // 驗證驗證碼或者密码
             String msg;
             if (org.apache.commons.lang3.StringUtils.equals(transInvestmentVo.getAuthType(), "password")) {
                 msg = checkPassword(transInvestmentVo.getUserPassword());
             } else {
                 msg = checkAuthCode("conversion", transInvestmentVo.getAuthenticationNum());
             }
             if (!StringUtils.isEmpty(msg)) {
                 addSystemError(msg);
                 return "forward:fund5";
             } else {
                 UsersVo user = getUserDetail();
                 try {

               TransInvestmentVo vo=       transInvestmentService.insertTransFundConversion(transInvestmentVo,user);
                    // 當前使用用的模板後期會變化

                   sendNotification(vo, user);
                 } catch (Exception e) {
                     logger.error(e);
                     return "forward:fund5";
                 }
             }
         } catch (Exception ex) {
             logger.error("Unable to init from FundConversionController  fundSuccess: {}", ExceptionUtils.getStackTrace(ex));
             addDefaultSystemError();
             return "forward:fund5";
         }
        return "frontstage/onlineChange/fundConversion/fund-success";
    }
    @RequestLog
    @PostMapping("/getTransInvestmentConversionDetail")
    public String getTransInvestmentConversionDetail(@RequestParam("transNum") String transNum) {

        try {
            List<List<TransFundConversionVo>>  listTransFundConversionVo  = transInvestmentService.transInvestmentConversionDetail(transNum);
            addAttribute("listTransFundConversionVo",listTransFundConversionVo);
        } catch (Exception e) {
            logger.error("Unable to getTransInvestmentDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/fundConversion/fund-detail";
    }


    private String checkPassword(String authenticationNum) {
        try {
            UsersVo userDetail = getUserDetail();
            if(StringUtils.isEmpty(userDetail.getUserId())){
                return "密碼驗證失敗！";
            }
            LoginRequestVo loginRequestVo = new LoginRequestVo();
            String decodePasswd = decodeBase64(authenticationNum);
            loginRequestVo.setUserId(userDetail.getUserId());
            loginRequestVo.setPassword(decodePasswd);
            LoginResultVo restLogin = loginService.doLogin(loginRequestVo);
            if(restLogin!=null && StringUtils.equals("SUCCESS", restLogin.getReturnCode())){
                return  null;
            }else{
                return "密碼驗證失敗！";
            }
        } catch (Exception ex) {
            logger.error("==========密碼驗證失敗=========== {}", ExceptionUtils.getStackTrace(ex));
            return "密碼驗證失敗！";
        }
    }

    private String decodeBase64(String mi) {
        String mingwen = "";
        if (StringUtils.isNotBlank(mi)) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] by = decoder.decodeBuffer(mi);
                mingwen = new String(by);
            } catch (Exception ex) {
                logger.error("==========密碼加密失敗=========== {}", ExceptionUtils.getStackTrace(ex));
            }
        }
        return mingwen;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendNotification(TransInvestmentVo vo, UsersVo user) {
        try {
            Map<String, Object> mailInfo = transInvestmentService.getSendMailInfo();
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("TransNum", vo.getTransNum());
            paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
            paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
            logger.info("Trans Num : {}", vo.getTransNum());
            logger.info("Status Name : {}", mailInfo.get("statusName"));
            logger.info("Trans Remark : {}", mailInfo.get("transRemark"));
            logger.info("receivers={}", mailInfo.get("receivers"));
            logger.info("user phone : {}", user.getMobile());
            logger.info("user mail : {}", user.getEmail());
            //获取保单编号
            paramMap.put("POLICY_NO", vo.getPolicyNo());
            logger.info("POLICY_NO : {}", vo.getPolicyNo());

            List<String> receivers = new ArrayList<String>();

            String applyDate = DateUtil.formatDateTime(vo.getApplyDate(), "yyyy年MM月dd日 HH時mm分ss秒");
            paramMap.put("DATA", applyDate);
            //申請狀態-申請中
            paramMap.put("TransStatus","已審核");
            //申請功能
            ParameterVo parameterValueByCode = parameterService.getParameterByParameterValue(
                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.INVESTMENT_CONVERSION_CODE);
            paramMap.put("APPLICATION_FUNCTION", parameterValueByCode.getParameterName());


            //發送系統管理員
            receivers = (List) mailInfo.get("receivers");
            //推送管 理已接收 保單編號: [保單編號]  保戶[同意/不同意]轉送聯盟鏈
            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_027, receivers, paramMap, "email");

            //發送保戶SMS
            //receivers = new ArrayList<String>();
            receivers.clear();//清空
            paramMap.clear();//清空模板參數
            //設置模板參數
            paramMap.put("TITLE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_TITLE);
            paramMap.put("MESSAGE",OnlineChangMsgUtil.INVESTMENT_POLICY_APPLY_CAPACITY);
            receivers.add(user.getMobile());
            logger.info("user phone : {}", user.getMobile());
                messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_028, receivers, paramMap, "sms");
            //發送保戶MAIL
            //receivers = new ArrayList<String>();
            if (user.getEmail() != null) {
                receivers.clear();//清空
                receivers.add(user.getEmail());
                logger.info("user mail : {}", user.getEmail());
                messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_028, receivers, paramMap, "email");
            }
        } catch (Exception e) {
            logger.info("insertTransInvestment() success, but send notify mail/sms error.");
        }
        logger.info("End send mail");
    }
    
	private boolean insertOrUpdateForIndividual(LicohilVo licohilVo) {
		transInvestmentService.insertOrUpdateForIndividual(licohilVo);
		transInvestmentService.insertOrUpdateForIndividualChoose(licohilVo);
		IndividualChooseVo vo = indivdualChooseService.getIndividualChoosData(licohilVo.getLipmId());
		if(licohilVo.getRatingDate() != null && vo != null) {
			if(indivdualChooseService.compareDate(vo.getRatingDate(), licohilVo.getRatingDate())) {
				 if(indivdualChooseService.checkRatingDate(vo.getRatingDate())){
					 return false;
				 }else {
					 return true;
				 }
			}else {
				if(indivdualChooseService.checkRatingDate(licohilVo.getRatingDate())) {
					return false;
				}else {
					 return true;
				}
			}
		}else if(licohilVo.getRatingDate() != null){
			if(indivdualChooseService.checkRatingDate(licohilVo.getRatingDate())) {
				return false;
			}else {
				 return true;
			}
		}
		return true;
	}
}
