package com.twfhclife.alliance.model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimMedicalInfoVo;

import java.util.List;

public class Bxcz414ReturnVo {

    private String code;
    private String msg;
    private String idNo;
    private String name;
    private String birdate;
    private String acExpiredSec;
    private String to;
    private String id_token;
    private String redirectUri;
    private List<CoapContentVo> coapContent = Lists.newArrayList();

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirdate() {
        return birdate;
    }

    public void setBirdate(String birdate) {
        this.birdate = birdate;
    }

    public String getAcExpiredSec() {
        return acExpiredSec;
    }

    public void setAcExpiredSec(String acExpiredSec) {
        this.acExpiredSec = acExpiredSec;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public List<CoapContentVo> getCoapContent() {
        return coapContent;
    }

    public void setCoapContent(List<CoapContentVo> coapContent) {
        this.coapContent = coapContent;
    }
}
