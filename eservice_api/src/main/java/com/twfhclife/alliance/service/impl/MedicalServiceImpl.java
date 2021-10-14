package com.twfhclife.alliance.service.impl;

import com.google.gson.Gson;
import com.twfhclife.alliance.dao.IMedicalDao;
import com.twfhclife.alliance.dao.NotifyOfNewCaseMedicalDao;
import com.twfhclife.alliance.dao.UnionCourseDao;
import com.twfhclife.alliance.model.MedicalTreatmentClaimFileDataVo;
import com.twfhclife.alliance.model.MedicalTreatmentClaimVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseMedicalVo;
import com.twfhclife.alliance.model.UnionCourseVo;
import com.twfhclife.alliance.service.IMedicalService;
import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.service.IMedicalTreatmentService;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.generic.dao.adm.ParameterDao;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.CallApiCode;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.StatuCode;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hui.chen
 * @create 2021-09-20
 */
@Service
public class MedicalServiceImpl implements IMedicalService {
    @Autowired
    private IMedicalDao iMedicalDao;

    @Autowired
    @Qualifier("apiParameterDao")
    private ParameterDao parameterDao;

	@Autowired
    private IMedicalTreatmentService iMedicalTreatmentService;

    @Autowired
    private IMessagingTemplateService messagingTemplateService;

    @Autowired
    private NotifyOfNewCaseMedicalDao notifyOfNewCaseMedicalDao;

    private static final Logger logger = LogManager.getLogger(MedicalServiceImpl.class);

    @Override
    public List<MedicalTreatmentClaimVo> getMedicalTreatmentByNoCaseId()throws Exception {
        return iMedicalDao.getMedicalTreatmentByNoCaseId();
    }

    @Override
    @Transactional
    public int updateMedicalTreatmentClaimToAlliance(MedicalTreatmentClaimVo vo) throws Exception {
        int  i=0;
        //進行回應對於的狀態,信息
        i+=iMedicalDao.updateMedicalTreatmentClaimToAllianceAndCaseId(vo);
        i+=iMedicalDao.updateTransMedicalTreatmentClaimToAllianceAndCaseId(vo);
        return i;
    }

    @Override
    public List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByCaseIdAndAllinaceStatus(String itpsPthg, String itpsEnd) throws Exception {
        return iMedicalDao.getTransMedicalTreatmentByCaseIdAndAllinaceStatus( itpsPthg,itpsEnd);
    }

    @Override
    public int updateTarnsMedicalTreatmentClaimToAllianceFileStatus(MedicalTreatmentClaimVo vo) throws Exception {
        return iMedicalDao.updateTarnsMedicalTreatmentClaimToAllianceFileStatus(vo);
    }

    @Override
    public List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByAllinaceStatus(String pqhfEnd, String itpsEnd) throws Exception {

        ArrayList<String> lists = new ArrayList<>();
        lists.add(pqhfEnd);
        lists.add(itpsEnd);
        List<MedicalTreatmentClaimVo> itpsEndList   = iMedicalDao.getTransMedicalTreatmentAndTransByAllinaceStatus(lists);
        return  itpsEndList;
    }

    @Override
    public int updateTarnsMedicalTreatmentClaimToAllianceStatus(MedicalTreatmentClaimVo vo) throws Exception {
        return iMedicalDao.updateTarnsMedicalTreatmentClaimToAllianceStatusAndMsg(vo);
    }

