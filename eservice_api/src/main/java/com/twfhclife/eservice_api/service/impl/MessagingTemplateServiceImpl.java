package com.twfhclife.eservice_api.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.api.adm.domain.MessagingTemplateReqVo;
import com.twfhclife.eservice.api.adm.model.MessagingTemplateVo;
import com.twfhclife.eservice_api.service.ICommLogService;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.dao.adm.MessageTemplateDao;
import com.twfhclife.generic.domain.CommLogRequest;
import com.twfhclife.generic.service.MailService;
import com.twfhclife.generic.service.SmsService;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.MyStringUtil;

@Service
public class MessagingTemplateServiceImpl implements IMessagingTemplateService{
	
	private static final Logger logger = LogManager.getLogger(MessagingTemplateServiceImpl.class);
	
	@Autowired
	private SmsService smsService;
	@Autowired
	private MailService mailService;
	@Autowired
	private IParameterService parameterService;
	@Autowired
	private MessageTemplateDao messageTemplateDao;
	@Autowired
	private ICommLogService commLogService;

	/**
	 * 觸發通信模板-通信API
	 * 
	 * @param email
	 * @param mobile
	 * @return
	 */
	@Override
	public String triggerMessageTemplate(MessageTriggerRequestVo requestVo){
		String errMsg = "";
		try {
			String sendType = requestVo.getSendType();
			
			MessagingTemplateVo template = messageTemplateDao.getMessageTemplateByCode(requestVo.getMessagingTemplateCode());
			String content = template.getMessagingContent();
			
			//replace ${NOW}-start
			Map<String, String> valuesMap = new java.util.HashMap<String, String>();
			valuesMap.put("NOW", getNowString());//replace ${NOW}
			StringSubstitutor sub = new StringSubstitutor(valuesMap);
			content = sub.replace(content);
			//replace ${NOW}-end
			
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
							return errMsg;
						}
						content = content.replace("${"+key+"}", paramMap.get(key));
					}
				}
			}
			}
			
			List<String> receivers = null;
			if("dynamic".equals(template.getReceiverMode())) {
				if(requestVo.getMessagingReceivers() == null ) {
					errMsg = "此通信模板收件者為動態指定，請輸入收件者資訊";
					return errMsg;
				}
				//動態指定收件者
				receivers = requestVo.getMessagingReceivers();
			} else {
				//撈設定的收件者
				receivers = messageTemplateDao.getReceiversByTemplateId(template.getMessagingTemplateId().intValue(), sendType);
			}
			if(sendType.equals("email")) {
				String subject = template.getMessagingSubject();
				logger.debug("Send mail: email=" + MyJacksonUtil.object2Json(receivers) + ", subject=" + subject + ", content=" + content);
				mailService.sendMail(content, subject, receivers, null, null);
				// 儲存郵件簡訊發送紀錄
				for (String target : receivers) {
					try {
						commLogService.addCommLog(new CommLogRequest(requestVo.getSystemId(), sendType, target, content));
					} catch (Exception e) {
						logger.error("Unable to addCommLog in MessagingTemplateController: {}", ExceptionUtils.getStackTrace(e));
					}
				}
			} else if(sendType.equals("sms")) {
				String mobile = receivers.get(0);
				logger.debug("Send sms: mobile=" + mobile + ", content=" + content);
				smsService.sendSms(mobile, content);
				try {
					commLogService.addCommLog(new CommLogRequest(requestVo.getSystemId(), sendType, mobile, content));
				} catch (Exception e) {
					logger.error("Unable to addCommLog in MessagingTemplateController: {}", ExceptionUtils.getStackTrace(e));
				}
			}
			
		} catch (Exception e) {
			logger.error("sendAuthentication: {}", ExceptionUtils.getStackTrace(e));
			return e.getMessage();
		}
		return errMsg;
	}
	
	private String getNowString() {
		String strNow = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
		strNow = formater.format(System.currentTimeMillis());
		return strNow;
	}
	
	
	@Override
	public List<Map<String, Object>> getMessagingTemplatePageList(MessagingTemplateReqVo messagingTemplateVo) {
		return messageTemplateDao.getMessagingTemplatePageList(messagingTemplateVo);
	}
	
	@Override
	public int getMessagingTemplatePageTotal(MessagingTemplateReqVo messagingTemplateVo) {
		return messageTemplateDao.getMessagingTemplatePageTotal(messagingTemplateVo);
	}
	
	public static void main(String[] args) {
		MessagingTemplateServiceImpl impl = new MessagingTemplateServiceImpl();
		
		String templateString = "abcd${NOW}efg";
		
		Map<String, String> valuesMap = new java.util.HashMap<String, String>();
		valuesMap.put("NOW", impl.getNowString());
		
		StringSubstitutor sub = new StringSubstitutor(valuesMap);

		templateString = sub.replace(templateString);
		
		System.out.println(templateString);
	}

}
