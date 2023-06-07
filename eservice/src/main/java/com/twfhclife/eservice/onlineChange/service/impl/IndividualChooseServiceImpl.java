package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.service.IndividualChooseService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.LicohilVo;
import com.twfhclife.generic.api_model.PolicyDetailRequest;
import com.twfhclife.generic.api_model.PolicyDetailResponse;
import com.twfhclife.generic.api_model.PolicyDetailVo;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.service.ISendSmsService;
import com.twfhclife.generic.util.RptUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndividualChooseServiceImpl implements IndividualChooseService {

	private static final Logger logger = LogManager.getLogger(IndividualChooseServiceImpl.class);

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;

	@Autowired
	private ITransService transService;

	@Autowired
	private IndividualChooseDao individualChooseDao;

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransRiskLevelDao transRiskLevelDao;

	@Autowired
	private TransChooseLevelDao transChooseLevelDao;

	@Autowired
	private IRegisterUserService registerUserService;

    @Autowired
    private ParameterDao parameterDao;
	
    @Autowired
    private MessageTemplateClient messageTemplateClient;
    
	@Autowired
	private ITransInvestmentService transInvestmentService;
	
	@Autowired
	private ISendSmsService sendSmsService;
	
	@Autowired
	private IndividualChooseBlackListDao individualChooseBlackListDao;
	
	@Autowired
	private IndividualChooseIpDao individualChooseIpDao;
    
	@Autowired
	private IMailService mailService;
	
	private final static DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public List<PolicyDetailVo> getPolicyDataByRocId(String rocId) {
		PolicyDetailRequest req = new PolicyDetailRequest();
		req.setRocId(rocId);
		PolicyDetailResponse response = transCtcSelectUtilClient.getPolicyDataByRocId(req);
		return response.getPolicyDetailList();
	}

	@Override
	public String checkDetail(LicohilVo licohilVo , IndividualChooseVo individualChooseVo) {
		try {
			// 驗證
//			if (!licohilVo.getLipmName1().equals(individualChooseVo.getName())) {
//				return OnlineChangMsgUtil.CHECK_SCREEN_DATA;
//			}

			Date bnagBirth = changeDate(licohilVo.getBnagBirth());
			Date birthDate = new SimpleDateFormat("yyyy/MM/dd").parse(individualChooseVo.getBirthDate());
			if (bnagBirth.compareTo(birthDate) != 0) {
				return OnlineChangMsgUtil.CHECK_SCREEN_DATA;
			}
		} catch (Exception e) {
			return OnlineChangMsgUtil.CHECK_SCREEN_DATA;
		}
		return null;
	}
	
	@Override
	public String checkDetail2(IndividualChooseVo oldIndividualChooseVo , IndividualChooseVo individualChooseVo) {
		try {
			// 驗證
//			if (!oldIndividualChooseVo.getName().equals(individualChooseVo.getName())) {
//				return OnlineChangMsgUtil.CHECK_SCREEN_DATA2;
//			}
			Date bnagBirth = new SimpleDateFormat("yyyy/MM/dd").parse(oldIndividualChooseVo.getBirthDate());
			Date birthDate = new SimpleDateFormat("yyyy/MM/dd").parse(individualChooseVo.getBirthDate());
			if (bnagBirth.compareTo(birthDate) != 0) {
				return OnlineChangMsgUtil.CHECK_SCREEN_DATA2;
			}
			if(!oldIndividualChooseVo.getMobile().equals(individualChooseVo.getMobile())) {
				return OnlineChangMsgUtil.CHECK_SCREEN_DATA2;
			}
		} catch (Exception e) {
			return OnlineChangMsgUtil.CHECK_SCREEN_DATA2;
		}
		return null;
	}

	@Override
	public IndividualChooseVo getIndividualChoosData(String rocId) {
		return individualChooseDao.getIndividualChoosData(rocId);
	}

	@Override
	public boolean checkRatingDate(Date ratingDate) {
		String ruleDate = parameterService.getParameterValueByCode("eservice", "INDIVDUAL_CHOOSE_DATE");
		LocalDate localSystem = LocalDate.parse(ruleDate);
		LocalDate localRating = ratingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (localSystem.isAfter(localRating)) {//比對箝制評估 是否滿一年
			return true;
		}
		LocalDate localSystem1 = LocalDate.now();		
		localRating = localRating.plusYears(1L);
		if (localRating.isAfter(localSystem1)) { //比對前次申請是否滿一年
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkRatingDate2(Date ratingDate) {

		LocalDate localSystem = LocalDate.now();
		LocalDate localRating = ratingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		localRating = localRating.plusYears(1L);
		if (localSystem.isAfter(localRating)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int insertIndividualChoose(IndividualChooseVo individualChooseVo) {
		try {
			String transNum = transService.getTransNum();
			UsersVo usersVo = registerUserService.getUserByRocId(individualChooseVo.getRocId());
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			if (individualChooseVo.isChooseType() && usersVo != null) {
				transVo.setTransType(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
				transVo.setUserId(usersVo.getUserId());
				transVo.setCreateUser(usersVo.getUserId());
				individualChooseVo.setUsersType(true);
			} else {
				transVo.setTransType(TransTypeUtil.CHOOSE_LEVEL_PARAMETER_CODE);
				transVo.setCreateUser("twfhclife.com.tw");
			}
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);

			if(transVo.getTransType().equals(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE)) {
				insertTransChooseLevelVo(individualChooseVo , usersVo);
			}
			
			IndividualChooseVo selectIndividualChooseVo = getIndividualChoosData(individualChooseVo.getRocId());
			if (selectIndividualChooseVo != null) {
				individualChooseVo.setSource("1");
				individualChooseVo.setEditDate(new Date());
				individualChooseVo.setRatingDate(new Date());
				individualChooseDao.updateIndividualChoose(individualChooseVo);
			} else {
				individualChooseVo.setSource("1");
				individualChooseVo.setEditDate(new Date());
				individualChooseVo.setRatingDate(new Date());
				individualChooseDao.insertIndividualChoose(individualChooseVo);
			}
			
			if (individualChooseVo.isChooseType() && individualChooseVo.isUsersType()) {
				// 官網會員
				TransRiskLevelVo transRiskLevelVo = new TransRiskLevelVo();
				transRiskLevelVo.setTransNum(transNum);
				transRiskLevelVo.setRocId(individualChooseVo.getRocId());
				transRiskLevelVo.setRiskLevelOld(individualChooseVo.getOldRiskAttr());
				transRiskLevelVo.setRiskLevelNew(individualChooseVo.getRiskAttr());
				transRiskLevelVo.setRiskScore(Integer.valueOf(individualChooseVo.getScore()));
				transRiskLevelVo.setRiskLevelDesc(individualChooseVo.getDesc());
				transRiskLevelVo.setChoose(individualChooseVo.getChoose());
				transRiskLevelVo.setRuleStatus(individualChooseVo.getRuleStatus());
				transRiskLevelDao.insertTransRiskLevel(transRiskLevelVo);
			} else {
				TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
				transChooseLevelVo.setTransNum(transNum);
				transChooseLevelVo.setRocId(individualChooseVo.getRocId());
				transChooseLevelVo.setChooseLevelOld(individualChooseVo.getOldRiskAttr());
				transChooseLevelVo.setChooseLevelNew(individualChooseVo.getRiskAttr());
				transChooseLevelVo.setChooseScore(Integer.valueOf(individualChooseVo.getScore()));
				transChooseLevelVo.setChooseLevelDesc(individualChooseVo.getDesc());
				transChooseLevelVo.setChoose(individualChooseVo.getChoose());
				transChooseLevelVo.setRuleStatus(individualChooseVo.getRuleStatus());
				transChooseLevelDao.insertTransChooseLevel(transChooseLevelVo);
			}
			sendNotification(individualChooseVo);
		} catch (Exception e) {
			logger.error("Unable to init from insertIndividualChoose: {}", ExceptionUtils.getStackTrace(e));
			return 0;
		}
		return 1;
	}
	
	private void insertTransChooseLevelVo( IndividualChooseVo individualChooseVo , UsersVo usersVo) {
		String transNum = transService.getTransNum();
		TransVo transVo = new TransVo();
		transVo.setTransNum(transNum);
		transVo.setTransDate(new Date());
		transVo.setTransType(TransTypeUtil.CHOOSE_LEVEL_PARAMETER_CODE);
		transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
		if (usersVo != null) {
			transVo.setUserId(usersVo.getUserId());
			transVo.setCreateUser(usersVo.getUserId());
		} else {
			transVo.setCreateUser("twfhclife.com.tw");
		}
		transVo.setCreateDate(new Date());
		transDao.insertTrans(transVo);
		
		TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
		transChooseLevelVo.setTransNum(transNum);
		transChooseLevelVo.setRocId(individualChooseVo.getRocId());
		transChooseLevelVo.setChooseLevelOld(individualChooseVo.getOldRiskAttr());
		transChooseLevelVo.setChooseLevelNew(individualChooseVo.getRiskAttr());
		transChooseLevelVo.setChooseScore(Integer.valueOf(individualChooseVo.getScore()));
		transChooseLevelVo.setChooseLevelDesc(individualChooseVo.getDesc());
		transChooseLevelVo.setChoose(individualChooseVo.getChoose());
		transChooseLevelVo.setRuleStatus(individualChooseVo.getRuleStatus());
		transChooseLevelDao.insertTransChooseLevel(transChooseLevelVo);
	} 

	@Override
	public TransChooseLevelVo getTransChooseLevel(String rocId) {
		return transChooseLevelDao.getTransChooseLevel(rocId);
	}

	@Override
	public List<String> getpolicyInvestmentType() {
		return individualChooseDao.getpolicyInvestmentType();
	}

	@Override
	public LicohilVo getpolicyHaveInvestmentType(List<LicohilVo> licohilVoList) {
		List<LicohilVo> licohilList = licohilVoList.stream().filter(x-> { 
			if(x.getPolicyListType().equals("1")) {
				return true;	
			}
			return false;
			}).collect(Collectors.toList());
		if(licohilList.size() != 0) {
			licohilList = licohilList.stream().sorted((t1,t2)-> t2.getRatingDate().compareTo(t1.getRatingDate())).collect(Collectors.toList());		
			return licohilList.get(0);			
		}else {
			return null;
		}
	}

	@Override
	public LicohilVo getpolicyNotHaveInvestmentType(List<LicohilVo> licohilVoList) {
		List<LicohilVo> licohilList = licohilVoList.stream().filter(x-> { 
			if(x.getPolicyListType().equals("2")) {
				return true;	
			}
			return false;
			}).collect(Collectors.toList());
		if(licohilList.size() != 0) {
			licohilList = licohilList.stream().sorted((t1,t2)-> t2.getRatingDate().compareTo(t1.getRatingDate())).collect(Collectors.toList());		
			return licohilList.get(0);			
		}else {
			return null;
		}
	}

	@Override
	public boolean compareDate(Date individalRatingDate, Date licohilRatingDate) {
		return individalRatingDate.after(licohilRatingDate) ? true : false;
	}

	@Override
	public IndividualChooseVo verificationDate(IndividualChooseVo individualChooseVo, LicohilVo licohilVo, IndividualChooseVo oldIndividualChooseVo) {
		//判斷之前是否已經做過風險評估
		if (oldIndividualChooseVo != null) {
			//有做過風險評估 需判別 評估日期最新的是哪個
			if (compareDate(oldIndividualChooseVo.getRatingDate(),licohilVo.getRatingDate())) {
				if (!checkRatingDate(oldIndividualChooseVo.getRatingDate())) {
					individualChooseVo.setRiskAttr(oldIndividualChooseVo.getRiskAttr());
					individualChooseVo.setRiskDateType(false);
				}
			} else {
				if (!checkRatingDate(licohilVo.getRatingDate())) {
					individualChooseVo.setRiskAttr(licohilVo.getInveAttr());
					individualChooseVo.setRiskDateType(false);
				}
			}
		} else {
			//沒做過風險評估  取核心的日期判斷
			if (!checkRatingDate(licohilVo.getRatingDate())) {
				individualChooseVo.setRiskAttr(licohilVo.getInveAttr());
				individualChooseVo.setRiskDateType(false);
			}
		}
		return individualChooseVo;
	}
	
	private Date changeDate(Date date) {
		try {
			String stDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
			return new SimpleDateFormat("yyyy/MM/dd").parse(stDate);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	
	public void sendNotification(IndividualChooseVo individualChooseVo) {
        try {
        	String accessKey = parameterDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
            String desc = transInvestmentService.transRiskLevelToName(individualChooseVo.getRiskAttr());
            List<String> receivers = new ArrayList<String>();
            //申請功能
            StringBuilder sb = new StringBuilder();
            if("D".equals(individualChooseVo.getRiskAttr())) {
                sb.append("【臺銀人壽通知】親愛的客戶您好，您於");
                sb.append(individualChooseVo.getRatingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                sb.append("填寫本公司投資型保險商品要保人風險屬性評估問卷，");
                sb.append("經評估後，投資型商品不適合您，如您需要其他商品，請洽詢本公司，客服電話 0800-011-966，謝謝。");
            }else {
                sb.append("【臺銀人壽通知】親愛的客戶您好，您於");
                sb.append(individualChooseVo.getRatingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                sb.append("填寫本公司投資型保險商品風險屬性評估問卷，");
                sb.append("經評估後您的風險屬性類型為");
                sb.append(desc);
                sb.append("，謝謝。");
            }

            logger.info("Send IndividualChoose Mail Start");
          	//設置模板參數
//            paramMap.clear();// 清空模板參數m
            receivers.add(individualChooseVo.getMobile());
	        logger.info("user phone : {}", individualChooseVo.getMobile());
	        sendSmsService.sendSms(individualChooseVo.getMobile(), sb.toString());
//	        messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_SMS_039, receivers, paramMap, "sms");	   
//			httpUtil.postCommLogAdd(url, accessKey, new CommLogRequest(ApConstants.SYSTEM_ID, "sms", mobileTo, content));

        } catch (Exception e) {
            logger.info("insertIndividualChoose() success, but send notify mail/sms error." , e);
        }
        logger.info("End send mail");
    }

	@Override
	public IndividualChooseBlackListVo getIndividualChooseBlackList(String ip) {
		return	individualChooseBlackListDao.getIndividualChooseBlackList(ip);
	}

	@Override
	public int insertIndividualChooseIp(IndividualChooseIpVo individualChooseIpVo) {
		return individualChooseIpDao.insertIndividualChooseIp(individualChooseIpVo);
	}

	@Override
	public List<IndividualChooseIpVo> getIndividualChooseIp(String ip , int time) {
		return individualChooseIpDao.getIndividualChooseIp(ip , time);
	}

	@Override
	public int insertOrUpdateIndividualChooseBlackList(String ip) {
		IndividualChooseBlackListVo individualChooseBlackListVo = new IndividualChooseBlackListVo();
		individualChooseBlackListVo.setIp(ip);
		individualChooseBlackListVo.setBlackCategory("1");
		LocalDateTime sysTime = LocalDateTime.now();
		individualChooseBlackListVo.setBlackStart(sysTime);
		individualChooseBlackListVo.setBlackEnd(sysTime.plusHours(6));
		individualChooseBlackListVo.setStatus("1");
		IndividualChooseBlackListVo selectVo = individualChooseBlackListDao.getIndividualChooseBlackList(individualChooseBlackListVo.getIp());
		if(selectVo == null) {
			return individualChooseBlackListDao.insertIndividualChooseBlackList(individualChooseBlackListVo);
		}else {
			return individualChooseBlackListDao.updateIndividualChooseBlackList(individualChooseBlackListVo);
		}
	}

	@Override
	public boolean sendPdfMail(IndividualChooseVo individualChooseVo , String email1 , String email2) {
		try {
		String transNum = "";
		TransChooseLevelVo transChooseLevelVo =	transChooseLevelDao.getTransChooseLevel(individualChooseVo.getRocId());
		if(transChooseLevelVo == null) {
			TransRiskLevelVo transRiskLevelVo = transRiskLevelDao.getTransRiskLevel(individualChooseVo.getRocId());			
			transNum = transRiskLevelVo.getTransNum();
		}else {
			transNum = transChooseLevelVo.getTransNum();
		}			
		String desc = transInvestmentService.transRiskLevelToName(individualChooseVo.getRiskAttr());
		String attr = individualChooseVo.getRiskAttr();
		String rocID = individualChooseVo.getRocId();
		rocID = rocID.substring(0,7) + "***";
		String name = individualChooseVo.getName();
		if(name.length()>=3) {
			name = name.substring(0,name.length()-2) + "O" +name.substring(name.length()-1);
		}else {
			name = name.substring(0,1) + "O" ;
		}
		LocalDateTime sysDate = LocalDateTime.now();
		RptUtils rptUtils = new RptUtils("indivdualChoose.pdf");
		Integer fontSize = 12;
		rptUtils.txt(LocalDateTime.now().format(formater).toString(),fontSize, 1, 125f, 650f);
		rptUtils.txt(name +"  "+ rocID,fontSize, 1, 80f, 614f);
		if("D".equals(attr)) {
			rptUtils.txt(desc, 24 , 1, 205f, 582f);
		}else {
			rptUtils.txt(desc, 24 , 1, 260f, 582f);
		}

		rptUtils.txt(individualChooseVo.getRatingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(), fontSize , 1, 188f, 548f);
		rptUtils.toPdf();
    	String filename = "/temp/BankTaiwan LIFE_KYC_" + transNum + ".pdf";
    	
    	java.io.File sendFile = new java.io.File(filename);
    	FileUtils.writeByteArrayToFile(sendFile, rptUtils.toPdf());
    	
    	StringBuffer sb = new StringBuffer();
		sb.append(
				"<!DOCTYPE html><html lang=\"zh-Hant\"><head><meta charset=\"utf-8\"/></head><body>CONTENT</body></html>");
		String content = sb.toString();
		content = content.replace("CONTENT", "臺銀人壽風險屬性評估結果");
    	String subject ="臺銀人壽風險屬性評估結果 : "  + transNum;
    	List<java.io.File>listFile = new ArrayList<>();
    	listFile.add(sendFile);
    	if(StringUtils.isNotEmpty(email1)) {
    		mailService.sendMail(content, subject,email1, null, listFile);    	}
    	
    	if(StringUtils.isNotEmpty(email2)) {
    		mailService.sendMail(content, subject,email2, null, listFile);
    	}
		return true;
		}catch (Exception e) {
			 logger.info("insertIndividualChoose() sendPdfMail error." , e);
			return false;
		}
	}
	
}
