package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicySafeGuardVo;

import java.io.Serializable;

public class PolicySafeGuardDataResponse implements Serializable {

    private PolicySafeGuardVo safeGuard;

    public PolicySafeGuardVo getSafeGuardVo() {
        return safeGuard;
    }

    public void setSafeGuardVo(PolicySafeGuardVo safeGuard) {
        this.safeGuard = safeGuard;
    }
}
