package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.domain.CaseQueryVo;
import com.twfhclife.eservice.web.model.CaseVo;

import java.util.List;

public interface ICaseService {
    List<CaseVo> getCaseList(KeycloakUser user, CaseQueryVo vo);

    CaseVo getCaseProcess(String policyNo);

    CaseVo getCasePolicyInfo(String policyNo);

    List<CaseVo> getNoteContent(String policyNo);

    byte[] getNotePdf(String policyNo) throws Exception;
}
