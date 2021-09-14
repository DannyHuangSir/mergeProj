package com.twfhclife.eservice.api.elife.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twfhclife.alliance.service.IContactInfoService;
import com.twfhclife.generic.utils.*;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.CancelAuthVo;
import com.twfhclife.eservice.onlineChange.model.MaturityVo;
import com.twfhclife.eservice.onlineChange.model.TransAnnuityMethodVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryVo;
import com.twfhclife.eservice.onlineChange.model.TransBounsVo;
import com.twfhclife.eservice.onlineChange.model.TransCancelContractVo;
import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransChangeAccountVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoOldVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransCreditCardDateVo;
import com.twfhclife.eservice.onlineChange.model.TransCushionVo;
import com.twfhclife.eservice.onlineChange.model.TransDnsVo;
import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationVo;
import com.twfhclife.eservice.onlineChange.model.TransFundSwitchVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.model.TransLoanVo;
import com.twfhclife.eservice.onlineChange.model.TransPaymethodVo;
import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.onlineChange.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyVo;
import com.twfhclife.eservice.onlineChange.model.TransRenewReduceVo;
import com.twfhclife.eservice.onlineChange.model.TransResendPolicyVo;
import com.twfhclife.eservice.onlineChange.model.TransRewardVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.model.TransSurrenderVo;
import com.twfhclife.eservice.onlineChange.model.TransValuePrintVo;
import com.twfhclife.eservice.onlineChange.service.ICancelAuthService;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.IMaturityService;
import com.twfhclife.eservice.onlineChange.service.IPolicyHolderProfileService;
import com.twfhclife.eservice.onlineChange.service.ITransAnnuityMethodService;
import com.twfhclife.eservice.onlineChange.service.ITransBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ITransBounsService;
import com.twfhclife.eservice.onlineChange.service.ITransCancelContractService;
import com.twfhclife.eservice.onlineChange.service.ITransCertPrintService;
import com.twfhclife.eservice.onlineChange.service.ITransChangeAccountService;
import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransCreditCardDateService;
import com.twfhclife.eservice.onlineChange.service.ITransCushionService;
import com.twfhclife.eservice.onlineChange.service.ITransDnsService;
import com.twfhclife.eservice.onlineChange.service.ITransFundNotificationService;
import com.twfhclife.eservice.onlineChange.service.ITransFundSwitchService;
import com.twfhclife.eservice.onlineChange.service.ITransLoanService;
import com.twfhclife.eservice.onlineChange.service.ITransPaymethodService;
import com.twfhclife.eservice.onlineChange.service.ITransPaymodeService;
import com.twfhclife.eservice.onlineChange.service.ITransReducePolicyService;
import com.twfhclife.eservice.onlineChange.service.ITransRenewReduceService;
import com.twfhclife.eservice.onlineChange.service.ITransResendPolicyService;
import com.twfhclife.eservice.onlineChange.service.ITransRewardService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.service.ITransSurrenderService;
import com.twfhclife.eservice.onlineChange.service.ITransValuePrintService;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 送出線上申請服務.
 * 
 * @author all
 */
@Service
public class TransAddServiceImpl implements ITransAddService {

	private static final Logger logger = LogManager.getLogger(TransAddServiceImpl.class);
	
	@Autowired
	public UsersDao usersDao;
	
	@Autowired
	public TransDao transDao;
	@Autowired
	public TransPolicyDao transPolicyDao;
	@Autowired
	private ITransService transService;
	@Autowired
	private ITransAnnuityMethodService transAnnuityMethodService;
	@Autowired
	private ITransBeneficiaryService transBeneficiaryService;
	@Autowired
	private ITransBounsService transBounsService;
	@Autowired
	private ITransCreditCardDateService transCreditCardDateService;
	@Autowired
	private ITransCushionService transCushionService;
	@Autowired
	private ITransFundNotificationService transFundNotificationService;
	@Autowired
	private ITransPaymethodService transPaymethodService;
	@Autowired
	private ITransPaymodeService transPaymodeService;
	@Autowired
	private ITransReducePolicyService transReducePolicyService;
	@Autowired
	private ITransRenewReduceService transRenewReduceService;
	@Autowired
	private ITransRewardService transRewardService;
	@Autowired
	private ITransCertPrintService transCertPrintService;
	@Autowired
	private ITransChangeAccountService transChangeAccountService;
	@Autowired
	private ITransContactInfoService transContactInfoService;
	@Autowired
	private ITransValuePrintService transValuePrintService;
	@Autowired
	private ICancelAuthService cancelAuthService;
	@Autowired
	private ITransCancelContractService transCancelContractService;
	@Autowired
	private ITransLoanService transLoanService;
	@Autowired
	private IMaturityService maturityService;
	@Autowired
	private ITransSurrenderService transSurrenderService;
	@Autowired
	private ITransResendPolicyService transResendPolicyService;
	@Autowired
	private ITransRiskLevelService transRiskLevelService;
	@Autowired
	private IPolicyHolderProfileService policyHolderProfileService;
	@Autowired
	private ITransFundSwitchService transFundSwitchService;
	@Autowired
	private IInsuranceClaimService insuranceClaimService;
	
