package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.model.CaseVo;

import java.util.List;

public interface ICaseService {
    List<CaseVo> getCaseList(KeycloakUser user);
}