    @Override
    public List<MedicalTreatmentClaimVo> getTransMedicalTreatmentUploadFileid(String pqhfEnd) throws Exception {
        //獲取到對應的醫療保單
        List<MedicalTreatmentClaimVo> transMedical
                = iMedicalDao.getTransMedicalTreatmentByAllinaceStatusIsItpsEnd(pqhfEnd);
        //獲取聯盟回傳的文件的類型
        List<String> parameterByCategoryCode = parameterDao.getParameterByParameterValue(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_TREATMENT_FEDERATION_FILE_TYPE);
        List<MedicalTreatmentClaimVo> collect = transMedical.stream().map((X) -> {
            Float claimSeqId = X.getClaimSeqId();
            try {
                List<MedicalTreatmentClaimFileDataVo> fileList
                        = iMedicalDao.getTransMedicalTreatmentFileDataByClaimSeqId(claimSeqId, parameterByCategoryCode);
                System.out.println(fileList);
                X.setFileData(fileList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return X;
        }).collect(Collectors.toList());


        return  collect;
    }

    @Override
    public int updateTarnsMedicalTreatmentFileDataStatus(MedicalTreatmentClaimFileDataVo fileDataVo) throws Exception {

         return iMedicalDao.updateTarnsMedicalTreatmentFileDataStatus(fileDataVo);
    }

    @Override
    public List<MedicalTreatmentClaimVo> getTransMedicalTreatmentAgainUploadFileid(String pqhfEnd) throws Exception {
        //獲取到對應的醫療保單
        List<MedicalTreatmentClaimVo> transMedical
                = iMedicalDao.getTransMedicalTreatmentAgainUploadFileid(pqhfEnd);
        //獲取聯盟回傳的文件的類型
        List<String> parameterByCategoryCode = parameterDao.getParameterByParameterValue(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_TREATMENT_FEDERATION_FILE_TYPE);
        List<MedicalTreatmentClaimVo> collect = transMedical.stream().map((X) -> {
            Float claimSeqId = X.getClaimSeqId();
            try {
                List<MedicalTreatmentClaimFileDataVo> fileList
                        = iMedicalDao.getTransMedicalTreatmentFileDataByClaimSeqId(claimSeqId, parameterByCategoryCode);
                System.out.println(fileList);
                X.setFileData(fileList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return X;
        }).collect(Collectors.toList());
        return  collect;
    }

    @Override
    public List<MedicalTreatmentClaimVo> getMedicalTreatmentClaimVoBySeqIdNoTransNum() throws Exception {
        List<MedicalTreatmentClaimVo> medical= iMedicalDao.getMedicalTreatmentClaimVoBySeqIdNoTransNum();
        List<MedicalTreatmentClaimVo> collect=null;
        if (!CollectionUtils.isEmpty(medical)) {
            collect= medical.stream().map(x -> {
                List<MedicalTreatmentClaimFileDataVo> medicalFilData = null;
                try {
                    medicalFilData = iMedicalDao.getMedicalTreatmentFileDataByClaimSeqId(x.getClaimSeqId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                x.setFileData(medicalFilData);
                return x;
            }).collect(Collectors.toList());
        }
        return  collect;
    }

    @Override
    public int addTransRequest(TransMedicalTreatmentClaimVo tvo) throws Exception {
            // 狀態歷程
            TransStatusHistoryVo hisVo = new TransStatusHistoryVo();
            hisVo.setCustomerName("系統日程");
            hisVo.setIdentity("SYS_TASK");
            //System.out.println(tvo);
            
            int result = 0;
            Map<String, Object> rMap = iMedicalTreatmentService.inserttransMedicalTreatmentClaimVo(tvo,hisVo);
            logger.info("***after IMedicalTreatmentService.inserttransMedicalTreatmentClaimVo()***");
            if(rMap!=null) {
                logger.info("result="+ Arrays.asList(rMap));
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
                if(tvo.getCreateDate()==null) {
                    loginTime = formater.format(System.currentTimeMillis());
                }else {
                    loginTime = formater.format(tvo.getCreateDate());
                }

                //異常件
                if(ApConstants.TRANS_STATUS_ABNORMAL.equals(status)) {
                    paramMap.put("TransNum", tvo.getTransNum());
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
                    receivers.add(tvo.getMail());
                    vo = getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_023, receivers, paramMap, "email");
                    String resultInsuredMailMsg = messagingTemplateService.triggerMessageTemplate(vo);
                    logger.info("發送系統保戶通知RESULT : " + resultInsuredMailMsg);

                    //發送保戶SMS
                    logger.info("***發送保戶SMS_start");
                    receivers.clear();
                    receivers.add(tvo.getPhone());
                    paramMap.put("TransRemark", abnormalTransRemark);
                    vo = getMessageTriggerRequestVo(ApConstants.ELIFE_SMS_006, receivers, paramMap, "sms");
                    String resultUserSMSMsg = messagingTemplateService.triggerMessageTemplate(vo);
                    logger.info("***發送保戶SMS : " + resultUserSMSMsg);

                }else {
                    paramMap.clear();
                    paramMap.put("TransNum", tvo.getTransNum());
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
                    receivers.add(tvo.getMail());
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
                    receivers.add(tvo.getPhone());
                    paramMap.put("TransRemark", transRemark);
                    vo = getMessageTriggerRequestVo(ApConstants.ELIFE_SMS_005, receivers, paramMap, "sms");
                    String resultUser_SMS_Msg = messagingTemplateService.triggerMessageTemplate(vo);
                    logger.info("***發送保戶SMS : " + resultUser_SMS_Msg);

                }
                logger.info("End send mail");
            }else {
                logger.info("result<=0,no send mail/sms.");
            }
        return result;
    }

    @Override
    public int updateMedicalTreatmentClaimToStatusTransNumByCaseId(MedicalTreatmentClaimVo vo) throws Exception {
        return iMedicalDao.updateMedicalTreatmentClaimToStatusByTransNum(vo.getTransNum(),vo.getStauts(),vo.getCaseId());
    }

    @Override
    public List<NotifyOfNewCaseMedicalVo> getNotifyOfNewCaseMedicalByNcStatus(String statusDefault) throws Exception {
        return  notifyOfNewCaseMedicalDao.getNotifyOfNewCaseMedicalByNcStatus(statusDefault);
    }

    @Override
    public String getTransMedicalTreatmentByCaseId(String caseId) throws Exception {
    	ArrayList<String> list = new ArrayList<>();
    	//獲取聯盟回傳類型
    	String pqhf_end = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_PQHF_END);
        String itps_end = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_ITPS_END);
        String status_pqhf = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_PQHF);
        list.add(pqhf_end);
        list.add(itps_end);
        list.add(status_pqhf);
        return iMedicalDao.getTransMedicalTreatmentByCaseId(caseId,list);
    }

    @Override
    public int updateNotifyOfNewCaseMedicalNcStatusBySeqId(NotifyOfNewCaseMedicalVo vo) throws Exception {
        return notifyOfNewCaseMedicalDao.updateNotifyOfNewCaseMedicalNcStatusBySeqId(vo);
    }

    @Override
    public TransMedicalTreatmentClaimVo getTransMedicalTreatmentByTransNum(String transNum) throws Exception {
        return iMedicalDao.getTransMedicalTreatmentByTransNum(transNum);
    }

    @Override
    public int addMedicalRequest(MedicalTreatmentClaimVo medicalVo) throws Exception {
        /**
         * 進行添加上新案件的數據資料
         */
    	int iRtn = -1;
    	
        //獲取到SequenceId數據
        Float seqId = iMedicalDao.getMedicalTreatmentSequence();
        medicalVo.setClaimSeqId(seqId);
        logger.info("添加的數據信息===",medicalVo);
        int addMedicalCountResult = iMedicalDao.addMedicalTreatment(medicalVo);
        if (addMedicalCountResult > 0) {
        	iRtn = addMedicalCountResult;
            List<MedicalTreatmentClaimFileDataVo> fileDatas = medicalVo.getFileData();
            if (fileDatas != null && fileDatas.size() > 0) {
            	int addFileCountResult = 0;

                for (MedicalTreatmentClaimFileDataVo fileData : fileDatas) {
                    if (fileData != null) {
                        fileData.setClaimsSeqId(seqId);
                        fileData.setFileBase64("");

                        addFileCountResult = iMedicalDao.addMedicalTreatmentFileData(fileData);
                    }
                }
                
                if(addFileCountResult>0) {
                	iRtn = addFileCountResult;
                }
            }
        }

        /**
         * 進行發送郵件信息
         
        if(j>0){
            MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
            List<String> receivers = new ArrayList<String>();
            Map<String, String> paramMap = new HashMap<String, String>();
            logger.info("Start send mail");
            String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_AMD, ApConstants.MEDICAL_ALLIANCE_MAIL_TWFHCLIFE_ADM);
            String[] mails = mailTo.split(";");
            if(mails.length > 0) {
                for (String mail : mails) {
                    receivers.add(mail);
                    logger.info("Mail Address : " + mail);
                }
            }
            //發送系統管理員
            logger.info("***發送系統管理員 _start");
            paramMap.put("CODE", "403");
            String allianceStatus = medicalVo.getAllianceStatus();
            String statusMessage="";
            paramMap.put("STATUS",allianceStatus);
            //獲取對於狀態的描述信息
            List<ParameterVo> parameterByCategoryCode = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS);
            for (ParameterVo parameterVo : parameterByCategoryCode) {
                String parameterValue = parameterVo.getParameterValue();
                if (parameterValue!=null  && parameterValue.equals(allianceStatus)) {
                    statusMessage=parameterVo.getParameterName();
                }
            }
            paramMap.put("STATUS_MESSAGE", statusMessage);
            paramMap.put("HOSPITAL_CODE", medicalVo.getToHospitalId());
            paramMap.put("INSURED_ID", medicalVo.getIdNo());
            //使用郵件範本
            vo = getMessageTriggerRequestVo(ApConstants.MEDICAL_MAIL_034, receivers, paramMap, "email");
            String resultSYS_MailMsg = messagingTemplateService.triggerMessageTemplate(vo);
            logger.info("***發送系統管理員 : " + resultSYS_MailMsg);
            logger.info("end mail");
        }
        */

        return iRtn;
    }

    @Override
    @Transactional
    public int updateTransMedicalTreatmentByCaseId(MedicalTreatmentClaimVo claimVo,String fileSavePath) throws Exception {
    	/**
    	 * 1.更新之前保單的  聯盟狀態
    	 * 2.更新當前保單的文件狀態(存在更新),不存在新增
    	 */
    	//update TRANS_MEDICAL_TREATMENT_CLAIM.ALLIANCE_STATUS
        int j = 0;
        //String transNum = claimVo.getTransNum();
        String caseId = claimVo.getCaseId();
        String allianceStatus = claimVo.getAllianceStatus();
        j = iMedicalDao.updateTransMedicalTreatmentByCaseId(caseId,allianceStatus);
        
        //update TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS
        List<MedicalTreatmentClaimFileDataVo> fileDatas = claimVo.getFileData();
        if (fileDatas!=null && fileDatas.size()>0) {
            for (MedicalTreatmentClaimFileDataVo fileData : fileDatas) {
                String fileId = fileData.getFileId();
                if (StringUtils.isNotBlank(fileId)) {
                	int hasFileCnt = iMedicalDao.getTransMedicalTreatmentFiledatasByFileId(fileId);
                    String fileStatus = fileData.getFileStatus();
                    if (StringUtils.isNotBlank(fileStatus)) {
                        if (hasFileCnt>0) {//有filedata就更新file_status.
                            j = iMedicalDao.updateTarnsMedicalTreatmentFileStatus(fileId,fileStatus);
                        }else {//無filedata就新增filedata record.
                            float claimsSeqId = iMedicalDao.getTransMedicalTreatmentClaimsSeqIdByCaseId(caseId);
                        	fileData.setClaimsSeqId(claimsSeqId);
                            fileData.setFileBase64("");
                            
                            j = iMedicalDao.addTarnsMedicalTreatmentFile(fileData);
                        }
                    }
                }
            }
        }

        /**
         * 更新狀態訊息後,進行發送郵件信息
         */
        if(j>0){
            MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
            List<String> receivers = new ArrayList<String>();
            Map<String, String> paramMap = new HashMap<String, String>();
            logger.info("Start send mail");
            String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_AMD, ApConstants.MEDICAL_ALLIANCE_MAIL_TWFHCLIFE_ADM);
            String[] mails = mailTo.split(";");
            if(mails.length > 0) {
                for (String mail : mails) {
                    receivers.add(mail);
                    logger.info("Mail Address : " + mail);
                }
            }
            //發送系統管理員
            logger.info("***發送系統管理員 _start");
            paramMap.put("CODE", "403");
            String alliance_status = claimVo.getAllianceStatus();
            String statusMessage="";
            paramMap.put("STATUS",alliance_status);
            //獲取對於狀態的描述信息
            List<ParameterVo> parameterByCategoryCode = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS);
            for (ParameterVo parameterVo : parameterByCategoryCode) {
                String parameterValue = parameterVo.getParameterValue();
                if (parameterValue!=null  && parameterValue.equals(alliance_status)) {
                    statusMessage=parameterVo.getParameterName();
                }
            }
            paramMap.put("STATUS_MESSAGE", !StringUtils.isEmpty(statusMessage)?statusMessage:alliance_status);
            paramMap.put("HOSPITAL_CODE", !StringUtils.isEmpty(claimVo.getToHospitalId() )?claimVo.getToHospitalId():"[無]");
            paramMap.put("INSURED_ID", claimVo.getIdNo());
            //使用郵件範本
            vo = getMessageTriggerRequestVo(ApConstants.MEDICAL_MAIL_034, receivers, paramMap, "email");
            String resultSYS_MailMsg = messagingTemplateService.triggerMessageTemplate(vo);
            logger.info("***發送系統管理員 : " + resultSYS_MailMsg);
            logger.info("end mail");
        }

        return j;
    }

