package com.twfhclife.eservice.web.service.impl;

import com.google.gson.Gson;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.PersonalCaseDataResponse;
import com.twfhclife.eservice.api_model.PolicyTypeListResponse;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.web.dao.OptionDao;
import com.twfhclife.eservice.web.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements IOptionService {

	@Autowired
	private OptionDao optionDao;

    @Override
    public List<Map<String, Object>> getLevelStates() {
        return optionDao.getLevelStates();
    }

    @Override
    public List<Map<String, Object>> getPayModeList() {
        return optionDao.getPayModeList();
    }

    @Override
    public List<Map<String, Object>> getTransactionCodeList() {
        return optionDao.getTransactionCodeList(ApConstants.TRANSACTION_PARAMETER_CATEGORY_CODE);
    }

    @Override
    public List<Map<String, String>> getBankList() {
        return optionDao.getBankList();
    }

    @Autowired
    private BaseRestClient baseRestClient;

    @Value("${eservice_api.policy.type.list.url}")
    private String policyTypeListUrl;
    @Override
    public List<Map<String, String>> getPolicyTypeList() {
        PolicyTypeListResponse responseObj = baseRestClient.postApi("", policyTypeListUrl, PolicyTypeListResponse.class);
        return responseObj.getPolicyTypeList();
    }
}