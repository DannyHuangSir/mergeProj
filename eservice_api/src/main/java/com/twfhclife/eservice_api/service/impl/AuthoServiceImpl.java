package com.twfhclife.eservice_api.service.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.twfhclife.eservice.api.elife.domain.BxczLoginRequest;
import com.twfhclife.eservice.api.elife.domain.BxczLoginResponse;
import com.twfhclife.eservice.auth.dao.BxczDao;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.utils.MyJacksonUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.domain.FuncItemReqObj;
import com.twfhclife.eservice.api.adm.domain.FuncListReqObj;
import com.twfhclife.eservice.api.adm.domain.Function;
import com.twfhclife.eservice.api.adm.domain.FunctionDiv;
import com.twfhclife.eservice.api.adm.domain.Role;
import com.twfhclife.eservice.api.adm.domain.RoleFuncAuthReqObj;
import com.twfhclife.eservice.api.adm.domain.RoleFuncAuthVo;
import com.twfhclife.eservice.api.adm.domain.Systems;
import com.twfhclife.eservice.api.adm.domain.UserAuth;
import com.twfhclife.eservice.api.adm.domain.UserFuncAuthReqVo;
import com.twfhclife.eservice.api.adm.domain.UserRepresentation;
import com.twfhclife.eservice.api.adm.model.DepartmentVo;
import com.twfhclife.eservice.api.adm.model.FunctionDivEntity;
import com.twfhclife.eservice.api.adm.model.FunctionItemEntity;
import com.twfhclife.eservice.api.adm.model.FunctionVoEntity;
import com.twfhclife.eservice.api.adm.model.JobTitleVo;
import com.twfhclife.eservice_api.service.IAuthoService;
import com.twfhclife.generic.dao.AuthoDao;
import com.twfhclife.generic.dao.KeycloakUserDao;
import com.twfhclife.generic.dao.adm.UserDepartmentTitleDao;
import com.twfhclife.generic.model.UserAuthVoEntity;
import com.twfhclife.generic.model.UserRepresentationEntity;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.utils.MyStringUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Authorization service implementation.
 * 
 * @author tcMarcus
 * @version 1.0
 */
@Service
public class AuthoServiceImpl implements IAuthoService {

	private static final Logger logger = LogManager.getLogger(AuthoServiceImpl.class);

	/**
	 * Way to invoke authoDao process: inject authoDao instance.
	 */
	private AuthoDao authoDao;
	
	@Autowired
	private KeycloakUserDao keycloakUserDao;

	@Autowired
	private UserDepartmentTitleDao userDepartmentTitleDao;

	@Autowired
	public AuthoServiceImpl(AuthoDao authoDao) {

		this.authoDao = authoDao;
		try {
			SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
					SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
					NoopHostnameVerifier.INSTANCE);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);

			restTemplate = new RestTemplate(requestFactory);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			logger.debug("Create httpClient fail: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public List<Map<String, Object>> getFuncDivAuthByUser(UserFuncAuthReqVo vo) throws Exception {
//		return authoDao.getFuncDivAuths(userId, sysId);
		String isAdmin = vo.getIsAdmin();
		if(MyStringUtil.isNullOrEmpty(isAdmin) || !"Y".equals(isAdmin)) {
			isAdmin = "N";
		}
		String userId = vo.getUserId();
		if(MyStringUtil.isNullOrEmpty(userId) && MyStringUtil.isNotNullOrEmpty(vo.getUserName())) {
			userId = keycloakUserDao.getUserIdByUsername(vo.getUserName(), "twfhclife");
			logger.info("userId is null, get userId by username.");
		}
		if(MyStringUtil.isNullOrEmpty(userId)) {
			// 使用者不存在
			return null;
		}
		
		return authoDao.getMenuList(userId, vo.getSysId(), isAdmin);
	}

