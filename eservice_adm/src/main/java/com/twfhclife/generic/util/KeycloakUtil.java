package com.twfhclife.generic.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twfhclife.adm.domain.HttpResponseVo;

/**
 * 請以@Autowired注入使用
 * @author David
 *
 */
@Service
public class KeycloakUtil extends KeycloakRestUtil {
	private static final Logger logger = LogManager.getLogger(KeycloakUtil.class);
	
	// Keycloak API之固定URI不可修改,不需放到設定檔
	protected final String URI_GET_USER = "/admin/realms/{realm}/users/{id}";// 取得User資料
	protected final String URI_LOGIN = "/realms/{realm}/protocol/openid-connect/token";// 登入取得Token
	protected final String URI_LOGOUT = "/admin/realms/{realm}/users/{id}/logout";
	protected final String URI_GET_SESSION_COUNT = "/admin/realms/{realm}/clients/{clientId}/session-count";
	protected final String URI_GET_SESSION_STATS = "/admin/realms/{realm}/client-session-stats";
	//protected final String URI_RESET_PWD = "/admin/realms/{realm}/users/{id}/reset-password";
	protected final String URI_CREATE_ROLE = "/admin/realms/{realm}/roles";
	protected final String URI_DEL_ROLE = "/admin/realms/{realm}/roles/{role-name}";
	protected final String URI_UPDATE_ROLE = "/admin/realms/{realm}/roles/{role-name}";
	protected final String VALIDATE_TOKEN = "/realms/{realm}/protocol/openid-connect/userinfo";

	/**
	 * 取得User data
	 * @param userId
	 * @return
	 */
	public UserRepresentation getUser(String userId) {
		UserRepresentation user = null;
		// String uri = KEYCLOAK_URL + "/admin/realms/" + REALM + "/users/" + userId;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("realm", ADM_REALM);
		pathParams.put("id", userId);
		try {
			HttpResponseVo httpResponseVo = this.getApi(URI_GET_USER, pathParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				ObjectMapper objMapper = new ObjectMapper();
				if (!StringUtils.isEmpty(body)) {
					user = objMapper.readValue(body, UserRepresentation.class);

					System.out.println("user id:" + user.getId());
				}
			}
		} catch (Exception e) {
			logger.error("Unable to getUser: {}", ExceptionUtils.getStackTrace(e));
		}
		return user;
	}

	/**
	 * 新增User帳號(註冊)
	 */
	public void createUser() {
		// 1.先用admin帳戶取得權限
		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);

		// 2.定義欲新增的User資訊
		UserRepresentation user = new UserRepresentation();
		user.setUsername("testuser0");
		user.setFirstName("test0");
		user.setLastName("User0");
		user.setEmail("mailTest@gmail.com");
		user.setEnabled(true);
		// 2.1設定認證資訊
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue("test123");
		user.setCredentials(Arrays.asList(credential));
		// 2.2設定Attribute
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("mobile", Arrays.asList(new String[] { "0977222333" }));
		user.setAttributes(attributes);

