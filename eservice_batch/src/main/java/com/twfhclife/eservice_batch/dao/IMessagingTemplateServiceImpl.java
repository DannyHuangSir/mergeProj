package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.MessageTriggerRequestVo;
import com.twfhclife.eservice_batch.model.MessagingTemplateVo;
import com.twfhclife.eservice_batch.service.BatchApiService;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hui.chen
 * @create 2021-07-23
 */
public class IMessagingTemplateServiceImpl implements  IMessagingTemplateService {
    private static final Logger logger = LogManager.getLogger(IMessagingTemplateServiceImpl.class);
    private  MessageTemplateMapperDao  messageTemplateMapperDao =new MessageTemplateMapperDao();
    private  ParameterDao  parameterDao =new ParameterDao();

    /**
     * 觸發通信模板-通信API
     *
     * @param email
     * @param mobile
     * @return
     */
    @Override
    public String triggerMessageTemplate(MessageTriggerRequestVo requestVo){
    	logger.info("**In triggerMessageTemplate**");
        String errMsg = "";
        try {
            String sendType = requestVo.getSendType();

            MessagingTemplateVo template = messageTemplateMapperDao.getMessageTemplateByCode(requestVo.getMessagingTemplateCode());
            String content = template.getMessagingContent();
            logger.info("content="+content);
            //replace ${NOW}-start
            Map<String, String> valuesMap = new HashMap<String, String>();
            valuesMap.put("NOW", getNowString());//replace ${NOW}
            StringSubstitutor sub = new StringSubstitutor(valuesMap);
            content = sub.replace(content);
            //replace ${NOW}-end
            logger.info("content="+content);
            
            String params = template.getParams();
            if(MyStringUtil.isNotNullOrEmpty(params)) {
                String[] paramList = params.split("\\,");
                if(paramList != null && paramList.length > 0) {
                    Map<String, String> paramMap = requestVo.getParameters();
                    for(String key : paramList) {
                        if(MyStringUtil.isNotNullOrEmpty(key)) {
                            if(MyStringUtil.isNullOrEmpty(paramMap.get(key))) {
                                // 缺少所需參數
                                errMsg = "缺少所需參數:"+key;
                                logger.info("errMsg="+errMsg);
                                return errMsg;
                            }
                            content = content.replace("${"+key+"}", paramMap.get(key));
                        }
                    }
                }
            }
            logger.info("content="+content);

            List<String> receivers = null;
            if("dynamic".equals(template.getReceiverMode())) {
                if(requestVo.getMessagingReceivers() == null ) {
                    errMsg = "此通信模板收件者為動態指定，請輸入收件者資訊";
                    logger.info("errMsg="+errMsg);
                    return errMsg;
                }
                //動態指定收件者
                receivers = requestVo.getMessagingReceivers();
            } else {
                //撈設定的收件者
                receivers = messageTemplateMapperDao.getReceiversByTemplateId(template.getMessagingTemplateId().intValue(), sendType);
            }
            if(sendType.equals("email")) {
                String subject = template.getMessagingSubject();
                MailService mailService = new MailService();
                // logger.debug("Send mail: email=" + MyJacksonUtil.object2Json(receivers) + ", subject=" + subject + ", content=" + content);
                logger.debug("Send mail: email=" +receivers+ ", subject=" + subject + ", content=" + content);
                mailService.sendMail(content, subject, receivers, null, null);

                // 儲存郵件簡訊發送紀錄
                BatchApiService apiService = new BatchApiService();
                for (String addr : receivers) {
                    try {
                        apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr,content));
                    } catch (Exception e) {
                        logger.error("Unable to addCommLog in IMessagingTemplateServiceImpl: {}", ExceptionUtils.getStackTrace(e));
                    }
                }
            }

        } catch (Exception e) {
            logger.error("triggerMessageTemplate: {}", e.toString());
            errMsg = e.getMessage();
        }
        logger.info("**Out triggerMessageTemplate**");
        return errMsg;
    }

    @Override
    public Map<String, Object> getSendMailInfo() {


        String transRemark = this.parameterDao.getStatusName("MessagingParameter", "CONTACT_INFO_TRANS_REMARK");
        String mailTo = this.parameterDao.getParameterValueByCode("eservice_adm", "security_alliance_twfhclife_adm");
        String[] mails = mailTo.split(";");
        Map<String, Object> rMap = new HashMap();
        List<String> receivers = new ArrayList();
        if (mails.length > 0) {
            String[] var6 = mails;
            int var7 = mails.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String mail = var6[var8];
                receivers.add(mail);
                logger.info("Mail Address : " + mail);
            }
        }

        rMap.put("transRemark", transRemark);
        rMap.put("receivers", receivers);
        return rMap;
    }

    private String getNowString() {
        String strNow = "";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
        strNow = formater.format(System.currentTimeMillis());
        return strNow;
    }


}
