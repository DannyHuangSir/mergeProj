package com.twfhclife.generic.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.twfhclife.adm.domain.HttpResponseVo;

@Service
public class KeycloakRestUtil {
	
	private static final Logger logger = LogManager.getLogger(KeycloakRestUtil.class);

	// 以下value放設定檔
	@Value("${keycloak.auth-server-url}")
	protected String KEYCLOAK_URL;// = "http://220.133.126.209:8082/auth";
	@Value("${keycloak.admin-realm:master}")
	protected String ADMIN_REALM;
	@Value("${keycloak.admin-user}")
	protected String ADMIN_USER;
	@Value("${keycloak.admin-pwd}")
	protected String ADMIN_PWD;
	@Value("${keycloak.admin-security-client:security-admin-console}")
	protected String ADMIN_SECURITY_CLIENT;
	@Value("${keycloak.adm-realm:twfhclife}")
	protected String ADM_REALM;
	@Value("${keycloak.elife-realm:elife}")
	protected String ELIFE_REALM;
	
	/**
	 * GET API
	 * 
	 * @param apiUri
	 * @param pathParams
	 * @param needAdminAuth
	 * @return
	 */
	public HttpResponseVo getApi(String apiUri, Map<String, String> pathParams, boolean needAdminAuth) {
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + apiUri;
		for (String param : pathParams.keySet()) {
			uri = uri.replace("{" + param + "}", pathParams.get(param));
		}
		logger.info("Calling Keycloak Admin API, Method=GET, URI=" + uri);
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(uri);
			
			if (needAdminAuth) {
				Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD,
						ADMIN_SECURITY_CLIENT);
				TokenManager tm = kc.tokenManager();
				logger.debug("--access_token:" + tm.getAccessTokenString());
				get.setHeader("Authorization", "Bearer " + tm.getAccessTokenString());
			}
			try {
				HttpResponse response = client.execute(get);
				httpResponseVo.setHeader(response.getAllHeaders());
				int statusCode = response.getStatusLine().getStatusCode();
				httpResponseVo.setStatusCode(statusCode);
				logger.info("Response Code : " + statusCode);

				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line1 = "";
				while ((line1 = rd.readLine()) != null) {
					result.append(line1);
				}
				resultString = result.toString();
				httpResponseVo.setResponseBody(resultString);
				logger.debug("resultString=" + resultString);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return httpResponseVo;
	}
	
	/**
	 * POST API
	 * 
	 * @param apiUri
	 * @param pathParams
	 * @param postParams
	 * @param needAdminAuth
	 * @return
	 */
	public HttpResponseVo postApi(String apiUri, Map<String, String> pathParams, Map<String, String> postParams, boolean needAdminAuth) {
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + apiUri;
		for (String param : pathParams.keySet()) {
			uri = uri.replace("{" + param + "}", pathParams.get(param));
		}
		logger.info("Calling Keycloak Admin API, Method=POST, URI=" + uri);
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(uri);
		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		for (String param : postParams.keySet()) {
			urlParameters.add(new BasicNameValuePair(param, postParams.get(param)));
		}

		try {
			if (needAdminAuth) {
				Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD,
						ADMIN_SECURITY_CLIENT);
				TokenManager tm = kc.tokenManager();
				logger.debug("--access_token:" + tm.getAccessTokenString());
				post.setHeader("Authorization", "Bearer " + tm.getAccessTokenString());
			}
			//post.setHeader("Content-Type", "application/json");
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
	
	public HttpResponseVo postJsonApi(String apiUri, Map<String, String> pathParams, Map<String, String> postParams, boolean needAdminAuth) {
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + apiUri;
		for (String param : pathParams.keySet()) {
			uri = uri.replace("{" + param + "}", pathParams.get(param));
		}
		logger.info("Calling Keycloak Admin API, Method=POST, URI=" + uri);
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(uri);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		for (String param : postParams.keySet()) {
			node.put(param, postParams.get(param));
		}

		try {
			if (needAdminAuth) {
				Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD,
						ADMIN_SECURITY_CLIENT);
				TokenManager tm = kc.tokenManager();
				logger.debug("--access_token:" + tm.getAccessTokenString());
				post.setHeader("Authorization", "Bearer " + tm.getAccessTokenString());
			}
			post.setHeader("Content-Type", "application/json");
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
			StringEntity params = new StringEntity(jsonString);
			post.setEntity(params);
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
	
	public HttpResponseVo putApi(String apiUri, Map<String, String> pathParams, Map<String, String> putParams, boolean needAdminAuth) {
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + apiUri;
		for (String param : pathParams.keySet()) {
			uri = uri.replace("{" + param + "}", pathParams.get(param));
		}
		logger.info("Calling Keycloak Admin API, Method=POST, URI=" + uri);
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(uri);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		for (String param : putParams.keySet()) {
			node.put(param, putParams.get(param));
		}
		
		try {
			if (needAdminAuth) {
				Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD,
						ADMIN_SECURITY_CLIENT);
				TokenManager tm = kc.tokenManager();
				logger.debug("--access_token:" + tm.getAccessTokenString());
				put.setHeader("Authorization", "Bearer " + tm.getAccessTokenString());
			}

			put.setHeader("Content-Type", "application/json");
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
			StringEntity params = new StringEntity(jsonString);
			put.setEntity(params);
			HttpResponse response = client.execute(put);
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
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return httpResponseVo;
	}
	
	public HttpResponseVo deleteApi(String apiUri, Map<String, String> pathParams, boolean needAdminAuth) {
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + apiUri;
		for (String param : pathParams.keySet()) {
			uri = uri.replace("{" + param + "}", pathParams.get(param));
		}
		logger.info("Calling Keycloak Admin API, Method=POST, URI=" + uri);
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete delete = new HttpDelete(uri);
		try {
			if (needAdminAuth) {
				Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD,
						ADMIN_SECURITY_CLIENT);
				TokenManager tm = kc.tokenManager();
				logger.debug("--access_token:" + tm.getAccessTokenString());
				delete.setHeader("Authorization", "Bearer " + tm.getAccessTokenString());
			}

			HttpResponse response = client.execute(delete);
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
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return httpResponseVo;
	}
}
