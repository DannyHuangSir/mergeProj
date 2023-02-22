package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.ApiResponseObj;
import com.twfhclife.eservice.api_model.PolicyListDataResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.PolicyVo;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class PolicyServiceImpl implements IPolicyService {

    @Value("${eservice_api.policy.list.url}")
    private String policyListUrl;
    @Autowired
    private BaseRestClient baseRestClient;

    @Autowired
    private UsersDao usersDao;


    @Override
    public List<PolicyVo> getPolicyList(KeycloakUser user, PolicyVo vo) {
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員
        int role = usersDao.checkUserRole(user.getId());
        List<PolicyVo> result = Lists.newArrayList();
        List<String> serialNums = Lists.newArrayList();
        switch (role) {
            case 2:
                serialNums.addAll(usersDao.getSerialNumsBySupervisor(user.getId()));
                break;
            case 3:
                serialNums.addAll(usersDao.getSerialNumsByPassageWay(user.getId()));
                break;
            case 4:
                serialNums.addAll(usersDao.getSerialNumsByIc(user.getId()));
                break;
            default:
                serialNums.addAll(usersDao.getSerialNumsByUser(user.getId()));
                break;
        }
        if (!CollectionUtils.isEmpty(serialNums)) {
            vo.setSerialNums(serialNums);
            PolicyListDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(vo), policyListUrl, PolicyListDataResponse.class);
            result.addAll(responseObj.getPolicyList());
        }
        return result;
    }
}
