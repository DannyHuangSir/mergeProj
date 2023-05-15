package com.twfhclife.jd.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.jd.web.model.CaseVo;
import com.twfhclife.jd.web.model.PageInfoVo;

import java.io.Serializable;
import java.util.List;

public class CaseListDataResponse extends PageInfoVo implements Serializable {

    private List<CaseVo> rows = Lists.newArrayList();

    public List<CaseVo> getRows() {
        return rows;
    }

    public void setRows(List<CaseVo> rows) {
        this.rows = rows;
    }
}
