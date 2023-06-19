package com.twfhclife.generic.api_client;

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
import com.twfhclife.generic.api_model.PageResponseObj;
import com.twfhclife.generic.api_model.ParamCategoryRequestVo;
import com.twfhclife.generic.api_model.ParamRequestVo;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class ParameterClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(SsoClient.class);

	@Value("${eservice_api.param-category.searches.url}")
	private String PARAMCATE_SEARCH_URI;
//	@Value("${eservice_api.param-category.create.url}")
//	private String PARAMCATE_CREATE_URI;
//	@Value("${eservice_api.param-category.update.url}")
//	private String PARAMCATE_UPDATE_URI;
//	@Value("${eservice_api.param-category.delete.url}")
//	private String PARAMCATE_DELETE_URI;
	
	@Value("${eservice_api.param.searches.url}")
	private String PARAM_SEARCH_URI;
//	@Value("${eservice_api.param.create.url}")
//	private String PARAM_CREATE_URI;
//	@Value("${eservice_api.param.update.url}")
//	private String PARAM_UPDATE_URI;
//	@Value("${eservice_api.param.delete.url}")
//	private String PARAM_DELETE_URI;

	//參數類型管理-查詢
	public PageResponseObj<Map<String, Object>> searchParamCategory(ParamCategoryRequestVo requestBody) {
		PageResponseObj<Map<String, Object>> pageResponseObj = null;
		ApiResponseObj<PageResponseObj<Map<String, Object>>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = null;
		String url = PARAMCATE_SEARCH_URI;// "http://220.133.126.209:8084/eservice_api/sso/login";
		logger.debug("invoke login post api: url=" + url + ", requestBody=" + MyJacksonUtil.object2Json(requestBody));

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<ParamCategoryRequestVo> entity = new HttpEntity<>(requestBody, headers);

		ParameterizedTypeReference<ApiResponseObj<PageResponseObj<Map<String, Object>>>> typeRef = new ParameterizedTypeReference<ApiResponseObj<PageResponseObj<Map<String, Object>>>>() {
		};
		ResponseEntity<ApiResponseObj<PageResponseObj<Map<String, Object>>>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, typeRef);
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
				pageResponseObj = apiResponseObj.getResult();
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
		logger.info("searchParamCategory result = " + returnHeader.getReturnCode());
		return pageResponseObj;
	}
	
	//參數管理-查詢
	public PageResponseObj<Map<String, Object>> searchParameter(ParamRequestVo requestBody) {
		PageResponseObj<Map<String, Object>> pageResponseObj = null;
		ApiResponseObj<PageResponseObj<Map<String, Object>>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = null;
		String url = PARAM_SEARCH_URI;// "http://220.133.126.209:8084/eservice_api/sso/login";

//		String url = "http://220.133.126.209:8280/searchParam/v1.0/eservice_api/param/searches";
//		url = "http://220.133.126.209:8084/eservice_api/param/searches";
		logger.debug("invoke login post api: url=" + url + ", requestBody=" + MyJacksonUtil.object2Json(requestBody));

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<ParamRequestVo> entity = new HttpEntity<>(requestBody, headers);

		ParameterizedTypeReference<ApiResponseObj<PageResponseObj<Map<String, Object>>>> typeRef = new ParameterizedTypeReference<ApiResponseObj<PageResponseObj<Map<String, Object>>>>() {
		};
		ResponseEntity<ApiResponseObj<PageResponseObj<Map<String, Object>>>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, typeRef);
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
				pageResponseObj = apiResponseObj.getResult();
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
		logger.info("searchParamCategory result = " + returnHeader.getReturnCode());
		return pageResponseObj;
	}
	
	public static void main(String[] args) {
		ParameterClient pc = new ParameterClient();
		ParamRequestVo i = new ParamRequestVo();
		i.setSystemId("eform");
		i.setUserId("davidyu");
		i.setRows(10);
		i.setPage(1);
		PageResponseObj res = pc.searchParameter(i);
		List list = res.getPageData();
	}
}
