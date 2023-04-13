package com.twfhclife.eservice.onlineChange.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransElectronicFormDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.ContractRevocationVo;
import com.twfhclife.eservice.onlineChange.model.ElectronicFormVo;
import com.twfhclife.eservice.onlineChange.model.TransElectronicFormVo;
import com.twfhclife.eservice.onlineChange.service.ITransElectronicFormService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.TransCtcLipmdaVo;
import com.twfhclife.generic.api_model.TransCtcSelectDataResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataVo;
import com.twfhclife.generic.api_model.TransCtcSelectDetailResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDetailVo;
import com.twfhclife.generic.api_model.TransCtcSelectUtilRequest;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;

@Service
public class TransElectronicFormServiceImpl implements ITransElectronicFormService{
	
    private static final Logger logger = LogManager.getLogger(TransChangePremiumServiceImpl.class);

	
	@Autowired
	private TransElectronicFormDao dao;
	
    @Autowired
    private TransDao transDao;
    
    @Autowired
    private TransPolicyDao transPolicyDao ;
    
    @Autowired
    private ParameterDao parameterDao;
    
	@Autowired
	private IParameterService parameterService;
	
    @Autowired
    private MessageTemplateClient messageTemplateClient;

	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;
	
	private final static String COVENANT = "ELECTRONIC_FORM_COVENANT";
	
	private final static String STRING_FORM_DATE = "yyyy/MM/dd hh:mm:ss";
	
	private final static String STRING_DATE = "yyyy-MM-dd hh:mm:ss";
	
