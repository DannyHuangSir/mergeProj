package com.twfhclife.generic.api_client;

import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.model.PolicyClaimDetailResponse;
import com.twfhclife.generic.model.UserDetailResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class BaseRestClient {
	private static final Logger logger = LogManager.getLogger(BaseRestClient.class);
	
	@Value("${eservice_api.domain}")
	public String API_DOMAIN;
	
	@Value("${keycloak.adm-realm:twfhclife}")
	public String ADM_REALM;
	
	@Value("${keycloak.adm-clientId:eservice_adm}")
	public String ADM_CLIENT;
	
	@Value("${eservice_api.secret}")
	public String ESERVICE_API_SECRET;
	
	@Value("${wso2.accessKey}")
	public String WSO2_API_KEY;

	@Value("${wso2.jd.accessKey}")
	public String WSO2_API_JD_KEY;

	public RestTemplate restTemplate;

	@Autowired
	public BaseRestClient() {
		try {
			//RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
			//restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
			SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				     SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), 
				        NoopHostnameVerifier.INSTANCE);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);

			restTemplate = new RestTemplate(requestFactory);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			// TODO Auto-generated catch block
			logger.debug("Create httpClient fail: {}", ExceptionUtils.getStackTrace(e));
		}

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		// Add the Jackson Message converter
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		// Note: here we are making this converter to process any kind of response,
		// not only application/*json, which is the default behaviour
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
	}
	
	public String getApiDomain() {
		return this.API_DOMAIN;
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
	
	public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
//		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
	}
	
	/** 
	 * 提供設定 eservice api access key 
	 * @param accessKey
	 */
	public void setEserviceAccessKey(String accessKey) {
		this.ESERVICE_API_SECRET = accessKey;
	}

	protected Map<Class<?>, ParameterizedTypeReference> typeReferences() {
		final Map<Class<?>, ParameterizedTypeReference> map = new HashMap<>();
		map.put(PolicyClaimDetailResponse.class, new ParameterizedTypeReference<ApiResponseObj<PolicyClaimDetailResponse>>() { });
		map.put(UserDetailResponse.class, new ParameterizedTypeReference<ApiResponseObj<UserDetailResponse>>() { });
		return Collections.unmodifiableMap(map);
	}
}