	@Autowired
	private ITransDnsService transDnsServiceImpl;
	
	@Autowired
	IMessagingTemplateService messagingTemplateService;
	
	@Autowired
	private ParameterDao parameterDao;
	@Autowired
	private IContactInfoService iContactInfoService;

	/**
	 * 送出線上申請資料.
	 * 
	 * @param apiReq TransAddRequest
	 * @return 回傳線上申請結果
	 */
	@Override
	public TransAddResponse addTransRequest(TransAddRequest apiReq) {
		TransAddResponse transAddResponse = new TransAddResponse();
		String transType = apiReq.getTransType();
		String userId = apiReq.getUserId();
		
		int result = 0;
		try {
			// TODO 若一個policyNo配一個交易序號，需根據保單號碼清單取多筆交易序號
			String transNum = transService.getTransNum();
			transAddResponse.setTransNums(Arrays.asList(transNum));
			
			switch (transType) {
			case TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE:
				// 變更年金給付方式
				TransAnnuityMethodVo transAnnuityMethodVo = apiReq.getTransAnnuityMethodVo();
				if (StringUtils.isEmpty(transAnnuityMethodVo.getUserId())) {
					transAnnuityMethodVo.setUserId(userId);
				}
				transAnnuityMethodVo.setTransNum(transNum);
				result = transAnnuityMethodService.insertTransAnnuityMethod(transAnnuityMethodVo);
				break;
			case TransTypeUtil.BENEFICIARY_PARAMETER_CODE:
				// 變更受益人
				TransBeneficiaryVo transBeneficiaryVo = apiReq.getTransBeneficiaryVo();
				if (StringUtils.isEmpty(transBeneficiaryVo.getUserId())) {
					transBeneficiaryVo.setUserId(userId);
				}
				transBeneficiaryVo.setTransNum(transNum);
				result = transBeneficiaryService.insertTransBeneficiary(transBeneficiaryVo);
				break;
			case TransTypeUtil.BONUS_PARAMETER_CODE:
				// 變更紅利選擇權
				TransBounsVo transBounsVo = apiReq.getTransBounsVo();
				if (StringUtils.isEmpty(transBounsVo.getUserId())) {
					transBounsVo.setUserId(userId);
				}
				transBounsVo.setTransNum(transNum);
				result = transBounsService.insertTransBouns(transBounsVo);
				break;
			case TransTypeUtil.CANCEL_AUTH_ACCOUNT_PARAMETER_CODE:
				// 終止授權
				CancelAuthVo cancelAuthVo = apiReq.getCancelAuthVo();
				if (StringUtils.isEmpty(cancelAuthVo.getUserId())) {
					cancelAuthVo.setUserId(userId);
				}
				cancelAuthVo.setTransNum(transNum);
				result = cancelAuthService.insertTransCancelAuth(cancelAuthVo);
				break;
			case TransTypeUtil.CANCEL_CONTRACT_PARAMETER_CODE:
				// 解約
				TransCancelContractVo transCancelContractVo = apiReq.getTransCancelContractVo();
				if (StringUtils.isEmpty(transCancelContractVo.getUserId())) {
					transCancelContractVo.setUserId(userId);
				}
				transCancelContractVo.setTransNum(transNum);
				result = transCancelContractService.insertTransCancelContract(transCancelContractVo);
				break;
			case TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE:
				// 變更信用卡效期
				TransCreditCardDateVo transCreditCardDateVo = apiReq.getTransCreditCardDateVo();
				if (StringUtils.isEmpty(transCreditCardDateVo.getUserId())) {
					transCreditCardDateVo.setUserId(userId);
				}
				transCreditCardDateVo.setTransNum(transNum);
				result = transCreditCardDateService.insertTransCreditCardDate(transCreditCardDateVo);
				break;
			case TransTypeUtil.CUSHION_PARAMETER_CODE:
				// 自動墊繳選擇權
				TransCushionVo transCushionVo = apiReq.getTransCushionVo();
				if (StringUtils.isEmpty(transCushionVo.getUserId())) {
					transCushionVo.setUserId(userId);
				}
				transCushionVo.setTransNum(transNum);
				result = transCushionService.insertTransCushion(transCushionVo);
				break;
			case TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE:
				// 設定停損停利通知
				TransFundNotificationVo transFundNotificationVo = apiReq.getTransFundNotificationVo();
				if (StringUtils.isEmpty(transFundNotificationVo.getUserId())) {
					transFundNotificationVo.setUserId(userId);
				}
				transFundNotificationVo.setTransNum(transNum);
				result = transFundNotificationService.insertTransFundNotification(transFundNotificationVo);
				break;
			case TransTypeUtil.LOAN_PARAMETER_CODE:
				// 申請保單貸款
				TransLoanVo transLoanVo = apiReq.getTransLoanVo();
				if (StringUtils.isEmpty(transLoanVo.getUserId())) {
					transLoanVo.setUserId(userId);
				}
				transLoanVo.setTransNum(transNum);
				result = transLoanService.insertTransLoan(transLoanVo);
				break;
			case TransTypeUtil.LOAN_NEW_PARAMETER_CODE:
				// 申請保單貸款(已申請指定帳號)
				TransLoanVo transLoanNewVo = apiReq.getTransLoanVo();
				if (StringUtils.isEmpty(transLoanNewVo.getUserId())) {
					transLoanNewVo.setUserId(userId);
				}
				transLoanNewVo.setTransNum(transNum);
				List<TransExtendAttrVo> list = apiReq.getTransExtendAttrList();
				result = transLoanService.insertTransLoanNew(transLoanNewVo, list);
				break;
			case TransTypeUtil.MATURITY_PARAMETER_CODE:
				// 滿期
				MaturityVo maturityVo = apiReq.getMaturityVo();
				if (StringUtils.isEmpty(maturityVo.getUserId())) {
					maturityVo.setUserId(userId);
				}
				maturityVo.setTransNum(transNum);
				result = maturityService.insertTransMaturity(maturityVo);
				break;
			case TransTypeUtil.PAYMETHOD_PARAMETER_CODE:
				// 變更收費管道
				TransPaymethodVo transPaymethodVo = apiReq.getTransPaymethodVo();
				if (StringUtils.isEmpty(transPaymethodVo.getUserId())) {
					transPaymethodVo.setUserId(userId);
				}
				transPaymethodVo.setTransNum(transNum);
				result = transPaymethodService.insertTransPaymethod(transPaymethodVo);
				break;
			case TransTypeUtil.PAYMODE_PARAMETER_CODE:
				// 變更繳別
				TransPaymodeVo transPaymodeVo = apiReq.getTransPaymodeVo();
				if (StringUtils.isEmpty(transPaymodeVo.getUserId())) {
					transPaymodeVo.setUserId(userId);
				}
				transPaymodeVo.setTransNum(transNum);
				result = transPaymodeService.insertTransPaymode(transPaymodeVo);
				break;
			case TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE:
				// 減少保險金額
				TransReducePolicyVo transReducePolicyVo = apiReq.getTransReducePolicyVo();
				if (StringUtils.isEmpty(transReducePolicyVo.getUserId())) {
					transReducePolicyVo.setUserId(userId);
				}
				transReducePolicyVo.setTransNum(transNum);
				result = transReducePolicyService.insertTransReducePolicy(transReducePolicyVo);
				break;
			case TransTypeUtil.RENEW_PARAMETER_CODE: case TransTypeUtil.REDUCE_PARAMETER_CODE:
				// 變更展期定期保險/減額繳清保險
				TransRenewReduceVo transRenewReduceVo = apiReq.getTransRenewReduceVo();
				if (StringUtils.isEmpty(transRenewReduceVo.getUserId())) {
					transRenewReduceVo.setUserId(userId);
				}
				transRenewReduceVo.setTransNum(transNum);
				result = transRenewReduceService.insertTransRenewReduce(transRenewReduceVo);
				break;
			case TransTypeUtil.REWARD_PARAMETER_CODE:
				// 變更增值回饋分享金領取方式
				TransRewardVo transRewardVo = apiReq.getTransRewardVo();
				if (StringUtils.isEmpty(transRewardVo.getUserId())) {
					transRewardVo.setUserId(userId);
				}
				transRewardVo.setTransNum(transNum);
				result = transRewardService.insertTransReward(transRewardVo);
				break;
			case TransTypeUtil.SURRENDER_PARAMETER_CODE:
				// 紅利(儲存生息)/增值之提領
				TransSurrenderVo transSurrenderVo = apiReq.getTransSurrenderVo();
				if (StringUtils.isEmpty(transSurrenderVo.getUserId())) {
					transSurrenderVo.setUserId(userId);
				}
				transSurrenderVo.setTransNum(transNum);
				result = transSurrenderService.insertTransSurrender(transSurrenderVo);
				break;
			case TransTypeUtil.CERT_PRINT_PARAMETER_CODE:
				// 投保證明列印
				TransCertPrintVo transCertPrintVo = apiReq.getTransCertPrintVo();
				if (StringUtils.isEmpty(transCertPrintVo.getUserId())) {
					transCertPrintVo.setUserId(userId);
				}
				transCertPrintVo.setTransNum(transNum);
				result = transCertPrintService.insertTransCertPrint(transCertPrintVo, apiReq.getTransExtendAttrList());
				break;
			case TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE:
				// 匯款帳號變更
				TransChangeAccountVo transChangeAccountVo = apiReq.getTransChangeAccountVo();
				if (StringUtils.isEmpty(transChangeAccountVo.getUserId())) {
					transChangeAccountVo.setUserId(userId);
				}
				transChangeAccountVo.setTransNum(transNum);
				result = transChangeAccountService.insertTransChangeAccount(transChangeAccountVo);
				break;
			case TransTypeUtil.CONTACT_INFO_PARAMETER_CODE:
				// 變更保單聯絡資料
				TransContactInfoDtlVo transContactInfoDtlVo = apiReq.getTransContactInfoDtlVo();
				if (StringUtils.isEmpty(transContactInfoDtlVo.getUserId())) {
					transContactInfoDtlVo.setUserId(userId);
				}
				TransContactInfoVo transContactInfoVo = apiReq.getTransContactInfoVo();
				// 狀態歷程
				TransStatusHistoryVo cHisVo = new TransStatusHistoryVo();
				cHisVo.setCustomerName("系統日程");
				cHisVo.setIdentity("SYS_TASK");
				
				//有註冊eservice-才能由Users Table取客資
				//未註冊eservice-其mobile,email拿聯盟傳來的cphone,cmail
				TransContactInfoOldVo transContactInfoOldVo = apiReq.getTransContactInfoOldVo();
				com.twfhclife.eservice.web.model.UsersVo usersVo = null; 
				if(transContactInfoOldVo!=null) {
					usersVo = usersDao.getUserByRocId(transContactInfoOldVo.getIdNo());
				}
				if(usersVo!=null) {
					//do nothing.
				}else {
					usersVo = new com.twfhclife.eservice.web.model.UsersVo();
					if(transContactInfoOldVo!=null) {
						usersVo.setMobile(transContactInfoOldVo.getMobile());
						usersVo.setEmail(transContactInfoOldVo.getEmail());
					}
				}
				cHisVo.setUsersVo(usersVo);

				Map<String,Object> rMapCIO = transContactInfoService.insertTransContactInfo(transContactInfoVo, transContactInfoDtlVo, cHisVo);
				MessageTriggerRequestVo voCIO = new MessageTriggerRequestVo();
				result = (int) rMapCIO.get("result");
				String transNums = (String) rMapCIO.get("transNums");
				String statusCIO = (String) rMapCIO.get("status");
				//获取所有的保单
				String policyNo = (String) rMapCIO.get("policyNo");
				//轉收至本公司的案件資料成功存入本公司系統，並以電子郵件通知後台管理人員
				//logger.info("轉收至本公司的案件資料及影像成功存入本公司系統-發送郵件---保單單號"+	policyNo);
				//iContactInfoService.forwardingCaseImageSuccessMail(policyNo);//轉'資料'進eservice時,不加發信件給後台管理人員
				if(result > 0) {
					logger.info("Start send mail");
					String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_AMD, "security_alliance_twfhclife_adm");
					String[] mails = mailTo.split(";");
					String statusName  = parameterDao.getStatusName(ApConstants.ONLINE_CHANGE_STATUS, statusCIO);
					String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, "CONTACT_INFO_TRANS_REMARK");
					String transRemarkAbnormal = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, "CONTACT_INFO_TRANS_ABNORMAL_REMARK");
					
					logger.info("Mail Address : " + mailTo);
					logger.info("Status Name : " + statusName);
					logger.info("Trans Remark : " + transRemark);
					logger.info("Trans Remark abnormal: " + transRemarkAbnormal);
					
					Map<String, String> paramMap = new HashMap<String, String>();
					List<String> receivers = new ArrayList<String>();
					if(mails!=null && mails.length > 0) {
						Collections.addAll(receivers, mails);//for receivers.clear()
					}
					logger.info("security_alliance_twfhclife_adm mail receivers:" + receivers.toString());
					long timeMillis = System.currentTimeMillis();
					SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
					String loginTime = formater.format(timeMillis);
					
					//異常件
					if(ApConstants.TRANS_STATUS_ABNORMAL.equals(statusCIO)) {
						//發送系統管理員
//						paramMap.put("TransNum", transNums);
//						paramMap.put("TransStatus", statusName);
//						paramMap.put("TransRemark", transRemarkAbnormal);
//						paramMap.put("LoginTime", loginTime);
						/**
						 * 「已註記異常件」的處理方式，通知管理人員內容
						 * */
						//voCIO = getMessageTriggerRequestVo(ApConstants.ELIFE_EMAIL_008, receivers, paramMap, "email");
						String email = apiReq.getTransContactInfoDtlVo().getEmail();
						boolean b = ValidateUtil.notNULL(email);
						paramMap.put("EMAIL",b? EMailTemplate.INSURED_NOT_EMAIL_NULL:EMailTemplate.INSURED_EMAIL_IS_NULL);
						paramMap.put("DATA",CallApiDateFormatUtil.getTimeMillisToString(timeMillis,"yyyyMMddHHmmss"));
						String caseId = transContactInfoVo.getCaseId();
						paramMap.put("CACE_NUMBER",caseId);
						voCIO = getMessageTriggerRequestVo(ApConstants.TRANSFER_MAIL_018, receivers, paramMap, "email");
						String resultSYSMailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
						logger.info("發送系統管理員RESULT : " + resultSYSMailMsg);
						
						//發送保戶SMS
						receivers.clear();
						paramMap.put("DATA",CallApiDateFormatUtil.getCurrenTimeMillisString(timeMillis));
						receivers.add(transContactInfoDtlVo.getMobile());
						voCIO = getMessageTriggerRequestVo(ApConstants.ELIFE_SMS_008, receivers, paramMap, "sms");
						String resultUserSMSMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
						logger.info("發送保戶SMS : " + resultUserSMSMsg);

						/**
						 * 「已註記異常件」的處理方式，通知保戶內容
						 * */
						if (b) {
							receivers.clear();
							receivers.add(email);
							voCIO = getMessageTriggerRequestVo(ApConstants.TRANSFER_MAIL_019, receivers, paramMap, "email");
							String resultInsuredMailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
							logger.info("發送通知保戶內容RESULT : " + resultInsuredMailMsg);
						}else {
							logger.info("*************保戶未留E-Mail，需管理者電洽保戶**********" );
						}

					}else {
						paramMap.put("TransNum", transNums);
						paramMap.put("TransStatus", statusName);
						paramMap.put("TransRemark", transRemark);
						paramMap.put("LoginTime", loginTime);
						//保单编号
						paramMap.put("POLICY_NUMBER", policyNo);

						//發送系統管理員
						//voCIO = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_007, receivers, paramMap, "email");
						//轉收件通知中的本次新變更email
						voCIO = getMessageTriggerRequestVo(ApConstants.TRANSFER_MAIL_014, receivers, paramMap, "email");
						String resultSYS_MailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
						logger.info("發送系統管理員 : " + resultSYS_MailMsg);
						
						//發送保戶SMS
						receivers.clear();
						receivers.add(transContactInfoDtlVo.getMobile());
						voCIO = getMessageTriggerRequestVo(ApConstants.ELIFE_SMS_007, receivers, paramMap, "sms");
						String resultUser_SMS_Msg = messagingTemplateService.triggerMessageTemplate(voCIO);
						logger.info("發送保戶SMS : " + resultUser_SMS_Msg);
						
						//發送保戶MAIL
						receivers.clear();
						if(transContactInfoDtlVo.getEmail()!=null) {
							receivers.add(transContactInfoDtlVo.getEmail());
						}
						if(transContactInfoOldVo.getEmail()!=null) {
							receivers.add(transContactInfoOldVo.getEmail());
						}
						//voCIO = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_007, receivers, paramMap, "email");
						//轉收件通知中的本次新變更email
						voCIO = getMessageTriggerRequestVo(ApConstants.TRANSFER_MAIL_015, receivers, paramMap, "email");
						String resultUser_MailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
						//messageTemplateClient.sendNoticeViaMsgTemplate(ApConstants.ELIFE_MAIL_005, receivers, paramMap, "email");
						logger.info("發送保戶MAIL : " + resultUser_MailMsg);
						
					}
					logger.info("End send mail");
				}
				break;
			case TransTypeUtil.POLICY_RESEND_PARAMETER_CODE:
				// 補發保單
				TransResendPolicyVo transResendPolicyVo = apiReq.getTransResendPolicyVo();
				if (StringUtils.isEmpty(transResendPolicyVo.getUserId())) {
					transResendPolicyVo.setUserId(userId);
				}
				transResendPolicyVo.setTransNum(transNum);
				result = transResendPolicyService.insertTransResendPolicy(transResendPolicyVo);
				break;
			case TransTypeUtil.VALUE_PRINT_PARAMETER_CODE:
				// 保單價值列印
				TransValuePrintVo transValuePrintVo = apiReq.getTransValuePrintVo();
				if (StringUtils.isEmpty(transValuePrintVo.getUserId())) {
					transValuePrintVo.setUserId(userId);
				}
				transValuePrintVo.setTransNum(transNum);
				result = transValuePrintService.insertTransValuePrint(transValuePrintVo);
				break;
			case TransTypeUtil.RISK_LEVEL_PARAMETER_CODE:
				// 變更風險屬性
				TransRiskLevelVo transRiskLevelVo = apiReq.getTransRiskLevelVo();
				if (StringUtils.isEmpty(transRiskLevelVo.getUserId())) {
					transRiskLevelVo.setUserId(userId);
				}
				transRiskLevelVo.setTransNum(transNum);
				result = transRiskLevelService.insertTransRiskLevel(transRiskLevelVo);
				break;
			case TransTypeUtil.POLICY_HOLDER_PROFILE_PARAMETER_CODE:
				// 保戶基本資料更新
				TransPolicyHolderProfileVo transPolicyHolderProfileVo = apiReq.getTransPolicyHolderProfileVo();
				if (StringUtils.isEmpty(transPolicyHolderProfileVo.getUserId())) {
					transPolicyHolderProfileVo.setUserId(userId);
				}
				result = policyHolderProfileService.insertPolicyHolderProfile(transPolicyHolderProfileVo);
				break;
			case TransTypeUtil.FUND_SWITCH_PARAMETER_CODE:
				// 變更投資標的及配置比例
				TransFundSwitchVo transFundSwitchVo = apiReq.getTransFundSwitchVo();
				if (StringUtils.isEmpty(transFundSwitchVo.getUserId())) {
					transFundSwitchVo.setUserId(userId);
				}
				transFundSwitchVo.setTransNum(transNum);
				result = transFundSwitchService.insertTransFundSwitch(transFundSwitchVo);
				break;
			case TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE:
				//保單理賠
				TransInsuranceClaimVo tic = apiReq.getTransInsuranceClaimVo();
				if(tic.getTransNum()==null) {
					//AllianceServiceTask塞過來的資料會先塞好TransNum
					tic.setTransNum(transNum);
				}
				// 狀態歷程
				TransStatusHistoryVo hisVo = new TransStatusHistoryVo();
				hisVo.setCustomerName("系統日程");
				hisVo.setIdentity("SYS_TASK");

