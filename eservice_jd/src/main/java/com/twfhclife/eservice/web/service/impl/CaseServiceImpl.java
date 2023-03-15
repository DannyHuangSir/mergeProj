package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.CaseProcessDataResponse;
import com.twfhclife.eservice.api_model.PersonalCaseDataResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.PermQueryVo;
import com.twfhclife.eservice.web.domain.CaseQueryVo;
import com.twfhclife.eservice.web.model.CaseVo;
import com.twfhclife.eservice.web.model.PolicyBaseVo;
import com.twfhclife.eservice.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private BaseRestClient baseRestClient;

    @Value("${eservice_api.personal.case.url}")
    private String personalCaseUrl;

    @Override
    public List<CaseVo> getCaseList(KeycloakUser user, CaseQueryVo vo) {
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員
        int role = usersDao.checkUserRole(user.getId());
        List<CaseVo> result = Lists.newArrayList();
        List<PermQueryVo> caseQuery = Lists.newArrayList();
        switch (role) {
            case 2:
                caseQuery.addAll(usersDao.getCaseQueryBySupervisor(user.getId()));
                break;
            case 3:
                caseQuery.addAll(usersDao.getCaseQueryByPassageWay(user.getId()));
                break;
            case 4:
                caseQuery.addAll(usersDao.getCaseQueryByIc(user.getId()));
                break;
            default:
                caseQuery.addAll(usersDao.getCaseQueryByUser(user.getId()));
                break;
        }
        if (!CollectionUtils.isEmpty(caseQuery)) {
            if (vo == null) {
                vo = new CaseQueryVo();
            }
            vo.setCaseQuery(caseQuery);
            PersonalCaseDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(vo), personalCaseUrl, PersonalCaseDataResponse.class);
            result.addAll(responseObj.getCaseList());
        }
        return result;
    }

    @Value("${eservice_api.case.process.url}")
    private String caseProcessUrl;

    @Override
    public CaseVo getCaseProcess(String policyNo) {
        CaseProcessDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), caseProcessUrl, CaseProcessDataResponse.class);
        return responseObj.getCaseVo();
    }

    @Value("${eservice_api.case.policy.info.url}")
    private String casePolicyInfoUrl;
    @Override
    public CaseVo getCasePolicyInfo(String policyNo) {
        CaseProcessDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), casePolicyInfoUrl, CaseProcessDataResponse.class);
        return responseObj.getCaseVo();
    }
}
