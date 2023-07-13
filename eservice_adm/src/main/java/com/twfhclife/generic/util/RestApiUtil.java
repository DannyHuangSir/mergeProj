package com.twfhclife.generic.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.twfhclife.adm.domain.HttpResponseVo;
import com.twfhclife.generic.api_client.BaseRestClient;

@Service
public class RestApiUtil {
	
	private static final Logger logger = LogManager.getLogger(RestApiUtil.class);
	
	@Value("${eservice_api.domain}")
	public String API_DOMAIN;
	
	@Value("${keycloak.adm-realm:twfhclife}")
	public String ADM_REALM;
	
	@Value("${keycloak.adm-clientId:eservice_adm}")
	public String ADM_CLIENT;
	
	@Value("${eservice_api.secret}")
	public static String ESERVICE_API_SECRET;
	
	public HttpResponseVo postApi(String apiUri, Map<String, String> headerParams, Map<String, String> pathParams, Map<String, Object> postParams) {
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = API_DOMAIN + apiUri;
		if(pathParams != null) {
			for (String param : pathParams.keySet()) {
				uri = uri.replace("{" + param + "}", pathParams.get(param));
			}
		}
		logger.info("Calling Keycloak Admin API, Method=POST, URI=" + uri);
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(uri);
		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		if(postParams != null) {
			for (String param : postParams.keySet()) {
				if(postParams.get(param) != null) {
					urlParameters.add(new BasicNameValuePair(param, postParams.get(param).toString()));
				}
			}
		}
		
		if(headerParams != null) {
			for (String param : headerParams.keySet()) {
				post.setHeader(param, headerParams.get(param));
			}
		}

		try {
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(post);
			httpResponseVo.setHeader(response.getAllHeaders());
			int statusCode = response.getStatusLine().getStatusCode();
			httpResponseVo.setStatusCode(statusCode);
			logger.info("Response Code : " + statusCode);

			if(response.getEntity() != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line1 = "";
				while ((line1 = rd.readLine()) != null) {
					result.append(line1);
				}
				resultString = result.toString();
				httpResponseVo.setResponseBody(resultString);
				logger.debug("resultString=" + resultString);
//				JacksonJsonParser jjp = new JacksonJsonParser();
//				Map<String, Object> map = jjp.parseMap(result.toString());
//				System.out.println("access_token:" + map.get("access_token"));
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return httpResponseVo;
	}
	
//	private I requestBody;
//
//	private O responseBody;
//
//	public O post(String url, Map<String, String> headerMap, I requestBody, O responseBody) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		if (headerMap != null) {
//			for (String key : headerMap.keySet()) {
//				headers.add(key, headerMap.get(key));
//			}
//		}
//
//		HttpEntity<I> entity = new HttpEntity<I>(requestBody, headers);
//
//		ResponseEntity responseEntity = new RestTemplate().postForEntity(url, entity, responseBody.getClass());
//		O res = (O) responseEntity.getBody();
//		return res;
//	}
//
//	public O get(String url, Map<String, String> headerMap, Map<String, String> requestParam, O responseBody) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//		if (headerMap != null) {
//			for (String key : headerMap.keySet()) {
//				headers.add(key, headerMap.get(key));
//			}
//		}
//		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
//		if (requestParam != null && !requestParam.isEmpty()) {
//			for (String key : requestParam.keySet()) {
//				builder.queryParam(key, requestParam.get(key));
//			}
//		}
//		HttpEntity<I> entity = new HttpEntity<I>(headers);
//
//		ResponseEntity responseEntity = new RestTemplate().exchange(builder.build().encode().toUri(), HttpMethod.GET,
//				entity, responseBody.getClass());
//
//		O res = (O) responseEntity.getBody();
//
//		return res;
//	}
}
