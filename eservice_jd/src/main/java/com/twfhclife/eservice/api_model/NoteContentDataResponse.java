package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.CaseVo;

import java.io.Serializable;
import java.util.List;

public class NoteContentDataResponse implements Serializable {

    private List<CaseVo> cases = Lists.newArrayList();

    public List<CaseVo> getCases() {
        return cases;
    }

    public void setCases(List<CaseVo> cases) {
        this.cases = cases;
    }
}
