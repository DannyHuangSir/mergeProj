package com.twfhclife.eservice.api.jdzq.domain;

import com.twfhclife.eservice.api.jdzq.model.CaseVo;

import java.io.Serializable;

public class CaseProcessDataResponse implements Serializable {

    private CaseVo caseVo;

    public CaseVo getCaseVo() {
        return caseVo;
    }

    public void setCaseVo(CaseVo caseVo) {
        this.caseVo = caseVo;
    }
}
