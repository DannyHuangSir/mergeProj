package com.twfhclife.alliance.model;

import com.google.common.collect.Lists;

import java.util.List;

public class MedicalInfoVo {

    private long seNo;
    private String hsTime;
    private String heTime;
    private String otype;
    private String depid;
    private List<String> dtypes =Lists.newArrayList();

    public long getSeNo() {
        return seNo;
    }

    public void setSeNo(long seNo) {
        this.seNo = seNo;
    }

    public String getHsTime() {
        return hsTime;
    }

    public void setHsTime(String hsTime) {
        this.hsTime = hsTime;
    }

    public String getHeTime() {
        return heTime;
    }

    public void setHeTime(String heTime) {
        this.heTime = heTime;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getDepid() {
        return depid;
    }

    public void setDepid(String depid) {
        this.depid = depid;
    }

    public List<String> getDtypes() {
        return dtypes;
    }

    public void setDtypes(List<String> dtypes) {
        this.dtypes = dtypes;
    }
}
