package com.twfhclife.generic.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twfhclife.generic.domain.HttpResponseVo;
import com.twfhclife.generic.model.KeycloakUserSession;

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
	protected final String URI_GET_SESSION_STATS = "/admin/realms/{realm}/client-session-stats";//GET
	protected final String URI_USER_SESSION = "/admin/realms/{realm}/users/{userId}/sessions";//GET
	protected final String URI_RESET_PWD = "/admin/realms/{realm}/users/{userId}/reset-password";
	protected final String URI_CREATE_ROLE = "/admin/realms/{realm}/roles";
	protected final String URI_DEL_ROLE = "/admin/realms/{realm}/roles/{role-name}";
	protected final String URI_UPDATE_ROLE = "/admin/realms/{realm}/roles/{role-name}";
	protected final String URI_VALIDATE_TOKEN = "/realms/{realm}/protocol/openid-connect/userinfo";
	protected final String URI_REMOVE_SESSION = "/admin/realms/{realm}/sessions/{session}";//DELETE
	
	
	public static final String VALIDATE_TOKEN_ERROR_MSG = "Token invalid: Token is not active";
	public static final String REFRESH_TOKEN_EXPIRE_MSG = "Refresh token expired";
	
	/**
	 * 取得User data
	 * @param userId
	 * @return
	 */
	public UserRepresentation getUser(String userId) {
		UserRepresentation user = null;
		// String uri = KEYCLOAK_URL + "/admin/realms/" + REALM + "/users/" + userId;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("realm", this.REALM);
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
		try (Response res = kc.realm(REALM).users().create(user);) {
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

		UserResource userResource = kc.realm(REALM).users().get(userId);
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
			pathParams.put("realm", this.REALM);
			
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
	
	protected String getCreatedUserId(Response response) {
		String locationHeader = response.getHeaderString("Location");
		String userId = locationHeader.replaceAll(".*/(.*)$", "$1");
		return userId;
	}
	
	/**
	 * 
	 * @param refresh_token
	 * @return
	 */
	public Map<String, Object> refreshToken(String refresh_token, String realm, String clientId) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", realm);
			
			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("grant_type", "refresh_token");
			postParams.put("client_id", clientId);
			postParams.put("refresh_token", refresh_token);
			
			HttpResponseVo httpResponseVo = this.postApi(URI_LOGIN, pathParams, postParams, false);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				if (body != null) {
					JacksonJsonParser jjp = new JacksonJsonParser();
					resultMap = jjp.parseMap(body);
					//System.out.println("access_token:" + resultMap.get("access_token"));
					resultMap.put("result", "true");
				} else {
					resultMap.put("result", "false");
				}
			} else if(statusCode == 401) {
				//Unauthorized 驗證失敗
				resultMap.put("result", "false");
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
			pathParams.put("realm", this.REALM);
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
	
	/**
	 * 使用FB的Token交換Keycloak AccessToken
	 * 
	 * @param realm
	 * @param clientId
	 * @param clientSecret
	 * @param fbToken
	 */
	public Map<String, Object> exchangeFbToken(String realm, String clientId, String clientSecret, String fbToken) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", realm);

			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
			postParams.put("client_id", clientId);
			postParams.put("client_secret", clientSecret);
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
			logger.error(e.getMessage(), e);
		}
		return resultMap;
	}
	

	public void login(String username, String pwd) {
		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, REALM, username, pwd, "eservice_adm");

		TokenManager tm = kc.tokenManager();
		System.out.println("access_token:" + tm.getAccessTokenString());
		AccessTokenResponse refreshResopnse = tm.refreshToken();
		refreshResopnse.getToken();
		refreshResopnse.getRefreshToken();
		System.out.println("expires_in:" + refreshResopnse.getExpiresIn());
		System.out.println("refresh_expires_in:" + refreshResopnse.getRefreshExpiresIn());
		System.out.println("refresh_token:" + refreshResopnse.getRefreshToken());
	}

	/**
	 * 搜尋使用者帳號
	 * @param queryStr
	 */
	public void searchUser(String queryStr) {
		// 1.先用admin帳戶取得權限
		Keycloak kc = Keycloak.getInstance(KEYCLOAK_URL, ADMIN_REALM, ADMIN_USER, ADMIN_PWD, ADMIN_SECURITY_CLIENT);
		List<UserRepresentation> users = kc.realm(REALM).users().search(queryStr);
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

			UserResource userResource = kc.realm(REALM).users().get(userId);
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
	
	
	public Map<String, Object> resetPwdByRest(String realm, String userId, String newPwd) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", realm);
			pathParams.put("userId", userId);
			
			// set post parameters
			Map<String, String> postParams = new HashMap<>();
			postParams.put("type", "password");
			postParams.put("temporary", "false");
			postParams.put("value", newPwd);
			
			HttpResponseVo httpResponseVo = this.putApi(URI_RESET_PWD, pathParams, postParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode >= 200 && statusCode <= 226) {
				resultMap.put("result", "true");
			} else if(statusCode == 400) {
				//Bad request
				String body = httpResponseVo.getResponseBody();
				if (body != null) {
					JacksonJsonParser jjp = new JacksonJsonParser();
					resultMap = jjp.parseMap(body);
				}
				resultMap.put("result", "false");
			} else {
				resultMap.put("result", "false");
			}
		} catch (Exception e) {
			logger.error("Unable to loginUser: {}", ExceptionUtils.getStackTrace(e));
			resultMap.put("result", "false");
		}
		return resultMap;
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
			pathParams.put("realm", this.REALM);
			
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
			pathParams.put("realm", this.REALM);
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
			pathParams.put("realm", this.REALM);
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
	public Map<String, String> getSessionStats() {
		// String uri = KEYCLOAK_URL + "/admin/realms/" + REALM + "/users/" + userId;
		Map<String, String> statsMap = null;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("realm", this.REALM);
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
	
	
	public List<KeycloakUserSession> getUserSessions(String realm, String userId, String clientId) {
		// String uri = KEYCLOAK_URL + "/admin/realms/" + REALM + "/users/" + userId;
		List<KeycloakUserSession> sessionList = new ArrayList<>();
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("realm", realm);
		pathParams.put("userId", userId);
		try {
			HttpResponseVo httpResponseVo = this.getApi(URI_USER_SESSION, pathParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			
			if (statusCode >= 200 && statusCode <= 226) {
				String body = httpResponseVo.getResponseBody();
				ObjectMapper objMapper = new ObjectMapper();
				if (!StringUtils.isEmpty(body)) {
					// convert JSON string to Map
					List<Map<String, Object>> resultList = objMapper.readValue(body, new TypeReference<List<Map<String, Object>>>(){});
					if(resultList != null && resultList.size() > 0) {
						for(Map<String, Object> map : resultList) {
							KeycloakUserSession userSession = new KeycloakUserSession();
							userSession.setSessionId(MyStringUtil.nullToString(map.get("id")));
							userSession.setUserId(MyStringUtil.nullToString(map.get("userId")));
							userSession.setUsername(MyStringUtil.nullToString(map.get("username")));
							userSession.setIpAddress(MyStringUtil.nullToString(map.get("ipAddress")));
							if(map.get("clients") != null) {
								Map cmap = (Map) map.get("clients");
								Object[] obj = cmap.values().toArray();
								userSession.setClientId(MyStringUtil.nullToString(obj[0]));
							}
							if(clientId != null && !clientId.equals(userSession.getClientId())) {
								userSession = null;
								continue;
							}
							sessionList.add(userSession);
						}
					}
					logger.info("User Sessions: ", sessionList);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sessionList;
	}
	
	public boolean deleteSession(String realm, String sessionId) {
		boolean result = false;
		
		try {
			// set path parameters
			Map<String, String> pathParams = new HashMap<>();
			pathParams.put("realm", realm);
			pathParams.put("session", sessionId);

			HttpResponseVo httpResponseVo = this.deleteApi(URI_REMOVE_SESSION, pathParams, true);
			int statusCode = httpResponseVo.getStatusCode();
			if (statusCode == 204) {
				// create success
				result = true;
				logger.info("delete session "+sessionId+ ": Success.");
			} else if(statusCode == 404) {
				// 404 Not Found
				logger.info("delete session "+sessionId+ ": Not found.");
			} else {
				// 
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 透過合法的AccessToken取得使用者ID資訊，若無法取得則代表該Token無效
	 * @param accessToken
	 * @return
	 */
	public Map validateToken(String accessToken, String realm) {
		Map resultMap = new HashMap();
		String userId = null;
		HttpResponseVo httpResponseVo = new HttpResponseVo();
		String resultString = "";
		String uri = KEYCLOAK_URL + URI_VALIDATE_TOKEN;
		uri = uri.replace("{realm}", realm);

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
			resultMap = jjp.parseMap(resultString);

			if (statusCode == 200) {
				userId = (String) resultMap.get("sub");
				resultMap.put("userId", userId);
				logger.debug("UUID=" + resultMap.get("sub"));
			} else if (statusCode == 401) {
				logger.debug("ERROR=" + resultMap.get("error") + ":" + resultMap.get("error_description"));
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return resultMap;
	}

	public static void main(String[] args) {
		KeycloakUtil ku = new KeycloakUtil();
		Map<String, String> props = PropertiesUtil.getProperties("application.properties");
		ku.KEYCLOAK_URL = props.get("keycloak.param.auth-server-url");
		ku.REALM = props.get("keycloak.param.default-realm");
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
		ku.exchangeFbToken("elife", "eservice", "abaacee9-1393-4c8b-9a3f-a85c1427c1d9", 
				"EAAC1yxZBpSyMBAHOGWP7PtvEOE1U9c8OyqwFcAFh3XyqNMeICBR20O8ZAhDzMD2v19owLE7sxryQE2DauqG6wLQpGFZCKuRoODtzylYdCwqqdf4FIaChGewMtG5TrEqDL6ZA2HkBFmbMd3iHh5UhIUhjAk0rFuxk8Ir7Q4XEK6lz8ASXh1PNE2TZA9LOaVsO4lgV4xz0L7AZDZD");
//		 ku.searchUser("test");
//		ku.getSessionStats();
//		ku.reset.Password("318cdf46-151a-475f-aab6-006662ca87d7", "12qwaszx");
//		ku.updateRole("t2","t1");
	}

}
