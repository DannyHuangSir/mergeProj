package com.twfhclife.eservice.onlineChange.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.internal.org.apache.commons.lang3.exception.ExceptionUtils;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransDeratePaidOffDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransDeratePaidOffVo;
import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.onlineChange.service.ITransDeratePaidOffService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
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
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.api_model.TransCtcLilipmVo;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeVo;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.TransRulesUtil;

@Service
public class TransDeratePaidOffServiceImpl implements ITransDeratePaidOffService{

	private static final Logger logger = LogManager.getLogger(TransDeratePaidOffServiceImpl.class);

	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private TransDao transDao;
	
	@Autowired
	private TransPolicyDao transPolicyDao;
	
	@Autowired
	private TransDeratePaidOffDao transDeratePaidOffDao;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private MessageTemplateClient messageTemplateClient;	
	
	private final static String STRING_FORM_DATE = "yyyy/MM/dd hh:mm:ss";
	
	private final static String COVENANT = "DERATE_PAID_OFF_COVENANT";
	
	private final static String RIDER ="DERATE_PAID_OFF_RIDER";
			
	@Override
	public List<TransCtcSelectDataAddCodeVo> getRuleResult(List<TransCtcSelectDataAddCodeVo> dataAddCodeList,
			String userRocId ) {
		List<String> applyPolicyNos = new ArrayList<>();
		List<String> rollover = transPolicyDao.getProgressPolicyNoList(userRocId, TransTypeUtil.ROLLOVER_PERIODICALLY);
		List<String> derate = transPolicyDao.getProgressPolicyNoList(userRocId, TransTypeUtil.DERATE_PAID_OFF);
		List<String>  paymode= transPolicyDao.getProgressPolicyNoList(userRocId, TransTypeUtil.PAYMODE_PARAMETER_CODE);
		applyPolicyNos.addAll(rollover);
		applyPolicyNos.addAll(derate);
		applyPolicyNos.addAll(paymode);
		try {

			for (TransCtcSelectDataAddCodeVo vo : dataAddCodeList) {
				/**
				 * 1.檢則保單日期 : 保單超過失效日期(系統日大於到期日) 系統日 +1 >= LIPI_INSU_END_DATE
				 */
				int days = DateUtil.getDateInterval(vo.getLipiInsuEndDate().substring(0, 10));
				if (days > 0) {
					vo.setApplyLockedFlag("Y");
					vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_EXPIREDATE_MSG);
				}
				//日期轉換
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo.getLipmInsuBegDate());
				vo.setLipmInsuBegDate(DateUtil.formatDateTime(date, "yyyy/MM/dd"));
				/**
				 * 2.判斷是否有前一次申請
				 */
				if(vo.getApplyLockedFlag().equals("N")) {
					if (!CollectionUtils.isEmpty(applyPolicyNos)) {
						// 保單未到期但已經有在申請中
						if (StringUtils.isEmpty(vo.getApplyLockedFlag()) || "N".equals(vo.getApplyLockedFlag())) {
							if (applyPolicyNos.contains(vo.getLipmInsuNo())) {
								vo.setApplyLockedFlag("Y");
								// 前次申請尚在處理中，暫無法進行此項申請
								vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
							}
						}
					}
				}
				/**
				 * 3.驗證主約 附約 規則
				 */
				if(vo.getApplyLockedFlag().equals("N")) {
					String covenant = parameterDao.getParameterValueByCode("eservice", COVENANT); // 主約
					String rider = parameterDao.getParameterValueByCode("eservice", RIDER); // 附約
					String[] covenantList = covenant.split(",");
					String[] riderList = rider.split(",");
					/**
					 * 3-1.契約狀況(LIPM_ST) 不可為 ’0’,’00’,’11’，有無借款(LIPM_LO_MK) 不可為 ‘Y’
					 */
					checkStMk(vo);
					/**
					 * 3-2.規則一驗證通過，在判斷主約種狀態
					 */
					checkLipmInsuTyp(vo, covenantList);
					/**
					 * 3-3.主約比對完成後 比對 副約 LIPA_ADD_CODE ,LIPF_ADD_CODE
					 */
					checkLipaAddCode(vo, riderList);
				}
				/**
				 * 4.檢視是否符合
				 */
				if (!"Y".equals(vo.getApplyLockedFlag())) {
					vo.setApplyLockedFlag(TransRulesUtil.verifyPolicyRuleByRenewAndReduce(vo));
					if("Y".equals(vo.getApplyLockedFlag())) {
						vo.setApplyLockedMsg("您選擇的保單不提供申請減額繳清保險/展期定期保險!!");
					}
				}
			}
		} catch (ParseException e) {
			logger.error("Unable to init from select DeratePaidOff: {}", ExceptionUtils.getStackTrace(e));
		}
		return dataAddCodeList;
	}
	
	@Override
	public TransDeratePaidOffVo getLilipmDetail(TransCtcLilipmResponse response) {
		TransDeratePaidOffVo vo = new TransDeratePaidOffVo();
		TransCtcLilipmVo lipmVo = response.getCtcLilipmListVo().get(0);
		/** 保單號 */
		vo.setPolicyNo(lipmVo.getLipmInsuNo());
		/** 投保日期, */
		vo.setIinsuBegDate(getStringDate(lipmVo.getLipmInsuBegDate()));
		/** 最近繳費日 */
		vo.setTredRcpDate(getStringDate(lipmVo.getLipmTredRcpDate()));
		/** 繳別 */
		vo.setRcpCode(lipmVo.getLipmRcpCode());
		/** 最近已繳費期次 */
		vo.setTredPaabDate(getStringDate(lipmVo.getLipmTredPaabDate()));
		/** 主保金額 */
		vo.setMainAmt(lipmVo.getLipiMainAmt());
		//NEXT_LIPM_TRED_PAAB_DATE 是下期應繳期次(=介接資料的生效日),
		//若(LIPM_RCP_CODE=M && NEXT_LIPM_TRED_PAAB_DATE的月份=系統月份) 則使用
		//NEXT_LIPM_TRED_PAAB_DATE2  做為下期應繳期次(=介接資料的生效日)
		/** 下期應繳期次 */
		String lipmRcpCode = lipmVo.getLipmRcpCode();
		if (StringUtils.isNotEmpty(lipmRcpCode)) {
			if ("M".equals(lipmRcpCode)) {
				LocalDateTime systemDate = LocalDateTime.now();
				LocalDateTime paabDate = LocalDateTime.parse(lipmVo.getNextLipmTredPaabDate(),
						DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
//				if (paabDate.getMonthValue() == systemDate.getMonthValue()) {
//					vo.setNextTredPaabDate(getStringDate(lipmVo.getNextLipmTredPaabDate2()));
//				}else {
					vo.setNextTredPaabDate(getStringDate(lipmVo.getNextLipmTredPaabDate()));
//				}
			}else {
				vo.setNextTredPaabDate(getStringDate(lipmVo.getNextLipmTredPaabDate()));
			}
		} else {
			vo.setNextTredPaabDate(getStringDate(lipmVo.getNextLipmTredPaabDate()));
		}
		return vo;
	}

	@Override
	public int insertDeratePaidOff(String transNum, TransDeratePaidOffVo transDeratePaidOffVo , UsersVo userVo) {
		int result = 0;
		try {
				// 新增線上申請主檔
				TransVo transVo = new TransVo();
				transVo.setTransNum(transNum);
				transVo.setTransDate(new Date());
				transVo.setTransType(TransTypeUtil.DERATE_PAID_OFF);
				transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_RECEIVED);
				transVo.setUserId(userVo.getUserId());
				transVo.setCreateUser(userVo.getUserId());
				transVo.setCreateDate(new Date());
				transDao.insertTrans(transVo);

				//新增申請的保險單號
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(transDeratePaidOffVo.getPolicyNo());
				transPolicyDao.insertTransPolicy(transPolicyVo);
				//新增要申請的電子表單狀態
				TransDeratePaidOffVo deratePaidOffVo = new TransDeratePaidOffVo();
				deratePaidOffVo.setTransNum(transNum);
				deratePaidOffVo.setDerateAmt(transDeratePaidOffVo.getDerateAmt());
				deratePaidOffVo.setEmail(userVo.getEmail());
				deratePaidOffVo.setNextTredPaabDate(transDeratePaidOffVo.getNextTredPaabDate().replace("/",""));
				transDeratePaidOffDao.insertTransDeratePaidOff(deratePaidOffVo);
                sendNotification(deratePaidOffVo,userVo , transDeratePaidOffVo.getPolicyNo());	
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertDeratePaidOff: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	 public void sendNotification(TransDeratePaidOffVo transDeratePaidOffVo, UsersVo user , String policyNo) {
	        try {
	            Map<String, Object> mailInfo = getSendMailInfo();
	            Map<String, String> paramMap = new HashMap<String, String>();
	            paramMap.put("TransNum", transDeratePaidOffVo.getTransNum());
	            paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
	            paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
	            logger.info("Trans Num : {}", transDeratePaidOffVo.getTransNum());
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
		        parameterValueByCode = parameterService.getParameterByParameterValue(
		                    ApConstants.SYSTEM_ID,OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.DERATE_PAID_OFF);
	            paramMap.put("APPLICATION_FUNCTION", parameterValueByCode.getParameterName());

	            //發送系統管理員
	            receivers = (List) mailInfo.get("receivers");
	            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_032, receivers, paramMap, "email");

	            //發送保戶SMS
	            receivers.clear();//清空
	            paramMap.clear();//清空模板參數
	            //設置模板參數
	            receivers.add(user.getMobile());
	            logger.info("user phone : {}", user.getMobile());
	            messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_033, receivers, paramMap, "sms");
	            //發送保戶MAIL
	            if (user.getEmail() != null) {
	                receivers.clear();//清空
	                receivers.add(user.getEmail());
	                logger.info("user mail : {}", user.getEmail());
	                messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_033, receivers, paramMap, "email");
	            }
	        } catch (Exception e) {
	            logger.info("insertTransInvestment() success, but send notify mail/sms error.");
	        }
	        logger.info("End send mail");
	    }
	 

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
	    
		public String getStringDate(String date) {
			try {
				if (StringUtils.isNotEmpty(date)) {
					SimpleDateFormat formater = new SimpleDateFormat(STRING_FORM_DATE);
					return DateUtil.getRocDate(formater.parse(date));
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
	    
	    /**
	     * 判斷 契約狀態 與 有無借款 規則
	     * @param TransCtcSelectDataAddCodeVo codeVo
	     * @return
	     */
		private TransCtcSelectDataAddCodeVo checkStMk(TransCtcSelectDataAddCodeVo codeVo) {
			if (StringUtils.isNotEmpty(codeVo.getLipmSt())) {
				String lipmSt = codeVo.getLipmSt();
				if (lipmSt.equals("0") || lipmSt.equals("00") || lipmSt.equals("11")) {
					if (StringUtils.isEmpty(codeVo.getLipmLoMk()) || codeVo.getLipmLoMk().equals("N")) {
						codeVo.setApplyLockedFlag("N");
					} else {
						if (codeVo.getLipmLoMk().equals("Y")) {
							codeVo.setApplyLockedFlag("Y");
							codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_MK);
						}
					}
				} else {
					codeVo.setApplyLockedFlag("Y");
					codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_STATUS);
				}
			} else {
				codeVo.setApplyLockedFlag("Y");
				codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_STATUS);
			}
			return codeVo;
		}
		/**
		 * 判斷 主約規則
		 * @param TransCtcSelectDataAddCodeVo codeVo
		 * @param covenantList
		 * @return
		 */
		private TransCtcSelectDataAddCodeVo  checkLipmInsuTyp(TransCtcSelectDataAddCodeVo codeVo, String[] covenantList) {
			if(codeVo.getApplyLockedFlag().equals("N")) {
				if (StringUtils.isNotEmpty(codeVo.getLipmInsuType())) {
					String insuTtp = codeVo.getLipmInsuType();
					if (Arrays.asList(covenantList).contains(insuTtp)) {
						codeVo.setApplyLockedFlag("Y");
						codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_COVENANT);
					}
				} else {
					codeVo.setApplyLockedFlag("Y");
					codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_COVENANT);
				}
			}
			return codeVo;
		}
		
		/**
		 * 判斷 附約規則
		 * @param TransCtcSelectDataAddCodeVo codeVo
		 * @param riderList
		 * @return
		 */
		private TransCtcSelectDataAddCodeVo  checkLipaAddCode(TransCtcSelectDataAddCodeVo codeVo , String[] riderList) {
			if(codeVo.getApplyLockedFlag().equals("N")) {
				if(StringUtils.isNotEmpty(codeVo.getLipaAddCode())) {
					String lipaCode = codeVo.getLipaAddCode();
					if (!Arrays.asList(riderList).contains(lipaCode)) {
						if(StringUtils.isNotEmpty(codeVo.getLipfAddCode())) {
							String lipfCode = codeVo.getLipfAddCode();
							if (Arrays.asList(riderList).contains(lipfCode)) {
								codeVo.setApplyLockedFlag("Y");
								codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_RIDER);
							}
						}						
					} else {
						codeVo.setApplyLockedFlag("Y");
						codeVo.setApplyLockedMsg(OnlineChangMsgUtil.DERATE_PAID_OFF_RIDER);
					}	
				}				
			}		
			return codeVo;
		}

		@Override
		public TransDeratePaidOffVo getTransDeratePaidOffDetail(String transNum) {
			return transDeratePaidOffDao.getTransDeratePaidOffDetail(transNum);
		}
	
}
