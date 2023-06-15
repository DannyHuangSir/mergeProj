package com.twfhclife.alliance.model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimMedicalInfoVo;

import java.util.List;

public class CoapContentVo {

    private String hpId;
    private String subHpId;
    private String seNo;
    private String hsTime;
    private String heTime;
    private String otype;
    private String depid;
    private String dtypes;
    private List<TransMedicalTreatmentClaimMedicalInfoVo> medicalInfo = Lists.newArrayList();

    public void setMedicalInfo(List<TransMedicalTreatmentClaimMedicalInfoVo> medicalInfo) {
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

    public String getSeNo() {
        return seNo;
    }

    public void setSeNo(String seNo) {
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

    public String getDtypes() {
        return dtypes;
    }

    public void setDtypes(String dtypes) {
        this.dtypes = dtypes;
    }

    public List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfo() {
        return medicalInfo;
    }
}
