package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdPolicyClaimDetailDao;
import com.twfhclife.adm.model.JdPolicyClaimDetailVo;
import com.twfhclife.adm.service.IJdPolicyClaimDetailService;
import com.twfhclife.generic.api_client.BaseRestClient;
import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.model.PolicyClaimDetailResponse;
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


    public <T> T getInsClaimStatisticsReport(JdPolicyClaimDetailVo jdPolicyClaimDetailVo) {
        logger.debug("Invoke eservice api[{}]: {}", policyUrl, MyJacksonUtil.object2Json(jdPolicyClaimDetailVo));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + WSO2_API_KEY);
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        HttpHeaders headers = this.setHeader(headerMap);
        HttpEntity<JdPolicyClaimDetailVo> entity = new HttpEntity<>(jdPolicyClaimDetailVo, headers);
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
        logger.debug("Invoke eservice api[{}]: {}", policyUrl, MyJacksonUtil.object2Json(jdPolicyClaimDetailVo));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + WSO2_API_KEY);
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

}
