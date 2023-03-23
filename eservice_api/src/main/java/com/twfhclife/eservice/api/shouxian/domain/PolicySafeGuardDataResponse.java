package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyBaseVo;
import com.twfhclife.eservice.api.shouxian.model.PolicySafeGuardVo;
import com.twfhclife.eservice.api.shouxian.model.SafeGuardVo;

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
