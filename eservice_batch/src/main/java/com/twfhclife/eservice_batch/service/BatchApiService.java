package com.twfhclife.eservice_batch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.MessageTriggerRequestVo;

public class BatchApiService {

	private static final Logger logger = LogManager.getLogger(BatchApiService.class);
	
	private ParameterDao parameterDao = new ParameterDao();
	
	public void postMessageTemplateTrigger(MessageTriggerRequestVo req)
			throws ClientProtocolException, IOException {
		try {
			String url = parameterDao.getParameterValueByCode("eservice_batch", "MESSAGE_TRIGGER_URL");
			String accessKey = parameterDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
			logger.debug("postMessageTemplateTrigger:url={}, accessKey={}, req={}", url, accessKey, new Gson().toJson(req));
			
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			String json = new Gson().toJson(req);
			StringEntity entity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(entity);
			httpPost.setHeader("Authorization", "Bearer " + accessKey);
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = client.execute(httpPost);
			client.close();
		} catch (Exception e) {
			logger.error("BatchApiService.postMessageTemplateTrigger error: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	public void postCommLogAdd(CommLogRequestVo req)
			throws ClientProtocolException, IOException {
		try {
			String url = parameterDao.getParameterValueByCode("eservice_batch", "COMM_LOG_ADD_URL");
			String accessKey = parameterDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
			logger.debug("postCommLogAdd:url={}, accessKey={}, req={}", url, accessKey, new Gson().toJson(req));
			
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			String json = new Gson().toJson(req);
			StringEntity entity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(entity);
			httpPost.setHeader("Authorization", "Bearer " + accessKey);
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = client.execute(httpPost);
			client.close();
		} catch (Exception e) {
			logger.error("BatchApiService.postCommLogAdd error: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		BatchApiService api = new BatchApiService();
		/*try {
			MessageTriggerRequestVo req = new MessageTriggerRequestVo();
			req.setMessagingTemplateCode("ELIFE_MAIL-004");
			req.setSendType(MessageTriggerRequestVo.SEND_TYPE_MAIL);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransNum", "2019061700001");
			paramMap.put("TransStatus", "失敗");
			paramMap.put("TransRemark", "TEST");
			req.setParameters(paramMap);
			
			List<String> receivers = new ArrayList<String>();
			receivers.add("evanlin01k@gmail.com");
			req.setMessagingReceivers(receivers);
			
			String url = "http://220.133.126.209:8084/eservice_api/message-template/trigger";
			String accessKey = "22fa002a-ceb3-31de-9b0a-0f86f73b317b";
			api.postMessageTemplateTrigger(url, accessKey, req);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		api.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", "test123@gmail.com", "test123"));
		api.postCommLogAdd(new CommLogRequestVo("eservice_batch", "sms", "0999999999", "test456"));
	}
}