	@Override
	public List<Function> getAuthInfoByUser(String userId, String sysId) throws Exception {

		List<FunctionVoEntity> entities = authoDao.getAuthInfoByUser(userId, sysId);

		// 收集有權限的功能代號
		Map<Integer, FunctionVoEntity> funcIdsByAutho = new HashMap<>();
		for (FunctionVoEntity entity : entities) {

			funcIdsByAutho.put(entity.getFunctionId(), entity);
		}

		// 判斷所有功能底下那些當前使用者有權限
		for (FunctionVoEntity entity : entities) {

			if (funcIdsByAutho.containsKey(entity.getFunctionId())) {
				entity.setAutho(true);
			} else {
				entity.setAutho(false);
			}
		}

		// Set function result
		List<Function> functions = handlingFunctionResult(entities);

		return functions;
	}

	@Override
	public List<Systems> getSysListByUser(String userId) throws Exception {

		return authoDao.getSysListByUser(userId);
	}

	@Override
	public List<Function> getFunListBySys(String sysId) throws Exception {

		List<FunctionVoEntity> entities = authoDao.getFunListBySys(sysId, null, null);

		List<Function> functions = new ArrayList<>();

		// Set function result
		functions = handlingFunctionResult(entities);

		return functions;
	}

	@Override
	public List<Role> getRoleListByRoleName(String roleNameQuery) throws Exception {

		return authoDao.getRoleListByRoleName(roleNameQuery);
	}

	@Override
	public List<Function> getRoleFunctionAuthByRole(String roleId, String roleName, String sysId) throws Exception {

		// 取得所有功能
		List<FunctionVoEntity> entities = authoDao.getAllFunction();

		// 根據條件取得有權限的功能
		List<FunctionVoEntity> entitiesByAutho = authoDao.getRoleFunctionAuthByRole(roleId, roleName, sysId);

		// 收集有權限的功能代號
		Map<Integer, FunctionVoEntity> funcIdsByAutho = new HashMap<>();
		for (FunctionVoEntity entityByAutho : entitiesByAutho) {

			funcIdsByAutho.put(entityByAutho.getFunctionId(), entityByAutho);
		}

		// 判斷所有功能底下那些當前使用者有權限
		for (FunctionVoEntity entity : entities) {

			if (funcIdsByAutho.containsKey(entity.getFunctionId())) {
				entity.setAutho(true);
			} else {
				entity.setAutho(false);
			}
		}

		// Set function result
		List<Function> functions = handlingFunctionResult(entities);

		return functions;
	}

	@Override
	public List<Function> getSysFunctionAuthByUser(String userId, String sysId) throws Exception {

		// 取得所有功能
		List<FunctionVoEntity> entities = authoDao.getAllFunction();

		// 根據條件取得有權限的功能
		List<FunctionVoEntity> entitiesByAutho = authoDao.getSysFunctionAuthByUser(userId, sysId);

		// 收集有權限的功能代號
		Map<Integer, FunctionVoEntity> funcIdsByAutho = new HashMap<>();
		for (FunctionVoEntity entityByAutho : entitiesByAutho) {

			funcIdsByAutho.put(entityByAutho.getFunctionId(), entityByAutho);
		}

		// 判斷所有功能底下那些當前使用者有權限
		for (FunctionVoEntity entity : entities) {

			if (funcIdsByAutho.containsKey(entity.getFunctionId())) {
				entity.setAutho(true);
			} else {
				entity.setAutho(false);
			}
		}

		// Set function result
		List<Function> functions = handlingFunctionResult(entities);

		return functions;
	}

