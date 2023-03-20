package com.twfhclife.eservice.api_client;

import com.twfhclife.eservice.api_model.*;
import com.twfhclife.eservice.keycloak.model.KeycloakLoginResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUserSession;
import com.twfhclife.eservice.util.MyJacksonUtil;
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
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.util.*;

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
		ArrayList<KeycloakUserSession> userSessionResponse = new ArrayList<KeycloakUserSession>();
		map.put(KeycloakLoginResponse.class, new ParameterizedTypeReference<ApiResponseObj<KeycloakLoginResponse>>() { });
		map.put(userSessionResponse.getClass(), new ParameterizedTypeReference<ApiResponseObj<List<KeycloakUserSession>>>() { });
		map.put(PersonalCaseDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PersonalCaseDataResponse>>() { });
		map.put(PolicyListDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyListDataResponse>>() { });
		map.put(PolicyBaseDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyBaseDataResponse>>() { });
		map.put(PolicySafeGuardDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicySafeGuardDataResponse>>() { });
		map.put(PolicyPaymentRecordDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyPaymentRecordDataResponse>>() { });
		map.put(PolicyPremiumDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyPremiumDataResponse>>() { });
		map.put(PolicyExpireOfPaymentDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyExpireOfPaymentDataResponse>>() { });
		map.put(PolicyChangeInfoDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyChangeInfoDataResponse>>() { });
		map.put(PolicyIncomeDistributionDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyIncomeDistributionDataResponse>>() { });
		map.put(JdPolicyFundTransactionResponse.class, new ParameterizedTypeReference<ApiResponseObj<JdPolicyFundTransactionResponse>>() { });
		map.put(PolicyPremiumCostResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyPremiumCostResponse>>() { });
		map.put(PolicyInvtFundDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyInvtFundDataResponse>>() { });
		map.put(PortfolioResponse.class, new ParameterizedTypeReference<ApiResponseObj<PortfolioResponse>>() { });
		map.put(PolicyCancellationMoneyDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyCancellationMoneyDataResponse>>() { });
		map.put(ExchangeRateDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<ExchangeRateDataResponse>>() { });
		map.put(CaseProcessDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<CaseProcessDataResponse>>() { });
		map.put(NoteContentDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<NoteContentDataResponse>>() { });
		map.put(NotePdfDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<NotePdfDataResponse>>() { });
		map.put(NoteNotifyDataResponse.class, new ParameterizedTypeReference<ApiResponseObj<NoteNotifyDataResponse>>() { });

		return Collections.unmodifiableMap(map);
	}

	private RestTemplate restTemplate;

	@Autowired
	public BaseRestClient() {
		HttpComponentsClientHttpRequestFactory requestFactory = generateHttpRequestFactory();
		restTemplate = new RestTemplate(requestFactory);
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
		HttpEntity<?> entity = new HttpEntity<>(postJson, headers);
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