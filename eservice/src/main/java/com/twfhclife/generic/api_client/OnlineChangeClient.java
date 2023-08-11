package com.twfhclife.generic.api_client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.twfhclife.eservice.odm.OnlineChangeModel;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class OnlineChangeClient {

	private static final Logger logger = LogManager.getLogger(OnlineChangeClient.class);
	
	private RestTemplate restTemplate;
	
	public OnlineChangeClient() throws Exception {
		
		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
				NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		restTemplate = (this.restTemplate == null) ? new RestTemplate(requestFactory) : restTemplate;
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}
	
	public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
	}
	
	public String postForEntity(String url, OnlineChangeModel model) throws Exception {
		String strRes = null;
		if(url!=null && model!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			//headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			Gson gson = new Gson(); 
	        String json = gson.toJson(model);
	        
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);

	        if (!this.checkResponseStatus(responseEntity)) {
				return null;
			}
	        
	        strRes= responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);

		}
		return strRes;
	}
	
	public String postForEntity(String url, Map<String,String> mapVo) throws Exception {
		String strRes = null;
		if(url!=null && mapVo!=null && !mapVo.isEmpty()) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			//headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			Gson gson = new Gson(); 
	        String json = gson.toJson(mapVo);
	        
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);

	        if (!this.checkResponseStatus(responseEntity)) {
				return null;
			}
	        
	        strRes= responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);

		}
		return strRes;
	}
	
	/**
	 * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
	 * @param responseJsonString
	 * @param pathFieldName ex:"/code"
	 * @param checkValue ex:"0"
	 * @return boolean
	 */
	public boolean checkLiaAPIResponseValue(String responseJsonString,String pathFieldName,String checkValue) throws Exception{
		boolean b = false;

		if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
			String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
			System.out.println("checkLiaAPIResponseValue="+code);
			if(checkValue.equals(code)) {//success
				b = true;
			}
		}
		System.out.println("checkLiaAPIResponseValue,return="+b);
		return b;
	}
	
	/**
	 * 從JSON String中取出指定json path的值
	 * @param jsonString ex:{"code": "0","msg": "success","data": {"fileReceived": "1"}}
	 * @param pathFieldName ex:"/data/fileRecived"
	 * @return String
	 * @throws Exception
	 */
	public static String readValue(String jsonString, String pathFieldName) throws Exception{
		String rtn = null;
		if(jsonString!=null && pathFieldName!=null) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonString);
			
			if(jsonNode!=null) {
				rtn = jsonNode.at(pathFieldName).asText();
			}
		}
		return rtn;
	}

	public String postForParams(String url, Map<String, String> params) throws Exception {
		String strRes = null;
		if (url != null && params != null) {
			ResponseEntity<String> responseEntity = null;
			HttpHeaders headers = new HttpHeaders();
			String secret = BaseRestClient.getAccessKey();
			if(StringUtils.isNotBlank(secret)) {
				headers.add("Authorization", "Bearer " + secret);
			}

			MultiValueMap<String, String> httpParams = new LinkedMultiValueMap();
			params.entrySet().forEach(e -> httpParams.add(e.getKey(), e.getValue()));

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(httpParams, headers);
			responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			if (!this.checkResponseStatus(responseEntity)) {
				return null;
			}

			strRes = responseEntity.getBody();
			logger.info("responseEntity.getBody()=" + strRes);

		}
		return strRes;
	}

}
