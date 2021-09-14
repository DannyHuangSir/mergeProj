package com.twfhclife.generic.api_client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.MessageTriggerRequestVo;
import com.twfhclife.generic.api_model.PageResponseObj;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class MessageTemplateClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(MessageTemplateClient.class);

	@Value("${eservice_api.message-template.trigger.url}")
	private String MSG_API_URI;//message-template/trigger


	//參數類型管理-查詢
	public ReturnHeader msgApi(MessageTriggerRequestVo requestBody) {
		ApiResponseObj apiResponseObj = new ApiResponseObj();
		ReturnHeader returnHeader = null;
		String url = MSG_API_URI;// "http://220.133.126.209:8084/eservice_api/message-template/trigger";
		//url = "http://220.133.126.209:8084/eservice_api/message-template/trigger";
		logger.debug("invoke msgApi post api: url=" + url + ", requestBody=" + MyJacksonUtil.object2Json(requestBody));

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<MessageTriggerRequestVo> entity = new HttpEntity<>(requestBody, headers);

		ParameterizedTypeReference<ApiResponseObj> typeRef = new ParameterizedTypeReference<ApiResponseObj>() {
		};
		ResponseEntity<ApiResponseObj> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, typeRef);
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof ApiResponseObj) {
				apiResponseObj = (ApiResponseObj<PageResponseObj<Map<String, Object>>>) obj;
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
		logger.info("searchParamCategory result = " + returnHeader.getReturnCode());
		return returnHeader;
	}
	
	
	public static void main(String[] args) {
		MessageTemplateClient pc = new MessageTemplateClient();
		MessageTriggerRequestVo i = new MessageTriggerRequestVo();
		i.setMessagingTemplateCode("ELIFE-MAIL_001");
		i.setSendType("email");
		List<String> mm = new ArrayList<>();
		mm.add("evanlin01k@gmail.com");
		i.setMessagingReceivers(mm);
		Map<String, String> pp = new HashMap<>();
		pp.put("Username", "AA123");
		pp.put("Password", "1234567");
		i.setParameters(pp);
		pc.msgApi(i);
	}
}
