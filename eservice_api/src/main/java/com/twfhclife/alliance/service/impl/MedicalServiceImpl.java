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
import org.springframework.util.StringUtils;
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

    public String ACCESS_TOKEN;

    private RestTemplate restTemplate;

    @Autowired
    private UnionCourseDao unionCourseDao;

    @Autowired
    private IMedicalTreatmentService iMedicalTreatmentService;

    @Autowired
    private  IMessagingTemplateService messagingTemplateService;

    @Autowired
    private NotifyOfNewCaseMedicalDao notifyOfNewCaseMedicalDao;

    private static final Logger logger = LogManager.getLogger(MedicalServiceImpl.class);
    
    public MedicalServiceImpl() {
		
		//Fix the RestTemplate to be a singleton instance.
	    restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;

	    // Set the request factory. 
	    // IMPORTANT: This section I had to add for POST request. Not needed for GET
	    int milliseconds = 20*1000;
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(milliseconds);
		httpRequestFactory.setConnectTimeout(milliseconds);
		httpRequestFactory.setReadTimeout(milliseconds);
	    restTemplate.setRequestFactory(httpRequestFactory);

	    // Add converters
	    // Note I use the Jackson Converter, I removed the http form converter 
	    // because it is not needed when posting String, used for multipart forms.
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}

    @Override
    public List<MedicalTreatmentClaimVo> getMedicalTreatmentByNoCaseId()throws Exception {

        return iMedicalDao.getMedicalTreatmentByNoCaseId();
    }

    @Override
    @Transactional
    public int updateMedicalTreatmentClaimToAlliance(MedicalTreatmentClaimVo vo) throws Exception {
        //進行回應對於的狀態,信息
        iMedicalDao.updateMedicalTreatmentClaimToAllianceAndCaseId(vo);
        iMedicalDao.updateTransMedicalTreatmentClaimToAllianceAndCaseId(vo);
        return 0;
    }

    @Override
    public String postForEntity(String url_api401, MedicalTreatmentClaimVo vo, String s){
    	logger.info("**start into postForEntity()**");
        String strRes = null;
        
        try {
        	UnionCourseVo uc = new UnionCourseVo();
            uc.setCaseId(vo.getCaseId());
            uc.setTransNum(vo.getTransNum());
            uc.setType(uc.TYPE);
            uc.setName(s);
            uc.setCreateDate(new Date());
            
            logger.info("debug00");
            logger.info("url_api401="+url_api401);
            if(url_api401!=null && vo!=null) {
                ResponseEntity<String> responseEntity = null;

                HttpHeaders headers = new HttpHeaders();
                headers.set("Access-token", ACCESS_TOKEN);
                headers.setContentType(MediaType.APPLICATION_JSON);
                logger.info("debug01");
                
                //進行封裝數據,進行傳輸給聯盟
                //hpid
                vo.setHpId(vo.getToHospitalId());
                
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("本人");
                stringBuffer.append(vo.getName());
                stringBuffer.append("/");
                stringBuffer.append(vo.getIdNo());
                
                logger.info("debug02");
                //查詢同意條款
                String parameterValueByCode = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ESERVICE, ApConstants.MEDICAL_TREATMENT_CONTENT);
                StringBuffer str = new StringBuffer();
                str.append(parameterValueByCode);
                String replace = str.toString().replaceAll("<([^>]*)>", "").replace("\r", "").replace("\t", "").replace("\n", "");
                stringBuffer.append(replace);

                logger.info("debug03");
                vo.setCpoa(stringBuffer.toString());
                vo.setHsTime(vo.getAuthorizationStartDate());
                vo.setHeTime(vo.getAuthorizationEndDate());

                logger.info("debug04");
                vo.setTo(vo.getToCompanyId());
                
                String authorizationFileType = vo.getAuthorizationFileType();
                String[] split = authorizationFileType.split(",");
                List<String> objects = new ArrayList<>();
                //1":診斷證明書，"2":收據
                for (int i = 0; i < split.length; i++) {
                	switch (split[i]){
                		case "1":
                			objects.add("CertificateDiagnosis");
                			break;
                		case "2":
                			objects.add("Receipt");
                			break;
                		default:
                			break;
                	}
                }
                vo.setDtypes(objects);
                
                //accidentCause
                //disease=疾病,accident=意外
                String oriAccidentCause = vo.getAccidentCause();
                if("1".equals(oriAccidentCause)) {
                	vo.setAccidentCause("disease");
                }else if("2".equals(oriAccidentCause)){
                	vo.setAccidentCause("accident");
                }else {
                	logger.info("***Can't judge accidentCause,oriAccidentCause="+oriAccidentCause);
                }

                logger.info("debug05");
                Gson gson = new Gson();
                String json = gson.toJson(vo);
                logger.info("api401 resquest json="+json);

                HttpEntity<String> entity = new HttpEntity<String>(json,headers);
                restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
                responseEntity = restTemplate.postForEntity(url_api401, entity, String.class);

                if(responseEntity!=null) {
                	logger.info("api401 responseEntity.getBody()="+strRes);
                    strRes = responseEntity.getBody();
                }else {
                	logger.info("api401 responseEntity obj is null.");
                }
                // strRes= "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"caseId\":\"20210125153001-45c17f68e615-L01\"}}";
                
                logger.info("debug06");
                boolean checkRes = this.checkResponseStatus(responseEntity);
                if(checkRes) {
                    uc.setNcStatus(uc.NC_STATUS_S);
                    String caseId = MyJacksonUtil.readValue(strRes, "/data/caseId");
                    uc.setCaseId(caseId);
                }else {
                    uc.setNcStatus(uc.NC_STATUS_F);
                }
                uc.setCompleteDate(new Date());
                MyJacksonUtil.getNodeString(strRes, "msg");
                uc.setMsg(getResInfo(strRes));
                unionCourseDao.insertUnionCourseVo(uc);

                if (!checkRes) {
                    return null;
                }

            }
        }catch(Exception e) {
        	logger.info(e.toString());
        }
        
        return strRes;
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
                X.setFileDatas(fileList);
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
                X.setFileDatas(fileList);
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
                List<MedicalTreatmentClaimFileDataVo> medicalFilData
                        = null;
                try {
                    medicalFilData = iMedicalDao.getMedicalTreatmentFileDataByClaimSeqId(x.getClaimSeqId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                x.setFileDatas(medicalFilData);
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
        System.out.println(tvo);
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
    public int updateMedicalTreatmentClaimToStatusByTransNum(MedicalTreatmentClaimVo vo) throws Exception {
        return iMedicalDao.updateMedicalTreatmentClaimToStatusByTransNum(vo.getTransNum(),vo.getStauts());
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
         * */
          int j=0;
            //獲取到SequenceId數據
        Float seqId =    iMedicalDao.getMedicalTreatmentSequence();
        medicalVo.setClaimSeqId(seqId);
        logger.info("添加的數據信息===",medicalVo);
       int i = iMedicalDao.addMedicalTreatment(medicalVo);
        if (i > 0) {
            j = i;
            List<MedicalTreatmentClaimFileDataVo> fileDatas = medicalVo.getFileDatas();
            if (fileDatas != null && fileDatas.size() > 0) {
                for (MedicalTreatmentClaimFileDataVo fileData : fileDatas) {
                    if (fileData != null) {
                        fileData.setClaimsSeqId(seqId);
                        fileData.setFileBase64("");
                        String fileName = fileData.getFileName();
                        fileName=StringUtils.isEmpty(fileName)?fileData.getDtype():fileName;
                        fileData.setFileName(fileName);
                        j = iMedicalDao.addMedicalTreatmentFileData(fileData);
                    }
                }
            }
        }
        /**
         * 進行發送郵件信息
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
        return j;
    }

    @Override
    @Transactional
    public int updateTransMedicalTreatmentByTransNum(MedicalTreatmentClaimVo claimVo) throws Exception {
        /**
         * 1.更新之前保單的  聯盟狀態
         *   2.更新當前保單的文件狀態(存在更新),不存在新增
         * */
        int  j=0;
        String transNum = claimVo.getTransNum();
        String allianceStatus = claimVo.getAllianceStatus();
        j=iMedicalDao.updateTransMedicalTreatmentByTransNum(transNum,allianceStatus);
        List<MedicalTreatmentClaimFileDataVo> fileDatas = claimVo.getFileDatas();
        if (fileDatas!=null && fileDatas.size()>0) {
            for (MedicalTreatmentClaimFileDataVo fileData : fileDatas) {
                String fileId = fileData.getFileId();
                if (!StringUtils.isEmpty(fileId)) {
                  int i=  iMedicalDao.getTransMedicalTreatmentFiledatasByFileId(fileId);
                    String fileStatus = fileData.getFileStatus();
                    if (!StringUtils.isEmpty(fileStatus)) {
                        if (i>0) {
                            j = iMedicalDao.updateTarnsMedicalTreatmentFileStatus(fileId,fileStatus);
                        }else {
                            float claimsSeqId=  iMedicalDao.getTransMedicalTreatmentByClaimsSeqId(transNum);
                            fileData.setClaimsSeqId(claimsSeqId);
                            fileData.setFileBase64("");
                            String fileName = fileData.getFileName();
                            fileName=StringUtils.isEmpty(fileName)?fileData.getDtype():fileName;
                            fileData.setFileName(fileName);
                            j = iMedicalDao.addTarnsMedicalTreatmentFile(fileData);
                        }
                    }
                }
            }
        }
        /**
         * 進行發送郵件信息
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
            paramMap.put("STATUS_MESSAGE", statusMessage);
            paramMap.put("HOSPITAL_CODE", claimVo.getToHospitalId());
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
        List<TransMedicalTreatmentClaimVo>  voList=   iMedicalDao.getTransMedicalTreatmentBySendAlliance(StatuCode.AUDIT_CODE_Y.code);
          int  rtn=0;
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


    private MessageTriggerRequestVo getMessageTriggerRequestVo(String msgCode, List<String> receivers, Map<String, String> paramMap,String type) {
        MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
        vo.setMessagingTemplateCode(msgCode);
        vo.setSendType(type);
        vo.setMessagingReceivers(receivers);
        vo.setParameters(paramMap);
        vo.setSystemId(ApConstants.SYSTEM_ID);
        return vo;
    }
    
    public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
    	if(responseEntity==null) {
    		return false;
    	}
    	
        logger.info("http status=" + responseEntity.getStatusCodeValue());
        if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
            // 200 OK
            return true;
        } else {
            return false;
        }
    }
    
    private String getResInfo(String strRes) {
        String str = "";
        try {
            if (strRes != null) {
                str = "{\"code\":"+MyJacksonUtil.getNodeString(strRes, "code")+",\"msg\":"+MyJacksonUtil.getNodeString(strRes, "msg")+"}";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

}