		// 3.送出
		try (Response res = kc.realm(ELIFE_REALM).users().create(user);) {
			if (res != null) {
				StatusType statusType = res.getStatusInfo();
				int status = statusType.getStatusCode();
				String reason = statusType.getReasonPhrase();
				// MultivaluedMap<String, String> header = res.getStringHeaders();
				// "[Connection=keep-alive,Content-Length=0,Date=Fri, 09 Mar 2018 04:31:51
				// GMT,Location=http://220.133.126.209:8082/auth/admin/realms/twfhclife/users/358522fe-b39a-4c35-a2d2-e1d00a6173f8]"
				// String getUserUri = header.getFirst("Location");
				// Read the contents of an entity and return it as a String.
				String body = res.readEntity(String.class);// 取得Message Body
				Map<String, Object> bodymap = new HashMap<>();
				JacksonJsonParser jjp = new JacksonJsonParser();
				if (status != 201) {
					if (!StringUtils.isEmpty(body)) {
						bodymap = jjp.parseMap(body);
					}
				}
				System.out.println("status:" + status + ", reason:" + reason + ", errorMessage:" + bodymap.get("errorMessage"));
			}
		} catch (Exception e) {
			logger.error("Unable to createUser: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 修改User帳號取得Token資料
	 * @param userId
	 */
	public void updateUser(String userId) {
		// 1.先用admin帳戶取得權限
		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);

		// List<UserRepresentation> user =
		// kc.realm("twfhclife").users().search("hpe_david");
		// if(user != null && user.size() > 0) {
		// user.get(0).getId();
		// }

		UserResource userResource = kc.realm(ELIFE_REALM).users().get(userId);
		UserRepresentation user = userResource.toRepresentation();
		// user.setFirstName("new First Name");
		// user.setLastName("new Last Name");

		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("mobile", Arrays.asList(new String[] { "0912000111" }));
		user.setAttributes(attributes);
		// user.setCredentials(credentials);
		userResource.update(user);

	}

	/**
	 *  登入
	 * @param username
	 * @param pwd
	 */
	public Map<String, Object> loginUser(String username, String pwd) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);
			
			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("grant_type", "password");
			postParams.put("client_id", "eservice_adm");
			postParams.put("username", username);
			postParams.put("password", pwd);
			
