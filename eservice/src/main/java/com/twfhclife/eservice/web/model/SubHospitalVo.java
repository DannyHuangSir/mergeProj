package com.twfhclife.eservice.web.model;

public class SubHospitalVo {
    private String subHpId;
    private String subHpName;

    private String hpId;

    public String getHpId() {
        return hpId;
    }

    public void setHpId(String hpId) {
        this.hpId = hpId;
    }

    public String getSubHpId() {
        return subHpId;
    }

    public void setSubHpId(String subHpId) {
        this.subHpId = subHpId;
    }

    public String getSubHpName() {
        return subHpName;
    }

    public void setSubHpName(String subHpName) {
        this.subHpName = subHpName;
    }
}