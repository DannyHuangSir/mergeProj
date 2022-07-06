package com.twfhclife.generic.api_client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.BenefitDetailResponse;
import com.twfhclife.generic.api_model.DashboardResponse;
import com.twfhclife.generic.api_model.FunctionUsageAddResponse;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.generic.api_model.KeycloakUserResponse;
import com.twfhclife.generic.api_model.OnlineChangePolicyListResponse;
import com.twfhclife.generic.api_model.PolicyAcctValueResponse;
import com.twfhclife.generic.api_model.PolicyBonusResponse;
import com.twfhclife.generic.api_model.PolicyDataResponse;
import com.twfhclife.generic.api_model.PolicyDividendResponse;
import com.twfhclife.generic.api_model.PolicyFundTransactionResponse;
import com.twfhclife.generic.api_model.PolicyListResponse;
import com.twfhclife.generic.api_model.PolicyLoanResponse;
import com.twfhclife.generic.api_model.PolicyPaidResponse;
import com.twfhclife.generic.api_model.PolicyPaymentRecordResponse;
import com.twfhclife.generic.api_model.PolicyPremiumTransactionResponse;
import com.twfhclife.generic.api_model.PortfolioResponse;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
import com.twfhclife.generic.api_model.TransHistoryListResponse;
import com.twfhclife.generic.api_model.UserPolicyAcctValueResponse;
import com.twfhclife.generic.model.KeycloakUserSession;
import com.twfhclife.generic.util.MyJacksonUtil;

import javax.net.ssl.SSLContext;

@Service
public class BaseRestClient {
	private static final Logger logger = LogManager.getLogger(BaseRestClient.class);

	@Value("${eservice_api.accessKey}")
	private static String ESERVICE_API_SECRET;

	public static String getAccessKey() {
		return BaseRestClient.ESERVICE_API_SECRET;
	}
	
	public static void setAccessKey(String accessKey) {
		BaseRestClient.ESERVICE_API_SECRET = accessKey;
	}

	@SuppressWarnings("rawtypes")
	private Map<Class<?>, ParameterizedTypeReference> typeReferences() {
		final Map<Class<?>, ParameterizedTypeReference> map = new HashMap<>();
		map.put(BenefitDetailResponse.class, new ParameterizedTypeReference<ApiResponseObj<BenefitDetailResponse>>() { });
		map.put(DashboardResponse.class, new ParameterizedTypeReference<ApiResponseObj<DashboardResponse>>() { });
		map.put(PolicyAcctValueResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyAcctValueResponse>>() { });
		map.put(PolicyBonusResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyBonusResponse>>() { });
		map.put(PolicyPaidResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyPaidResponse>>() { });
		map.put(PolicyDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyDataResponse>>() { });
		map.put(PolicyDividendResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyDividendResponse>>() { });
		map.put(PolicyFundTransactionResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyFundTransactionResponse>>() { });
		map.put(PolicyListResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyListResponse>>() { });
		map.put(PolicyLoanResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyLoanResponse>>() { });
		map.put(PolicyPaymentRecordResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyPaymentRecordResponse>>() { });
		map.put(PolicyPremiumTransactionResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyPremiumTransactionResponse>>() { });
		map.put(PortfolioResponse.class, new ParameterizedTypeReference<ApiResponseObj<PortfolioResponse>>() { });
		map.put(UserPolicyAcctValueResponse.class, new ParameterizedTypeReference<ApiResponseObj<UserPolicyAcctValueResponse>>() { });
		map.put(OnlineChangePolicyListResponse.class, new ParameterizedTypeReference<ApiResponseObj<OnlineChangePolicyListResponse>>() { });
		map.put(KeycloakLoginResponse.class, new ParameterizedTypeReference<ApiResponseObj<KeycloakLoginResponse>>() { });
		map.put(TransHistoryListResponse.class, new ParameterizedTypeReference<ApiResponseObj<TransHistoryListResponse>>() { });
		map.put(TransHistoryDetailResponse.class, new ParameterizedTypeReference<ApiResponseObj<TransHistoryDetailResponse>>() { });
		map.put(TransAddResponse.class, new ParameterizedTypeReference<ApiResponseObj<TransAddResponse>>() { });
		map.put(KeycloakUserResponse.class, new ParameterizedTypeReference<ApiResponseObj<KeycloakUserResponse>>() {});
		map.put(FunctionUsageAddResponse.class, new ParameterizedTypeReference<ApiResponseObj<FunctionUsageAddResponse>>() {});
		ArrayList<KeycloakUserSession> userSessionResponse = new ArrayList<KeycloakUserSession>();
		map.put(userSessionResponse.getClass(), new ParameterizedTypeReference<ApiResponseObj<List<KeycloakUserSession>>>() { });
		
		return Collections.unmodifiableMap(map);
	}

	private RestTemplate restTemplate;

