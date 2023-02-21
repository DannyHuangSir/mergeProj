package com.twfhclife.eservice.api.jdzq.domain;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;

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
