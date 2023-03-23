package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyBaseVo;
import com.twfhclife.eservice.web.model.SafeGuardVo;

import java.io.Serializable;
import java.util.List;

public class PolicySafeGuardDataResponse implements Serializable {

    private List<SafeGuardVo> safeGuard;

    private PolicyBaseVo policyBase;

    public List<SafeGuardVo> getSafeGuard() {
        return safeGuard;
    }

    public void setSafeGuard(List<SafeGuardVo> safeGuard) {
        this.safeGuard = safeGuard;
    }

    public PolicyBaseVo getPolicyBase() {
        return policyBase;
    }

    public void setPolicyBase(PolicyBaseVo policyBase) {
        this.policyBase = policyBase;
    }
}
