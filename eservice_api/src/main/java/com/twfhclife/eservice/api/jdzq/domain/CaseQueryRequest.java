package com.twfhclife.eservice.api.jdzq.domain;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.elife.domain.AbstractBasePolicyNoDomain;
import com.twfhclife.eservice.api.jdzq.model.PermQueryVo;
import com.twfhclife.eservice.api.jdzq.model.PersonSortVo;

import java.util.List;

public class CaseQueryRequest extends AbstractBasePolicyNoDomain {

    private String lipmName;
    private String lipmId;
    private String lipiName;
    private String lipiId;
    private String caseStatus;

    private int pageSize = 10;

    private int pageNum;

    private PersonSortVo sort;

    public PersonSortVo getSort() {
        return sort;
    }

    public void setSort(PersonSortVo sort) {
        this.sort = sort;
    }

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

    public int getPageSize() {
        return pageSize = 10;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
