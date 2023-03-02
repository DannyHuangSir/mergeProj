package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.model.PolicyChangeInfoVo;
import com.twfhclife.eservice.web.model.PolicyExpireOfPaymentVo;

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