	@Override
	public int insertElectronicForm(String transNum, ElectronicFormVo electronicFormVo, String userId ,String policyNo , UsersVo userVo)
			throws Exception {
		int result = 0;
		try {
				// 新增線上申請主檔
				TransVo transVo = new TransVo();
				transVo.setTransNum(transNum);
				transVo.setTransDate(new Date());
				if(electronicFormVo.getElectronicFormStatus().equals("application")) {
					transVo.setTransType(TransTypeUtil.ELECTRONIC_FORM_A_CODE);
				}else {
					transVo.setTransType(TransTypeUtil.ELECTRONIC_FORM_C_CODE);
				}

				transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_RECEIVED);
				transVo.setUserId(userId);
				transVo.setCreateUser(userId);
				transVo.setCreateDate(new Date());
				transDao.insertTrans(transVo);

				//新增申請的保險單號
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
				//新增要申請的電子表單狀態
				TransElectronicFormVo tefVo = new TransElectronicFormVo();
				if (electronicFormVo.getElectronicFormStatus().equals("application")) {
					tefVo.setTransNum(transNum);
					tefVo.seteInfoN("Y");
				} else {
					tefVo.setTransNum(transNum);
					tefVo.seteInfoN("N");
					tefVo.setOldEInfoN("Y");
				}
				for(TransCtcLipmdaVo clipmdaVo : electronicFormVo.getClopmdaVo()) {
					if(clipmdaVo.getLipmInsuNo().equals(policyNo)) {
						tefVo.setEmail(clipmdaVo.getPmdaApplicantEmail());
					}
				}
				dao.insertTransElectronicForm(tefVo);
                sendNotification(tefVo,userVo , policyNo);	
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertElectronicForm: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	
	@Override
    public Map<String,Object> getSendMailInfo() {
        String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INVESTMENT_TRANS_REMARK);
        String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ADM, OnlineChangeUtil.TWFHCLIFE_ELECTRONIC_ADM);
        String[] mails = mailTo.split(";");
        Map<String,Object> rMap = new HashMap<String,Object>();
        List<String> receivers = new ArrayList<String>();
        if(mails.length > 0) {
            for (String mail : mails) {
                receivers.add(mail);
                logger.info("Mail Address : " + mail);
            }
        }
        rMap.put("transRemark", transRemark);
        rMap.put("receivers", receivers);
        return rMap;
    }
	
	 public void sendNotification(TransElectronicFormVo transElectronicFormVo, UsersVo user , String policyNo) {
	        try {
	            Map<String, Object> mailInfo = getSendMailInfo();
	            Map<String, String> paramMap = new HashMap<String, String>();
	            paramMap.put("TransNum", transElectronicFormVo.getTransNum());
	            paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
	            paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
	            logger.info("Trans Num : {}", transElectronicFormVo.getTransNum());
	            logger.info("Status Name : {}", mailInfo.get("statusName"));
	            logger.info("Trans Remark : {}", mailInfo.get("transRemark"));
	            logger.info("receivers={}", mailInfo.get("receivers"));
	            logger.info("user phone : {}", user.getMobile());
	            logger.info("user mail : {}", user.getEmail());
	            //获取保单编号
	            paramMap.put("POLICY_NO", policyNo);
	            logger.info("POLICY_NO : {}", policyNo);

	            List<String> receivers = new ArrayList<String>();

	            String applyDate = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
	            paramMap.put("DATA", applyDate);
	            //申請狀態-申請中
	            paramMap.put("TransStatus","已審核");
	            //申請功能
	            ParameterVo parameterValueByCode  = new ParameterVo();
	            if(transElectronicFormVo.geteInfoN().equals("Y")) {
		            parameterValueByCode = parameterService.getParameterByParameterValue(
		                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.ELECTRONIC_FORM_A_CODE);
	            }else {
		            parameterValueByCode = parameterService.getParameterByParameterValue(
		                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.ELECTRONIC_FORM_C_CODE);
	            }
	            paramMap.put("APPLICATION_FUNCTION", parameterValueByCode.getParameterName());

	            //發送系統管理員
	            receivers = (List) mailInfo.get("receivers");
	            //推送管 理已接收 保單編號: [保單編號]  保戶[同意/不同意]轉送聯盟鏈
	            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_030, receivers, paramMap, "email");

	            //發送保戶SMS
	            receivers.clear();//清空
	            paramMap.clear();//清空模板參數
	            if(parameterValueByCode.getParameterValue().equals(TransTypeUtil.ELECTRONIC_FORM_A_CODE)) {
	            	 //設置模板參數
		            receivers.add(user.getMobile());
		            logger.info("user phone : {}", user.getMobile());
		            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_029, receivers, paramMap, "sms");
		            //發送保戶MAIL
		            if (user.getEmail() != null) {
		                receivers.clear();//清空
		                receivers.add(user.getEmail());
		                logger.info("user mail : {}", user.getEmail());
		                messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_029, receivers, paramMap, "email");	                
		            }
	            }else {
	            	 //設置模板參數
		            receivers.add(user.getMobile());
		            logger.info("user phone : {}", user.getMobile());
		            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_036, receivers, paramMap, "sms");
		            //發送保戶MAIL
		            if (user.getEmail() != null) {
		                receivers.clear();//清空
		                receivers.add(user.getEmail());
		                logger.info("user mail : {}", user.getEmail());
		                messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_036, receivers, paramMap, "email");	                
		            }
	            }	           
	        } catch (Exception e) {
	            logger.info("insertTransInvestment() success, but send notify mail/sms error.");
	        }
	        logger.info("End send mail");
	    }


	@Override
	public List<ElectronicFormVo> getPolicyDetail(String lipmId, String userId , String transType) {
		List<ElectronicFormVo> electronicFormVoList = new ArrayList<ElectronicFormVo>();
		TransCtcSelectUtilRequest transCtcSelectUtilRequest = new TransCtcSelectUtilRequest();
		transCtcSelectUtilRequest.setLipmId(lipmId);
		TransCtcSelectDataResponse resp = transCtcSelectUtilClient.getTransCtcSelectDataByLipmId(transCtcSelectUtilRequest);
		
		for (TransCtcSelectDataVo vo : resp.getSelectDataList()) {
			ElectronicFormVo electronicFormVo = new ElectronicFormVo();
			//保單
			electronicFormVo.setPolicyNo(vo.getLipmInsuNo());
			//要保人	
			electronicFormVo.setCustomerName(vo.getLipmName1());
			//主被保險人	
			electronicFormVo.setMainInsuredName(vo.getLipiName());
			//保單生效日
			electronicFormVo.setEffectiveDate(getStringDate(vo.getLipmInsuBegDate()));
			//每期保費	
			BigDecimal amount = new BigDecimal(vo.getLipiTablPrem());			
			electronicFormVo.setPaidAmount(amount);
			//繳別	
			electronicFormVo.setPaymentMode(vo.getLipmRcpCode());
			//開通狀態
			electronicFormVo.setPmdaEInfoNStatus(vo.getPmdaEInfoN());	
			//契約狀況
			electronicFormVo.setLipmSt(vo.getLipmSt());
			//保單名稱
			electronicFormVo.setSettChName(vo.getSettChName());
			electronicFormVoList.add(electronicFormVo);
		}
		//取得不可申請的契約狀態
		String covenant = parameterDao.getParameterValueByCode("eservice", COVENANT);
		String [] covenantList = covenant.split(",");
		for(ElectronicFormVo vo : electronicFormVoList) {
			if (!Arrays.asList(covenantList).contains(vo.getLipmSt())) {
				vo.setApplyLockedFlag("Y");
				vo.setApplyLockedMsg(OnlineChangMsgUtil.TRANS_ELECTRONIC_FORM_STATUS);
			}
		}
		// 取得目前申請中的保單號碼
		List<String> applyPolicyNos = transPolicyDao.getProgressPolicyNoList(userId, transType);
		if (!CollectionUtils.isEmpty(applyPolicyNos)) {
			for (ElectronicFormVo vo : electronicFormVoList) {
				// 保單未到期但已經有在申請中
				if (StringUtils.isEmpty(vo.getApplyLockedFlag())) {
					if (applyPolicyNos.contains(vo.getPolicyNo())) {
						vo.setApplyLockedFlag("Y");
						// 前次申請尚在處理中，暫無法進行此項申請
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
					}
				} else {
					if (!vo.getApplyLockedFlag().equals("Y")) {
						if (applyPolicyNos.contains(vo.getPolicyNo())) {
							vo.setApplyLockedFlag("Y");
							// 前次申請尚在處理中，暫無法進行此項申請
							vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
						}
					}
				}
			}
		}	
		
		return electronicFormVoList;
	}
	
	public String getStringDate(String date) {
		try {
			if (StringUtils.isNotEmpty(date)) {
				SimpleDateFormat formater = new SimpleDateFormat(STRING_FORM_DATE);
				return DateUtil.getRocDate(formater.parse(date));
			}
		} catch (ParseException e) {
			SimpleDateFormat formater = new SimpleDateFormat(STRING_DATE);
			try {
				return DateUtil.getRocDate(formater.parse(date));
			} catch (ParseException e1) {
				return null;
			}
		}
		return null;
	}


	@Override
	public  HashMap getUserCurrentNetworkData(String policyNo) {
		TransCtcSelectUtilRequest request = new TransCtcSelectUtilRequest();
		request.setLipmInsuSeqNo(policyNo);
		TransCtcSelectDetailResponse response = new TransCtcSelectDetailResponse();
		response = transCtcSelectUtilClient.getTransCtcSelectDetailByLipmInsuSeqNo(request);
		List<TransCtcSelectDetailVo> transCtcSelectDetailVoList = new ArrayList<TransCtcSelectDetailVo>();
		transCtcSelectDetailVoList = response.getSelectDetailList();
		TransCtcSelectDetailVo transCtcSelectDetailVo = new TransCtcSelectDetailVo();
		transCtcSelectDetailVo = transCtcSelectDetailVoList.get(0);
		HashMap map = new HashMap();
		map.put("NAME", transCtcSelectDetailVo.getLipmName1());
		map.put("LIPM_TEL_H", transCtcSelectDetailVo.getLipmTelH());
		map.put("LIPM_TEL_O", transCtcSelectDetailVo.getLipmTelO());
		map.put("MOBILE", transCtcSelectDetailVo.getPmdaApplicantCellphone());
		map.put("EMAIL", transCtcSelectDetailVo.getPmdaApplicantEmail());
		map.put("LIPM_ADDR", transCtcSelectDetailVo.getLipmAddr());
		map.put("LIPM_CHAR_ADDR", transCtcSelectDetailVo.getLipmCharAddr());				
		return map;
	}
}