	@Override
	public boolean addFuncItem(FuncItemReqObj request) throws Exception {

		// Prepare insert parameters
		Function function = request.getFunction();
		List<FunctionDiv> functionDivs = function.getDivList();

		// Prepare insert parameters : FunctionItemEntity
		FunctionItemEntity funcItemEntity = new FunctionItemEntity();
		PropertyUtils.copyProperties(funcItemEntity, function);
		funcItemEntity.setSysId(request.getSysId());
		funcItemEntity.setCreateUser(request.getUserId());

		// Insert into FunctionItem
		boolean isSuccess = authoDao.insertFuncItem(funcItemEntity) > 0;

		// Select functionItem which you had inserted
		List<FunctionVoEntity> funcItems = authoDao.getFunListBySys(request.getSysId(),
				function.getParentFuncId(), function.getFunctionName());
		FunctionVoEntity funcItem = funcItems.get(0);

		// Prepare insert parameters : FunctionDivEntity
		List<FunctionDivEntity> funcDivEntities = new ArrayList<>();
		for (FunctionDiv funcDiv : functionDivs) {

			FunctionDivEntity funcDivEntity = new FunctionDivEntity();

			PropertyUtils.copyProperties(funcDivEntity, funcDiv);
			funcDivEntity.setFunctionId(funcItem.getFunctionId());

			funcDivEntities.add(funcDivEntity);
		}

		// Insert into FunctionDiv
		Iterator<FunctionDivEntity> iterator = funcDivEntities.iterator();
		while (iterator.hasNext()) {
			if (isSuccess) {
				isSuccess = authoDao.insertFuncDiv(iterator.next()) > 0;
			}
		}

		return isSuccess;
	}

	@Override
	public boolean addFuncList(FuncListReqObj request) throws Exception {

		// Prepare insert parameters
		List<Function> functionList = request.getFunctionList();

		// Prepare insert parameters : FunctionItems
		List<FunctionItemEntity> funcItemEntities = new ArrayList<>();
		for (Function function : functionList) {

			FunctionItemEntity funcItemEntity = new FunctionItemEntity();
			PropertyUtils.copyProperties(funcItemEntity, function);
			funcItemEntity.setCreateUser(request.getUserId());

			funcItemEntities.add(funcItemEntity);
		}

		// Insert into FunctionItem
		boolean isSuccess = true;

		Iterator<FunctionItemEntity> iterator = funcItemEntities.iterator();
		while (iterator.hasNext()) {
			if (isSuccess) {
				isSuccess = authoDao.insertFuncItem(iterator.next()) > 0;
			}
		}

		return isSuccess;
	}

	@Override
	public boolean updateFunctionItem(FuncItemReqObj request) throws Exception {

		// Prepare parameters to update data
		Function function = request.getFunction();
		List<FunctionDiv> divList = function.getDivList();

		// Update FUNCTION_ITEM table
		FunctionItemEntity itemEntity = new FunctionItemEntity();
		PropertyUtils.copyProperties(itemEntity, function);
		itemEntity.setSysId(request.getSysId());
		itemEntity.setUpdateUser(request.getUserId());

		boolean isSuccess = authoDao.updateFunctionItem(itemEntity) > 0;

		// Select functionItem which you had updated
		List<FunctionVoEntity> funcItems = authoDao.getFunListBySys(request.getSysId(),
				function.getParentFuncId(), function.getFunctionName());
		FunctionVoEntity funcItem = funcItems.get(0);

		// Update FUNCTION_DIV table
		FunctionDivEntity divEntity = null;
		for (FunctionDiv functionDiv : divList) {

			divEntity = new FunctionDivEntity();

			PropertyUtils.copyProperties(divEntity, functionDiv);
			divEntity.setFunctionId(funcItem.getFunctionId());

			if (isSuccess) {
				isSuccess = authoDao.updateFunctionDiv(divEntity) > 0;
			}
		}

		return isSuccess;
	}

	@Override
	public boolean deleteFuncItem(Function request) throws Exception {

		boolean isSuccess = authoDao.deleteFuncItem(request.getFunctionId()) > 0;

		// F : means this function_item has function_div
		if ("F".equals(request.getFunctionType()) && isSuccess) {

			isSuccess = authoDao.deleteFuncDiv(request.getFunctionId()) > 0;
		}

		return isSuccess;
	}

