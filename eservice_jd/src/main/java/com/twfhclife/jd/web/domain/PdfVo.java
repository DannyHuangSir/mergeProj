package com.twfhclife.jd.web.domain;

import com.twfhclife.jd.api_model.AbstractBasePolicyNoDomain;

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