	@Autowired
	public BaseRestClient() {
//		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

		HttpComponentsClientHttpRequestFactory requestFactory = generateHttpRequestFactory();
//		requestFactory.setHttpClient(httpClient);

		restTemplate = new RestTemplate(requestFactory);
//		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
//		restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	public static HttpComponentsClientHttpRequestFactory generateHttpRequestFactory() {

		TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(httpClient);
		return factory;
	}

	public HttpHeaders setHeader(Map<String, String> headerMap) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (headerMap != null) {
			for (String key : headerMap.keySet()) {
				headers.set(key, headerMap.get(key));
			}
		}
		return headers;
	}

	public String buildGETurl(String baseUrl, Map<String, String> paramMap) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		if(paramMap != null) {
			for(String key : paramMap.keySet()) {
				builder.queryParam(key, paramMap.get(key));
			}
		}
		return builder.toUriString();

	}

	@SuppressWarnings("unchecked")
	public <T> T postApi(String postJson, String url, Class<T> clazz) {
		logger.debug("Invoke eservice api[{}]: {}", url, postJson);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json;charset=UTF-8");

		HttpHeaders headers = this.setHeader(headerMap);
//		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<?> entity = new HttpEntity<>(postJson, headers);
		
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();        
//        //Add the Jackson Message converter
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		// Note: here we are making this converter to process any kind of response, 
//		// not only application/*json, which is the default behaviour
//		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//		messageConverters.add(converter);  
//		restTemplate.setMessageConverters(messageConverters);  

		ResponseEntity<ApiResponseObj<T>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, typeReferences().get(clazz));
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}

		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return ((ApiResponseObj<T>) obj).getResult();
	}

	@SuppressWarnings("unchecked")
	public <T> ApiResponseObj<T> postApiResponse(String postJson, String url, Class<T> clazz) {
		logger.debug("Invoke eservice api[{}]: {}", url, postJson);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json");

		HttpHeaders headers = this.setHeader(headerMap);
		//headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(postJson, headers);

//		try {
//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();        
//			//Add the Jackson Message converter
//			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//			//converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
//			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//			messageConverters.add(converter);  
////			restTemplate.setMessageConverters(messageConverters);
//		} catch (Exception e) {
//			logger.error("restTemplate error!", e);
//		}  

		Object obj = null;
		try {
			ResponseEntity<ApiResponseObj<T>> responseEntity = restTemplate.exchange(url,
					HttpMethod.POST, entity, typeReferences().get(clazz));
			logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
			if (!this.checkResponseStatus(responseEntity)) {
				return null;
			}

			obj = responseEntity.getBody();
			if (obj == null) {
				return null;
			}
		} catch (Exception e) {
			logger.error("restTemplate.exchange() error!", e);
		}
		return (ApiResponseObj<T>) obj;
	}

	public String getPostApiReturnCode(String postJson, String url) {
		logger.debug("Invoke eservice api[{}]: {}", url, postJson);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json;charset=UTF-8");

		HttpHeaders headers = this.setHeader(headerMap);
		//headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(postJson, headers);

		new ParameterizedTypeReference<ApiResponseObj<TransHistoryDetailResponse>>() { };
		
		ResponseEntity<ApiResponseObj<?>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, new ParameterizedTypeReference<ApiResponseObj<?>>() { });
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}

		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return ((ApiResponseObj<?>) obj).getReturnHeader().getReturnCode();
	}

	public ApiResponseObj<?> getPostApiResponseObj(String postJson, String url) {
		logger.debug("Invoke eservice api[{}]: {}", url, postJson);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json;charset=UTF-8");

		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<?> entity = new HttpEntity<>(postJson, headers);

		new ParameterizedTypeReference<ApiResponseObj<TransHistoryDetailResponse>>() { };
		
		ResponseEntity<ApiResponseObj<?>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, new ParameterizedTypeReference<ApiResponseObj<?>>() { });
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}

		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return ((ApiResponseObj<?>) obj);
	}

	@SuppressWarnings("unchecked")
	public <T> T getApi(String url, Class<T> clazz) {
		logger.debug("Invoke eservice api[{}]", url);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json");

		HttpHeaders headers = this.setHeader(headerMap);
		//headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ApiResponseObj<T>> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, typeReferences().get(clazz));
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}

		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return ((ApiResponseObj<T>) obj).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public <T> ApiResponseObj<T> getApiResponse(String url, Class<T> clazz) {
		logger.debug("Invoke eservice api[{}]", url);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json");

		HttpHeaders headers = this.setHeader(headerMap);
		//headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ApiResponseObj<T>> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, typeReferences().get(clazz));
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}

		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return (ApiResponseObj<T>) obj;
	}
	
	public String getGetApiReturnCode(String url) {
		logger.debug("Invoke eservice api[{}]: {}", url);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + this.ESERVICE_API_SECRET);
		headerMap.put("Content-Type", "application/json;charset=UTF-8");

		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<ApiResponseObj<?>> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, new ParameterizedTypeReference<ApiResponseObj<?>>() { });
		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}

		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return ((ApiResponseObj<?>) obj).getReturnHeader().getReturnCode();
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
}