    @Override
    @Transactional
    public int getTransMedicalTreatmentBySendAlliance() throws Exception {
        //進行查詢出對於已開啓傳送公會聯盟鏈并且覆核人員審核的案件數據
        List<TransMedicalTreatmentClaimVo> voList = iMedicalDao.getTransMedicalTreatmentBySendAlliance(StatuCode.AUDIT_CODE_Y.code);
        int rtn=0;
        if (voList != null && voList.size() != 0) {
            for (TransMedicalTreatmentClaimVo voTemp : voList) {
                Float seq = iMedicalDao.getMedicalTreatmentSequence();
                voTemp.setClaimSeqId(seq);
                rtn = iMedicalDao.addTransMedicalToMedicalTreatment(voTemp);
                if(rtn>0){
                    iMedicalDao.updetaTransMedicalTreatmentClaimBySendAlliancePush(voTemp,StatuCode.AUDIT_CODE_Y.code);
                }
            }
        }
        return rtn;
    }

    @Override
    public int updaetNotifyOfNewCaseMedicalStatus(String caseId,String transNum) throws Exception {
                String pths = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_PTHS);
        int   i= iMedicalDao.updateTarnsMedicalTreatmentClaimToAllianceStatus(transNum,pths);
        return i;
    }

    @Override
    public List<MedicalTreatmentClaimFileDataVo> getTransMedicalTreatmentClaimFileData(String has_file) throws Exception {
        return  iMedicalDao.getTransMedicalTreatmentClaimFileData(has_file);
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

	@Override
	public MedicalTreatmentClaimVo getMedicalTreatmentByCaseId(String caseId) throws Exception {
		MedicalTreatmentClaimVo vo = null;
		if(StringUtils.isNotBlank(caseId)) {
			vo = iMedicalDao.getMedicalTreatmentByCaseId(caseId);
		}
		return vo;
	}

}