				Map<String, Object> rMap = insuranceClaimService.insertTransInsuranceClaim(tic,hisVo);
				logger.info("***after insuranceClaimService.insertTransInsuranceClaim()***");
				if(rMap!=null) {
					logger.info("result="+Arrays.asList(rMap));
				}else {
					logger.info("result is null.");
				}

				MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
				result = (int) rMap.get("result");
				String status = (String) rMap.get("status");
				if(result > 0) {
					logger.info("Start send mail");
					String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_AMD, ApConstants.TWFHCLIFE_ADM);
					String[] mails = mailTo.split(";");
					String statusName = parameterDao.getStatusName(ApConstants.ONLINE_CHANGE_STATUS, status);
					String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INSURANCE_CLAIM_TRANS_REMARK);
					String abnormalTransRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, "INSURANCE_CLAIM_ABNORMAL_TRANS_REMARK");
					logger.info("Mail Address : " + mailTo);
					logger.info("Status  Name : " + statusName);
					logger.info("Trans Remark : " + transRemark);
					logger.info("Trans Remark abnormalTransRemark: " + abnormalTransRemark);
					Map<String, String> paramMap = new HashMap<String, String>();
					List<String> receivers = new ArrayList<String>();
					if(mails.length > 0) {
						for (String mail : mails) {
							receivers.add(mail);
							logger.info("Mail Address : " + mail);
						}
					}
				
					logger.info("**start to send email/sms to sysAdmin,customer**");
					SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
					String loginTime = null;
					if(tic.getCreateDate()==null) {
						loginTime = formater.format(System.currentTimeMillis());
					}else {
						loginTime = formater.format(tic.getCreateDate());
					}
					
					//異常件
					if(ApConstants.TRANS_STATUS_ABNORMAL.equals(status)) {
						paramMap.put("TransNum", tic.getTransNum());
						//paramMap.put("TransStatus", statusName);
						//paramMap.put("TransRemark", abnormalTransRemark);

						//發送系統管理員
						logger.info("***發送系統管理員_start");
						paramMap.put("LoginTime", loginTime);
					   // vo = getMessageTriggerRequestVo(ApConstants.ELIFE_EMAIL_006, receivers, paramMap, "email");
					    vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_026, receivers, paramMap, "email");
						String resultSYSMailMsg = messagingTemplateService.triggerMessageTemplate(vo);
						logger.info("發送系統管理員RESULT : " + resultSYSMailMsg);

						logger.info("***發送系統保戶通知_start***********");
						receivers.clear();
						paramMap.put("TransStatus", statusName);
						receivers.add(tic.getMail());
						vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_023, receivers, paramMap, "email");
						String resultInsuredMailMsg = messagingTemplateService.triggerMessageTemplate(vo);
						logger.info("發送系統保戶通知RESULT : " + resultInsuredMailMsg);

						//發送保戶SMS
						logger.info("***發送保戶SMS_start");
						receivers.clear();
						receivers.add(tic.getPhone());
						paramMap.put("TransRemark", abnormalTransRemark);
						vo = getMessageTriggerRequestVo(ApConstants.ELIFE_SMS_006, receivers, paramMap, "sms");
						String resultUserSMSMsg = messagingTemplateService.triggerMessageTemplate(vo);
						logger.info("***發送保戶SMS : " + resultUserSMSMsg);

					}else {
						paramMap.clear();
						paramMap.put("TransNum", tic.getTransNum());
					//	paramMap.put("TransStatus", statusName);
					//	paramMap.put("TransRemark", transRemark);
						
						/**
						 * EMAIL
						 */
						//發送系統管理員
						logger.info("***發送系統管理員 _start");
						paramMap.put("LoginTime", loginTime);
						//vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_005, receivers, paramMap, "email");
						//使用新郵件範本
						vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_026, receivers, paramMap, "email");
						String resultSYS_MailMsg = messagingTemplateService.triggerMessageTemplate(vo);
						logger.info("***發送系統管理員 : " + resultSYS_MailMsg);
						
						//發送保戶MAIL
						logger.info("***發送保戶MAIL_start");
						receivers.clear();
						receivers.add(tic.getMail());
						paramMap.put("TransStatus", statusName);
						//vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_005, receivers, paramMap, "email");
						//使用新郵件範本
						vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_023, receivers, paramMap, "email");
						String resultUser_MailMsg = messagingTemplateService.triggerMessageTemplate(vo);
						//messageTemplateClient.sendNoticeViaMsgTemplate(ApConstants.ELIFE_MAIL_005, receivers, paramMap, "email");
						logger.info("發送保戶MAIL : " + resultUser_MailMsg);
						
						/**
						 * SMS
						 */
						//發送保戶SMS
						logger.info("***發送保戶SMS-start");
						receivers.clear();
						receivers.add(tic.getPhone());
						paramMap.put("TransRemark", transRemark);
						vo = getMessageTriggerRequestVo(ApConstants.ELIFE_SMS_005, receivers, paramMap, "sms");
						String resultUser_SMS_Msg = messagingTemplateService.triggerMessageTemplate(vo);
						logger.info("***發送保戶SMS : " + resultUser_SMS_Msg);
						
					}
					logger.info("End send mail");
				}else {
					logger.info("result<=0,no send mail/sms.");
				}
				
				break;
			case TransTypeUtil.DNS_PARAMETER_CODE:
				TransDnsVo transDnsVo = apiReq.getTransDnsVo();
				if(transDnsVo.getTransNum()==null) {
					//AllianceServiceTask塞過來的資料會先塞好TransNum
					transDnsVo.setTransNum(transNum);
				}
				// 狀態歷程
				TransStatusHistoryVo dnsHisVo = new TransStatusHistoryVo();
				dnsHisVo.setCustomerName("系統日程");
				dnsHisVo.setIdentity("SYS_TASK");
				
				transDnsVo.setSeqId(null);//transDnsServiceImpl.insertTransDns will get a new seqId
				Map<String, String> mapResult = transDnsServiceImpl.insertTransDns(transDnsVo, dnsHisVo);
				if(mapResult.get("INSERT_RESULT")!=null) {
					try {
						result = Integer.parseInt((String)mapResult.get("INSERT_RESULT"));
					}catch(Exception e) {
						logger.error(e);
					}
				}
				
				//發送系統通知信件-start
				logger.info("**start send sysAdmin Email.**");
				Map<String, String> paramMap = new HashMap<String, String>();//ELIFE_MAIL_DNSxxxx has no ${}
				
				List<String> receivers = new ArrayList<String>();
				String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_AMD, "dns_alliance_twfhclife_adm");
				String[] mails = mailTo.split(";");
				if(mails!=null && mails.length>0) {
					receivers = Arrays.asList(mails);
				}
				logger.info("Trans DNS send sysadmin email:"+receivers.toString());
				
				MessageTriggerRequestVo dnsMsgTrigReqVo = new MessageTriggerRequestVo();
				String resultUser_MailMsg = "";
				if(result > 0) {//轉存TRANS,TRANS_POLICY,TRANS_DNS成功
					logger.info("Trans DNS send sysadmin success email.");
					//發送MAIL:系統理員
					dnsMsgTrigReqVo = getMessageTriggerRequestVo("ELIFE_MAIL_DNS_OK", receivers, paramMap, "email");
					resultUser_MailMsg = messagingTemplateService.triggerMessageTemplate(dnsMsgTrigReqVo);
					
				}else {//轉存失敗
					logger.info("Trans DNS send sysadmin fail email.");
					//發送MAIL:系統理員
					dnsMsgTrigReqVo = getMessageTriggerRequestVo("ELIFE_MAIL_DNS_FAIL", receivers, paramMap, "email");
					resultUser_MailMsg = messagingTemplateService.triggerMessageTemplate(dnsMsgTrigReqVo);
				}
				logger.info("發送MAIL系統理員：" + resultUser_MailMsg);
				logger.info("**end send sysAdmin Email.**");
				//發送系統通知信件-start
				
				break;
			
			}
			
			transAddResponse.setTransResult(
					(result <= 0 ? ReturnHeader.FAIL_CODE : ReturnHeader.SUCCESS_CODE));
		} catch (Exception e) {
			/**
			 * 在轉修eservice進件過程中有出程錯誤時通知後台管理人員
			 * */
			//發送MAIL:系統理員
			callFailedMail(e);
			logger.error("Unable to addTransRequest: {}", ExceptionUtils.getStackTrace(e));
			transAddResponse.setTransResult(ReturnHeader.FAIL_CODE);
			transAddResponse.setTransResultMsg(String.format("Unable to addTransRequest: %s", e.getMessage()));
		}
		return transAddResponse;
	}

	private  void  callFailedMail(Exception e){
		try {
			Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			paramMap.put("PROGRAM_NAME", stackTraceElement.getClassName()+":"+stackTraceElement.getMethodName());
			paramMap.put("NUMBER", new String().valueOf(stackTraceElement.getLineNumber()));
			paramMap.put("DATA", CallApiDateFormatUtil.getCurrentTimeString());
			paramMap.put("EXCEPTION_LOG", e.getMessage());
			//發送系統管理員
			List<String> receivers = new ArrayList<String>();
			receivers = (List)mailInfo.get("receivers");

			MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
			vo.setMessagingTemplateCode(ApConstants.TRANSFER_MAIL_013);
			vo.setSendType("email");
			vo.setMessagingReceivers(receivers);
			vo.setParameters(paramMap);
			vo.setSystemId(ApConstants.SYSTEM_ID);
			logger.info("MessageTriggerRequestVo="+vo);
			//进行发送通信
			String resultMsg = messagingTemplateService.triggerMessageTemplate(vo);
			if(MyStringUtil.isNullOrEmpty(resultMsg)) {
				logger.info("Send message successfully complete.");
			} else {
				logger.info("Failed to send message successfully");
			}
		}catch (Exception ex){
			logger.error("Failed to send message successfully:"+ex.getMessage());
		}
	}

	private MessageTriggerRequestVo getMessageTriggerRequestVo(String msgCode, List<String> receivers, Map<String, String> paramMap,String type) {
		MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
		vo.setMessagingTemplateCode(msgCode);
		vo.setSendType(type);
		vo.setMessagingReceivers(receivers);
		vo.setParameters(paramMap);
		vo.setSystemId(ApConstants.SYSTEM_ID);
		return vo;
	}
	
	public static void main(String[] args) {
		 java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
		//String loginTime = formater.format(timestamp);
		String loginTime = formater.format(System.currentTimeMillis());
		System.out.println(loginTime);
		
	}
}
