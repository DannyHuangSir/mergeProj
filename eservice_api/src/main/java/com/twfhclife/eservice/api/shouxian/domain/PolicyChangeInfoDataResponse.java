package com.twfhclife.eservice.api.shouxian.domain;

import com.twfhclife.eservice.api.shouxian.model.PolicyChangeInfoVo;

import java.io.Serializable;

public class PolicyChangeInfoDataResponse implements Serializable {

    private PolicyChangeInfoVo changeInfo;

    public PolicyChangeInfoVo getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(PolicyChangeInfoVo changeInfo) {
        this.changeInfo = changeInfo;
    }
}
