package com.twfhclife.eservice.api.jdzq.domain;

import com.twfhclife.eservice.api.jdzq.model.NotePdfVo;

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
