package com.twfhclife.jd.service.impl;

import com.twfhclife.jd.api_model.CommLogRequest;
import com.twfhclife.jd.service.IMailService;
import com.twfhclife.jd.service.ISendAuthenticationService;
import com.twfhclife.jd.service.ISendSmsService;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.util.HttpUtil;
import com.twfhclife.jd.util.MyStringUtil;
import com.twfhclife.jd.web.dao.ParameterDao;
import com.twfhclife.jd.web.service.IParameterService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class SendAuthenticationServiceImpl implements ISendAuthenticationService {
	
	private static final Logger logger = LogManager.getLogger(SendAuthenticationServiceImpl.class);
	
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IMailService mailService;
	@Autowired
	private IParameterService parameterService;
	@Autowired
	private ParameterDao parameterDao;
	
	private static String url;
	private static String accessKey;

	private HttpUtil httpUtil = new HttpUtil();

	/**
	 * 寄送認證碼
	 * 
	 * @param emails   格式  email1;email2;
	 * @param mobile    格式  email1;email2;
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String sendAuthentication(String emails, String mobile) throws Exception {
		Random rnd = SecureRandom.getInstanceStrong();
		String authentication = "";
		
		// 20210421 調整應至少6位數
		//for(int i=0; i<4 ; i++){
		for(int i=0; i<6 ; i++){
			authentication = authentication + rnd.nextInt(10);
		}
		String content = parameterService.getParameterValueByCode(null, "AUTH_CODE_CONT");
		content = content.replace("#{A}", authentication);
		//String content = "您的驗證碼為: "+ authentication +" 請於五分鐘內輸入您的驗證碼進行認證。";
		logger.debug("email=" + emails + ", mobile=" + mobile + ", content=" + content);
		if(MyStringUtil.isNullOrEmpty(emails) && MyStringUtil.isNullOrEmpty(mobile)) {
			throw new RuntimeException("email and mobile is all empty.");
		}

		if (url == null) {
			url = parameterDao.getParameterValueByCode("eservice_batch", "COMM_LOG_ADD_URL");
		}
		if (accessKey == null) {
			accessKey = parameterDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
		}
		
		String emailErr = "";
		try {
			if (emails != null) {
				if(!emails.equals("")){
					//設定於代碼中 台銀人壽 驗證碼 寄送郵件
					String subject = parameterService.getParameterValueByCode(null, "AUTH_CODE_SUB");
					mailService.sendMail(content, subject, emails, "", null);
					try {
						httpUtil.postCommLogAdd(url, accessKey, new CommLogRequest(ApConstants.SYSTEM_ID, "email", emails, content));
					} catch (Exception e) {
						logger.error("Unable to postCommLogAdd(email) in SendAuthenticationServiceImpl: {}", ExceptionUtils.getStackTrace(e));
					}
				}
			}
		} catch(Exception e) {
			emailErr = e.getMessage();
		}

		if(MyStringUtil.isNotNullOrEmpty(emails) && MyStringUtil.isNotNullOrEmpty(emailErr)) {
			throw new RuntimeException("Send email fail.");
		}
		
		return authentication;
	}
	
}
