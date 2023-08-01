package com.twfhclife.generic.api_client;

import com.twfhclife.adm.model.FunctionItemVo;
import com.twfhclife.adm.model.FunctionVo;
import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.UserFuncAuthReqVo;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FuncMgmtClient extends BaseRestClient {

	private static final Logger logger = LogManager.getLogger(FuncMgmtClient.class);

	@Value("${eservice_api.func.sys-functions.url}")
	private String GET_SYS_FUNCTION_URI;// = "/funcMgnt/{sysId}/getFunctions";
	@Value("${eservice_api.func.insert.url}")
	private String INSERT_FUNCTION_URI;// = "/funcMgnt/insertFunctions";
//	@Value("${eservice_api.func.update.url}")
//	private String UPDATE_FUNCTION_URI;// = "/funcMgnt/updateFunctions";
//	@Value("${eservice_api.func.delete.url}")
//	private String DELETE_FUNCTION_URI;// = "/funcMgnt/deleteFunctions";
	@Value("${eservice_api.func.function-auth.url}")
	private String FUNCTION_AUTH_URI;// = "/user/function-auth";

	/**
	 * 取得系統功能清單
	 * @param sysId
	 * @return
	 */
	public List<FunctionItemVo> getSystemFunctions(String sysId) {
		List<FunctionItemVo> resultList = null;
		ApiResponseObj<List<FunctionItemVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = null;
		String url = GET_SYS_FUNCTION_URI;
		//url = "http://220.133.126.209:8084/eservice_api/funcMgnt/{sysId}/getFunctions";// TODO for test
		url = url.replace("{sysId}", sysId.trim());
		logger.debug("invoke getFunctions api: url=" + url + ", sysId=" + sysId);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("secret", this.ESERVICE_API_SECRET);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ParameterizedTypeReference<ApiResponseObj<List<FunctionItemVo>>> typeRef = new ParameterizedTypeReference<ApiResponseObj<List<FunctionItemVo>>>() {
		};
		ResponseEntity<ApiResponseObj<List<FunctionItemVo>>> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, typeRef);
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
				apiResponseObj = (ApiResponseObj<List<FunctionItemVo>>) obj;
				resultList = apiResponseObj.getResult();
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
		logger.info("getSystemFunctions result = " + returnHeader.getReturnCode());
		return resultList;
	}
	
	public ReturnHeader addFunction(FunctionItemVo functionVo) {
		ApiResponseObj apiResponseObj = new ApiResponseObj();
		ReturnHeader returnHeader = null;
		String url = INSERT_FUNCTION_URI;
		//url = "http://220.133.126.209:8084/eservice_api/funcMgnt/insertFunctions";// TODO for test
		logger.debug("invoke addFunction api: url=" + url + ", FunctionItemVo=" + MyJacksonUtil.object2Json(functionVo));

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("secret", this.ESERVICE_API_SECRET);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<FunctionItemVo> entity = new HttpEntity<>(functionVo, headers);

		ParameterizedTypeReference<ApiResponseObj> typeRef = new ParameterizedTypeReference<ApiResponseObj>() {
		};
		ResponseEntity<ApiResponseObj> responseEntity = restTemplate.exchange(url,
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
				apiResponseObj = (ApiResponseObj) obj;
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
		logger.info("getSystemFunctions result = " + returnHeader.getReturnCode());
		return returnHeader;
	}
	
	public List<FunctionVo> getMenuList(String sysId, String userId, String isAdmin) {
		List<FunctionVo> resultList = null;
		ApiResponseObj<List<FunctionVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = null;
		String url = FUNCTION_AUTH_URI;
//		url = "http://220.133.126.209:8084/eservice_api/user/function-auth";// TODO for test
		
//		logger.debug("invoke getFunctions api: url=" + url + ", sysId=" + sysId +",userId="+userId);

		UserFuncAuthReqVo vo =  new UserFuncAuthReqVo();
		vo.setSysId(sysId);
		vo.setUserId(userId);
		vo.setIsAdmin(isAdmin);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + new String(WSO2_API_KEY));
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		headerMap.put("secret", this.ESERVICE_API_SECRET);
		HttpHeaders headers = this.setHeader(headerMap);
		HttpEntity<?> entity = new HttpEntity<>(vo, headers);

		ParameterizedTypeReference<ApiResponseObj<List<FunctionVo>>> typeRef = new ParameterizedTypeReference<ApiResponseObj<List<FunctionVo>>>() {
		};
		ResponseEntity<ApiResponseObj<List<FunctionVo>>> responseEntity = restTemplate.exchange(url,
				HttpMethod.POST, entity, typeRef);
//		logger.debug("API ResponseEntity=" + MyJacksonUtil.object2Json(responseEntity));
		if (!this.checkResponseStatus(responseEntity)) {
			return null;
		}
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		Object obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof ApiResponseObj) {
				apiResponseObj = (ApiResponseObj<List<FunctionVo>>) obj;
				resultList = apiResponseObj.getResult();
				returnHeader = apiResponseObj.getReturnHeader();
			} else {
				return null;
			}
		}
//		logger.info("getSystemFunctions result = " + returnHeader.getReturnCode());
		return resultList;
	}
}
