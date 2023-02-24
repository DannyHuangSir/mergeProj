package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.model.PolicyBaseVo;
import com.twfhclife.eservice.web.model.PolicySafeGuardVo;
import com.twfhclife.eservice.web.model.PolicyVo;

import java.util.List;

public interface IPolicyService {

    List<PolicyVo> getPolicyList(KeycloakUser loginUser, PolicyVo vo);

    PolicyBaseVo getPolicyBase(String policyNo);

    PolicySafeGuardVo getPolicyGuard(String policyNo);
}
