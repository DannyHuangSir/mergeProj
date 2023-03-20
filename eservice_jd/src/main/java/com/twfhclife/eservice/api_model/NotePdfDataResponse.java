package com.twfhclife.eservice.api_model;

import com.twfhclife.eservice.web.domain.NotePdfVo;

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
