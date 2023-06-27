package com.twfhclife.generic.api_client;

import com.twfhclife.adm.model.Spa402RequestVo;
import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.Spa401RequestVo;
import com.twfhclife.generic.api_model.Spa401ResponseVo;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceBillingClient extends BaseRestClient {

    private static final Logger logger = LogManager.getLogger(ServiceBillingClient.class);

    @Value("${eservice_api.spa401.url}")
    private String spa401url;
    @Value("${eservice_api.spa402.url}")
    private String spa402url;

    public Spa401ResponseVo callSpa401(Spa401RequestVo params) {
        ReturnHeader returnHeader = null;
        logger.debug("invoke callSpa401 post api: url=" + spa401url + ", requestBody=" + MyJacksonUtil.object2Json(params));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + this.WSO2_API_KEY);
        HttpHeaders headers = this.setHeader(headerMap);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Spa401RequestVo> entity = new HttpEntity<>(params, headers);
        ParameterizedTypeReference<ApiResponseObj<Spa401ResponseVo>> typeRef = new ParameterizedTypeReference<ApiResponseObj<Spa401ResponseVo>>() {
        };
        ResponseEntity<ApiResponseObj<Spa401ResponseVo>> responseEntity = restTemplate.exchange(spa401url,
                HttpMethod.POST, entity, typeRef);

        if (!this.checkResponseStatus(responseEntity)) {
            return null;
        }
        Object obj = responseEntity.getBody();
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof ApiResponseObj) {
                ApiResponseObj apiResponseObj = (ApiResponseObj) obj;
                returnHeader = apiResponseObj.getReturnHeader();
                logger.info("callSpa401 result = " + returnHeader.getReturnCode());
                return (Spa401ResponseVo) apiResponseObj.getResult();
            } else {
                return null;
            }
        }
    }

    public String callSpa402(Spa402RequestVo params) {
        ReturnHeader returnHeader = null;
        logger.debug("invoke callSpa402 post api: url=" + spa402url + ", requestBody=" + MyJacksonUtil.object2Json(params));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + this.WSO2_API_KEY);
        HttpHeaders headers = this.setHeader(headerMap);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Spa402RequestVo> entity = new HttpEntity<>(params, headers);
        ParameterizedTypeReference<ApiResponseObj<Spa401ResponseVo>> typeRef = new ParameterizedTypeReference<ApiResponseObj<Spa401ResponseVo>>() {
        };
        ResponseEntity<ApiResponseObj<Spa401ResponseVo>> responseEntity = restTemplate.exchange(spa402url,
                HttpMethod.POST, entity, typeRef);

        if (!this.checkResponseStatus(responseEntity)) {
            return null;
        }
        Object obj = responseEntity.getBody();
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof ApiResponseObj) {
                ApiResponseObj apiResponseObj = (ApiResponseObj) obj;
                returnHeader = apiResponseObj.getReturnHeader();
                logger.info("callSpa402 result = " + returnHeader.getReturnCode());
                return returnHeader.getReturnMesg();
            } else {
                return null;
            }
        }
    }
}