	@Override
	public boolean updateRoleFuncAuth(RoleFuncAuthReqObj request) throws Exception {

		List<RoleFuncAuthVo> roleFuncAuthVos = request.getRoleFunc();

		// Delete Role_Func_Auth by roleId first.
		boolean isSuccess = authoDao.deleteRoleFuncAuth(request.getRoleId()) > 0;

		for (RoleFuncAuthVo roleFuncAuth : roleFuncAuthVos) {

			if (isSuccess) {
				isSuccess = authoDao.insertRoleFuncAuth(roleFuncAuth) > 0;
			}
		}

		return isSuccess;
	}

	@Override
	public List<UserRepresentation> getSystemUsers(String sysId) throws Exception {

		List<UserRepresentation> result = new ArrayList<>();

		List<UserRepresentationEntity> entities = authoDao.getSystemUsers(sysId);

		for (UserRepresentationEntity entity : entities) {

			UserRepresentation userRepresentation = new UserRepresentation();

			userRepresentation.setCreatedTimestamp(entity.getCreatedTimestamp());
			userRepresentation.setEmail(entity.getEmail());
			userRepresentation.setFirstName(entity.getFirstName());
			userRepresentation.setLastName(entity.getLastName());
			userRepresentation.setId(entity.getId());
			userRepresentation.setServiceAccountClientId(entity.getServiceAccountClientId());
			userRepresentation.setUserName(entity.getUserName());

			// Set boolean parameters
			if (entity.getEmailVerified() == 1) {
				userRepresentation.setEmailVerified(true);
			}

			if (entity.getEnabled() == 1) {
				userRepresentation.setEnabled(true);
			}

			result.add(userRepresentation);
		}

		return result;
	}

	@Override
	public List<UserAuth> getUsersAuthReport(String userId) throws Exception {

		List<UserAuth> userAuths = new ArrayList<>();

		// 取得所有功能
		// List<FunctionVoEntity> funcEntities = authoDao.getAllFunction();

		// 根據條件取得有權限的功能和使用者
		List<UserAuthVoEntity> userAuthEntities = authoDao.getUsersAuthReport(userId);

		// Use userName divide entity
		Map<String, List<UserAuthVoEntity>> userAuthResult = userAuthEntities.stream()
				.collect(Collectors.groupingBy(UserAuthVoEntity::getUserName));

		Set<String> strList = userAuthResult.keySet();
		for (String key : strList) {

			List<FunctionDiv> divLit = new ArrayList<>();
			List<UserAuthVoEntity> userAuthVoEntities = userAuthResult.get(key);

			// Set FunctionDiv
			for (UserAuthVoEntity entity : userAuthVoEntities) {

				FunctionDiv div = new FunctionDiv();
				PropertyUtils.copyProperties(div, entity);
				div.setAutho(true);

				divLit.add(div);
			}

			// Set FunctionItem
			UserAuthVoEntity userAuthVoEntity = userAuthVoEntities.get(0);

			Function functionAuth = new Function();
			PropertyUtils.copyProperties(functionAuth, userAuthVoEntity);
			functionAuth.setAutho(true);
			functionAuth.setDivList(divLit);

			// Set User
			UserAuth userAuth = new UserAuth();
			PropertyUtils.copyProperties(userAuth, userAuthVoEntity);
			userAuth.setFuncionAuth(functionAuth);

			userAuths.add(userAuth);
		}

		return userAuths;
	}

