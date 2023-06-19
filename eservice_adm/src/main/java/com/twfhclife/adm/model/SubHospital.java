package com.twfhclife.adm.model;


public class SubHospital {

    public Integer id;
    public String subHpId;
    public String subHpName;
    public Integer hpId;

    public Integer getHpId() {
        return hpId;
    }

    public void setHpId(Integer hpId) {
        this.hpId = hpId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
