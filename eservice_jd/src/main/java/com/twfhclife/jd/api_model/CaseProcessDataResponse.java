package com.twfhclife.jd.api_model;

import com.twfhclife.jd.web.model.CaseVo;

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
