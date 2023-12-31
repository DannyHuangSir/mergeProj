package com.twfhclife.eservice.api.jdzq.model;


import com.twfhclife.eservice.api.elife.domain.AbstractBasePolicyNoDomain;

public class PdfVo extends AbstractBasePolicyNoDomain {
    private String noteKey;

    public PdfVo() {
    }

    public PdfVo(String policyNo, String noteKey, String systemId, String userId) {
        setUserId(userId);
        setSysId(systemId);
        setPolicyNo(policyNo);
        this.noteKey = noteKey;
    }

    public String getNoteKey() {
        return noteKey;
    }

    public void setNoteKey(String noteKey) {
        this.noteKey = noteKey;
    }
}
