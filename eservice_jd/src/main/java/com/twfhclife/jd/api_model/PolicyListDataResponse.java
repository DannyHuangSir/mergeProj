package com.twfhclife.jd.api_model;


import com.google.common.collect.Lists;
import com.twfhclife.jd.web.model.PageInfoVo;
import com.twfhclife.jd.web.model.PolicyCountVo;
import com.twfhclife.jd.web.model.PolicyVo;

import java.io.Serializable;
import java.util.List;

public class PolicyListDataResponse extends PageInfoVo implements Serializable  {


    private PolicyCountVo policyCountVo;

    public PolicyCountVo getPolicyCountVo() {
        return policyCountVo;
    }

    public void setPolicyCountVo(PolicyCountVo policyCountVo) {
        this.policyCountVo = policyCountVo;
    }

    private List<PolicyVo> rows = Lists.newArrayList();

    public List<PolicyVo> getRows() {
        return rows;
    }

    public void setRows(List<PolicyVo> rows) {
        this.rows = rows;
    }

}
