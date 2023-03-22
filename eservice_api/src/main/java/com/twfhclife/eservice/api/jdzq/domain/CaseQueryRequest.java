package com.twfhclife.eservice.api.jdzq.domain;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.elife.domain.AbstractBasePolicyNoDomain;
import com.twfhclife.eservice.api.jdzq.model.PermQueryVo;

import java.util.List;

public class CaseQueryRequest extends AbstractBasePolicyNoDomain {

    private String lipmName;
    private String lipmId;
    private String lipiName;
    private String lipiId;
    private String caseStatus;

    private List<PermQueryVo> caseQuery = Lists.newArrayList();

    public String getLipmName() {
        return lipmName;
    }

    public void setLipmName(String lipmName) {
        this.lipmName = lipmName;
    }

    public String getLipmId() {
        return lipmId;
    }

    public void setLipmId(String lipmId) {
        this.lipmId = lipmId;
    }

    public String getLipiName() {
        return lipiName;
    }

    public void setLipiName(String lipiName) {
        this.lipiName = lipiName;
    }

    public String getLipiId() {
        return lipiId;
    }

    public void setLipiId(String lipiId) {
        this.lipiId = lipiId;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public List<PermQueryVo> getCaseQuery() {
        return caseQuery;
    }

    public void setCaseQuery(List<PermQueryVo> caseQuery) {
        this.caseQuery = caseQuery;
    }
}