			HttpResponseVo httpResponseVo = this.postApi(URI_LOGIN, pathParams, postParams, false);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				if (body != null) {
					JacksonJsonParser jjp = new JacksonJsonParser();
					resultMap = jjp.parseMap(body);
					System.out.println("access_token:" + resultMap.get("access_token"));
					resultMap.put("loginResult", "true");
				}
			} else if(statusCode == 401) {
				//Unauthorized 驗證失敗
				resultMap.put("loginResult", "false");
			} else {
				resultMap.put("loginResult", "false");
			}
		} catch (Exception e) {
			logger.error("Unable to loginUser: {}", ExceptionUtils.getStackTrace(e));
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @param refresh_token
	 * @return
	 */
	public Map<String, Object> refreshToken(String refresh_token) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);
			
			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("grant_type", "refresh_token");
			postParams.put("client_id", "eservice_adm");
			postParams.put("refresh_token", refresh_token);
			
			HttpResponseVo httpResponseVo = this.postApi(URI_LOGIN, pathParams, postParams, false);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				if (body != null) {
					JacksonJsonParser jjp = new JacksonJsonParser();
					resultMap = jjp.parseMap(body);
					//System.out.println("access_token:" + resultMap.get("access_token"));
					resultMap.put("loginResult", "true");
				}
			} else if(statusCode == 401) {
				//Unauthorized 驗證失敗
				resultMap.put("loginResult", "false");
			}
		} catch (Exception e) {
			logger.error("Unable to loginUser: {}", ExceptionUtils.getStackTrace(e));
		}
		return resultMap;
	}
	

	/**
	 *  登出
	 * @param userid
	 */
	public void logoutUser(String userid) {

		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);
			pathParams.put("id", userid);

			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("client_id", "eservice_adm");
			HttpResponseVo httpResponseVo = this.postApi(URI_LOGOUT, pathParams, postParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			if(statusCode >= 200 && statusCode <= 226) {
				// 登出成功
				logger.info("User:"+userid+" logout success.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	
	
	public void exchangeFbToken(String fbToken) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);

			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
			postParams.put("client_id", "eservice");
			postParams.put("client_secret", "6cf29829-debd-4ff5-b00b-846ae2f7d940");
			postParams.put("subject_token", fbToken);
			postParams.put("subject_issuer", "facebook");
			postParams.put("subject_token_type", "urn:ietf:params:oauth:token-type:access_token");

			HttpResponseVo httpResponseVo = this.postApi(URI_LOGIN, pathParams, postParams, false);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				if (body != null) {
					JacksonJsonParser jjp = new JacksonJsonParser();
					resultMap = jjp.parseMap(body);
					// System.out.println("access_token:" + resultMap.get("access_token"));
					resultMap.put("loginResult", "true");
				}
			} else if (statusCode == 401) {
				// Unauthorized 驗證失敗
				resultMap.put("loginResult", "false");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

//	public void login(String username, String pwd) {
//		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, REALM, username, pwd, "eservice_adm");
//
//		TokenManager tm = kc.tokenManager();
//		System.out.println("access_token:" + tm.getAccessTokenString());
//		AccessTokenResponse refreshResopnse = tm.refreshToken();
//		refreshResopnse.getToken();
//		refreshResopnse.getRefreshToken();
//		System.out.println("expires_in:" + refreshResopnse.getExpiresIn());
//		System.out.println("refresh_expires_in:" + refreshResopnse.getRefreshExpiresIn());
//		System.out.println("refresh_token:" + refreshResopnse.getRefreshToken());
//	}

	/**
	 * 搜尋使用者帳號
	 * @param queryStr
	 */
	public void searchUser(String queryStr) {
		// 1.先用admin帳戶取得權限
		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);
		List<UserRepresentation> users = kc.realm(ADM_REALM).users().search(queryStr, 0, 10);
		users.forEach(user -> System.out.println(user.getUsername()));
	}
	
	/**
	 * 重設密碼
	 * @param userId
	 * @param newPwd
	 * @return
	 */
	public boolean resetPassword(String userId, String newPwd) {
		boolean result = true;
		try {
			// 1.先用admin帳戶取得權限
			Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);

			UserResource userResource = kc.realm(ADM_REALM).users().get(userId);
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue(newPwd);
			credential.setTemporary(false);
			userResource.resetPassword(credential);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 新增角色
	 * @param roleName
	 * @return
	 */
	public Map<String, Object> createRole(String roleName) {
		Map<String, Object> resultMap = new HashMap<>();
		
//		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);
//		RoleRepresentation roleRepresentation = new RoleRepresentation();
//		roleRepresentation.setName(roleName);
//		kc.realm(REALM).roles().create(roleRepresentation);
		
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);
			
			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("name", roleName);
			
			HttpResponseVo httpResponseVo = this.postJsonApi(URI_CREATE_ROLE, pathParams, postParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			resultMap.put("resultCode", statusCode);
			if (statusCode == 201) {
				// create success
				resultMap.put("result", "true");
			} else if(statusCode == 409) {
				// 409 Conflict
				resultMap.put("result", "false");
				resultMap.put("resultMsg", "角色已存在");
			} else {
				resultMap.put("result", "false");
				
				resultMap.put("resultMsg", "角色新增失敗");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	public Map<String, Object> updateRole(String roleName, String newRoleName) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);
			pathParams.put("role-name", roleName);
			
			// set post parameters
			Map<String, String> putParams = new HashMap<>();
			putParams.put("name", newRoleName);
			
			HttpResponseVo httpResponseVo = this.putApi(URI_UPDATE_ROLE, pathParams, putParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			resultMap.put("resultCode", statusCode);
			if (statusCode == 201) {
				// create success
				resultMap.put("result", "true");
			} else if(statusCode == 409) {
				// 409 Conflict
				resultMap.put("result", "false");
				resultMap.put("resultMsg", "角色已存在");
			} else {
				resultMap.put("result", "false");
				
				resultMap.put("resultMsg", "角色新增失敗");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 刪除角色
	 * @param roleName
	 * @return
	 */
	public boolean deleteRole(String roleName) {
		boolean result = false;
		
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", this.ADM_REALM);
			pathParams.put("role-name", roleName);

			HttpResponseVo httpResponseVo = this.deleteApi(URI_DEL_ROLE, pathParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode == 204) {
				// create success
				result = true;
				logger.info("deleteRole "+roleName+ ": Success.");
			} else if(statusCode == 404) {
				// 404 Not Found
				logger.info("deleteRole "+roleName+ ": Not found.");
			} else {
				// 
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 查詢Active Session
	 * 回傳clientId Map, 以clientId取得active number
	 */
	public Map<String, String> getSessionStats(String realm) {
		// String uri = KEYCLOAK_URL + "/admin/realms/" + REALM + "/users/" + userId;
		Map<String, String> statsMap = null;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("realm", this.ADM_REALM);
		if(MyStringUtil.isNotNullOrEmpty(realm)) {
			pathParams.put("realm", realm);
		} else {
			pathParams.put("realm", this.ADM_REALM);
		}
		try {
			HttpResponseVo httpResponseVo = this.getApi(URI_GET_SESSION_STATS, pathParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				ObjectMapper objMapper = new ObjectMapper();
				if (!StringUtils.isEmpty(body)) {
					statsMap = new HashMap<>();
					// convert JSON string to Map
					List<Map<String, Object>> resultList = objMapper.readValue(body, new TypeReference<List<Map<String, Object>>>(){});
					if(resultList != null && resultList.size() > 0) {
						for(Map<String, Object> map : resultList) {
							statsMap.put(map.get("clientId").toString(), map.get("active").toString());
						}
					}
					logger.info("Session stats: ", statsMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return statsMap;
	}
	
	/**
	 * 透過合法的AccessToken取得使用者ID資訊，若無法取得則代表該Token無效
	 * @param accessToken
	 * @return
	 */
	public String getUserIdByValidToken(String accessToken) {
		String userId = null;
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + VALIDATE_TOKEN;
		uri = uri.replace("{realm}", "twfhclife");

		logger.info("Calling Keycloak Admin API, Method=GET, URI=" + uri);
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(uri);
			get.setHeader("Authorization", "Bearer " + accessToken);
			HttpResponse response = client.execute(get);
			httpResponseVo.setHeader(response.getAllHeaders());
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("Response Code : " + statusCode);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line1 = "";
			while ((line1 = rd.readLine()) != null) {
				result.append(line1);
			}
			resultString = result.toString();
			JacksonJsonParser jjp = new JacksonJsonParser();
			Map resultMap = jjp.parseMap(resultString);

			if (statusCode == 200) {
				userId = (String) resultMap.get("sub");
				logger.debug("UUID=" + resultMap.get("sub"));
			} else if (statusCode == 401) {
				logger.debug("ERROR=" + resultMap.get("error"));
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return userId;
	}
	

	public static void main(String[] args) {
		KeycloakUtil ku = new KeycloakUtil();
		Map<String, String> props = PropertiesUtil.getProperties("application.properties");
		ku.KEYCLOAK_URL = props.get("keycloak.param.auth-server-url");
		ku.ADM_REALM = props.get("keycloak.param.default-realm");
		ku.ADMIN_REALM = props.get("keycloak.param.admin-realm");
		ku.ADMIN_USER = props.get("keycloak.param.admin-user");
		ku.ADMIN_PWD = props.get("keycloak.param.admin-pwd");
		ku.ADMIN_SECURITY_CLIENT = props.get("keycloak.param.admin-security-client");
		// ku.getUserByUri("http://220.133.126.209:8082/auth/admin/realms/twfhclife/users/318cdf46-151a-475f-aab6-006662ca87d7");
		//ku.getUser("318cdf46-151a-475f-aab6-006662ca87d7");
//		 ku.createUser();
		// ku.updateUser("318cdf46-151a-475f-aab6-006662ca87d7");
//		ku.loginUser("hpe_david", "12qwaszx");
//		ku.refresgToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2TGJDUEJaN1JNeHRmeFdTRUlfNmVnbGJPZ2tOMWNib3k1Vlh1c2Ixd0N3In0.eyJqdGkiOiI4OWE5ZDMzOS1hNDY5LTQ3OTUtYjk2MC01ZjI1YWFmZjM2YTgiLCJleHAiOjE1MjY2MzI2MzQsIm5iZiI6MCwiaWF0IjoxNTI2NjI2NjM0LCJpc3MiOiJodHRwOi8vMjIwLjEzMy4xMjYuMjA5OjgwODIvYXV0aC9yZWFsbXMvdHdmaGNsaWZlIiwiYXVkIjoiZXNlcnZpY2VfYWRtIiwic3ViIjoiMzE4Y2RmNDYtMTUxYS00NzVmLWFhYjYtMDA2NjYyY2E4N2Q3IiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImVzZXJ2aWNlX2FkbSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjMxZDViY2I4LTczYTUtNGI5YS1hM2ZhLWMxMjVmMzE3ZmZiYiIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJEZXZlbG9wZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19fQ.mCtR-kPJFxBvxOC7ndonEio-H706o7Ndjmx5eYwuUdgiEEn8p-q0REMiu4gvOLMueCB_fVvkO4hbIleCMhnYKDHWZboV42BxapQ_79Hrw-QbzA8AAGK3ZaJ9auveSP-UTyPwv6qyn0gAy6ouhoz4AWdpbVDZ6zqIiTYxVcc_XhJDbDTI-YWUInaw2GhWM8QlKyYHokEQY6RY4TmZYGnQkQOqVOp4z_secyWU_juca-eo4eZOij6S-iGQG6WJezOgOrwyKoRi7gAEzkjCEuuaUlRJmVMIs6tzBbf08C-et6NqBm0D27lp1bYuqTfPzWZYRd4gLMUXOA1y-lzK8Mo_mg");
//		ku.logoutUser("318cdf46-151a-475f-aab6-006662ca87d7");
//		ku.exchangeFbToken("EAAC4ZAydKgNIBAFHsKhjZC9XrHa3hrb2LSpbyH143wwjcfIhDeuDYZBkkkdyzMBaJx8teXRpEVPnO22ihZAypAtqpn0YXWJnjFrbAISrG90AcmIEaHdeO3b240K5U1yesDeX9LSZCOE3n83qEZCjMdEWfr2MHKZBfZCQTVfkzH1aZAgC65uuQK6pRf7NddzpVTgZAHVUvgZBwdAuyNS7URY6Y7ZA");
//		 ku.searchUser("david.yu0903@gmail.com");
//		ku.getSessionStats();
//		ku.reset.Password("318cdf46-151a-475f-aab6-006662ca87d7", "12qwaszx");
//		ku.updateRole("t2","t1");
		ku.getUserIdByValidToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2TGJDUEJaN1JNeHRmeFdTRUlfNmVnbGJPZ2tOMWNib3k1Vlh1c2Ixd0N3In0.eyJqdGkiOiJmOTliOTg4MC03MWM5LTQzMDAtODE1OC05MzlkNzEyMTNkY2YiLCJleHAiOjE1MjkwMzk3NzgsIm5iZiI6MCwiaWF0IjoxNTI5MDM3OTc4LCJpc3MiOiJodHRwOi8vMjIwLjEzMy4xMjYuMjA5OjgwODIvYXV0aC9yZWFsbXMvdHdmaGNsaWZlIiwiYXVkIjoiZXNlcnZpY2UiLCJzdWIiOiI4ODMxOWUzYS02ZWI0LTQzNjAtYWI0MS05MGRhODA3NDBjYWMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJlc2VydmljZSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjE0MjVhMmJkLTBkMmEtNGUxYS05MmRkLWNiMjY4OGJlYWUzNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOltdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZXNlcnZpY2VfYWRtIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctdXNlcnMiLCJxdWVyeS1ncm91cHMiLCJxdWVyeS11c2VycyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZGF2aWR5dSIsImVtYWlsIjoieWx3MDkwM0BnbWFpbC5jb20ifQ.L0_3L4wu4SpTGd_bqafUv3JQaLJMkChoVCUU6uIjUTm4oQpPKWGvf4hVa9tSeApS_tthu6oleOG2B0paTdLOahhsC5VoSeIAo-4b04thflEYeL6yh6R07n_69nzRI3DHIRnuDzARZ8N8hT7Lyo1Ujx1tGIhT-r5zqIVusZUvlwj5O7V2NvRizjA_JsIS2xGQHxORYUfZBI_sdK1PWcgUfxRhm8JEzJokqvCATwQM363G0QjfiVpo1JtiNxEXwdiXDgYeTziutCHiZoyto3lePbUi4bYVJKc8t6PU0O_KMR-43wggOSl98YjGZpwqwMC1_-sxYTVEck4X8KsuTmQWfQ");
	}

}
