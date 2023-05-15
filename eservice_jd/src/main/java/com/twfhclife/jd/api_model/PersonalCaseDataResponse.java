package com.twfhclife.jd.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.jd.web.model.CaseVo;

import java.io.Serializable;
import java.util.List;

public class PersonalCaseDataResponse implements Serializable {

    private List<CaseVo> caseList = Lists.newArrayList();

    public List<CaseVo> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<CaseVo> caseList) {
        this.caseList = caseList;
    }
}
