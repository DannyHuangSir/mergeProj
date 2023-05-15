package com.twfhclife.jd.api_model;

import com.twfhclife.jd.web.domain.NotePdfVo;

import java.io.Serializable;

public class NotePdfDataResponse implements Serializable {

    private NotePdfVo notePdf;

    public NotePdfVo getNotePdf() {
        return notePdf;
    }

    public void setNotePdf(NotePdfVo notePdf) {
        this.notePdf = notePdf;
    }
}
