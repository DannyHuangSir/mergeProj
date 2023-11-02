package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdPolicyClaimDetailDao;
import com.twfhclife.adm.model.JdPolicyClaimDetailVo;
import com.twfhclife.adm.model.JdPolicyClaimReqVo;
import com.twfhclife.adm.model.JdUserDetailReqVo;
import com.twfhclife.adm.service.IJdPolicyClaimDetailService;
import com.twfhclife.generic.api_client.BaseRestClient;
import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.model.PolicyClaimDetailResponse;
import com.twfhclife.generic.model.UserDetailResponse;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Service
public class JdPolicyClaimDetailServiceImpl extends BaseRestClient implements IJdPolicyClaimDetailService {

    private static final Logger logger = LoggerFactory.getLogger(JdPolicyClaimDetailServiceImpl.class);

    @Autowired
    private JdPolicyClaimDetailDao jdPolicyClaimDetailDao;

    @Value("${eservice_api.jd.policy.url}")
    private String policyUrl;

    @Value("${eservice_api.jd.policy.policyTypeUrl}")
    private String policyTypeUrl;

    @Value("${eservice_api.jd.policy.userDetailUrl}")
    private String userDetailUrl;

    public <T> T getInsClaimStatisticsReport(JdPolicyClaimReqVo jdPolicyClaimDetailVo) {
        logger.debug("Invoke eservice api[{}]: {}", policyUrl, MyJacksonUtil.object2Json(jdPolicyClaimDetailVo));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + WSO2_API_JD_KEY);
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        HttpHeaders headers = this.setHeader(headerMap);
        HttpEntity<JdPolicyClaimReqVo> entity = new HttpEntity<>(jdPolicyClaimDetailVo, headers);
        ResponseEntity<ApiResponseObj<PolicyClaimDetailResponse>> responseEntity = restTemplate.exchange(policyUrl,
                HttpMethod.POST, entity, typeReferences().get(PolicyClaimDetailResponse.class));
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

    public <T> T getPolicyTypeNameList(JdPolicyClaimDetailVo jdPolicyClaimDetailVo) {
        logger.info("Invoke eservice api[{}]: {}", policyTypeUrl, MyJacksonUtil.object2Json(jdPolicyClaimDetailVo));
        logger.info("Invoke eservice WSO2_API_JD_KEY: {}", WSO2_API_JD_KEY);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + WSO2_API_JD_KEY);
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        HttpHeaders headers = this.setHeader(headerMap);
        HttpEntity<JdPolicyClaimDetailVo> entity = new HttpEntity<>(jdPolicyClaimDetailVo, headers);
        ResponseEntity<ApiResponseObj<PolicyClaimDetailResponse>> responseEntity = restTemplate.exchange(policyTypeUrl,
                HttpMethod.POST, entity, typeReferences().get(PolicyClaimDetailResponse.class));
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
    @Override
    public List<JdPolicyClaimDetailVo> getBpmcurrenttak() {
        return jdPolicyClaimDetailDao.getBpmcurrenttak();
    }

    @Override
    public  <T> T getUserDetail(JdUserDetailReqVo vo){
        logger.debug("Invoke eservice api[{}]: {}", userDetailUrl, MyJacksonUtil.object2Json(vo));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + WSO2_API_JD_KEY);
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        HttpHeaders headers = this.setHeader(headerMap);
        HttpEntity<JdUserDetailReqVo> entity = new HttpEntity<>(vo, headers);
        ResponseEntity<ApiResponseObj<UserDetailResponse>> responseEntity = restTemplate.exchange(userDetailUrl,
                HttpMethod.POST, entity, typeReferences().get(UserDetailResponse.class));
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

}
