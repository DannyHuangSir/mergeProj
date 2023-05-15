package com.twfhclife.jd.api_client;

import com.twfhclife.jd.api_model.ApiResponseObj;
import com.twfhclife.jd.api_model.ReturnHeader;
import com.twfhclife.jd.util.MyJacksonUtil;
import com.twfhclife.jd.web.model.FunctionVo;
import com.twfhclife.jd.web.model.UserFuncAuthReqVo;
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

    @Value("${eservice_api.func.function-auth.url}")
    private String FUNCTION_AUTH_URI;// = "/user/function-auth";


    public List<FunctionVo> getMenuList(String sysId, String userId, String isAdmin) {
        List<FunctionVo> resultList = null;
        ApiResponseObj<List<FunctionVo>> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = null;
        String url = FUNCTION_AUTH_URI;

        logger.debug("invoke getFunctions api: url=" + url + ", sysId=" + sysId + ",userId=" + userId);
        UserFuncAuthReqVo vo = new UserFuncAuthReqVo();
        vo.setSysId(sysId);
        vo.setUserId(userId);
        vo.setIsAdmin(isAdmin);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + WSO2_API_KEY);
        headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        headerMap.put("secret", this.ESERVICE_API_SECRET);
        HttpHeaders headers = this.setHeader(headerMap);
        HttpEntity<?> entity = new HttpEntity<>(vo, headers);

        ParameterizedTypeReference<ApiResponseObj<List<FunctionVo>>> typeRef = new ParameterizedTypeReference<ApiResponseObj<List<FunctionVo>>>() {
        };
        ResponseEntity<ApiResponseObj<List<FunctionVo>>> responseEntity = restTemplate.exchange(url,
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
                apiResponseObj = (ApiResponseObj<List<FunctionVo>>) obj;
                resultList = apiResponseObj.getResult();
                returnHeader = apiResponseObj.getReturnHeader();
            } else {
                return null;
            }
        }
        logger.info("getSystemFunctions result = " + returnHeader.getReturnCode());
        return resultList;
    }
}
