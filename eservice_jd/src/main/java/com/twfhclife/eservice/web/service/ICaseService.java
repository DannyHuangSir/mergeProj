package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.domain.CaseQueryVo;
import com.twfhclife.eservice.web.model.CaseVo;

import java.util.List;

public interface ICaseService {
    List<CaseVo> getCaseList(KeycloakUser user, CaseQueryVo vo);

    CaseVo getCaseProcess(String userId, String policyNo);

    CaseVo getCasePolicyInfo(String userId, String policyNo);

    List<CaseVo> getNoteContent(String userId, String policyNo);

    byte[] getNotePdf(String userId, String policyNo) throws Exception;

    List<CaseVo> getPersonalCaseList(KeycloakUser loginUser);
}
