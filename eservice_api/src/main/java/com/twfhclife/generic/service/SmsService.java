package com.twfhclife.generic.service;

import java.util.Base64;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.dao.adm.ParameterDao;
import com.twfhclife.generic.service.SmsService;

@Service
public class SmsService {
	
	/**
	 * Logger utility.
	 */
	private static final Logger logger = LogManager.getLogger(MailService.class);

	@Autowired
	@Qualifier("apiParameterDao")
	private ParameterDao parameterDao;
	
	/**
	 * https://www.nexmo.com/products/sms/build
	 * @param	mobile	收件者
	 * @param	content	內容
	 */
	public void sendSms(String mobile, String content){
		// String key = parameterDao.getParameterValueByCode(null, "SMS_KEY");
		// String secret = parameterDao.getParameterValueByCode(null, "SMS_SECRET");
		// String sender = parameterDao.getParameterValueByCode(null, "SMS_SENDER");
		//
		// /* 測試用
		// key = "ceca07c1";
		// secret = "WSFUXHT5H66nNOVv";
		// sender = "NEXMO";
		// */
		//
		// mobile = "886"+mobile.substring(1,mobile.length());
		//
		// //記得去官網申請key 現在是測試key
		// AuthMethod auth = new TokenAuthMethod(key, secret);
		// NexmoClient client = new NexmoClient(auth);
		//
		// SmsSubmissionResult[] responses;
		// try {
		// //參數為: ( 寄送者, 收件者, 內容);
		// responses = client.getSmsClient().submitMessage(new TextMessage( sender,
		// mobile, content));
		// for (SmsSubmissionResult response : responses) {
		// System.out.println(response);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (NexmoClientException e) {
		// e.printStackTrace();
		// }

		final Base64.Encoder encoder = Base64.getEncoder();

		String endpoint = parameterDao.getParameterValueByCode(null, "SMS_ENDPOINT");
		String user = parameterDao.getParameterValueByCode(null, "SMS_USER");
		String pass = parameterDao.getParameterValueByCode(null, "SMS_PASS");
		
		String requestXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "  <soap:Body>" + "    <SendSMSwithUserData xmlns=\"http://tempuri.org/\">"
				+ "      <DstAddr>_Mobile_</DstAddr>" + "      <text>_Content_</text>" + "      <user>_user_</user>"
				+ "      <pass>_pass_</pass>" + "      <stoptime></stoptime>" + "      <isLong>0</isLong>"
				+ "      <Data1></Data1>" + "      <Data2></Data2>" + "      <Data3></Data3>" + "      <Data4></Data4>"
				+ "    </SendSMSwithUserData>" + "  </soap:Body>" + "</soap:Envelope>";

		logger.debug("SMS_ENDPOINT=" + endpoint + ", SMS_USER=" + user + ", SMS_PASS=" + pass);
		// 編碼
		String encodedPass = null;
		PostMethod post = null;
		try {
			encodedPass = encoder.encodeToString(pass.getBytes("UTF-8"));
//			user = "user";//test-code
//			encodedPass = "pass";//test-code
			requestXML = requestXML.replaceAll("_Mobile_", mobile).replaceAll("_Content_", content)
					.replaceAll("_user_", user).replaceAll("_pass_", encodedPass);
			logger.debug("send sms requestXML:" + requestXML);

			RequestEntity entity = new StringRequestEntity(requestXML, "application/xml", "utf-8");

			post = new PostMethod(endpoint);
			post.setRequestEntity(entity);
			post.setRequestHeader("SOAPAction", "http://tempuri.org/SendSMSwithUserData");
			post.setRequestHeader("Content-type", "text/xml; charset=utf-8");
			post.setRequestHeader("Content-Length", entity.getContentLength() + "");
			
			HttpClient httpclient = new HttpClient();
			int result = httpclient.executeMethod(post);
			logger.debug("Response status code: " + result);
			logger.debug("Response body: ");
			logger.debug(post.getResponseBodyAsString());
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
		
	}
	
	public static void main(String[] agres){
		SmsService exp = new SmsService();
		exp.sendSms("09xxxxxxxx", "Hello from Nexmo!");
	}
	
}
