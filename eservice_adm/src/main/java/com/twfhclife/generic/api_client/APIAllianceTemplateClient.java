package com.twfhclife.generic.api_client;

import com.twfhclife.generic.api_model.*;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class APIAllianceTemplateClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(APIAllianceTemplateClient.class);

	@Value("${eservice_api.APIAlliance-template.trigger.url}")
	private String API_ALLIANCE_URI;//alliance-template/APIAlliance


	//參數類型管理-查詢
	public ReturnHeader apiAlliance(APIAllianceRequestVo requestBody) {
		ApiResponseObj apiResponseObj = new ApiResponseObj();
		ReturnHeader returnHeader = null;
		String url = API_ALLIANCE_URI;//
		//String url = "http://localhost:8083/eservice_api/alliance-template/APIAlliance";
		logger.debug("invoke msgApi post api: url=" + url + ", requestBody=" + MyJacksonUtil.object2Json(requestBody));

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		//headerMap.put("Authorization", "Bearer " + new String("22fa002a-ceb3-31de-9b0a-0f86f73b317b"));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<APIAllianceRequestVo> entity = new HttpEntity<>(requestBody, headers);

		ParameterizedTypeReference<ApiResponseObj> typeRef = new ParameterizedTypeReference<ApiResponseObj>() {
		};
		ResponseEntity<ApiResponseObj> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, typeRef);
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}
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
		APIAllianceTemplateClient pc = new APIAllianceTemplateClient();
		APIAllianceRequestVo api = new APIAllianceRequestVo();
		//3.call api-406 to
		Map<String, String> params = new HashMap<>();
		String caseId = "20210125153001-45c17f68e615-L31";
		String transNum = "202110130002";
		params.put("caseId",caseId);
		//聯盟鏈歷程參數
		Map<String, String> unParams = new HashMap<>();
		unParams.put("name", "API-406 已完成案件申請");
		unParams.put("caseId", caseId);
		unParams.put("transNum", transNum);
		api.setUrl("HTTP:12111111");
		api.setParams(params);
		api.setUnParams(unParams);

		pc.apiAlliance(api);
	}
}
