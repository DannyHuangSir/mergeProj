package com.twfhclife.generic.util;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.generic.api_model.CommLogRequest;

public class HttpUtil {

	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	
	public void postCommLogAdd(String url, String accessKey, CommLogRequest req)
			throws ClientProtocolException, IOException {
		try {
			logger.debug("postCommLogAdd:url={}, accessKey={}, req={}", url, accessKey, MyJacksonUtil.object2Json(req));
			
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			String json = MyJacksonUtil.object2Json(req);
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
}