	private List<Function> handlingFunctionResult(List<FunctionVoEntity> entities) throws Exception {

		List<Function> functions = new ArrayList<>();

		// Use functionId divide entity
		Map<Integer, List<FunctionVoEntity>> result = entities.stream()
				.collect(Collectors.groupingBy(FunctionVoEntity::getFunctionId));

		Set<Integer> kList = result.keySet();
		for (Integer key : kList) {

			List<FunctionVoEntity> currentFuncs = result.get(key);

			// Handling FunctionDiv parameters
			List<FunctionDiv> functionDivs = new ArrayList<>();
			for (FunctionVoEntity currentFuncDiv : currentFuncs) {

				FunctionDiv functionDiv = new FunctionDiv();
				PropertyUtils.copyProperties(functionDiv, currentFuncDiv);
				functionDivs.add(functionDiv);
			}

			// Handling FunctionItem parameters
			FunctionVoEntity currentFunc = currentFuncs.get(0); // 對同一個FunctionId而言 , 每一筆FunctionItem都一樣

			Function function = new Function();
			PropertyUtils.copyProperties(function, currentFunc);
			function.setDivList(functionDivs);

			functions.add(function);
		}

		return functions;
	}

	@Override
	public List<DepartmentVo> getDepartmentsByUser(String userId) throws Exception {
		return userDepartmentTitleDao.getDepartmentsByUser(userId);
	}

	@Override
	public List<JobTitleVo> getJobTitles() throws Exception {
		return userDepartmentTitleDao.getTitlesAll();
	}
	
	@Override
	public List<DepartmentVo> getAllDepartments() throws Exception {
		return userDepartmentTitleDao.getDepartmentsAll();
	}

	@Override
	public List<UserVo> getUserByDepTitle(String realm, String depId, String titleId, String userId) throws Exception {
		return userDepartmentTitleDao.getUserByDepTitle(realm, depId, titleId, userId);
	}

	private RestTemplate restTemplate;


	@Value("${eservice.bxcz.pbs.102.url}")
	private String pbs102url;
	@Value("${eservice.bxcz.login.client_id}")
	private String clientId;
	@Value("${eservice.bxcz.login.client_secret}")
	private String clientSecret;

	@Autowired
	private BxczDao bxczDao;

	@Override
    public ApiResponseObj<String> doPostPbs102(BxczLoginRequest req) {

		try {

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			BxczLoginRequest bxczLoginRequest = new BxczLoginRequest();
			bxczLoginRequest.setCode(req.getCode());
			bxczLoginRequest.setGrant_type("authorization_code");
			bxczLoginRequest.setRedirect_uri(req.getRedirect_uri());
			MultiValueMap<String, Object> requestData = new LinkedMultiValueMap<>();
			requestData.add("code", req.getCode());
			requestData.add("grant_type", req.getGrant_type());
			requestData.add("redirect_uri", req.getRedirect_uri());

			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(requestData, headers);

			ResponseEntity<BxczLoginResponse> resp = restTemplate.postForEntity(pbs102url, entity, BxczLoginResponse.class);
			logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(resp));

			if (!this.checkResponseStatus(resp)) {
				return null;
			}

			BxczLoginResponse obj = resp.getBody();
			bxczDao.insertBxczApiLog("call", "PBS-102", new Gson().toJson(bxczLoginRequest), new Gson().toJson(obj));
			if (obj != null) {
				ApiResponseObj responseObj = new ApiResponseObj();
				responseObj.setResult(parseIdToken(obj.getId_token()));
				return responseObj;
			}
		} catch (Exception e) {
			logger.error("doLoinBxcz: " + e);
			return new ApiResponseObj<>();
		}
		return new ApiResponseObj<>();
    }

	private String parseIdToken(String idToken) throws Exception {

		if (StringUtils.isBlank(idToken)) {
			return null;
		}

		String[] split_string = idToken.split("\\.");
		String base64EncodedHeader = split_string[0];
		String base64EncodedBody = split_string[1];

		logger.debug("~~~~~~~~~ JWT Header ~~~~~~~");
		String header = new String(Base64.getUrlDecoder().decode(base64EncodedHeader));
		logger.debug("JWT Header : " + header);


		logger.debug("~~~~~~~~~ JWT Body ~~~~~~~");
		String body = new String(Base64.getUrlDecoder().decode(base64EncodedBody));
		logger.debug("JWT Body : " + body);
		return MyJacksonUtil.readValue(body, "/userId");
	}

	private boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == org.apache.http.HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
	}

}
