package com.twfhclife.alliance.model;

import com.google.common.collect.Lists;

import java.util.List;

public class CoapContentVo {

    private String hpId;
    private String subHpId;
    private List<MedicalInfoVo> medicalInfo = Lists.newArrayList();

    public void setMedicalInfo(List<MedicalInfoVo> medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

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

    public List<MedicalInfoVo> getMedicalInfo() {
        return medicalInfo;
    }
}
