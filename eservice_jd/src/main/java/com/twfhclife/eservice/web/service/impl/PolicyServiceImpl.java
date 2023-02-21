package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.ApiResponseObj;
import com.twfhclife.eservice.api_model.PolicyListDataResponse;
import com.twfhclife.eservice.web.model.PolicyVo;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyServiceImpl implements IPolicyService {

    @Value("${eservice_api.policy.list.url}")
    private String policyListUrl;
    @Autowired
    private BaseRestClient baseRestClient;


    @Override
    public List<PolicyVo> getPolicyList() {
        ApiResponseObj<PolicyListDataResponse> responseObj = baseRestClient.postApiResponse("", policyListUrl, PolicyListDataResponse.class);
        return responseObj.getResult().getPolicyList();
    }
}
